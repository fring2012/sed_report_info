package com.adupdate.sed_report_demo.mqtt.callback.base;

import com.abupdate.mqtt_libs.mqttv3.IMqttActionListener;
import com.abupdate.mqtt_libs.mqttv3.IMqttToken;
import com.adupdate.sed_report_demo.mqtt.MqttTool;
import com.adupdate.sed_report_demo.mqtt.constant.MqttConnectState;
import com.adupdate.sed_report_demo.util.Trace;

public class MqttConnectActionListener implements IMqttActionListener {
    private String TAG = "IMqttActionListener";


    @Override
    public void onSuccess(IMqttToken iMqttToken) {
        Trace.d(TAG,"onSuccess");
        MqttTool.MQTT_CONNECT_STATE = MqttConnectState.CONNECT;
    }

    @Override
    public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
        MqttTool.MQTT_CONNECT_STATE = MqttConnectState.CONNECT_LOST;
        Trace.e(TAG,"MqttConnect onFailure");
        throwable.printStackTrace();
    }
}
