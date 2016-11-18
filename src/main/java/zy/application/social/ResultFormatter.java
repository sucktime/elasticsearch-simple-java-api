package zy.application.social;

import net.sf.json.JSONObject;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;

import java.util.Map;
/**
 * Created by JiangGuofeng on 2016/10/28.
 */
public class ResultFormatter {

    public static JSONObject convertHit2Json(SearchHit hit, String type){
            if(type==null || "".equalsIgnoreCase(type)) return null;
            if(type.equalsIgnoreCase("front")) return getResult4Front(hit);
            if(type.equalsIgnoreCase("analysis")) return getResult4Analysis(hit);
            if(type.equalsIgnoreCase("detail")) return getResult4Detail(hit);
            return null;
    }

    /**
     * 返回前端页面检索所需的字段
     * @param sh
     * @return
     */
    private static JSONObject getResult4Front(SearchHit sh){
        Map<String, Object> map=sh.getSource();
        Map<String, HighlightField> fieldMap=sh.getHighlightFields();
		/*
		for(String key:fieldMap.keySet()){
			System.out.println(fieldMap.get(key).fragments()[0].string());
		}*/
        JSONObject obj=new JSONObject();

        obj.put(Mapper.FieldWeibo.UUID, map.get(Mapper.FieldWeibo.UUID));
        obj.put(Mapper.FieldWeibo.ID, map.get(Mapper.FieldWeibo.ID));
        obj.put(Mapper.FieldWeibo.TIME, map.get(Mapper.FieldWeibo.TIME));
        obj.put(Mapper.FieldWeibo.TIMESTR, map.get(Mapper.FieldWeibo.TIMESTR));

        obj.put(Mapper.FieldWeibo.TEXTLEN, map.get(Mapper.FieldWeibo.TEXTLEN));
        obj.put(Mapper.FieldWeibo.ISORI, map.get(Mapper.FieldWeibo.ISORI));
        obj.put(Mapper.FieldWeibo.SOURCEID, map.get(Mapper.FieldWeibo.SOURCEID));
        obj.put(Mapper.FieldWeibo.USERID, map.get(Mapper.FieldWeibo.USERID));
        obj.put(Mapper.FieldWeibo.NAME, map.get(Mapper.FieldWeibo.NAME));
        obj.put(Mapper.FieldWeibo.GENDER, map.get(Mapper.FieldWeibo.GENDER));
        obj.put(Mapper.FieldWeibo.PROVINCEID, map.get(Mapper.FieldWeibo.PROVINCEID));
        obj.put(Mapper.FieldWeibo.PROVINCE, map.get(Mapper.FieldWeibo.PROVINCE));
        obj.put(Mapper.FieldWeibo.CITYID, map.get(Mapper.FieldWeibo.CITYID));
        obj.put(Mapper.FieldWeibo.CITY, map.get(Mapper.FieldWeibo.CITY));
        obj.put(Mapper.FieldWeibo.VERIFIED, map.get(Mapper.FieldWeibo.VERIFIED));
        obj.put(Mapper.FieldWeibo.VERIFIEDREASON, map.get(Mapper.FieldWeibo.VERIFIEDREASON));
        obj.put(Mapper.FieldWeibo.USERTYPE, map.get(Mapper.FieldWeibo.USERTYPE));
        obj.put(Mapper.FieldWeibo.FLWCNT, map.get(Mapper.FieldWeibo.FLWCNT));
        obj.put(Mapper.FieldWeibo.FRDCNT, map.get(Mapper.FieldWeibo.FRDCNT));
        obj.put(Mapper.FieldWeibo.STACNT, map.get(Mapper.FieldWeibo.STACNT));
        obj.put(Mapper.FieldWeibo.USERAVATAR, map.get(Mapper.FieldWeibo.USERAVATAR));
        obj.put(Mapper.FieldWeibo.RPSCNT, map.get(Mapper.FieldWeibo.RPSCNT));
        obj.put(Mapper.FieldWeibo.CMTCNT, map.get(Mapper.FieldWeibo.CMTCNT));
        obj.put(Mapper.FieldWeibo.ATDCNT, map.get(Mapper.FieldWeibo.ATDCNT));
        obj.put(Mapper.FieldWeibo.COMMENTSINCE, map.get(Mapper.FieldWeibo.COMMENTSINCE));
        obj.put(Mapper.FieldWeibo.REPOSTSINCE, map.get(Mapper.FieldWeibo.REPOSTSINCE));
        obj.put(Mapper.FieldWeibo.UPDATETIME, map.get(Mapper.FieldWeibo.UPDATETIME));
        obj.put(Mapper.FieldWeibo.UPDATETIMESTR, map.get(Mapper.FieldWeibo.UPDATETIMESTR));



        obj.put(Mapper.FieldWeibo.SENTIMENT, map.get(Mapper.FieldWeibo.SENTIMENT));
        obj.put(Mapper.FieldWeibo.SENTIMENTORIENT, map.get(Mapper.FieldWeibo.SENTIMENTORIENT));
        obj.put(Mapper.FieldWeibo.PRODUCTS, map.get(Mapper.FieldWeibo.PRODUCTS));
        obj.put(Mapper.FieldWeibo.COMPANIES, map.get(Mapper.FieldWeibo.COMPANIES));
        obj.put(Mapper.FieldWeibo.LANGUAGECODE, map.get(Mapper.FieldWeibo.LANGUAGECODE));

        obj.put(Mapper.FieldWeibo.SOURCETYPE, map.get(Mapper.FieldWeibo.SOURCETYPE));
        obj.put(Mapper.FieldWeibo.COUNTRY, map.get(Mapper.FieldWeibo.COUNTRY));
        obj.put(Mapper.FieldWeibo.USERTAG, map.get(Mapper.FieldWeibo.USERTAG));

        obj.put(Mapper.FieldWeibo.URL, map.get(Mapper.FieldWeibo.URL));

        //*************************TEXT**************************
        HighlightField textHigh=fieldMap.get(Mapper.FieldWeibo.TEXT);
        String text="";
        if(textHigh==null){
            obj.put(Mapper.FieldWeibo.TEXT, map.get(Mapper.FieldWeibo.TEXT));
        }else{
            text=textHigh.fragments()[0].string().replaceAll("<\\\\/em>(.)<em>", "$1");
            obj.put(Mapper.FieldWeibo.TEXT, text);
        }
        //*************************TEXT**************************

        //*************************TEXTEN**************************
        HighlightField textEnHigh=fieldMap.get(Mapper.FieldWeibo.TEXTEN);
        String textEn="";
        if(textEnHigh==null){
            obj.put(Mapper.FieldWeibo.TEXTEN, map.get(Mapper.FieldWeibo.TEXTEN));
        }else{
            textEn=textEnHigh.fragments()[0].string();
            obj.put(Mapper.FieldWeibo.TEXTEN, textEn);
        }
        //*************************TEXT**************************

        //*************************TEXTZH**************************
        HighlightField textZhHigh=fieldMap.get(Mapper.FieldWeibo.TEXTZH);
        String textZh="";
        if(textZhHigh==null){
            obj.put(Mapper.FieldWeibo.TEXTZH, map.get(Mapper.FieldWeibo.TEXTZH));
        }else{
            textZh=textZhHigh.fragments()[0].string().replaceAll("<\\\\/em>(.)<em>", "$1");
            obj.put(Mapper.FieldWeibo.TEXTZH, textZh);
        }
        //*************************TEXTEN**************************
        return obj;
    }

