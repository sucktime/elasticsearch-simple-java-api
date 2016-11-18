package zy.engine.facet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import zy.application.news.AppConfig;
import zy.engine.client.ESClientManager;
import zy.engine.director.Director;
import zy.engine.utils.DateUtil;
import zy.engine.utils.JSONLIBUtil;
import zy.engine.utils.StringUtil;

import java.util.*;

/**
 * Created by JiangGuofeng on 2016/10/27.
 * 用于将中译原来的通用接口转换为新接口
 */
public final class InterfaceAdapter {

    public static JSONObject direct(JSONObject jrequest, String appId, TransportClient client){
        Request request = null;
        if(appId.trim().toLowerCase().equals("news")){
            request = adaptFromZyNews(jrequest);
        } else if (appId.trim().toLowerCase().equals("social")){
            request = adaptFromZySocial(jrequest);
        }
        // execute
        TransportClient client_ = client;
        try {
            if (client_ == null) {
                if (appId.equalsIgnoreCase("news")) {
                    client_ = ESClientManager.getESClient(zy.application.news.AppConfig.clusterName, zy.application.news.AppConfig.nodeAddresses);
                } else if (appId.equalsIgnoreCase("social")){
                    client_ = ESClientManager.getESClient(zy.application.social.AppConfig.clusterName, zy.application.social.AppConfig.nodeAddresses);
                }
            }
        } catch (Exception e){e.printStackTrace();}
        Response response = Director.direct(request, client_, null, AppConfig.indices);
        String resultType = null;
        if (!JSONLIBUtil.isEmpty(jrequest,"resultType")){
            resultType = jrequest.getString("resultType");
        } else {
            return null;
        }
        JSONObject jresponse = adaptToZy(response,appId,resultType);
        return jresponse;
    }


    private static Request adaptFromZyNews(JSONObject jrequest) {
        // A)SearchQuery:
        String queryStr = jrequest.getString("keyword");
        String[] queryFields = null;
        float[] queryBoosts = null;
        queryFields = "titleSrc,titleEn,titleZh,abstractEn,abstractZh".split(",");
        //queryFields = "text,textEn,textZh".split(",");
        queryBoosts = new float[queryFields.length];
        Arrays.fill(queryBoosts, 1.0f);
        SearchQuery query = SearchQuery.searchQuery(queryStr, queryFields).setFieldBoosts(queryBoosts).setSensitiveFilter(true);

        // B)SearchFilter:
        SearchFilter filter = SearchFilter.searchFilter();
        // 1) date:
        parseDateFilter(filter, jrequest, "pubTime");
        // 2) countryNameZh
        parseTermsFilter(filter, jrequest, "countryNameZh", true, "countryNameZh", false);
        // 3) languageCode
        parseTermsFilter(filter, jrequest, "languageCode", true, "languageCode", false);
        // 4) mediaType
        parseTermsFilter(filter, jrequest, "mediaType", true, "mediaType", false);
        // 5)mediaNameZh
        parseTermsFilter(filter, jrequest, "mediaNameZh", true, "mediaNameZh", false);
        // 6) exclude websiteId
        parseTermsFilter(filter, jrequest, "mediaIdNot", true, "websiteId", true);
        // 7) _id
        parseTermsFilter(filter, jrequest, "_id", true, "_id", false);
        // 8) comeFrom
        parseTermsFilter(filter, jrequest, "comeFrom", true, "comeFrom", false);
        // 9) similarityId
        parseTermsFilter(filter, jrequest, "similarityId", false, "similarity", false);

        // C)SearchScorer
        SearchScorer scorer = SearchScorer.searchScorer();
        String scriptStr1 = "min(max(1,doc['pubTime'].value - currTime ) , 10)";
        Map params1 = new HashMap();
        params1.put("currTime", System.currentTimeMillis() - 86400000);
        String scriptStr2 = "exp((doc['pubTime'].value - 1413000000000L) * 0.00000000001)";
        String scriptStr3 = "doc['mediaLevel'].value";
        SearchScorer.SearchScoreScript script1 = new SearchScorer.SearchScoreScript(scriptStr1, params1);
        SearchScorer.SearchScoreScript script2 = new SearchScorer.SearchScoreScript(scriptStr2, null);
        SearchScorer.SearchScoreScript script3 = new SearchScorer.SearchScoreScript(scriptStr3, null);
        scorer.addScoreScript(script1).addScoreScript(script2).addScoreScript(script3)
                .setScoreMode("multiply");

        // D)SearchResult
        SearchResult result = SearchResult.searchResult();
        parseResult(result, jrequest, "news");

        //finnaly: Request
        Request request = Request.request().setQuery(query).setFilter(filter).setScorer(scorer).setAggregation(null).setResult(result);
        return request;
    }

