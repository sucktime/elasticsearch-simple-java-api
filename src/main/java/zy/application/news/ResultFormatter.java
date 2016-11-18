package zy.application.news;

import net.sf.json.JSONObject;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;
import zy.engine.utils.CharUtil;

import java.util.Map;

/**
 * Created by JiangGuofeng on 2016/10/28.
 */
public class ResultFormatter {

    public static JSONObject convertHit2Json(SearchHit hit, String type){
        if (type == null || "".equalsIgnoreCase( type )) return null;
        if (type.equalsIgnoreCase("front")) return getResult4Front( hit );
        if (type.equalsIgnoreCase("analysis")) return getResult4Analysis( hit );
        if (type.equalsIgnoreCase("detail")) return getResult4Detail( hit );
        return null;
    }

    private static JSONObject getResult4Front(SearchHit sh) {
        Map<String, Object> map = sh.getSource();
        Map<String, HighlightField> fieldMap = sh.getHighlightFields();
		/*
		for(String key:fieldMap.keySet()){
			System.out.println(fieldMap.get(key).fragments()[0].string());
		}*/
        JSONObject obj = new JSONObject();
        obj.put( Mapper.FieldArticle.ID, map.get( Mapper.FieldArticle.ID ) );
        obj.put( Mapper.FieldArticle.LANGUAGE_CODE, map.get( Mapper.FieldArticle.LANGUAGE_CODE ) );

        //*************************titleSrc**************************
        HighlightField titleSrcHigh = fieldMap.get( Mapper.FieldArticle.TITLE_SRC );
        String titleSrc = "";
        if (titleSrcHigh == null) {
            Object o = map.get( Mapper.FieldArticle.TITLE_SRC );
            if (o == null) {
                titleSrc = "";
            } else {
                titleSrc = o.toString();
            }
        } else {
            titleSrc = titleSrcHigh.fragments()[0].string().replaceAll( "<\\\\/em>(.)<em>", "$1" );
        }
        if (titleSrc != null) {
            titleSrc = CharUtil.removeSpaceInChinese( titleSrc );
        }
        obj.put( Mapper.FieldArticle.TITLE_SRC, titleSrc );
        //*************************titleSrc**************************

        HighlightField titleEn = fieldMap.get( Mapper.FieldArticle.TITLE_EN );
        if (titleEn == null) {
            Object o = map.get( Mapper.FieldArticle.TITLE_EN );
            if (o == null) {
                obj.put( Mapper.FieldArticle.TITLE_EN, "" );
            } else {
                obj.put( Mapper.FieldArticle.TITLE_EN, o );
            }
        } else {
            obj.put( Mapper.FieldArticle.TITLE_EN, titleEn.fragments()[0].string() );
        }
        HighlightField titleZhHigh = fieldMap.get( Mapper.FieldArticle.TITLE_ZH );
        String titleZh = "";
        if (titleZhHigh == null) {
            Object o = map.get( Mapper.FieldArticle.TITLE_ZH );
            if (o == null) {
                titleZh = "";
            } else {
                titleZh = o.toString();
            }
        } else {
            titleZh = titleZhHigh.fragments()[0].string().replaceAll( "<\\\\/em>(.)<em>", "$1" );
        }
        if (titleZh != null) {
            titleZh = CharUtil.removeSpaceInChinese( titleZh );
        }
        obj.put( Mapper.FieldArticle.TITLE_ZH, titleZh );

        HighlightField absZhHigh = fieldMap.get( Mapper.FieldArticle.ABSTRACT_ZH );
        String absZh = "";
        if (absZhHigh == null) {
            Object o = map.get( Mapper.FieldArticle.ABSTRACT_ZH );
            if (o == null) {
                absZh = "";
            } else {
                absZh = o.toString();
                absZh = absZh.replaceAll( "<\\s*br\\s*/\\s*>", " " )
                        .replaceAll( "。。。。。。", " " )
                        .replaceAll( "<[^>]*>", " " ).trim();
            }
        } else {
            absZh = absZhHigh.fragments()[0].string().replaceAll( "<\\\\/em>(.)<em>", "$1" );
            if (absZh != null) {
                absZh = absZh.replaceAll( "<\\s*br\\s*/\\s*>", " " )
                        .replaceAll( "。。。。。。", " " );
            }
            //			obj.put(Mapper.FieldArticle.ABSTRACT_ZH, absZhHigh.fragments()[0].string());
        }

        /**
         * 临时处理摘要，现在应该已经处理完了
         */
        //		absZh = absZh.replaceAll("<\\s*br\\s*/\\s*>", " ");
        //		absZh = absZh.replaceAll("。。。。。。", " ");
        //		absZh = absZh.replaceAll("<[^>]*>", " ").trim();
        //		if(absZh.length() < 20)
        //		{
        //			absZh =((String)map.get(Mapper.FieldArticle.TEXT_ZH)).substring(0,50).replaceAll("<[^>]*>", " ").trim();
        //		}

        obj.put( Mapper.FieldArticle.ABSTRACT_ZH, absZh );

        HighlightField absEn = fieldMap.get( Mapper.FieldArticle.ABSTRACT_EN );
        if (absEn == null) {
            String absEnStr = (String) map.get( Mapper.FieldArticle.ABSTRACT_EN );
            if (absEnStr != null) {
                obj.put( Mapper.FieldArticle.ABSTRACT_EN, absEnStr.replaceAll( "<\\s*BR\\s*/\\s*>", " " ) );//((String)map.get(Mapper.FieldArticle.ABSTRACT_EN)).replaceAll("<BR/>", " "));
            } else {
                obj.put( Mapper.FieldArticle.ABSTRACT_EN, null );
            }
        } else {
            String absEnStr = (String) absEn.fragments()[0].string();
            if (absEnStr != null) {
                obj.put( Mapper.FieldArticle.ABSTRACT_EN, absEnStr.replaceAll( "<\\s*BR\\s*/\\s*>", " " ) );
            } else {
                obj.put( Mapper.FieldArticle.ABSTRACT_EN, null );
            }
        }
/*		obj.put(Mapper.FieldArticle.MEDIA_NAME_ZH, map.get(Mapper.FieldArticle.MEDIA_NAME_ZH));
		obj.put(Mapper.FieldArticle.MEDIA_NAME_EN, map.get(Mapper.FieldArticle.MEDIA_NAME_EN));
//		obj.put(Mapper.FieldArticle.MEDIA_ID, map.get(Mapper.FieldArticle.MEDIA_ID));
		obj.put(Mapper.FieldArticle.PUBDATE, map.get(Mapper.FieldArticle.PUBDATE));
		obj.put(Mapper.FieldArticle.MEDIA_TYPE, map.get(Mapper.FieldArticle.MEDIA_TYPE));
		obj.put(Mapper.FieldArticle.MEDIA_TNAME, map.get(Mapper.FieldArticle.MEDIA_TNAME));
//		obj.put(Mapper.FieldArticle.LANGUAGE_TYPE, map.get(Mapper.FieldArticle.LANGUAGE_TYPE));
		obj.put(Mapper.FieldArticle.LANGUAGE_TNAME, map.get(Mapper.FieldArticle.LANGUAGE_TNAME));
//		obj.put(Mapper.FieldArticle.COUNTRY_ID, map.get(Mapper.FieldArticle.COUNTRY_ID));
		obj.put(Mapper.FieldArticle.COUNTRY_NAME_ZH, map.get(Mapper.FieldArticle.COUNTRY_NAME_ZH));
		obj.put(Mapper.FieldArticle.COUNTRY_NAME_EN, map.get(Mapper.FieldArticle.COUNTRY_NAME_EN));
		obj.put(Mapper.FieldArticle.KEYWORDS_ZH, map.get(Mapper.FieldArticle.KEYWORDS_ZH));
		obj.put(Mapper.FieldArticle.KEYWORDS_EN, map.get(Mapper.FieldArticle.KEYWORDS_EN));
		obj.put(Mapper.FieldArticle.DOC_LENGTH, map.get(Mapper.FieldArticle.DOC_LENGTH));
		obj.put(Mapper.FieldArticle.URL, map.get(Mapper.FieldArticle.URL));
		*/


        obj.put( Mapper.FieldArticle.SENTIMENT_ID, map.get( Mapper.FieldArticle.SENTIMENT_ID ) );
        obj.put( Mapper.FieldArticle.SENTIMENT_NAME, map.get( Mapper.FieldArticle.SENTIMENT_NAME ) );
        if (((String) map.get( Mapper.FieldArticle.LANGUAGE_TNAME )).equalsIgnoreCase( "英文" )
                ||
                ((String) map.get( Mapper.FieldArticle.LANGUAGE_TNAME )).equalsIgnoreCase( "en" )
                ) {
            obj.put( Mapper.FieldArticle.LANGUAGE_TNAME, "英语" );
        } else {
            obj.put( Mapper.FieldArticle.LANGUAGE_TNAME, map.get( Mapper.FieldArticle.LANGUAGE_TNAME ) );
        }

        //		obj.put(Mapper.FieldArticle.LANGUAGE_TYPE, map.get(Mapper.FieldArticle.LANGUAGE_TYPE));
        obj.put( Mapper.FieldArticle.LANGUAGE_CODE, map.get( Mapper.FieldArticle.LANGUAGE_CODE ) );
        obj.put( Mapper.FieldArticle.KEYWORDS_EN, map.get( Mapper.FieldArticle.KEYWORDS_EN ) );
        obj.put( Mapper.FieldArticle.KEYWORDS_ZH, map.get( Mapper.FieldArticle.KEYWORDS_ZH ) );
        obj.put( Mapper.FieldArticle.CATEGORY_ID, map.get( Mapper.FieldArticle.CATEGORY_ID ) );
        obj.put( Mapper.FieldArticle.CATEGORY_NAME, map.get( Mapper.FieldArticle.CATEGORY_NAME ) );
        //		obj.put(Mapper.FieldArticle.REGION_ID, map.get(Mapper.FieldArticle.REGION_ID));
        //		obj.put(Mapper.FieldArticle.REGION_NAME, map.get(Mapper.FieldArticle.REGION_NAME));
        //		obj.put(Mapper.FieldArticle.COUNTRY_ID, map.get(Mapper.FieldArticle.COUNTRY_ID));


        obj.put( Mapper.FieldArticle.COUNTRY_NAME_ZH, map.get( Mapper.FieldArticle.COUNTRY_NAME_ZH ) );
        obj.put( Mapper.FieldArticle.COUNTRY_NAME_EN, map.get( Mapper.FieldArticle.COUNTRY_NAME_EN ) );
        if ((map.get( Mapper.FieldArticle.COUNTRY_NAME_ZH ) == null) && (map.get( Mapper.FieldArticle.COUNTRY_NAME_EN ) != null)) {
            obj.put( Mapper.FieldArticle.COUNTRY_NAME_ZH, Mapper.FieldArticle.COUNTRY_NAME_EN );
        } else if ((map.get( Mapper.FieldArticle.COUNTRY_NAME_EN ) == null) && (map.get( Mapper.FieldArticle.COUNTRY_NAME_ZH ) != null)) {
            obj.put( Mapper.FieldArticle.COUNTRY_NAME_EN, Mapper.FieldArticle.COUNTRY_NAME_ZH );
        } else if ((map.get( Mapper.FieldArticle.COUNTRY_NAME_ZH ) == null) && (map.get( Mapper.FieldArticle.COUNTRY_NAME_EN ) == null)) {
            obj.put( Mapper.FieldArticle.COUNTRY_NAME_ZH, "other" );
            obj.put( Mapper.FieldArticle.COUNTRY_NAME_EN, "other" );
        }

        obj.put( Mapper.FieldArticle.PROVINCE_NAME_ZH, map.get( Mapper.FieldArticle.PROVINCE_NAME_ZH ) );
        obj.put( Mapper.FieldArticle.PROVINCE_NAME_EN, map.get( Mapper.FieldArticle.PROVINCE_NAME_EN ) );
        obj.put( Mapper.FieldArticle.DISTRICT_NAME_ZH, map.get( Mapper.FieldArticle.DISTRICT_NAME_ZH ) );
        obj.put( Mapper.FieldArticle.DISTRICT_NAME_EN, map.get( Mapper.FieldArticle.DISTRICT_NAME_EN ) );

        obj.put( Mapper.FieldArticle.PUBDATE, map.get( Mapper.FieldArticle.PUBDATE ) );
        obj.put( Mapper.FieldArticle.CREATE_TIME, map.get( Mapper.FieldArticle.CREATE_TIME ) );
        obj.put( Mapper.FieldArticle.UPDATE_TIME, map.get( Mapper.FieldArticle.UPDATE_TIME ) );
        //obj.put(Mapper.FieldArticle.AUTHOR, map.get(Mapper.FieldArticle.AUTHOR));
        obj.put( Mapper.FieldArticle.IS_ORIGINAL, map.get( Mapper.FieldArticle.IS_ORIGINAL ) );

        //		obj.put(Mapper.FieldArticle.MEDIA_ID, map.get(Mapper.FieldArticle.MEDIA_ID));
        obj.put( Mapper.FieldArticle.MEDIA_NAME_ZH, map.get( Mapper.FieldArticle.MEDIA_NAME_ZH ) );
        obj.put( Mapper.FieldArticle.MEDIA_NAME_EN, map.get( Mapper.FieldArticle.MEDIA_NAME_EN ) );
        obj.put( Mapper.FieldArticle.MEDIA_NAME_SRC, map.get( Mapper.FieldArticle.MEDIA_NAME_SRC ) );
        obj.put( Mapper.FieldArticle.MEDIA_TYPE, map.get( Mapper.FieldArticle.MEDIA_TYPE ) );
        obj.put( Mapper.FieldArticle.MEDIA_TNAME, map.get( Mapper.FieldArticle.MEDIA_TNAME ) );
        obj.put( Mapper.FieldArticle.MEDIA_LEVEL, map.get( Mapper.FieldArticle.MEDIA_LEVEL ) );
        obj.put( Mapper.FieldArticle.WEBSITE_ID, map.get( Mapper.FieldArticle.WEBSITE_ID ) );
        //		obj.put(Mapper.FieldArticle.LEVEL_NAME, map.get(Mapper.FieldArticle.LEVEL_NAME));
        obj.put( Mapper.FieldArticle.DOC_LENGTH, map.get( Mapper.FieldArticle.DOC_LENGTH ) );
        obj.put( Mapper.FieldArticle.URL, map.get( Mapper.FieldArticle.URL ) );

        obj.put( Mapper.FieldArticle.TRANSFER, map.get( Mapper.FieldArticle.TRANSFER ) );
        obj.put( Mapper.FieldArticle.SIMILARITY_ID, map.get( Mapper.FieldArticle.SIMILARITY_ID ) );
        obj.put( Mapper.FieldArticle.TRANSFROMM, map.get( Mapper.FieldArticle.TRANSFROMM ) );
        obj.put( Mapper.FieldArticle.ISPICTURE, map.get( Mapper.FieldArticle.ISPICTURE ) );
        obj.put( Mapper.FieldArticle.PV, map.get( Mapper.FieldArticle.PV ) );
        obj.put( Mapper.FieldArticle.ISHOME, map.get( Mapper.FieldArticle.ISHOME ) );

        obj.put( Mapper.FieldArticle.PUBDATE_SORT, map.get( Mapper.FieldArticle.PUBDATE_SORT ) );
        obj.put( Mapper.FieldArticle.IS_SENSITIVE, map.get( Mapper.FieldArticle.IS_SENSITIVE ) );

        return obj;
    }

