package zy.engine.builder;

/**
 * Created by JiangGuofeng on 2016/10/20.
 */

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import zy.engine.facet.SearchFilter;
import zy.engine.facet.SearchFilter.Entry;
import zy.engine.facet.SearchFilter.Range;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * 根据请求中的过滤条件构造过滤查询
 */
public class FilterQueryBuilders {

    /**
     * @provided-for-director
     * @param filter
     * @return
     */
    public static BoolQueryBuilder filterQueryBuilder(SearchFilter filter){
        return FilterQueryBuilders.filterWithBool(filter);
    }

    public static BoolQueryBuilder filterWithBool(SearchFilter searchFilter){

        BoolQueryBuilder boolQueryBuilder = boolQuery();

        for(Entry<String, List<?>> tFilter : searchFilter.getTermsFilters()){
            String fieldName = tFilter.getKey();
            Object[] values = tFilter.getValue().toArray(new Object[]{});
            TermsQueryBuilder termQueryBuilder = termsQuery(fieldName, values);
            boolQueryBuilder.must(termQueryBuilder);
        }

        for(Entry<String, Range<?>> rFielter : searchFilter.getRangeFilters()){
            String fieldName = rFielter.getKey();
            Object from = rFielter.getValue().getFrom();
            Object to = rFielter.getValue().getTo();
            RangeQueryBuilder rangeQueryBuilder = rangeQuery(fieldName);
            if (from != null && to != null){
                rangeQueryBuilder.from(from);
                rangeQueryBuilder.to(to);
            } else if (from == null){
                rangeQueryBuilder.lte(to);
            } else {
                rangeQueryBuilder.gte(from);
            }
            boolQueryBuilder.must(rangeQueryBuilder);
        }

        for (Entry<String, List<?>> extFilter : searchFilter.getExcludeTermsFilters()){
            String fieldName = extFilter.getKey();
            Object[] values = extFilter.getValue().toArray(new Object[]{});
            TermsQueryBuilder termsQueryBuilder = termsQuery(fieldName, values);
            boolQueryBuilder.mustNot(termsQueryBuilder);
        }

        return boolQueryBuilder;
    }
}
