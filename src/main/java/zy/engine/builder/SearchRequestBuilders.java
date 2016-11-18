package zy.engine.builder;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import zy.engine.facet.SearchResult;
import zy.engine.utils.ArrayUtil;
import zy.engine.utils.StringUtil;

/**
 * Created by JiangGuofeng on 2016/10/25.
 */
public class SearchRequestBuilders {

    /**
     * @param client
     * @param indices
     * @param searchResult
     * @return
     * @provided-for-director
     */
    public static SearchRequestBuilder searchRequestBuilder(TransportClient client, String[] indices, String[] types, SearchResult searchResult) {
        assert client != null && !ArrayUtil.isEmpty(indices);

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch();
        if (!ArrayUtil.isEmpty(indices)){
            searchRequestBuilder.setIndices(indices);
        } else {
            return null;
        }
        if (!ArrayUtil.isEmpty(types)){
            searchRequestBuilder.setTypes(types);
        }
        searchRequestBuilder.setFetchSource(searchResult.isFetchSource()).setMinScore(searchResult.getMinScore());
        if ((searchResult.getFrom() >= 0)) {
            searchRequestBuilder.setFrom(searchResult.getFrom());
        }
        if (searchResult.getSize() >= 0) {
            searchRequestBuilder.setSize(searchResult.getSize());
        }
        if (!StringUtil.isEmpty(searchResult.getOrderFiledName())) {
            searchRequestBuilder.addSort(SortingBuilders.fieldSortBuilder(searchResult.getOrderFiledName(), searchResult.getOrder()));
        } else {
            searchRequestBuilder.addSort(SortingBuilders.scoreSortBuilder());
        }
        if (searchResult.getFields() != null) {
            for (String field : searchResult.getFields()) {
                searchRequestBuilder.addField(field);
            }
        }
        for (String fieldName : searchResult.getHighlightFiledNames()) {
            searchRequestBuilder.addHighlightedField(fieldName);
        }
        return searchRequestBuilder;
    }
}
