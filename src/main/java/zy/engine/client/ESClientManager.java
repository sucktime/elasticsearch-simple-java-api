package zy.engine.client;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.util.HashMap;

/**
 * Created by JiangGuofeng on 2016/10/20.
 *//**
 * Created by bomb on 2016/9/25.
 * 创建和池化管理 ES client
 * TOGO: 判断closed
 */
public class ESClientManager {

    private static HashMap<String,TransportClient> clientMap = new HashMap(8);
    private static final int TCP_PORT = 9300;

    private ESClientManager(){}

    public static synchronized TransportClient getESClient(String clusterName, String[] adddressNames) throws Exception{
        InetAddress[] addresses = new InetAddress[adddressNames.length];
        for (int i = 0; i < adddressNames.length; i++) {
            addresses[i] = InetAddress.getByName(adddressNames[i]);
        }
        return getESClient(clusterName, addresses);
    }

    public static synchronized TransportClient getESClient(String clusterName, InetAddress address) throws Exception{
        if (clientMap.containsKey(clusterName)){
            return clientMap.get(clusterName);
        }
        else {
            Settings settings = Settings.settingsBuilder().
                    put("client.transport.sniff", "true")
                    .put("cluster.name", clusterName)
                    .build();
            TransportClient client = TransportClient.builder().settings(settings).build()
                    .addTransportAddress(new InetSocketTransportAddress(address, TCP_PORT));
            clientMap.put(clusterName, client);
            return client;
        }
    }

    public static synchronized TransportClient getESClient(String clusterName, InetAddress[] addresses) throws Exception{
        if (clientMap.containsKey(clusterName))
            return clientMap.get(clusterName);
        else {
            Settings settings = Settings.settingsBuilder().
                    put("client.transport.sniff", "true")
                    .put("cluster.name", clusterName)
                    .build();
            TransportClient client = TransportClient.builder().settings(settings).build();
            for (InetAddress address : addresses){
                client.addTransportAddress(new InetSocketTransportAddress(address, TCP_PORT));
            }
            clientMap.put(clusterName, client);
            return client;
        }
    }

}
