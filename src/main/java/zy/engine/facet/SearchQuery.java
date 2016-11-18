package zy.engine.facet;

import java.util.Arrays;

/**
 * Created by JiangGuofeng on 2016/10/20.
 */
public class SearchQuery {
    //查询语句
    private String query = null;
    //query查询的字段
    private String[] queryFileds = null;
    //queryfields的boost值
    private float[] fieldBoosts = null;
    //在进行查询前是否对查询词进行敏感性过滤
    private boolean sensitiveFilter = true;
    //默认为支持最简查询语法，支持：AND OR NOT 及"PHRASE"查询
    private QueryType queryType = QueryType.SimplestQueryGrammar;

    public SearchQuery(String query, String[] queryFileds){
        this.query = query;
        this.queryFileds = queryFileds;
        this.fieldBoosts = new float[queryFileds.length];
        Arrays.fill(this.fieldBoosts, 1.0f);
    }

    public String getQuery() {
        return query;
    }

    public String[] getQueryFileds(){
        return this.queryFileds;
    }

    public float[] getFieldBoosts(){
        return fieldBoosts;
    }

    public boolean isSensitiveFilter() {
        return sensitiveFilter;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public SearchQuery setQuery(String query) {
        this.query = query; return this;
    }

    public SearchQuery setQueryFileds(String[] queryFileds){
        this.queryFileds = queryFileds;return this;
    }

    public SearchQuery setFieldBoosts(float[] fieldBoosts){
        this.fieldBoosts = fieldBoosts; return this;
    }

    public SearchQuery setSensitiveFilter(boolean sensitiveFilter) {
        this.sensitiveFilter = sensitiveFilter;
        return this;
    }

    public SearchQuery setQueryType(QueryType queryType) {
        this.queryType = queryType;
        return this;
    }

    public static SearchQuery searchQuery(String queryStr, String[] queryFileds){
        return new SearchQuery(queryStr, queryFileds);
    }
    /**
     * @// TODO: 2016/10/25 支持更多查询语句语法 
     */
    public enum QueryType{
        SimplestQueryGrammar, //采用最简查询语句语法解析器
        termsQuery;//直接进行TermsQuery
    }
}
