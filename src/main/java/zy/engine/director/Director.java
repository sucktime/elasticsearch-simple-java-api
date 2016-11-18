package zy.engine.director;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import zy.engine.builder.*;
import zy.engine.facet.*;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

/**
 * Created by JiangGuofeng on 2016/10/24.
 */
public class Director {


    /**
     * @param request
     * @param client
     * @param indices
     * @return 如果response.status=0, response.data即为检索结果SearchHit[]
     * 
     * @// TODO: 2016/10/28 type在何处设置咧
     */
    public static Response direct(Request request, TransportClient client, String[] indices, String[] types){
        try {
            /**
             * 构造QueryBuilder
             */
            QueryBuilder queryBuilder = null;
            Response response = directSearchBuilder(request);
            if (response.getStatus() == Response.SUCCESS) {
                queryBuilder = (QueryBuilder) response.getData();
            } else {
                return response;
            }
            /**
             * 构造SearchRequestBuilder
             */
            SearchResult result = request.searchResult;
            if (result.isDebug()){
                System.out.println("[QueryBuilder]:\n"+queryBuilder.toString());
            }
            if(result == null){
                return new Response(330, "searchRequest is null.", null);
            }
            SearchRequestBuilder srb = SearchRequestBuilders.searchRequestBuilder(client, indices, types, result);
            if (result.isDebug()){
                System.out.println("[SearchRequetBuilder]:\n"+srb.toString());
            }
            /**
             * 执行检索，返回结果
             */
            SearchResponse searchResponse =  srb.execute().actionGet();
            if (result.getFormat() == SearchResult.ResultFormat.SearchHits){
                return new Response(0, "success", searchResponse.getHits());
            }
            else if (result.getFormat() == SearchResult.ResultFormat.HitsArray){
                return new Response(0, "success", searchResponse.getHits().getHits());
            } else{
                return new Response(505, "resultFormat, not supportted yet", null);
            }
        } catch (Exception e){
            e.printStackTrace();
            return new Response(340, e.toString(), null);
        }
    }

    /**
     * 根据Request生成最终可用的QueryBuidler
     * @param request
     * @return 如果response.status=0, response.data即为生成的QueryBuilder
     */
    public static Response directSearchBuilder(Request request){

        try {
            /**
             * step1: load on FilteredQueryBuilder
             */
            SearchQuery query = request.searchQuery;
            SearchFilter filter = request.searchFilter;
            if (query == null && filter == null) {
                return new Response(300, "query and filter shall not be null at the same time.", null);
            }
            QueryBuilder initQueryBuilder = null;
            QueryBuilder filterQueryBuilder = null;
            if (query == null) {
                initQueryBuilder = matchAllQuery();
            } else {
                initQueryBuilder = InitQueryBuilders.initQueryBuilder(query);
            }
            if (filter == null){
                filterQueryBuilder = matchAllQuery();
            } else {
                filterQueryBuilder = FilterQueryBuilders.filterQueryBuilder(filter);
            }
            QueryBuilder filteredQueryBuilder = null;
            if (initQueryBuilder instanceof BoolQueryBuilder){
                filteredQueryBuilder = FilteredQueryBuilders.FilterBoolWithAny((BoolQueryBuilder) initQueryBuilder, filterQueryBuilder);
            } else {
                filteredQueryBuilder = FilteredQueryBuilders.FilterAnyWithAny(initQueryBuilder, filterQueryBuilder);
            }

            /**
             * step2: function score
             */
            SearchScorer scorer = request.searchScorer;
            QueryBuilder scoredQueryBuilder = null;
            if (scorer != null){
                scoredQueryBuilder = ScoredQueryBuilders.functionScoreQueryBuilder(filteredQueryBuilder,scorer);
            }

            /**
             * step3: return final QueryBuilder
             */
            return new Response(0, "success", scoredQueryBuilder == null ? filteredQueryBuilder : scoredQueryBuilder);
        } catch (Exception e){
            e.printStackTrace();
            return new Response(303, e.toString(), null);
        }
    }
}
