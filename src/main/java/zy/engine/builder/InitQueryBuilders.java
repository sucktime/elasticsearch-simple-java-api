package zy.engine.builder;

/**
 * Created by JiangGuofeng on 2016/10/20.
 */

import org.elasticsearch.index.query.QueryStringQueryBuilder;
import zy.engine.facet.SearchQuery;
import zy.engine.query.SimplestQueryNode;
import zy.engine.query.SimplestQueryParser;
import zy.engine.utils.ArrayUtil;
import zy.engine.utils.SensitiveAnalyzer;
import zy.engine.utils.StringUtil;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * 根据queryString来创建原始查询
 */
public class InitQueryBuilders {

    private static final Operator DEFAULT_OP_BETWEEN_FIELDS = Operator.OR;
    private static final Operator DEFAULT_OP_BETWEEN_SUBQUERIES = Operator.AND;

    public static QueryStringQueryBuilder initQueryBuilder(SearchQuery query){
        /**
         * 1) queryString
         * 2) sensitive
         * 3) queryType + queryFields + fieldBoosts -> InitQueryBuilder
         */
        String queryString = query.getQuery();
        if (query == null || StringUtil.isEmpty(queryString)){
            throw  new RuntimeException("queryString is empty.");
        }
        if (query.isSensitiveFilter() && SensitiveAnalyzer.isSensitive(queryString)) {
            throw new RuntimeException("queryString is sensitive and denied.");
        }
        QueryStringQueryBuilder initQuery = null;
        String[] queryFields = query.getQueryFileds();
        float[] fieldBoosts = query.getFieldBoosts();
        if(ArrayUtil.isEmpty(queryFields) || ArrayUtil.isEmpty(fieldBoosts)){
            throw new RuntimeException("invalid queryFields or fieldBoosts");
        }
        if (query.getQueryType() == SearchQuery.QueryType.SimplestQueryGrammar){
            SimplestQueryNode queryTreeRoot = SimplestQueryParser.parseRaw(queryString);
            initQuery = InitQueryBuilders.queryStringQueryBuilder(queryFields, queryTreeRoot, fieldBoosts);
        }
        return initQuery;
    }

    /**
     * 根据查询树构建bool查询
     * @param queryTree
     * @return
     * @Todo
     */
    public static QueryStringQueryBuilder queryStringQueryBuilder(String[] fileds, SimplestQueryNode queryTree, float[] boosts) {

        assert !(queryTree == null || fileds == null || fileds.length == 0 || boosts == null || boosts.length != fileds.length);

        // 1. query:
        String query = parseRoot(queryTree);
        StringBuilder queryStrBuilder = new StringBuilder();
        boolean hasStarter = false;
        for(String field : fileds){
            if (hasStarter){
                if (DEFAULT_OP_BETWEEN_FIELDS == Operator.OR){
                    queryStrBuilder.append(" ").append("OR");
                }
            }
            hasStarter = true;
            queryStrBuilder.append(" ").append(field).append(":(").append(query).append(")");
        }
        QueryStringQueryBuilder queryStringQueryBuilder = queryStringQuery(queryStrBuilder.toString().trim());
        // 2. default-operator:
        if(DEFAULT_OP_BETWEEN_SUBQUERIES == Operator.AND) {
            queryStringQueryBuilder.defaultOperator(QueryStringQueryBuilder.Operator.AND);
        }
        // 3. fields and boosts:
        for (int i=0; i<fileds.length; i++){
            queryStringQueryBuilder.field(fileds[i], boosts[i]);
        }
        // 4. phrase_slop:
        int slop = ensureSlopSize(queryTree);
        queryStringQueryBuilder.phraseSlop(slop);
        // finally return:
        return queryStringQueryBuilder;
    }

    public static String parseRoot(SimplestQueryNode root){
        StringBuilder queryBuilder = new StringBuilder();
        List<SimplestQueryNode> children = root.getChildren();
        boolean hasStarter = false;
        for (SimplestQueryNode child : children){
            if (hasStarter){
                queryBuilder.append(" OR");
            }
            queryBuilder.append(" ").append(parseChild(child));
            hasStarter = true;
        }
        return queryBuilder.toString().trim();
    }

    private static String parseChild(SimplestQueryNode child){
        StringBuilder subQueryBuilder = new StringBuilder();
        if(child.isLeaf()){
            if(child.isNot()){
                subQueryBuilder.append(" NOT");
            }
            subQueryBuilder.append(" ");
            if (child.isPhrase()){
                subQueryBuilder.append("\"").append(subQueryBuilder.append(child.getWord())).append("\"");
            } else {
                subQueryBuilder.append(child.getWord());
            }
        } else {
            List<SimplestQueryNode> grands = child.getChildren();
            boolean hasStater = false;
            for (SimplestQueryNode grand : grands){
                if (hasStater){
                    subQueryBuilder.append(" AND");
                }
                if (grand.isNot()){
                    subQueryBuilder.append(" NOT");
                }
                subQueryBuilder.append(" ");
                if (grand.isPhrase()) {
                    subQueryBuilder.append("\"").append(grand.getWord()).append("\"");
                } else {
                    subQueryBuilder.append(grand.getWord());
                }
                hasStater = true;
            }
        }
        return subQueryBuilder.toString().trim();
    }

    private static int ensureSlopSize(SimplestQueryNode root){
        int maxPhraseLen = ensureMaxPhraseLen(root);
        return (maxPhraseLen+1)/3 + 1;
    }

    private static int ensureMaxPhraseLen(SimplestQueryNode root){
        int maxLen = 0;
        if (root.isPhrase()){
            maxLen = Math.max(maxLen, root.getWord().length());
        }
        if (!root.isLeaf()){
            List<SimplestQueryNode> children = root.getChildren();
            for (SimplestQueryNode child : children) {
                    maxLen = Math.max(maxLen, ensureMaxPhraseLen(child));
            }
        }
        return maxLen;
    }

    public QueryStringQueryBuilder queryStringQueryBuilder(String[] fields, SimplestQueryNode queryTree, float[] boosts, Operator fieldsOperatioin){
        return queryStringQueryBuilder(fields, queryTree, boosts, DEFAULT_OP_BETWEEN_FIELDS);
    }

    public QueryStringQueryBuilder queryStringQueryBuilder(String[] fields, SimplestQueryNode[] queryTrees, float[] boosts){
        return null;
    }

    public QueryStringQueryBuilder queryStringQueryBuilder(String[] fields, SimplestQueryNode[] queryTrees, float[] boosts, Operator fieldsOperation){
        return queryStringQueryBuilder(fields, queryTrees, boosts, DEFAULT_OP_BETWEEN_FIELDS);
    }
}