    private static JSONObject getResult4Analysis(SearchHit sh){
        Map<String, Object> map=sh.getSource();
        Map<String, HighlightField> fieldMap=sh.getHighlightFields();
		/*
		for(String key:fieldMap.keySet()){
			System.out.println(fieldMap.get(key).fragments()[0].string());
		}*/
        JSONObject obj=new JSONObject();

        obj.put(Mapper.FieldWeibo.UUID, map.get(Mapper.FieldWeibo.UUID));
        obj.put(Mapper.FieldWeibo.ID, map.get(Mapper.FieldWeibo.ID));
        obj.put(Mapper.FieldWeibo.TIME, map.get(Mapper.FieldWeibo.TIME));
        obj.put(Mapper.FieldWeibo.TIMESTR, map.get(Mapper.FieldWeibo.TIMESTR));

        obj.put(Mapper.FieldWeibo.TEXTLEN, map.get(Mapper.FieldWeibo.TEXTLEN));
        obj.put(Mapper.FieldWeibo.ISORI, map.get(Mapper.FieldWeibo.ISORI));
        obj.put(Mapper.FieldWeibo.SOURCEID, map.get(Mapper.FieldWeibo.SOURCEID));
        obj.put(Mapper.FieldWeibo.USERID, map.get(Mapper.FieldWeibo.USERID));
        obj.put(Mapper.FieldWeibo.NAME, map.get(Mapper.FieldWeibo.NAME));
        obj.put(Mapper.FieldWeibo.GENDER, map.get(Mapper.FieldWeibo.GENDER));
        obj.put(Mapper.FieldWeibo.PROVINCEID, map.get(Mapper.FieldWeibo.PROVINCEID));
        obj.put(Mapper.FieldWeibo.PROVINCE, map.get(Mapper.FieldWeibo.PROVINCE));
        obj.put(Mapper.FieldWeibo.CITYID, map.get(Mapper.FieldWeibo.CITYID));
        obj.put(Mapper.FieldWeibo.CITY, map.get(Mapper.FieldWeibo.CITY));
        obj.put(Mapper.FieldWeibo.VERIFIED, map.get(Mapper.FieldWeibo.VERIFIED));
        obj.put(Mapper.FieldWeibo.VERIFIEDREASON, map.get(Mapper.FieldWeibo.VERIFIEDREASON));
        obj.put(Mapper.FieldWeibo.USERTYPE, map.get(Mapper.FieldWeibo.USERTYPE));
        obj.put(Mapper.FieldWeibo.FLWCNT, map.get(Mapper.FieldWeibo.FLWCNT));
        obj.put(Mapper.FieldWeibo.FRDCNT, map.get(Mapper.FieldWeibo.FRDCNT));
        obj.put(Mapper.FieldWeibo.STACNT, map.get(Mapper.FieldWeibo.STACNT));
        obj.put(Mapper.FieldWeibo.USERAVATAR, map.get(Mapper.FieldWeibo.USERAVATAR));
        obj.put(Mapper.FieldWeibo.RPSCNT, map.get(Mapper.FieldWeibo.RPSCNT));
        obj.put(Mapper.FieldWeibo.CMTCNT, map.get(Mapper.FieldWeibo.CMTCNT));
        obj.put(Mapper.FieldWeibo.ATDCNT, map.get(Mapper.FieldWeibo.ATDCNT));
        obj.put(Mapper.FieldWeibo.COMMENTSINCE, map.get(Mapper.FieldWeibo.COMMENTSINCE));
        obj.put(Mapper.FieldWeibo.REPOSTSINCE, map.get(Mapper.FieldWeibo.REPOSTSINCE));
        obj.put(Mapper.FieldWeibo.UPDATETIME, map.get(Mapper.FieldWeibo.UPDATETIME));
        obj.put(Mapper.FieldWeibo.UPDATETIMESTR, map.get(Mapper.FieldWeibo.UPDATETIMESTR));

        obj.put(Mapper.FieldWeibo.SENTIMENT, map.get(Mapper.FieldWeibo.SENTIMENT));
        obj.put(Mapper.FieldWeibo.SENTIMENTORIENT, map.get(Mapper.FieldWeibo.SENTIMENTORIENT));
        obj.put(Mapper.FieldWeibo.PRODUCTS, map.get(Mapper.FieldWeibo.PRODUCTS));
        obj.put(Mapper.FieldWeibo.COMPANIES, map.get(Mapper.FieldWeibo.COMPANIES));
        obj.put(Mapper.FieldWeibo.LANGUAGECODE, map.get(Mapper.FieldWeibo.LANGUAGECODE));

        obj.put(Mapper.FieldWeibo.TEXT, map.get(Mapper.FieldWeibo.TEXT));
        obj.put(Mapper.FieldWeibo.TEXTEN, map.get(Mapper.FieldWeibo.TEXTEN));
        obj.put(Mapper.FieldWeibo.TEXTZH, map.get(Mapper.FieldWeibo.TEXTZH));

        obj.put(Mapper.FieldWeibo.SOURCETYPE, map.get(Mapper.FieldWeibo.SOURCETYPE));
        obj.put(Mapper.FieldWeibo.COUNTRY, map.get(Mapper.FieldWeibo.COUNTRY));
        obj.put(Mapper.FieldWeibo.USERTAG, map.get(Mapper.FieldWeibo.USERTAG));

        obj.put(Mapper.FieldWeibo.URL, map.get(Mapper.FieldWeibo.URL));

        return obj;


    }

