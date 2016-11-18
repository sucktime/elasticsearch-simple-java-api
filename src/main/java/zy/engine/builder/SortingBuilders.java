package zy.engine.builder;

import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import zy.engine.facet.SearchResult;

/**
 * Created by JiangGuofeng on 2016/10/25.
 */
public class SortingBuilders {

    public static ScoreSortBuilder scoreSortBuilder(){
        return SortBuilders.scoreSort();
    }

    public static FieldSortBuilder fieldSortBuilder(String fieldName, SearchResult.SortingOrder order){

       return SortBuilders.fieldSort(fieldName).order(order == SearchResult.SortingOrder.ASC ? SortOrder.ASC : SortOrder.DESC);
    }
}
