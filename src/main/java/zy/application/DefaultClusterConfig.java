package zy.application;

/**
 * Created by JiangGuofeng on 2016/10/27.
 */
public class DefaultClusterConfig {
    //global settings
    public static String clusterName = "my-application";
    public static String[] hostNames = new String[]{"localhost"};

    public static void configCluster(String clusterName, String[] hostNames){
        DefaultClusterConfig.clusterName = clusterName;
        DefaultClusterConfig.hostNames = hostNames;
    }
}