    private static JSONObject getResult4Analysis(SearchHit sh) {

        Map<String, Object> map = sh.getSource();
        JSONObject obj = new JSONObject();
        obj.put( Mapper.FieldArticle.ID, map.get( Mapper.FieldArticle.ID ) );
        obj.put( Mapper.FieldArticle.TITLE_SRC, map.get( Mapper.FieldArticle.TITLE_SRC ) );
        obj.put( Mapper.FieldArticle.TITLE_EN, map.get( Mapper.FieldArticle.TITLE_EN ) );
        obj.put( Mapper.FieldArticle.TITLE_ZH, map.get( Mapper.FieldArticle.TITLE_ZH ) );
        obj.put( Mapper.FieldArticle.ABSTRACT_EN, map.get( Mapper.FieldArticle.ABSTRACT_EN ) );
        obj.put( Mapper.FieldArticle.ABSTRACT_ZH, map.get( Mapper.FieldArticle.ABSTRACT_ZH ) );
        obj.put( Mapper.FieldArticle.SENTIMENT_ID, map.get( Mapper.FieldArticle.SENTIMENT_ID ) );
        obj.put( Mapper.FieldArticle.SENTIMENT_NAME, map.get( Mapper.FieldArticle.SENTIMENT_NAME ) );
        obj.put( Mapper.FieldArticle.LANGUAGE_TNAME, map.get( Mapper.FieldArticle.LANGUAGE_TNAME ) );
        if (((String) map.get( Mapper.FieldArticle.LANGUAGE_TNAME )).equalsIgnoreCase( "英文" )
                ||
                ((String) map.get( Mapper.FieldArticle.LANGUAGE_TNAME )).equalsIgnoreCase( "en" )
                ) {
            obj.put( Mapper.FieldArticle.LANGUAGE_TNAME, "英语" );
        } else {
            obj.put( Mapper.FieldArticle.LANGUAGE_TNAME, map.get( Mapper.FieldArticle.LANGUAGE_TNAME ) );
        }


        //		obj.put(Mapper.FieldArticle.LANGUAGE_TYPE, map.get(Mapper.FieldArticle.LANGUAGE_TYPE));
        obj.put( Mapper.FieldArticle.LANGUAGE_CODE, map.get( Mapper.FieldArticle.LANGUAGE_CODE ) );
        obj.put( Mapper.FieldArticle.KEYWORDS_EN, map.get( Mapper.FieldArticle.KEYWORDS_EN ) );
        obj.put( Mapper.FieldArticle.KEYWORDS_ZH, map.get( Mapper.FieldArticle.KEYWORDS_ZH ) );
        obj.put( Mapper.FieldArticle.CATEGORY_ID, map.get( Mapper.FieldArticle.CATEGORY_ID ) );
        obj.put( Mapper.FieldArticle.CATEGORY_NAME, map.get( Mapper.FieldArticle.CATEGORY_NAME ) );
        //		obj.put(Mapper.FieldArticle.REGION_ID, map.get(Mapper.FieldArticle.REGION_ID));
        //		obj.put(Mapper.FieldArticle.REGION_NAME, map.get(Mapper.FieldArticle.REGION_NAME));
        //		obj.put(Mapper.FieldArticle.COUNTRY_ID, map.get(Mapper.FieldArticle.COUNTRY_ID));
        obj.put( Mapper.FieldArticle.COUNTRY_NAME_ZH, map.get( Mapper.FieldArticle.COUNTRY_NAME_ZH ) );
        obj.put( Mapper.FieldArticle.COUNTRY_NAME_EN, map.get( Mapper.FieldArticle.COUNTRY_NAME_EN ) );
        if ((map.get( Mapper.FieldArticle.COUNTRY_NAME_ZH ) == null) && (map.get( Mapper.FieldArticle.COUNTRY_NAME_EN ) != null)) {
            obj.put( Mapper.FieldArticle.COUNTRY_NAME_ZH, Mapper.FieldArticle.COUNTRY_NAME_EN );
        } else if ((map.get( Mapper.FieldArticle.COUNTRY_NAME_EN ) == null) && (map.get( Mapper.FieldArticle.COUNTRY_NAME_ZH ) != null)) {
            obj.put( Mapper.FieldArticle.COUNTRY_NAME_EN, Mapper.FieldArticle.COUNTRY_NAME_ZH );
        } else if ((map.get( Mapper.FieldArticle.COUNTRY_NAME_ZH ) == null) && (map.get( Mapper.FieldArticle.COUNTRY_NAME_EN ) == null)) {
            obj.put( Mapper.FieldArticle.COUNTRY_NAME_ZH, "other" );
            obj.put( Mapper.FieldArticle.COUNTRY_NAME_EN, "other" );
        }

        obj.put( Mapper.FieldArticle.PROVINCE_NAME_ZH, map.get( Mapper.FieldArticle.PROVINCE_NAME_ZH ) );
        obj.put( Mapper.FieldArticle.PROVINCE_NAME_EN, map.get( Mapper.FieldArticle.PROVINCE_NAME_EN ) );
        obj.put( Mapper.FieldArticle.DISTRICT_NAME_ZH, map.get( Mapper.FieldArticle.DISTRICT_NAME_ZH ) );
        obj.put( Mapper.FieldArticle.DISTRICT_NAME_EN, map.get( Mapper.FieldArticle.DISTRICT_NAME_EN ) );

        obj.put( Mapper.FieldArticle.PUBDATE, map.get( Mapper.FieldArticle.PUBDATE ) );
        obj.put( Mapper.FieldArticle.CREATE_TIME, map.get( Mapper.FieldArticle.CREATE_TIME ) );
        obj.put( Mapper.FieldArticle.UPDATE_TIME, map.get( Mapper.FieldArticle.UPDATE_TIME ) );
        //obj.put(Mapper.FieldArticle.AUTHOR, map.get(Mapper.FieldArticle.AUTHOR));
        obj.put( Mapper.FieldArticle.IS_ORIGINAL, map.get( Mapper.FieldArticle.IS_ORIGINAL ) );

        //		obj.put(Mapper.FieldArticle.MEDIA_ID, map.get(Mapper.FieldArticle.MEDIA_ID));
        obj.put( Mapper.FieldArticle.MEDIA_NAME_ZH, map.get( Mapper.FieldArticle.MEDIA_NAME_ZH ) );
        obj.put( Mapper.FieldArticle.MEDIA_NAME_EN, map.get( Mapper.FieldArticle.MEDIA_NAME_EN ) );
        obj.put( Mapper.FieldArticle.MEDIA_NAME_SRC, map.get( Mapper.FieldArticle.MEDIA_NAME_SRC ) );

        obj.put( Mapper.FieldArticle.MEDIA_TYPE, map.get( Mapper.FieldArticle.MEDIA_TYPE ) );
        obj.put( Mapper.FieldArticle.MEDIA_TNAME, map.get( Mapper.FieldArticle.MEDIA_TNAME ) );
        obj.put( Mapper.FieldArticle.MEDIA_LEVEL, map.get( Mapper.FieldArticle.MEDIA_LEVEL ) );
        obj.put( Mapper.FieldArticle.WEBSITE_ID, map.get( Mapper.FieldArticle.WEBSITE_ID ) );
        //		obj.put(Mapper.FieldArticle.LEVEL_NAME, map.get(Mapper.FieldArticle.LEVEL_NAME));
        obj.put( Mapper.FieldArticle.DOC_LENGTH, map.get( Mapper.FieldArticle.DOC_LENGTH ) );
        obj.put( Mapper.FieldArticle.URL, map.get( Mapper.FieldArticle.URL ) );


        obj.put( Mapper.FieldArticle.TRANSFER, map.get( Mapper.FieldArticle.TRANSFER ) );
        obj.put( Mapper.FieldArticle.SIMILARITY_ID, map.get( Mapper.FieldArticle.SIMILARITY_ID ) );
        obj.put( Mapper.FieldArticle.TRANSFROMM, map.get( Mapper.FieldArticle.TRANSFROMM ) );
        obj.put( Mapper.FieldArticle.ISPICTURE, map.get( Mapper.FieldArticle.ISPICTURE ) );
        obj.put( Mapper.FieldArticle.PV, map.get( Mapper.FieldArticle.PV ) );
        obj.put( Mapper.FieldArticle.ISHOME, map.get( Mapper.FieldArticle.ISHOME ) );

        obj.put( Mapper.FieldArticle.COME_FROM, map.get( Mapper.FieldArticle.COME_FROM ) );
        obj.put( Mapper.FieldArticle.IS_SENSITIVE, map.get( Mapper.FieldArticle.IS_SENSITIVE ) );

        return obj;
    }


