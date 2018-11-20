package com.adupdate.sed_report_demo.entity.observer.interval;

import com.abupdate.mqtt_libs.mqttv3.IMqttToken;
import com.adupdate.sed_report_demo.app.App;
import com.adupdate.sed_report_demo.entity.observer.interval.base.IntervalObserver;
import com.adupdate.sed_report_demo.mqtt.callback.base.MqttConnectActionListener;
import com.adupdate.sed_report_demo.presenter.MqttPresenter;

public class MqttConnectObserver extends IntervalObserver{
    private MqttPresenter mMqttPresenter;
    public MqttConnectObserver(MqttPresenter mqttPresenter) {
        mMqttPresenter = mqttPresenter;
    }
    @Override
    public void onNext(Long value) {
        super.onNext(value);
        mMqttPresenter.mqttConnect(new MqttConnectActionListener(){
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                super.onSuccess(iMqttToken);
                MqttConnectObserver.this.setUnsubscribe(true);
            }
        });
    }
}