    private static Request adaptFromZySocial(JSONObject jrequest){
        // A)SearchQuery:
        String queryStr = jrequest.getString("keyword");
        String[] queryFields = null;
        float[] queryBoosts = null;
        //queryFields = "titleSrc,titleEn,titleZh,abstractEn,abstractZh".split(",");
        queryFields = "text,textEn,textZh".split(",");
        queryBoosts = new float[]{5,2,2};
        SearchQuery query = SearchQuery.searchQuery(queryStr, queryFields).setFieldBoosts(queryBoosts).setSensitiveFilter(true);

        // B)SearchFilter:
        // 1) date
        SearchFilter filter = SearchFilter.searchFilter();
        parseDateFilter(filter, jrequest, "time");
        // 2) myId
        parseTermsFilter(filter, jrequest, "myId", true,"myId", false);
        // 3) country
        parseTermsFilter(filter, jrequest, "country", true, "country", false);
        // 4) sourceType
        parseTermsFilter(filter, jrequest, "sourceType", true, "sourceType", false);
        // 5) province
        parseTermsFilter(filter, jrequest, "province", true, "province", false);
        // 6) provinceId
        parseTermsFilter(filter, jrequest, "provinceId", true, "provinceId", false);
        // 7) languageCode
        parseTermsFilter(filter, jrequest, "languageCode", true, "languageCode", false);
        // 8) sentimentOrient
        parseTermsFilter(filter, jrequest, "sentimentOrient", true, "sentimentOrient", false);
        // 9) isOri
        parseTermsFilter(filter, jrequest, "isOri", true, "isOri", false);
        // 10) sourceWeiboId
        parseTermsFilter(filter, jrequest, "sourceWeiboId", true, "sourceWeiboId", false);
        // 11) name
        parseTermsFilter(filter, jrequest, "name", true, "name", false);

        // C)SearchScorer

        // D)SearchResult
        SearchResult result = SearchResult.searchResult();
        parseResult(result, jrequest, "social");

        // SearchRequest
        Request request = Request.request()
                .setQuery(query).setFilter(filter).setScorer(null).setAggregation(null).setResult(result);
        return request;
    }

    private static JSONObject adaptToZy(Response response, String appId, String resultType){
        if (response == null || response.status != 0 || response.getData() == null)
            return null;
        SearchHits shits = (SearchHits) response.getData();
        SearchHit[] searchHits = (SearchHit[]) shits.getHits();
        List<JSONObject> jsonObjects = new ArrayList<JSONObject>(searchHits.length);
        for (SearchHit hit : searchHits){
            JSONObject jobject = null;
            if (appId.equalsIgnoreCase("news")) {
                jobject = zy.application.news.ResultFormatter.convertHit2Json(hit, resultType);
            } else if (appId.equalsIgnoreCase("social")){
                jobject = zy.application.social.ResultFormatter.convertHit2Json(hit, resultType);
            }
            jsonObjects.add(zy.application.news.ResultFormatter.convertHit2Json(hit, resultType));
        }
        JSONArray jarray = JSONArray.fromObject(jsonObjects);
        JSONObject jresponse = new JSONObject();
        jresponse.put("resultList", jarray);
        jresponse.put("resultCount", shits.getTotalHits());

        return jresponse;
    }


