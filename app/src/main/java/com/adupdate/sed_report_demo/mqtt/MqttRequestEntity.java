package com.adupdate.sed_report_demo.mqtt;


import com.adupdate.sed_report_demo.entity.DeviceInfo;
import com.adupdate.sed_report_demo.util.GsonUtil;
import com.adupdate.sed_report_demo.util.ParamUtil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class MqttRequestEntity {
    @SerializedName("seqno")
    private String seqno;
    @SerializedName("body")
    private Map<String,Object> body;
    @Expose(serialize = false)
    private long timestamp;


    public MqttRequestEntity(String clientid,long timestamp){
        this.timestamp = timestamp;
        seqno = ParamUtil.createSeqNo(clientid,timestamp);
        body = new HashMap<>();
    }

    public void putBodyParams(String key,Object value){
        if (body == null){
            body = new HashMap<>();
        }
        body.put(key,value);
    }

    public String getSeqno() {
        return seqno;
    }

    public void setSeqno(String seqno) {
        this.seqno = seqno;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public void setBody(Map<String, Object> body) {
        this.body = body;
    }

    public String toJsonString(){
        return GsonUtil.toJsonString(this);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public static MqttRequestEntity obtainResponseEntity(String json){
        return GsonUtil.toObject(MqttRequestEntity.class,json);
    }
}
