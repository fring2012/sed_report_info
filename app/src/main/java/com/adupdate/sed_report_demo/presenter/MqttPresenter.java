package com.adupdate.sed_report_demo.presenter;

import com.abupdate.mqtt_libs.mqttv3.IMqttActionListener;
import com.adupdate.sed_report_demo.entity.TimeRecord;

public interface MqttPresenter {

    /**
     * mqtt链接
     */
    void mqttConnect(IMqttActionListener mqttActionListener);

    /**
     * mqtt断连
     */
    void mqttDisconnect();

    /**
     * 设备登录
     */
    void pubDeviceLogin();

    /**
     * 设备登录返回
     */
    void subDeviceLoginResponse(IMqttActionListener mqttActionListener);

    /**
     * 设备登出
     */
    void pubDeviceLoginOut();

    /**
     * 设备登出返回
     */
    void subDeviceLoginOutResponse(IMqttActionListener mqttActionListener);

    /**
     * 上报开机信息
     */
    void pubBootDeviceInfo(TimeRecord timeRecord);

    /**
     * 上报开关机内所有天数的运行时间
     */
    void pubRunTimeDeviceInfo(TimeRecord timeRecord);

    /**
     * 上报设备信息
     */
    void pubReportDeviceInfo(TimeRecord timeRecord);

    /**
     * 上报设备信息返回
     */
    void subReportDeviceInfoResponse(IMqttActionListener mqttActionListener);

    /**
     * 订阅命令接收
     */
    void subCommandReceive(IMqttActionListener mqttActionListener);

    /**
     * 发布命令接收状态
     */
    void pubCommandReceiveStatus();

    /**
     * 发布cup信息
     */
    void pubCpuInfo();

    /**
     * 发布温度
     */
    void pubTemp();

    /**
     * 发布设备本次开机的运行时间
     */
    void pubRunTime();
}
