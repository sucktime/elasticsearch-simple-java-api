package zy.engine.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by JiangGuofeng on 2016/10/20.
 *
 *@Do： 完全支持无嵌套的AND、OR、NOT查询和phrase查询
 *@Don't: 该解析器不支持括号和嵌套语法
 */

public class SimplestQueryParser {

    public static SimplestQueryNode parseRaw(String rawQuery){
        String[] tokens = SimplestQueryParser.supplyTokens(rawQuery);
        tokens = SimplestQueryParser.checkWords(tokens);
        tokens = SimplestQueryParser.cleanTokens(tokens);

        SimplestQueryNode rootOr = new SimplestQueryNode(false, new NodeType(NodeType.OR));
        List<SimplestQueryNode> leaves = new ArrayList<SimplestQueryNode>(6);
        boolean isNot = false;
        for (int index2=0; index2<tokens.length; index2++){
            if (tokens[index2].equals("OR")){
                if(leaves.size() > 1) {
                    SimplestQueryNode midAnd = new SimplestQueryNode(false, new NodeType(NodeType.AND));
                    midAnd.setChildren(leaves);
                    rootOr.addChild(midAnd);
                }else {
                    rootOr.addChild(leaves.get(0));
                }
                leaves = new ArrayList<SimplestQueryNode>(6);
                continue;
            }
            else if (tokens[index2].equals("AND")){
                continue;
            }
            else if (tokens[index2].equals("NOT")){
                isNot = true;
                continue;
            }
            else {
                SimplestQueryNode leaf = null;
                boolean isPhrase = false;
                if (tokens[index2].startsWith("\"") && tokens[index2].endsWith("\"")){
                    isPhrase = true;
                    tokens[index2] = tokens[index2].substring(1,tokens[index2].length()-1);
                }
                if (isNot) {
                    leaf = new SimplestQueryNode(tokens[index2], true, new NodeType(NodeType.NOT));
                }
                else {
                    leaf = new SimplestQueryNode(tokens[index2], true, new NodeType(NodeType.NORMAL));
                }
                leaf.setPhrase(isPhrase);
                leaves.add(leaf);
                isNot = false;
            }
        }
        if(leaves.size() > 1) {
            SimplestQueryNode midAnd = new SimplestQueryNode(false, new NodeType(NodeType.AND));
            midAnd.setChildren(leaves);
            rootOr.addChild(midAnd);
            leaves = new ArrayList<SimplestQueryNode>(6);
        }else {
            rootOr.addChild(leaves.get(0));
        }

        return rootOr;
    }

    /**
     * 1)补上两个词之间的默认逻辑词：AND
     * @param rawQuery
     * @return
     */
    public static String[] supplyTokens(String rawQuery) {
        String[] tokens = rawQuery.split("\\s+");
        List<String> addedTokens = new LinkedList<String>();
        boolean lastIsWord = false;
        for (String token : tokens) {
            String upperToken = token.toUpperCase();
            if (upperToken.equals("AND") || upperToken.equals("OR")) {
                addedTokens.add(upperToken);
                lastIsWord = false;
            } else {
                if (lastIsWord) {
                    addedTokens.add("AND");
                }
                if (upperToken.equals("NOT")) {
                    addedTokens.add("NOT");
                } else{
                    lastIsWord = true;
                    addedTokens.add(token);
                }
            }
        }
        return addedTokens.toArray(new String[]{});
    }

    /**
     * 2）去掉空的phrase："",并将其修饰逻辑词去掉
     * @param tokens
     * @return
     */
    public static String[] checkWords(String[] tokens){
        int pointer = -1;
        for(int index=0; index < tokens.length; index++){
            if (tokens[index].equals("\"\"")){
                while (pointer>=0 && (tokens[pointer].equals("AND") || tokens[pointer].equals("OR") || tokens[pointer].equals("NOT")) ){
                    pointer --;
                }
                continue;
            }
            tokens[++pointer] = tokens[index];
        }
        //noinspection Since15
        return Arrays.copyOf(tokens, pointer+1);
    }

    public static String[] cleanTokens(String[] tokens) {

        int pointer = -1;
        for (int index = 0; index < tokens.length; index++) {
            //last-one = null:
            if (pointer == -1){
                if(tokens[index].equals("AND") || tokens[index].equals("OR")){
                    continue;
                }else {
                    tokens[++pointer] = tokens[index];
                    continue;
                }
            }
            //last-one="NOT":
            if (tokens[pointer].equals("NOT")){
                if (tokens[index].equals("NOT")){
                    pointer--;
                    continue;
                }else if (tokens[index].equals("AND") || tokens[index].equals("OR")){
                    continue;
                } else {
                    tokens[++pointer] = tokens[index];
                }
            }
            //last-one="AND":
            else if (tokens[pointer].equals("AND")){
                if((tokens[index].equals("AND") || tokens[index].equals("OR"))) {
                    tokens[pointer] = tokens[index];
                    continue;
                }else {
                    tokens[++pointer] = tokens[index];
                    continue;
                }
            }
            //last-one="OR":
            else if (tokens[pointer].equals("OR")){
                if ((tokens[index].equals("AND") || tokens[index] == "OR")) {
                    continue;
                }else {
                    tokens[++pointer] = tokens[index];
                    continue;
                }
            }
            //last-one is a word:
            else {
                if (!tokens[index].equals("AND") && !tokens[index].equals("OR") && !tokens[index].equals("NOT")){
                    tokens[++pointer] = "AND";
                    tokens[++pointer] = tokens[index];
                    continue;
                } else {
                    tokens[++pointer] = tokens[index];
                }
            }
        }
        //noinspection Since15
        return Arrays.copyOf(tokens, pointer+1);
    }
}