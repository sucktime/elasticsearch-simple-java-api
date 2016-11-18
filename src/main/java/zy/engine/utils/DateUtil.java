package zy.engine.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by JiangGuofeng on 2016/10/27.
 */
public class DateUtil {

    public static long time2Unix(String  date){
        long epoch = 0;
        try {
            epoch = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date).getTime() ; // / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return epoch;
    }

    public static String formatDate(Date date, String format){
        if(date==null || "".equals(date)) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
}
