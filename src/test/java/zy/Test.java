package zy;

import org.elasticsearch.index.query.*;
import zy.application.DefaultClusterConfig;
import zy.engine.builder.InitQueryBuilders;
import zy.engine.client.ESClientManager;
import zy.engine.director.Director;
import zy.engine.facet.*;
import zy.engine.facet.SearchScorer.SearchScoreScript;
import zy.engine.query.SimplestQueryNode;
import zy.engine.query.SimplestQueryParser;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JiangGuofeng on 2016/10/21.
 */
public class Test {

    public static void test1(){
        String txt = "adaddafa   AND asdad    OR ada" +
                "\ndafa";
        String[] txts = txt.split("\\s+");
        for(String t : txts){
            System.out.print(t+"-");
        }
    }

    public static void test2(){
        System.out.println("阿凡达dadsadJI".toUpperCase());
    }

    public static void test3(){
        //String query = "not and or not 我 的 AND OR NOT 你 OR NOT hello AND not kitty And 功夫 not查了 \"\" 打开的那";
        String query = "我的 \"苹果\" OR 华为";
        String[] tokens = SimplestQueryParser.supplyTokens(query);
        tokens = SimplestQueryParser.checkWords(tokens);
        for (String t : tokens){
            System.out.print(t+"-");
        }
        System.out.println();
        tokens = SimplestQueryParser.cleanTokens(tokens);
        for (String t : tokens){
            System.out.print(t+"-");
        }
    }

    public static void test3_5(){
        //String query = "not and or not 我 的 AND OR NOT 你 OR NOT hello AND not kitty And 功夫 not查了 \"\" 打开的那";
        String query = "我的 \"苹果\" OR 华为";
        SimplestQueryNode root = SimplestQueryParser.parseRaw(query);
        System.out.println(root);
    }

    public static void test4(){
        String query = "not and or not 我 的 AND OR NOT 你 OR NOT hello AND not kitty And 功夫 not查了 \"\" \"打开的那\"";
        SimplestQueryNode root = SimplestQueryParser.parseRaw(query);
        System.out.println(root.toString());

        query = InitQueryBuilders.parseRoot(root);
        System.out.println(query);

        QueryStringQueryBuilder queryBuilder = InitQueryBuilders.queryStringQueryBuilder(new String[]{"f1", "f2"}, root, new float[]{1.0f, 2.0f});
        System.out.println(queryBuilder.toString());
    }

    public static void test5(){
        String query = "我的 小伙伴 呀呀呀呀";

        QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(query)
                .operator(MatchQueryBuilder.Operator.AND)
                .type(MultiMatchQueryBuilder.Type.PHRASE);

        System.out.println(queryBuilder.toString());
    }

    public static void test6(){
        SearchQuery searchQuery = new SearchQuery("我的 \"苹果\" OR 华为", new String[]{"f1", "f2"});
        SearchFilter searchFilter = new SearchFilter();
        searchFilter.addRangeFilter(new SearchFilter.Entry<String, SearchFilter.Range<?>>("field1", new SearchFilter.Range<Object>(1,10)));
        searchFilter.addTermsFilter(new SearchFilter.Entry<String, List<?>>("field2", Arrays.asList(new Integer[]{1,2,3})));
        SearchScorer searchScorer = new SearchScorer();
        String script = "(2.3+doc['f1']/(1.0+p1))";
        Map map = new HashMap();
        map.put("p1", 2.5);
        searchScorer.addScoreScript(new SearchScoreScript(script, map));
        searchScorer.setBoostMode("replace");
        searchScorer.setScoreMode("multiply");
        SearchResult searchResult = new SearchResult();
        searchResult.setFrom(0);
        searchResult.setSize(100);
        searchResult.setDebug(true);
        searchResult.setFetchSource(true);
        searchResult.setHighlightFiledNames(new String[]{"f1","f2"});
        searchResult.setOrderFiledName("f1");
        searchResult.setOrder(SearchResult.SortingOrder.ASC);
        searchResult.setFormat(SearchResult.ResultFormat.HitsArray);

        Request request = new Request();
        request.searchQuery = searchQuery;
        request.searchFilter = searchFilter;
        request.searchScorer = searchScorer;
        request.searchResult = searchResult;

        Response response = Director.directSearchBuilder(request);
        System.out.println(response);
    }

    public static void test7(){
        SearchQuery   searchQuery = SearchQuery.searchQuery("我的 \"苹果\" OR 华为", new String[]{"f1", "f2"})
                .setFieldBoosts(new float[]{1.0f,2.2f}).setSensitiveFilter(false);
        SearchFilter searchFilter = SearchFilter.searchFilter()
                .addRangeFilter(new SearchFilter.Entry<String, SearchFilter.Range<?>>("field1", new SearchFilter.Range<Object>(1,10)))
                .addTermsFilter(new SearchFilter.Entry<String, List<?>>("field2", Arrays.asList(new Integer[]{1,2,3})));
        String script = "(2.3+doc['f1'].value/(1.0+p1))";
        Map params = new HashMap();
        params.put("p1", 2.5);
        SearchScorer searchScorer = SearchScorer.searchScorer()
                .addScoreScript(new SearchScoreScript(script, params))
                .setBoostMode("replace")
                .setScoreMode("multiply");
        SearchResult searchResult = SearchResult.searchResult()
                .setFrom(0).setSize(100).setFetchSource(true)
                .setDebug(true)
                .setHighlightFiledNames(new String[]{"f1","f2"})
                .setOrderFiledName("f1").setOrder(SearchResult.SortingOrder.ASC);

        Request request = Request.request()
                .setQuery(searchQuery).setFilter(searchFilter)
                .setScorer(searchScorer).setAggregation(null).setResult(searchResult);
        Response response = null;
        try {
            response = Director.direct(request, ESClientManager.getESClient(DefaultClusterConfig.clusterName, InetAddress.getAllByName(DefaultClusterConfig.hostNames[0])), new String[]{"test"}, null);
            System.out.println(response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        test7();
    }


}