    /***
     * 详情，需要获取Comment
     * @param sh
     * @return
     */
    private static JSONObject getResult4Detail(SearchHit sh){
        Map<String, Object> map=sh.getSource();

        JSONObject obj=new JSONObject();

        obj.put(Mapper.FieldWeibo.UUID, map.get(Mapper.FieldWeibo.UUID));
        obj.put(Mapper.FieldWeibo.ID, map.get(Mapper.FieldWeibo.ID));
        obj.put(Mapper.FieldWeibo.TIME, map.get(Mapper.FieldWeibo.TIME));
        obj.put(Mapper.FieldWeibo.TIMESTR, map.get(Mapper.FieldWeibo.TIMESTR));

        obj.put(Mapper.FieldWeibo.TEXTLEN, map.get(Mapper.FieldWeibo.TEXTLEN));
        obj.put(Mapper.FieldWeibo.ISORI, map.get(Mapper.FieldWeibo.ISORI));
        obj.put(Mapper.FieldWeibo.SOURCEID, map.get(Mapper.FieldWeibo.SOURCEID));
        obj.put(Mapper.FieldWeibo.USERID, map.get(Mapper.FieldWeibo.USERID));
        obj.put(Mapper.FieldWeibo.NAME, map.get(Mapper.FieldWeibo.NAME));
        obj.put(Mapper.FieldWeibo.GENDER, map.get(Mapper.FieldWeibo.GENDER));
        obj.put(Mapper.FieldWeibo.PROVINCEID, map.get(Mapper.FieldWeibo.PROVINCEID));
        obj.put(Mapper.FieldWeibo.PROVINCE, map.get(Mapper.FieldWeibo.PROVINCE));
        obj.put(Mapper.FieldWeibo.CITYID, map.get(Mapper.FieldWeibo.CITYID));
        obj.put(Mapper.FieldWeibo.CITY, map.get(Mapper.FieldWeibo.CITY));
        obj.put(Mapper.FieldWeibo.VERIFIED, map.get(Mapper.FieldWeibo.VERIFIED));
        obj.put(Mapper.FieldWeibo.VERIFIEDREASON, map.get(Mapper.FieldWeibo.VERIFIEDREASON));
        obj.put(Mapper.FieldWeibo.USERTYPE, map.get(Mapper.FieldWeibo.USERTYPE));
        obj.put(Mapper.FieldWeibo.FLWCNT, map.get(Mapper.FieldWeibo.FLWCNT));
        obj.put(Mapper.FieldWeibo.FRDCNT, map.get(Mapper.FieldWeibo.FRDCNT));
        obj.put(Mapper.FieldWeibo.STACNT, map.get(Mapper.FieldWeibo.STACNT));
        obj.put(Mapper.FieldWeibo.USERAVATAR, map.get(Mapper.FieldWeibo.USERAVATAR));
        obj.put(Mapper.FieldWeibo.RPSCNT, map.get(Mapper.FieldWeibo.RPSCNT));
        obj.put(Mapper.FieldWeibo.CMTCNT, map.get(Mapper.FieldWeibo.CMTCNT));
        obj.put(Mapper.FieldWeibo.ATDCNT, map.get(Mapper.FieldWeibo.ATDCNT));
        obj.put(Mapper.FieldWeibo.COMMENTSINCE, map.get(Mapper.FieldWeibo.COMMENTSINCE));
        obj.put(Mapper.FieldWeibo.REPOSTSINCE, map.get(Mapper.FieldWeibo.REPOSTSINCE));
        obj.put(Mapper.FieldWeibo.UPDATETIME, map.get(Mapper.FieldWeibo.UPDATETIME));
        obj.put(Mapper.FieldWeibo.UPDATETIMESTR, map.get(Mapper.FieldWeibo.UPDATETIMESTR));

        obj.put(Mapper.FieldWeibo.SENTIMENT, map.get(Mapper.FieldWeibo.SENTIMENT));
        obj.put(Mapper.FieldWeibo.SENTIMENTORIENT, map.get(Mapper.FieldWeibo.SENTIMENTORIENT));
        obj.put(Mapper.FieldWeibo.PRODUCTS, map.get(Mapper.FieldWeibo.PRODUCTS));
        obj.put(Mapper.FieldWeibo.COMPANIES, map.get(Mapper.FieldWeibo.COMPANIES));
        obj.put(Mapper.FieldWeibo.LANGUAGECODE, map.get(Mapper.FieldWeibo.LANGUAGECODE));

        obj.put(Mapper.FieldWeibo.TEXT, map.get(Mapper.FieldWeibo.TEXT));
        obj.put(Mapper.FieldWeibo.TEXTEN, map.get(Mapper.FieldWeibo.TEXTEN));
        obj.put(Mapper.FieldWeibo.TEXTZH, map.get(Mapper.FieldWeibo.TEXTZH));

        obj.put(Mapper.FieldWeibo.SOURCETYPE, map.get(Mapper.FieldWeibo.SOURCETYPE));
        obj.put(Mapper.FieldWeibo.COUNTRY, map.get(Mapper.FieldWeibo.COUNTRY));
        obj.put(Mapper.FieldWeibo.USERTAG, map.get(Mapper.FieldWeibo.USERTAG));

        obj.put(Mapper.FieldWeibo.URL, map.get(Mapper.FieldWeibo.URL));

        return obj;
    }

}
