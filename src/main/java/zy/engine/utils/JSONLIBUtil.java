package zy.engine.utils;

import net.sf.json.JSONObject;

/**
 * Created by JiangGuofeng on 2016/10/27.
 */
public class JSONLIBUtil {
    public static boolean isEmpty(JSONObject json, String key){
        return !json.containsKey(key) || json.get(key)==null;
    }
}
