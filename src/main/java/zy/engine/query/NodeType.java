package zy.engine.query;

/*
 * Created by JiangGuofeng on 2016/10/21.
 */
public class NodeType {
   public static final int AND = ((int)0x1000);
   public static final int OR = ((int)0x0100);
   public static final int NOT = ((int)0x0010);
   public static final int PHRASE = ((int)0x0001);
   public static final int NORMAL = ((int)0x0000);

    private int type = NORMAL;

    public NodeType(){}

    public NodeType(int type){
        this.type = type;
    }

    public void setType(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String Logicname(){
        if((type & NodeType.AND) != 0){
            return "AND";
        }
        if ((type & NodeType.NOT) != 0){
            return "OR";
        }
        if((type & NodeType.NOT) != 0){
            return "NOT";
        }
        if((type & NodeType.PHRASE) != 0){
            return "PHRASE";
        }
        return "NORMAl";
    }
}