    private JSONObject getResult4Analysis_bak(SearchHit sh) {
        Map<String, Object> map = sh.getSource();
        JSONObject obj = new JSONObject();
        obj.put( Mapper.FieldArticle.ID, map.get( Mapper.FieldArticle.ID ) );
        String langCode = map.get( Mapper.FieldArticle.LANGUAGE_CODE ).toString();
        String titleSrc = map.get( Mapper.FieldArticle.TITLE_SRC ).toString();
        if ("zh".equalsIgnoreCase( langCode )) {
            titleSrc = CharUtil.removeSpaceInChinese( titleSrc );
        }
        obj.put( Mapper.FieldArticle.TITLE_SRC, titleSrc );
        obj.put( Mapper.FieldArticle.TITLE_EN, map.get( Mapper.FieldArticle.TITLE_EN ) );
        Object o = map.get( Mapper.FieldArticle.TITLE_ZH );
        String titleZh = "";
        if (o != null) {
            titleZh = o.toString();
        }
        titleZh = CharUtil.removeSpaceInChinese( titleZh );
        obj.put( Mapper.FieldArticle.TITLE_ZH, titleZh );

        String absEnStr = (String) map.get( Mapper.FieldArticle.ABSTRACT_EN );
        if (absEnStr != null) {
            obj.put( Mapper.FieldArticle.ABSTRACT_EN, absEnStr.replaceAll( "<\\s*BR\\s*/\\s*>", " " ) );//((String)map.get(Mapper.FieldArticle.ABSTRACT_EN)).replaceAll("<BR/>", " "));
        } else {
            obj.put( Mapper.FieldArticle.ABSTRACT_EN, null );
        }
        //obj.put(Mapper.FieldArticle.ABSTRACT_EN, map.get(Mapper.FieldArticle.ABSTRACT_EN).toString().replaceAll("<BR/>", " "));
        Object oa = map.get( Mapper.FieldArticle.ABSTRACT_ZH );
        String absZh = "";
        if (oa != null) {
            absZh = oa.toString();
        }
        absZh = CharUtil.removeSpaceInChinese( absZh )
                .replaceAll( "<\\s*br\\s*/\\s*>", " " )
                .replaceAll( "。。。。。。", " " )
                .replaceAll( "<[^>]*>", " " ).trim();

        obj.put( Mapper.FieldArticle.ABSTRACT_ZH, absZh );


        obj.put( Mapper.FieldArticle.SENTIMENT_ID, map.get( Mapper.FieldArticle.SENTIMENT_ID ) );
        obj.put( Mapper.FieldArticle.SENTIMENT_NAME, map.get( Mapper.FieldArticle.SENTIMENT_NAME ) );
        obj.put( Mapper.FieldArticle.LANGUAGE_TNAME, map.get( Mapper.FieldArticle.LANGUAGE_TNAME ) );
        if (((String) map.get( Mapper.FieldArticle.LANGUAGE_TNAME )).equalsIgnoreCase( "英文" )
                ||
                ((String) map.get( Mapper.FieldArticle.LANGUAGE_TNAME )).equalsIgnoreCase( "en" )
                ) {
            obj.put( Mapper.FieldArticle.LANGUAGE_TNAME, "英语" );
        } else {
            obj.put( Mapper.FieldArticle.LANGUAGE_TNAME, map.get( Mapper.FieldArticle.LANGUAGE_TNAME ) );
        }

        //		obj.put(Mapper.FieldArticle.LANGUAGE_TYPE, map.get(Mapper.FieldArticle.LANGUAGE_TYPE));
        obj.put( Mapper.FieldArticle.LANGUAGE_CODE, map.get( Mapper.FieldArticle.LANGUAGE_CODE ) );
        obj.put( Mapper.FieldArticle.KEYWORDS_EN, map.get( Mapper.FieldArticle.KEYWORDS_EN ) );
        obj.put( Mapper.FieldArticle.KEYWORDS_ZH, map.get( Mapper.FieldArticle.KEYWORDS_ZH ) );
        obj.put( Mapper.FieldArticle.CATEGORY_ID, map.get( Mapper.FieldArticle.CATEGORY_ID ) );
        obj.put( Mapper.FieldArticle.CATEGORY_NAME, map.get( Mapper.FieldArticle.CATEGORY_NAME ) );
        //		obj.put(Mapper.FieldArticle.REGION_ID, map.get(Mapper.FieldArticle.REGION_ID));
        //		obj.put(Mapper.FieldArticle.REGION_NAME, map.get(Mapper.FieldArticle.REGION_NAME));
        //		obj.put(Mapper.FieldArticle.COUNTRY_ID, map.get(Mapper.FieldArticle.COUNTRY_ID));
        obj.put( Mapper.FieldArticle.COUNTRY_NAME_ZH, map.get( Mapper.FieldArticle.COUNTRY_NAME_ZH ) );
        obj.put( Mapper.FieldArticle.COUNTRY_NAME_EN, map.get( Mapper.FieldArticle.COUNTRY_NAME_EN ) );
        if ((map.get( Mapper.FieldArticle.COUNTRY_NAME_ZH ) == null) && (map.get( Mapper.FieldArticle.COUNTRY_NAME_EN ) != null)) {
            obj.put( Mapper.FieldArticle.COUNTRY_NAME_ZH, Mapper.FieldArticle.COUNTRY_NAME_EN );
        } else if ((map.get( Mapper.FieldArticle.COUNTRY_NAME_EN ) == null) && (map.get( Mapper.FieldArticle.COUNTRY_NAME_ZH ) != null)) {
            obj.put( Mapper.FieldArticle.COUNTRY_NAME_EN, Mapper.FieldArticle.COUNTRY_NAME_ZH );
        } else if ((map.get( Mapper.FieldArticle.COUNTRY_NAME_ZH ) == null) && (map.get( Mapper.FieldArticle.COUNTRY_NAME_EN ) == null)) {
            obj.put( Mapper.FieldArticle.COUNTRY_NAME_ZH, "other" );
            obj.put( Mapper.FieldArticle.COUNTRY_NAME_EN, "other" );
        }
        obj.put( Mapper.FieldArticle.PROVINCE_NAME_ZH, map.get( Mapper.FieldArticle.PROVINCE_NAME_ZH ) );
        obj.put( Mapper.FieldArticle.PROVINCE_NAME_EN, map.get( Mapper.FieldArticle.PROVINCE_NAME_EN ) );
        obj.put( Mapper.FieldArticle.DISTRICT_NAME_ZH, map.get( Mapper.FieldArticle.DISTRICT_NAME_ZH ) );
        obj.put( Mapper.FieldArticle.DISTRICT_NAME_EN, map.get( Mapper.FieldArticle.DISTRICT_NAME_EN ) );

        obj.put( Mapper.FieldArticle.PUBDATE, map.get( Mapper.FieldArticle.PUBDATE ) );
        obj.put( Mapper.FieldArticle.CREATE_TIME, map.get( Mapper.FieldArticle.CREATE_TIME ) );
        obj.put( Mapper.FieldArticle.UPDATE_TIME, map.get( Mapper.FieldArticle.UPDATE_TIME ) );
        //obj.put(Mapper.FieldArticle.AUTHOR, map.get(Mapper.FieldArticle.AUTHOR));
        obj.put( Mapper.FieldArticle.IS_ORIGINAL, map.get( Mapper.FieldArticle.IS_ORIGINAL ) );

        //		obj.put(Mapper.FieldArticle.MEDIA_ID, map.get(Mapper.FieldArticle.MEDIA_ID));
        obj.put( Mapper.FieldArticle.MEDIA_NAME_ZH, map.get( Mapper.FieldArticle.MEDIA_NAME_ZH ) );
        obj.put( Mapper.FieldArticle.MEDIA_NAME_EN, map.get( Mapper.FieldArticle.MEDIA_NAME_EN ) );
        obj.put( Mapper.FieldArticle.MEDIA_NAME_SRC, map.get( Mapper.FieldArticle.MEDIA_NAME_SRC ) );

        obj.put( Mapper.FieldArticle.MEDIA_TYPE, map.get( Mapper.FieldArticle.MEDIA_TYPE ) );
        obj.put( Mapper.FieldArticle.MEDIA_TNAME, map.get( Mapper.FieldArticle.MEDIA_TNAME ) );
        obj.put( Mapper.FieldArticle.MEDIA_LEVEL, map.get( Mapper.FieldArticle.MEDIA_LEVEL ) );
        obj.put( Mapper.FieldArticle.WEBSITE_ID, map.get( Mapper.FieldArticle.WEBSITE_ID ) );
        //		obj.put(Mapper.FieldArticle.LEVEL_NAME, map.get(Mapper.FieldArticle.LEVEL_NAME));
        obj.put( Mapper.FieldArticle.DOC_LENGTH, map.get( Mapper.FieldArticle.DOC_LENGTH ) );
        obj.put( Mapper.FieldArticle.URL, map.get( Mapper.FieldArticle.URL ) );


        obj.put( Mapper.FieldArticle.TRANSFER, map.get( Mapper.FieldArticle.TRANSFER ) );
        obj.put( Mapper.FieldArticle.SIMILARITY_ID, map.get( Mapper.FieldArticle.SIMILARITY_ID ) );
        obj.put( Mapper.FieldArticle.TRANSFROMM, map.get( Mapper.FieldArticle.TRANSFROMM ) );
        obj.put( Mapper.FieldArticle.ISPICTURE, map.get( Mapper.FieldArticle.ISPICTURE ) );
        obj.put( Mapper.FieldArticle.PV, map.get( Mapper.FieldArticle.PV ) );
        obj.put( Mapper.FieldArticle.ISHOME, map.get( Mapper.FieldArticle.ISHOME ) );

        return obj;
    }

