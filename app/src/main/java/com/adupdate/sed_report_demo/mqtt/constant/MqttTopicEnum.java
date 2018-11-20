package com.adupdate.sed_report_demo.mqtt.constant;

import android.text.TextUtils;

import com.adupdate.sed_report_demo.entity.DeviceInfo;
import com.adupdate.sed_report_demo.entity.ProductInfo;
import com.adupdate.sed_report_demo.util.Trace;

import static android.content.ContentValues.TAG;

public enum  MqttTopicEnum {
    //设备登录
    DEVICE_LOGIN("product/{productId}/{deviceId}/login",1,"Pub"),
    //设备登出
    DEVICE_LOGIN_OUT("product/{productId}/{deviceId}/logout",1,"Pub"),
    //上报设备信息
    REPORT_DEVICE_INFO("product/{productId}/{deviceId}/shadow/update",1,"Pub"),
    //设备更新指令
    DEVICE_UPDATE("product/{productId}/{deviceId}/device/update",1,"Pub"),
    //检查更新接口
    CHECK_VERSION("product/{productId}/{deviceId}/ota/checkVersion",1,"Pub"),
    //下载上报
    DOWNLOAD_REPORT("product/{productId}/{deviceId}/ota/reportDownResult",1,"Pub"),
    //升级上报
    UPGRADE_REPORT("product/{productId}/{deviceId}/ota/reportUpgradeResult",1,"Pub"),
    //升级上报返回?
    UPGRADE_REPORT_RESPONSE("product/{ProductId}/{deviceId}/ota/reportUpgradeResult/response",1,"Pub"),
    //下发命令送达状态
    COMMAND_RECEIVE_STATUS("product/{productId}/{deviceId}/command/response",1,"Pub"),

    //登录返回
    DEVICE_LOGIN_RESPONSE("product/{productId}/{deviceId}/login/response",1,"Sub"),
    //登出返回
    DEVICE_LOGIN_OUT_RESPONSE("product/{productId}/{deviceId}/logout/response",1,"Sub"),
    //上报设备信息返回
    REPORT_DEVICE_INFO_RESPONSE("product/{productId}/{deviceId}/shadow/update/response",1,"Sub"),
    //设备更新状态返回
    DEVICE_UPDATE_RESPONSE("product/{productId}/{deviceId}/device/update/response",1,"Sub"),
    //消息推送
    NOTIFY("product/{productId}/{deviceId}/notify",1,"Sub"),
    //检查更新接口返回
    CHECK_VERSION_RESPONSE("product/{productId}/{deviceId}/ota/checkVersion/response",1,"Sub"),
    //下载上报返回
    DOWNLOAD_REPORT_RESPONSE("product/{productId}/{deviceId}/ota/reportDownResult/response",1,"Sub"),
    //服务器下发命令
    COMMAND_RECEIVE("product/{productId}/{deviceId}/command",1,"Sub");

    private String topicType;
    private int qos;
    private String pattern;
    private String topic;

    private MqttTopicEnum(String topicType,int qos,String pattern){
        this.pattern = pattern;
        this.topicType = topicType;
        this.qos = qos;
    }

    public String getTopicType() {
        return topicType;
    }

    public int getQos() {
        return qos;
    }

    public String getPattern() {
        return pattern;
    }

    public String getTopic(ProductInfo productInfo,DeviceInfo deviceInfo) {
        if(TextUtils.isEmpty(topic)){
            if (TextUtils.isEmpty(topicType)){
                return null;
            }
            if(productInfo.getProductId() == null){
                Trace.e(TAG,"productId is null!");
                return "";
            }
            if (deviceInfo.getDeviceId() == null){
                Trace.e(TAG,"deviceId is null");
                return "";
            }
            topic = topicType.replace("{productId}", productInfo.getProductId());
            topic = topic.replace("{deviceId}", deviceInfo.getDeviceId());
        }
        return topic;
    }

    public static MqttTopicEnum obtainMqttTopicEnum(String topic,ProductInfo productInfo,DeviceInfo deviceInfo){
        if (TextUtils.isEmpty(topic)){
            return null;
        }
        MqttTopicEnum mqttTopicEnum = null;
        for (MqttTopicEnum mt : MqttTopicEnum.values()){
            if (topic.equals(mt.getTopic(productInfo,deviceInfo))){
                mqttTopicEnum = mt;
            }
        }
        return mqttTopicEnum;
    }
}
