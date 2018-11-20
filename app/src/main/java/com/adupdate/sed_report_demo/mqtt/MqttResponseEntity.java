package com.adupdate.sed_report_demo.mqtt;

import com.adupdate.sed_report_demo.util.GsonUtil;
import com.adupdate.sed_report_demo.util.Trace;
import com.adupdate.sed_report_demo.util.Utils;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class MqttResponseEntity {
    @SerializedName("seqno")
    private String seqno;
    @SerializedName("replyno")
    private String replyno;
    @SerializedName("body")
    private Map<String,Object> body;

    public String getSeqno() {
        return seqno;
    }

    public void setSeqno(String seqno) {
        this.seqno = seqno;
    }

    public String getReplyno() {
        return replyno;
    }

    public void setReplyno(String replyno) {
        this.replyno = replyno;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public void setBody(Map<String, Object> body) {
        this.body = body;
    }

    public String getBodyValue(String key){
        Object value = body.get(key);
        if (value == null){
            return "";
        }
        return value.toString();
    }
    @Override
    public String toString() {
        return "MqttResponseEntity{" +
                "seqno='" + seqno + '\'' +
                ", replyno='" + replyno + '\'' +
                ", body=" + body +
                '}';
    }

    public String toJsonString(){
        return GsonUtil.toJsonString(this);
    }

    public static MqttResponseEntity obtainResponseEntity(String jsonStr){
        MqttResponseEntity mre = GsonUtil.toObject(MqttResponseEntity.class,jsonStr);
        Object statusObj = mre.getBody().get("status");
        if (statusObj != null){
            Double d = Double.valueOf(statusObj.toString());
            Integer status = d.intValue();
            mre.getBody().put("status",status);
        }
        return mre;
    }
}
