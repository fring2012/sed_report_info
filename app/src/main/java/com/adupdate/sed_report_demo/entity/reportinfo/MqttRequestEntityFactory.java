package com.adupdate.sed_report_demo.entity.reportinfo;

import com.adupdate.sed_report_demo.entity.CustomDeviceInfo;
import com.adupdate.sed_report_demo.entity.DeviceInfo;
import com.adupdate.sed_report_demo.mqtt.MqttRequestEntity;

import javax.inject.Inject;

public class MqttRequestEntityFactory {

    @Inject
    DeviceInfo mDeviceInfo;

    public MqttRequestEntity createMqttRequestEntity() {
        MqttRequestEntity re = new MqttRequestEntity(mDeviceInfo.getDeviceId(),System.currentTimeMillis());
        return re;
    }
}
