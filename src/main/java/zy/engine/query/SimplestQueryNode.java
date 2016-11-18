package zy.engine.query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JiangGuofeng on 2016/10/20.
 */
public class SimplestQueryNode {

    String word = null;
    boolean isLeaf = false;//是否是叶节点，只有叶节点才带有word
    NodeType type = new NodeType(NodeType.NORMAL); // Nothing or not decorated && is phrase or not
    List<SimplestQueryNode> children = null;

    public SimplestQueryNode(boolean isLeaf, NodeType type){
        this.children = new ArrayList<SimplestQueryNode>(6);
        this.isLeaf = false;
        this.type = type;
    }

    public SimplestQueryNode(String word, boolean isLeaf, NodeType type){
        this.word = word;
        this.isLeaf = true;
        this.type = type;
    }

    public void addChild(SimplestQueryNode node){
        this.children.add(node);
    }

    public List<SimplestQueryNode> getChildren(){
        return children;
    }

    //可以将叶子节点变成非叶子节点
    public void setChildren(List<SimplestQueryNode> children){
        this.children = children;
    }

    //遍历打印：
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("<").append(type.Logicname()).append(">\n");
        if(isLeaf()){
            if (isPhrase())
                sb.append("\"");
            sb.append(word);
            if (isPhrase())
                sb.append("\"");
            sb.append("\n");
        }
        if (children != null) {
            for (SimplestQueryNode node : children) {
                sb.append(node.toString());
            }
        }
        sb.append("<").append(type.Logicname()).append("/>\n");
        return sb.toString();
    }

    public boolean isLeaf(){
        return this.isLeaf;
    }

    public boolean isPhrase(){
        return (this.type.getType() & NodeType.PHRASE) != 0;
    }

    public void setPhrase(boolean isPhrase){
        if (isPhrase){
        this.type.setType(this.type.getType() | NodeType.PHRASE);
       }
    }

    public String getWord(){
        return word;
    }

    public void setWord(String word){
        this.word = word;
    }

    public boolean isNot(){
        return (type.getType() & NodeType.NOT) != 0;
    }

}
