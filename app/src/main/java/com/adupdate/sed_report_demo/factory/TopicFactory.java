package com.adupdate.sed_report_demo.factory;



import com.adupdate.sed_report_demo.entity.DeviceInfo;
import com.adupdate.sed_report_demo.entity.ProductInfo;
import com.adupdate.sed_report_demo.mqtt.constant.MqttTopicEnum;

import javax.inject.Inject;

public class TopicFactory {
    @Inject
    DeviceInfo mDeviceInfo;
    @Inject
    ProductInfo mProductInfo;

    public String createTopic(MqttTopicEnum mqttTopicEnum) {
        return mqttTopicEnum.getTopic(mProductInfo,mDeviceInfo);
    }

    public MqttTopicEnum createTopic(String topic) {
        return MqttTopicEnum.obtainMqttTopicEnum(topic,mProductInfo,mDeviceInfo);
    }
}
