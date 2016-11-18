package zy.engine.director;

import org.elasticsearch.search.SearchHit;

/**
 * Created by JiangGuofeng on 2016/10/24.
 */
public interface HitFilter {
    public boolean shouldBeKilled(SearchHit hit);
}
