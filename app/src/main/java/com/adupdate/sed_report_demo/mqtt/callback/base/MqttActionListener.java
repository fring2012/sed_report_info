package com.adupdate.sed_report_demo.mqtt.callback.base;

import com.abupdate.mqtt_libs.mqttv3.IMqttActionListener;
import com.abupdate.mqtt_libs.mqttv3.IMqttToken;
import com.adupdate.sed_report_demo.util.Trace;

public class MqttActionListener implements IMqttActionListener {
    private static final String TAG = "MqttActionListener";
    @Override
    public void onSuccess(IMqttToken iMqttToken) {

    }

    @Override
    public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
        Trace.d(TAG,"onFailure");
        if (throwable != null)
            throwable.printStackTrace();
    }
}
