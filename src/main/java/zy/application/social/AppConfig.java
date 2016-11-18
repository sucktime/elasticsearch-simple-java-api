package zy.application.social;

/**
 * Created by JiangGuofeng on 2016/10/27.
 */
public final class AppConfig {
    // cluster:
    public static String clusterName = "es_total";
    public static String[] nodeAddresses = new String[]{};
    public static String[] indices = new String[]{};

    //query settings:
    public static String queryType = "SimplestQueryGrammar";
    public static String[] queryFields = new String[]{};
    public static float[] fieldBoosts = new float[]{};
    public static boolean checkSensitive = false;

    //fliler settigns:

    //score Settings:
    public static String scoreMode = "multiply";
    public static String boostMode = "multiply";

    //aggregatioin settings:

    //result setting
    public static int from = -1;
    public static int size = -1;
    public static boolean fetchSource = true;
    public static float minScore = 0.1f;
    public static String[] fileds = new String[]{};
    public static String[] highlightFiledNames = new String[]{};
    public static String sortField = "";
    public static String sortOrder = "ASC";
    public static String resultFormat = "HitsArray";

    public static void configApp(String clusterName, String[] nodeAddresses, String[] indices){
        AppConfig.clusterName = clusterName;
        AppConfig.nodeAddresses = nodeAddresses;
        AppConfig.indices = indices;
    }
}
