package zy.engine.facet;

/**
 * Created by JiangGuofeng on 2016/10/20.
 */
public class Request {
    /**
     * @searchQuery与searchFilter不可同时为null
     */
    public SearchQuery searchQuery = null;
    public SearchFilter searchFilter = null;
    /**
     *@searchScore可以为null
     */
    public SearchScorer searchScorer = null;
    /*SearchAggregation和SearchResult必须一个null一格非null??
    * aggregation 1.0不提供
    */
    public SearchAggregation searchAggr = null;
    /**
     * @searchResult不可为null
     */
    public SearchResult searchResult = null;

    public Request(){}
    public Request(SearchQuery searchQuery, SearchFilter searchFilter, SearchScorer searchScorer, SearchAggregation searchAggr, SearchResult searchResult) {
        this.searchQuery = searchQuery;
        this.searchFilter = searchFilter;
        this.searchScorer = searchScorer;
        this.searchAggr = searchAggr;
        this.searchResult = searchResult;
    }

    public Request setQuery(SearchQuery searchQuery){
        this.searchQuery = searchQuery;
        return this;
    }
    public Request setFilter(SearchFilter searchFilter){
        this.searchFilter = searchFilter;
        return this;
    }
    public Request setScorer(SearchScorer searchScorer){
        this.searchScorer = searchScorer;
        return this;
    }
    public Request setAggregation(SearchAggregation searchAggr){
        this.searchAggr = searchAggr;
        return this;
    }
    public Request setResult(SearchResult searchResult){
        this.searchResult = searchResult;
        return this;
    }

    public static Request request(){
        return new Request();
    }
}
