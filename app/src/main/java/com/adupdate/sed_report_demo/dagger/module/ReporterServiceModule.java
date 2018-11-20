package com.adupdate.sed_report_demo.dagger.module;

import com.adupdate.sed_report_demo.mqtt.callback.MqttMessageCallBack;

import dagger.Module;
import dagger.Provides;

@Module
public class ReporterServiceModule {
    @Provides
    public MqttMessageCallBack provideMqttMessageCallBack() {
        return new MqttMessageCallBack();
    }
}
