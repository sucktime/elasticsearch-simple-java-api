package zy.engine.facet;

/**
 * Created by JiangGuofeng on 2016/10/20.
 */
public final class SearchResult {
    // 默认在index下的全部type上查询
    // String[] types = null;
    /**
     * 如果 from<0,采用es默认from设置
     * 如果 size<0,采用es默认size设置
     * 返回结果的某一区间，可以用来实现分页功能
     */
    int from = -1;
    int size = -1;
    /* 设置SearchHit中是否包含source字段，不拉回source效率会更好
    *  source 即完整的一条数据，包含数据的所有字段
    *  如果不包含source，只能从Searchhit中获得idnex、type、id、version信息
    */
    boolean fetchSource = true;
    //最小score
    float minScore = 0.1f;
    //如果设为null，不排序
    String orderFiledName = null;
    SortingOrder order = SortingOrder.ASC;
    /**设置只返回哪些字段,null则采用默认
     * Adds a field to load and return (note, it must be stored) as part of the search request.
     * If none are specified, the source of the document will be return.
     */
    String[] fields = null;
    //如果设为null，不高亮
    String[] highlightFiledNames = null;
    //默认返回所有原始结果:SearchHit[]
    ResultFormat format = ResultFormat.HitsArray;
    //开启debug时，会将执行的查询请求打印到stdout
    boolean debug = false;

    public int getFrom() {
        return from;
    }
    public SearchResult setFrom(int from) {
        this.from = from;
        return this;
    }

    public int getSize() {
        return size;
    }
    public SearchResult setSize(int size) {
        this.size = size;
        return this;
    }

    public boolean isFetchSource() {
        return fetchSource;
    }
    public SearchResult setFetchSource(boolean fetchSource) {
        this.fetchSource = fetchSource;
        return this;
    }

    public SearchResult setMinScore(float minScore){
        this.minScore = minScore;
        return this;
    }
    public float getMinScore(){
        return minScore;
    }

    public String getOrderFiledName() {
        return orderFiledName;
    }
    public SearchResult setOrderFiledName(String orderFiledName) {
        this.orderFiledName = orderFiledName;
        return this;
    }

    public SortingOrder getOrder() {
        return order;
    }
    public SearchResult setOrder(SortingOrder order) {
        this.order = order;
        return this;
    }

    public String[] getFields(){
        return fields;
    }
    public SearchResult setFields(String[] fields){
        this.fields = fields;
        return this;
    }

    public String[] getHighlightFiledNames() {
        return highlightFiledNames;
    }
    public SearchResult setHighlightFiledNames(String[] highlightFiledNames) {
        this.highlightFiledNames = highlightFiledNames;
        return this;
    }

    public ResultFormat getFormat() {
        return format;
    }
    public SearchResult setFormat(ResultFormat format) {
        this.format = format;
        return this;
    }

    public boolean isDebug(){
        return debug;
    }
    public SearchResult setDebug(boolean debug){
        this.debug = debug;
        return this;
    }

    public static SearchResult searchResult(){
        return new SearchResult();
    }

    public enum ResultFormat{
        SearchHits,
        HitsArray,
        SourceArray;
    }

    public enum SortingOrder{
        ASC,
        DESC;
    }
}


