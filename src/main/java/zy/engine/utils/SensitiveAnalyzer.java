package zy.engine.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by JiangGuofeng on 2016/10/20.
 */

public class SensitiveAnalyzer {

    private static PingyinTool pingyinTool = new PingyinTool();
    private static HashMap<String, Integer> hashLeaders;
    private static HashMap<String, Integer> hashLeadersPingyin;
    private static HashMap<String, Integer> hashSensiWords;
    private static HashMap<String, Integer> hashSensiWordsPingyin;
    private static final String leadersFile = "data/sensitive/leaders.txt";
    private static final String sensiWordsFile = "data/sensitive/sensiwords.txt";
    static {
        try {
            hashLeaders = new HashMap<String, Integer>();
            hashLeadersPingyin = new HashMap<String, Integer>();
            InputStreamReader isR = new InputStreamReader( new FileInputStream( leadersFile ), "utf-8" );
            BufferedReader br = new BufferedReader( isR );

            String line = "";
            String clearLine = "";
            while ((line = br.readLine()) != null) {
                if (line.trim().length() == 0) continue;

                clearLine = CharUtil.ToDBC( line );
                clearLine = CharUtil.removeUnChar( clearLine );
                if (clearLine.length() > 1) {
                    clearLine = pingyinTool.toPinYin( clearLine, "", PingyinTool.Type.LOWERCASE ).toLowerCase();
                    hashLeadersPingyin.put( clearLine, 1 );
                }
                hashLeaders.put( line.trim().toLowerCase(), 1 );

            }
            br.close();
            isR.close();
            hashSensiWords = new HashMap<String, Integer>();
            hashSensiWordsPingyin = new HashMap<String, Integer>();
            isR = new InputStreamReader( new FileInputStream( sensiWordsFile ), "utf-8" );
            br = new BufferedReader( isR );

            while ((line = br.readLine()) != null) {
                if (line.trim().length() == 0) continue;

                clearLine = CharUtil.ToDBC( line );
                clearLine = CharUtil.removeUnChar( clearLine );

                if (clearLine.length() > 1) {
                    clearLine = pingyinTool.toPinYin( clearLine, "", PingyinTool.Type.LOWERCASE ).toLowerCase();
                    hashSensiWordsPingyin.put( clearLine, 1 );
                }
                hashSensiWords.put( line.trim().toLowerCase(), 1 );
            }
            br.close();
            isR.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isSensitive(String word) {
        try {
            String clearword = CharUtil.ToDBC( word );
            clearword = CharUtil.removeUnChar( clearword ).toLowerCase();


            if (clearword.length() > 1) {
                String wordPingyin = pingyinTool.toPinYin( clearword, "", PingyinTool.Type.LOWERCASE );
                if (hashLeadersPingyin != null) {
                    for (String key : hashLeadersPingyin.keySet()) {
                        if (wordPingyin.contains( key )) {
                            return true;
                        }
                    }
                }
                if (hashSensiWordsPingyin != null) {
                    for (String key : hashSensiWordsPingyin.keySet()) {
                        if (wordPingyin.contains( key )) {
                            return true;
                        }
                    }
                }
            }

            if (hashLeaders != null) {
                for (String key : hashLeaders.keySet()) {
                    if (word.contains( key )) {
                        return true;
                    }
                }
            }
            if (hashSensiWords != null) {
                for (String key : hashSensiWords.keySet()) {
                    if (word.contains( key )) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }
}

