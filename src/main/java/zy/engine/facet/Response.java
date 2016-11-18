package zy.engine.facet;

/**
 * Created by JiangGuofeng on 2016/10/20.
 * Immutable class -- Response
 */
public final class Response {
    public static final int SUCCESS = 0;
    //返回的处理状态：失败=-1，成功=0
    int status = 0;
    //异常情况的说明，无异常和错误发生填写:"success"
    String msg = "success";
    //处理需要返回的结果, 如果无返回需结果填写："void"
    Object data = "void";

    public Response(int status, String msg, Object data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }

    public String toString(){
        return new StringBuilder()
                .append("Rsponse: status=").append(status)
                .append(", msg=").append(msg)
                .append(", data=").append(data.toString())
                .toString();
    }
}