    private static JSONObject getResult4Detail(SearchHit sh) {
        Map<String, Object> map = sh.getSource();
        JSONObject obj = new JSONObject();
        obj.put( Mapper.FieldArticle.ID, map.get( Mapper.FieldArticle.ID ) );
        obj.put( Mapper.FieldArticle.TITLE_SRC, map.get( Mapper.FieldArticle.TITLE_SRC ) );
        obj.put( Mapper.FieldArticle.TITLE_EN, map.get( Mapper.FieldArticle.TITLE_EN ) );
        Object o = map.get( Mapper.FieldArticle.TITLE_ZH );
        String titleZh = "";
        if (o != null) {
            titleZh = o.toString();
        }
        titleZh = CharUtil.removeSpaceInChinese( titleZh );
        obj.put( Mapper.FieldArticle.TITLE_ZH, titleZh );

        String absEnStr = (String) map.get( Mapper.FieldArticle.ABSTRACT_EN );
        if (absEnStr != null) {
            obj.put( Mapper.FieldArticle.ABSTRACT_EN, absEnStr.replaceAll( "<\\s*BR\\s*/\\s*>", " " ) );//((String)map.get(Mapper.FieldArticle.ABSTRACT_EN)).replaceAll("<BR/>", " "));
        } else {
            obj.put( Mapper.FieldArticle.ABSTRACT_EN, null );
        }
        //obj.put(Mapper.FieldArticle.ABSTRACT_EN, map.get(Mapper.FieldArticle.ABSTRACT_EN).toString().replaceAll("<BR/>", " "));
        Object oa = map.get( Mapper.FieldArticle.ABSTRACT_ZH );
        String absZh = "";
        if (oa != null) {
            absZh = oa.toString();
        }
        absZh = CharUtil.removeSpaceInChinese( absZh )
                .replaceAll( "<\\s*br\\s*/\\s*>", " " )
                .replaceAll( "。。。。。。", " " )
                .replaceAll( "<[^>]*>", " " ).trim();

        obj.put( Mapper.FieldArticle.ABSTRACT_ZH, absZh );
        obj.put( Mapper.FieldArticle.SENTIMENT_ID, map.get( Mapper.FieldArticle.SENTIMENT_ID ) );
        obj.put( Mapper.FieldArticle.SENTIMENT_NAME, map.get( Mapper.FieldArticle.SENTIMENT_NAME ) );
        obj.put( Mapper.FieldArticle.LANGUAGE_TNAME, map.get( Mapper.FieldArticle.LANGUAGE_TNAME ) );
        if (((String) map.get( Mapper.FieldArticle.LANGUAGE_TNAME )).equalsIgnoreCase( "英文" )
                ||
                ((String) map.get( Mapper.FieldArticle.LANGUAGE_TNAME )).equalsIgnoreCase( "en" )
                ) {
            obj.put( Mapper.FieldArticle.LANGUAGE_TNAME, "英语" );
        } else {
            obj.put( Mapper.FieldArticle.LANGUAGE_TNAME, map.get( Mapper.FieldArticle.LANGUAGE_TNAME ) );
        }

        //		obj.put(Mapper.FieldArticle.LANGUAGE_TYPE, map.get(Mapper.FieldArticle.LANGUAGE_TYPE));
        obj.put( Mapper.FieldArticle.LANGUAGE_CODE, map.get( Mapper.FieldArticle.LANGUAGE_CODE ) );
        obj.put( Mapper.FieldArticle.KEYWORDS_EN, map.get( Mapper.FieldArticle.KEYWORDS_EN ) );
        obj.put( Mapper.FieldArticle.KEYWORDS_ZH, map.get( Mapper.FieldArticle.KEYWORDS_ZH ) );
        obj.put( Mapper.FieldArticle.CATEGORY_ID, map.get( Mapper.FieldArticle.CATEGORY_ID ) );
        obj.put( Mapper.FieldArticle.CATEGORY_NAME, map.get( Mapper.FieldArticle.CATEGORY_NAME ) );
        //		obj.put(Mapper.FieldArticle.REGION_ID, map.get(Mapper.FieldArticle.REGION_ID));
        //		obj.put(Mapper.FieldArticle.REGION_NAME, map.get(Mapper.FieldArticle.REGION_NAME));
        //		obj.put(Mapper.FieldArticle.COUNTRY_ID, map.get(Mapper.FieldArticle.COUNTRY_ID));
        obj.put( Mapper.FieldArticle.COUNTRY_NAME_ZH, map.get( Mapper.FieldArticle.COUNTRY_NAME_ZH ) );
        obj.put( Mapper.FieldArticle.COUNTRY_NAME_EN, map.get( Mapper.FieldArticle.COUNTRY_NAME_EN ) );

        if ((map.get( Mapper.FieldArticle.COUNTRY_NAME_ZH ) == null) && (map.get( Mapper.FieldArticle.COUNTRY_NAME_EN ) != null)) {
            obj.put( Mapper.FieldArticle.COUNTRY_NAME_ZH, Mapper.FieldArticle.COUNTRY_NAME_EN );
        } else if ((map.get( Mapper.FieldArticle.COUNTRY_NAME_EN ) == null) && (map.get( Mapper.FieldArticle.COUNTRY_NAME_ZH ) != null)) {
            obj.put( Mapper.FieldArticle.COUNTRY_NAME_EN, Mapper.FieldArticle.COUNTRY_NAME_ZH );
        } else if ((map.get( Mapper.FieldArticle.COUNTRY_NAME_ZH ) == null) && (map.get( Mapper.FieldArticle.COUNTRY_NAME_EN ) == null)) {
            obj.put( Mapper.FieldArticle.COUNTRY_NAME_ZH, "other" );
            obj.put( Mapper.FieldArticle.COUNTRY_NAME_EN, "other" );
        }

        obj.put( Mapper.FieldArticle.PROVINCE_NAME_ZH, map.get( Mapper.FieldArticle.PROVINCE_NAME_ZH ) );
        obj.put( Mapper.FieldArticle.PROVINCE_NAME_EN, map.get( Mapper.FieldArticle.PROVINCE_NAME_EN ) );
        obj.put( Mapper.FieldArticle.DISTRICT_NAME_ZH, map.get( Mapper.FieldArticle.DISTRICT_NAME_ZH ) );
        obj.put( Mapper.FieldArticle.DISTRICT_NAME_EN, map.get( Mapper.FieldArticle.DISTRICT_NAME_EN ) );

        obj.put( Mapper.FieldArticle.PUBDATE, map.get( Mapper.FieldArticle.PUBDATE ) );
        obj.put( Mapper.FieldArticle.CREATE_TIME, map.get( Mapper.FieldArticle.CREATE_TIME ) );
        obj.put( Mapper.FieldArticle.UPDATE_TIME, map.get( Mapper.FieldArticle.UPDATE_TIME ) );
        //obj.put(Mapper.FieldArticle.AUTHOR, map.get(Mapper.FieldArticle.AUTHOR));
        obj.put( Mapper.FieldArticle.IS_ORIGINAL, map.get( Mapper.FieldArticle.IS_ORIGINAL ) );

        //		obj.put(Mapper.FieldArticle.MEDIA_ID, map.get(Mapper.FieldArticle.MEDIA_ID));
        obj.put( Mapper.FieldArticle.MEDIA_NAME_ZH, map.get( Mapper.FieldArticle.MEDIA_NAME_ZH ) );
        obj.put( Mapper.FieldArticle.MEDIA_NAME_EN, map.get( Mapper.FieldArticle.MEDIA_NAME_EN ) );
        obj.put( Mapper.FieldArticle.MEDIA_NAME_SRC, map.get( Mapper.FieldArticle.MEDIA_NAME_SRC ) );

        obj.put( Mapper.FieldArticle.MEDIA_TYPE, map.get( Mapper.FieldArticle.MEDIA_TYPE ) );
        obj.put( Mapper.FieldArticle.MEDIA_TNAME, map.get( Mapper.FieldArticle.MEDIA_TNAME ) );
        obj.put( Mapper.FieldArticle.MEDIA_LEVEL, map.get( Mapper.FieldArticle.MEDIA_LEVEL ) );
        obj.put( Mapper.FieldArticle.WEBSITE_ID, map.get( Mapper.FieldArticle.WEBSITE_ID ) );
        //		obj.put(Mapper.FieldArticle.LEVEL_NAME, map.get(Mapper.FieldArticle.LEVEL_NAME));
        obj.put( Mapper.FieldArticle.DOC_LENGTH, map.get( Mapper.FieldArticle.DOC_LENGTH ) );
        obj.put( Mapper.FieldArticle.URL, map.get( Mapper.FieldArticle.URL ) );


        obj.put( Mapper.FieldArticle.TRANSFER, map.get( Mapper.FieldArticle.TRANSFER ) );
        obj.put( Mapper.FieldArticle.SIMILARITY_ID, map.get( Mapper.FieldArticle.SIMILARITY_ID ) );
        obj.put( Mapper.FieldArticle.TRANSFROMM, map.get( Mapper.FieldArticle.TRANSFROMM ) );
        obj.put( Mapper.FieldArticle.ISPICTURE, map.get( Mapper.FieldArticle.ISPICTURE ) );
        obj.put( Mapper.FieldArticle.PV, map.get( Mapper.FieldArticle.PV ) );
        obj.put( Mapper.FieldArticle.ISHOME, map.get( Mapper.FieldArticle.ISHOME ) );


        obj.put( Mapper.FieldArticle.TEXT_SRC, map.get( Mapper.FieldArticle.TEXT_SRC ) );
        obj.put( Mapper.FieldArticle.TEXT_ZH, map.get( Mapper.FieldArticle.TEXT_ZH ) );
        obj.put( Mapper.FieldArticle.TEXT_EN, map.get( Mapper.FieldArticle.TEXT_EN ) );

        obj.put( Mapper.FieldArticle.IS_SENSITIVE, map.get( Mapper.FieldArticle.IS_SENSITIVE ) );

        return obj;
    }

}
