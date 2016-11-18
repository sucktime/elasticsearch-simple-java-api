package zy.engine.builder;

/**
 * Created by JiangGuofeng on 2016/10/20.
 */

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * 将原始查询与过滤查询项结合得到最终查询
 */
public class FilteredQueryBuilders {

    public static BoolQueryBuilder FilterBoolWithAny(BoolQueryBuilder initQuery, QueryBuilder filterQuery){
        return initQuery.filter(filterQuery);
    }

    public static BoolQueryBuilder FilterAnyWithAny(QueryBuilder initQuery, QueryBuilder filterQuery){
        return QueryBuilders.boolQuery().must(initQuery).filter(filterQuery);
    }
}