    private static SearchFilter parseTermsFilter(SearchFilter filter, JSONObject jrequest, String jsonKey, boolean isArray, String fieldName, boolean exclude) {
        if (!JSONLIBUtil.isEmpty(jrequest, jsonKey)) {
            List<String> list = new ArrayList<String>();
            if (isArray) {
                JSONArray array = jrequest.getJSONArray(jsonKey);
                for (String ele : (String[]) array.toArray()) {
                    list.add(ele);
                }
            } else {
                String ele = jrequest.getString(jsonKey);
                list.add(ele);
            }
            if (exclude) {
                filter.addExcludeTermsFilter(new SearchFilter.Entry<String, List<?>>(fieldName, list));
            } else {
                filter.addTermsFilter(new SearchFilter.Entry<String, List<?>>(fieldName, list));
            }
        }
        return filter;
    }

    private static SearchFilter parseDateFilter(SearchFilter filter, JSONObject jrequest, String fildName){
        long beginDate = 0, endDate = 0;
        if (JSONLIBUtil.isEmpty(jrequest, "beginDate")) {
            beginDate = DateUtil.time2Unix("1970-01-01 00:00:00");
        } else {
            beginDate = DateUtil.time2Unix(jrequest.getString("beginDate"));
        }
        if (JSONLIBUtil.isEmpty(jrequest, "endDate")) {
            endDate = DateUtil.time2Unix(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        } else {
            endDate = DateUtil.time2Unix(jrequest.getString("endDate"));
        }
        return filter.addRangeFilter(new SearchFilter.Entry<String, SearchFilter.Range<?>>(fildName, new SearchFilter.Range<Object>(beginDate, endDate)));
    }

    private static SearchResult parseResult(SearchResult result, JSONObject jrequest, String appId){
        // highlight
        if (!JSONLIBUtil.isEmpty(jrequest, "highlight")){
            boolean on = false;
            on = jrequest.getBoolean("hightlight");
            if(on){
                String[] names = null;
                if (appId.equalsIgnoreCase("social")) {
                    names = new String[]{zy.application.social.Mapper.FieldWeibo.TEXT,
                            zy.application.social.Mapper.FieldWeibo.TEXTLEN,
                            zy.application.social.Mapper.FieldWeibo.TEXTZH
                    };
                }
                else if (appId.equalsIgnoreCase("news")){
                    names = new String[]{
                            zy.application.news.Mapper.FieldArticle.TITLE_SRC,
                            zy.application.news.Mapper.FieldArticle.TITLE_ZH,
                            zy.application.news.Mapper.FieldArticle.TITLE_EN,
                            zy.application.news.Mapper.FieldArticle.ABSTRACT_EN,
                            zy.application.news.Mapper.FieldArticle.ABSTRACT_ZH
                    };
                }
                result.setHighlightFiledNames(names);
            }
        }
        // sort
        if (!JSONLIBUtil.isEmpty(jrequest, "sort") && !JSONLIBUtil.isEmpty(jrequest, "fieldName")){
            String order = jrequest.getString("sort");
            String field = jrequest.getString("fieldName");
            if (!StringUtil.isEmpty(field) && !StringUtil.isEmpty(order)){
                result.setOrderFiledName(field).setOrder(order.equals("asc")? SearchResult.SortingOrder.ASC: SearchResult.SortingOrder.DESC);
            }
        }
        // paging
        if (!JSONLIBUtil.isEmpty(jrequest, "pageNo") && !JSONLIBUtil.isEmpty(jrequest, "pageSize")){
            int pageNo = jrequest.getInt("pageNO");
            int pageSize = jrequest.getInt("pageSize");
            result.setFrom((pageNo-1)*pageSize).setSize(pageSize);
        }
        // minScore
        result.setMinScore(0.2f).setFormat(SearchResult.ResultFormat.SearchHits);
        // types
        return result;
    }
}