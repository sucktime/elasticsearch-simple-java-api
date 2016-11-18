package zy.engine.builder;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.script.ScriptScoreFunctionBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptService;
import zy.engine.facet.SearchScorer;
import zy.engine.utils.ListUtil;
import zy.engine.utils.StringUtil;

import java.util.Map;

/**
 * Created by JiangGuofeng on 2016/10/21.
 *
 * @Todo
 */
public class ScoredQueryBuilders {

    /**
     * @provided-for-director
     * @param searchScorer
     * @return
     */
    public static FunctionScoreQueryBuilder functionScoreQueryBuilder(QueryBuilder queryBuilder, SearchScorer searchScorer){
        if (ListUtil.isEmpty(searchScorer.getScoreScripts())){
            throw new RuntimeException("empty scoreScipts");
        }
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(queryBuilder);
        for (SearchScorer.SearchScoreScript script : searchScorer.getScoreScripts()){
            if (StringUtil.isEmpty(script.getScript())){
                throw new RuntimeException("empty script string");
            }
            functionScoreQueryBuilder = addScoreFunction(functionScoreQueryBuilder, script.getScript(), script.getParams());
        }
        functionScoreQueryBuilder.scoreMode(searchScorer.getScoreMode());
        functionScoreQueryBuilder.boostMode(searchScorer.getBoostMode());
        return functionScoreQueryBuilder;
    }

    public static FunctionScoreQueryBuilder functionScoreQuCQeryBuilder(QueryBuilder queryBuilder, String script, Map params){
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(queryBuilder);
        return addScoreFunction(functionScoreQueryBuilder, script, params);
    }

    public static FunctionScoreQueryBuilder functionScoreQueryBuilder(QueryBuilder queryBuilder, Script script){
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(queryBuilder);
        return addScoreFunction(functionScoreQueryBuilder, script);
    }

    public static FunctionScoreQueryBuilder addScoreFunction(FunctionScoreQueryBuilder functionScoreQueryBuilder, String script, Map params){
       Script script_ = new Script(script, ScriptService.ScriptType.INLINE, "groovy", params);
        return addScoreFunction(functionScoreQueryBuilder, script_);
    }

    public static FunctionScoreQueryBuilder addScoreFunction(FunctionScoreQueryBuilder functionScoreQueryBuilder, Script script){
        return functionScoreQueryBuilder.add(new ScriptScoreFunctionBuilder(script));
    }
}
