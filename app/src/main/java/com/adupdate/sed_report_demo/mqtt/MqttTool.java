package com.adupdate.sed_report_demo.mqtt;

import android.content.Context;
import android.text.TextUtils;

import com.abupdate.mqtt_libs.connect.ConnectCommand;
import com.abupdate.mqtt_libs.connect.DisconnectCommand;
import com.abupdate.mqtt_libs.connect.MqttManager;
import com.abupdate.mqtt_libs.connect.PubCommand;
import com.abupdate.mqtt_libs.connect.SubCommand;
import com.abupdate.mqtt_libs.connect.UnsubCommand;
import com.abupdate.mqtt_libs.mqttv3.IMqttActionListener;
import com.abupdate.mqtt_libs.mqttv3.MqttCallback;
import com.abupdate.mqtt_libs.mqttv3.MqttException;
import com.adupdate.sed_report_demo.BuildConfig;
import com.adupdate.sed_report_demo.app.constant.Constant;
import com.adupdate.sed_report_demo.entity.CustomDeviceInfo;
import com.adupdate.sed_report_demo.entity.DeviceInfo;
import com.adupdate.sed_report_demo.entity.ProductInfo;
import com.adupdate.sed_report_demo.entity.reportinfo.MqttRequestEntityFactory;
import com.adupdate.sed_report_demo.factory.TopicFactory;
import com.adupdate.sed_report_demo.mqtt.constant.MqttConnectState;
import com.adupdate.sed_report_demo.mqtt.constant.MqttTopicEnum;
import com.adupdate.sed_report_demo.util.Trace;

import javax.inject.Inject;

public class MqttTool {
    private static final String TAG = "MqttTool";
    private static final String MQTT_URL = BuildConfig.mqttUrl;
    private static final int MQTT_PORT = BuildConfig.mqttPort;
    public static MqttConnectState MQTT_CONNECT_STATE = MqttConnectState.AWAIT_CONNECT;

    @Inject
    DeviceInfo mDeviceInfo;

    @Inject
    MqttRequestEntityFactory mMqttRequestEntityFactory;

    @Inject
    ProductInfo mProductInfo;

    @Inject
    TopicFactory mTopicFactory;
    /**
     * @param context
     */
    public void initMqtt(Context context,MqttCallback mMqttCallBack){
        MqttManager.getInstance().setContext(context);
        MqttManager.getInstance().registerMessageListener(mMqttCallBack);
    }

    /**
     * 链接mqtt
     */
    public void mqttConnect(IMqttActionListener iMqttActionListener){
        Trace.d(TAG, String.format(Constant.BOUNDARY_LINE_BEGIN,"mqttConnect"));

        String username = mProductInfo.getProductId() + "/" + mDeviceInfo.getDeviceId();
        String password = mDeviceInfo.getDeviceSecret();
        MqttRequestEntity mr = mMqttRequestEntityFactory.createMqttRequestEntity();
        mr.putBodyParams("type",1);

        ConnectCommand connectCommand = new ConnectCommand()
                .setClientId(mDeviceInfo.getDeviceId())
                .setServer(MQTT_URL)
                .setPort(MQTT_PORT)
                .setTimeout(1000 * 5)
                .setKeepAlive(20)
                .setCleanSession(false)
                .setLastWill(mr.toJsonString(), mTopicFactory.createTopic(MqttTopicEnum.DEVICE_LOGIN_OUT),MqttTopicEnum.DEVICE_LOGIN_OUT.getQos(),false)
                .setUserNameAndPassword(username,password);
        try {
            MqttManager.getInstance().connect(connectCommand,iMqttActionListener);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发布
     * @param mqttTopicEnum
     * @param message
     */
    public void pub(MqttTopicEnum mqttTopicEnum,String message,IMqttActionListener iMqttActionListener){
        if (TextUtils.isEmpty(mTopicFactory.createTopic(mqttTopicEnum))){
            Trace.e(TAG,"topic is empty!");
            return;
        }
        Trace.d(TAG,"pub topic:" + mTopicFactory.createTopic(mqttTopicEnum));
        Trace.d(TAG,message);
        PubCommand pubCommand = new PubCommand().setTopic(mTopicFactory.createTopic(mqttTopicEnum))
                .setQos(mqttTopicEnum.getQos()).setMessage(message).setRetained(false);
        try {
            MqttManager.getInstance().pub(pubCommand,iMqttActionListener);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 订阅
     * @param mqttTopicEnum
     */
    public void sub(MqttTopicEnum mqttTopicEnum,IMqttActionListener iMqttActionListener){
        Trace.d(TAG,mTopicFactory.createTopic(mqttTopicEnum));
        SubCommand subCommand = new SubCommand()
                .setQos(mqttTopicEnum.getQos()).setTopic(mTopicFactory.createTopic(mqttTopicEnum));
        try {
            MqttManager.getInstance().sub(subCommand,iMqttActionListener);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    /**
     *mqtt断链接
     */
    public void disconnect(IMqttActionListener iMqttActionListener){
        DisconnectCommand disconnectCommand = new DisconnectCommand();
        try {
            MqttManager.getInstance().disConnect(disconnectCommand, iMqttActionListener);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * mqtt取消订阅
     */
    public void unSub(MqttTopicEnum mqttTopicEnum,IMqttActionListener iMqttActionListener){
        UnsubCommand unsubCommand = new UnsubCommand().setTopic(mTopicFactory.createTopic(mqttTopicEnum));
        try {
            MqttManager.getInstance().unSub(unsubCommand,iMqttActionListener);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


}
