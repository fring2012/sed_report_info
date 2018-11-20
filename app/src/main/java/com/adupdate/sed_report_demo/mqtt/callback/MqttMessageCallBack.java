package com.adupdate.sed_report_demo.mqtt.callback;

import android.content.Context;

import com.abupdate.mqtt_libs.connect.MqttManager;
import com.abupdate.mqtt_libs.mqttv3.IMqttDeliveryToken;
import com.abupdate.mqtt_libs.mqttv3.IMqttToken;
import com.abupdate.mqtt_libs.mqttv3.MqttCallback;
import com.abupdate.mqtt_libs.mqttv3.MqttException;
import com.abupdate.mqtt_libs.mqttv3.MqttMessage;
import com.adupdate.sed_report_demo.app.App;
import com.adupdate.sed_report_demo.app.constant.Constant;
import com.adupdate.sed_report_demo.app.constant.Error;
import com.adupdate.sed_report_demo.db.dao.TimetableDao;
import com.adupdate.sed_report_demo.entity.observer.interval.PubCpuInfoObserver;
import com.adupdate.sed_report_demo.entity.observer.interval.PubDeviceInfoObserver;
import com.adupdate.sed_report_demo.entity.observer.interval.ReportLogObserver;
import com.adupdate.sed_report_demo.entity.observer.interval.ReportRunTimeObserver;
import com.adupdate.sed_report_demo.entity.observer.interval.ReportTempObserver;
import com.adupdate.sed_report_demo.entity.observer.interval.base.IntervalObserver;
import com.adupdate.sed_report_demo.factory.IntervalObserverFactory;
import com.adupdate.sed_report_demo.factory.TopicFactory;
import com.adupdate.sed_report_demo.mqtt.MqttCommand;
import com.adupdate.sed_report_demo.mqtt.MqttResponseEntity;
import com.adupdate.sed_report_demo.mqtt.MqttTool;
import com.adupdate.sed_report_demo.mqtt.callback.base.MqttActionListener;
import com.adupdate.sed_report_demo.mqtt.constant.MqttCommandType;
import com.adupdate.sed_report_demo.mqtt.constant.MqttConnectState;
import com.adupdate.sed_report_demo.mqtt.constant.MqttTopicEnum;
import com.adupdate.sed_report_demo.presenter.HttpPresenter;
import com.adupdate.sed_report_demo.presenter.MqttPresenter;
import com.adupdate.sed_report_demo.presenter.impl.MqttPresenterImpl;
import com.adupdate.sed_report_demo.service.ReporterService;
import com.adupdate.sed_report_demo.util.Trace;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class MqttMessageCallBack implements MqttCallback{
    private String TAG = "MqttMessageCallBack";

    private PubCpuInfoObserver mPubCpuInfoObserver;
    private ReportLogObserver mReportLogObserver;
    private ReportTempObserver mReportTempObserver;
    private ReportRunTimeObserver mReportRunTimeObserver;

    @Inject
    public Context mCx;

    @Inject
    public MqttPresenter mMqttPresenter;

    @Inject
    public HttpPresenter mHttpPresenter;

    @Inject
    public TopicFactory mTopicFactory;

    @Inject
    public IntervalObserverFactory mIntervalObserverFactory;

    //订阅上传设备信息返回回调
    private MqttActionListener subReportResponseListener = new MqttActionListener(){
        @Override
        public void onSuccess(IMqttToken iMqttToken) {
            super.onSuccess(iMqttToken);
            ReporterService.startReporterService(mCx,ReporterService.TASK_ALL_RECORD);
        }
    };
    @Override
    public void connectionLost(Throwable throwable) {
        Trace.e(TAG,"Mqtt connection lost" );
        MqttTool.MQTT_CONNECT_STATE = MqttConnectState.CONNECT_LOST;
        MqttPresenterImpl.countDown();
        if (throwable != null) {
            Trace.e(TAG,throwable.getMessage() + " ");
            throwable.printStackTrace();
        }
        int reasonCode;
        if (throwable != null && throwable instanceof MqttException){
            reasonCode = ((MqttException)throwable).getReasonCode();
            Trace.e(TAG,"ReasonCode:" + reasonCode);
            if (reasonCode != Error.REASON_CODE_FAILED_AUTHENTICATION || reasonCode != Error.REASON_CODE_NOT_AUTHORIZED){
                //异常断开连接时开启重试机制
                MqttManager.getInstance().keepConnect(1 * 1000 * 60 * 1, System.currentTimeMillis() + 1 * 1000 * 30);
            }
        }


    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        Trace.d(TAG, String.format(Constant.BOUNDARY_LINE_BEGIN,"messageArrived"));
        Trace.d(TAG,"topic: " + topic);

        if(mTopicFactory.createTopic(MqttTopicEnum.COMMAND_RECEIVE).equals(topic)){
            Trace.d(TAG,"-------------接收到命令-----------------------");
            MqttCommand mc = MqttCommand.obtainMqttCommand(mqttMessage.toString());
            Trace.d(TAG,mc.toString());
            mMqttPresenter.pubCommandReceiveStatus();
            executiveCommand(mc);
            Trace.d(TAG, String.format(Constant.BOUNDARY_LINE_END,"messageArrived"));
            return;
        }

        Trace.d(TAG,"message: " + mqttMessage.toString());
        MqttTopicEnum resultTopicEnum = mTopicFactory.createTopic(topic);
        final MqttResponseEntity mre = MqttResponseEntity.obtainResponseEntity(mqttMessage.toString());
        Integer status = Integer.valueOf(mre.getBody().get("status").toString());

        if (status != 1000){
            Trace.e(TAG,  Error.errorInfo(status) + "!" + mre.getBody().get("msg"));
            Trace.e(TAG, String.format(Constant.BOUNDARY_LINE_END,"messageArrived"));
            //解锁
            MqttPresenterImpl.countDown();
            return;
        }

        switch (resultTopicEnum){
            case DEVICE_LOGIN_RESPONSE:
                Trace.d(TAG, "login " + Error.errorInfo(status) + "!");
                mMqttPresenter.subReportDeviceInfoResponse(subReportResponseListener);
                break;
            case REPORT_DEVICE_INFO_RESPONSE:
                Trace.d(TAG,"report device info " + Error.errorInfo(status) + "!");
                break;
            case DEVICE_LOGIN_OUT_RESPONSE:
                Trace.d(TAG,"device login out " + Error.errorInfo(status) + "!");
                mMqttPresenter.mqttDisconnect();
                break;
            case COMMAND_RECEIVE:
                Trace.d(TAG,"device login out " + Error.errorInfo(status) + "!");
                break;
        }
        Trace.d(TAG, String.format(Constant.BOUNDARY_LINE_END,"messageArrived"));

        //解锁
        MqttPresenterImpl.countDown();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }



    /**
     * 执行命令
     * @param mc
     */
    private void executiveCommand(MqttCommand mc){
        switch (mc.getCommandType()){
            case MqttCommandType.REPORT_TEMPERATURE:
                restartCommand(getmReportTempObserver(),mc);
                break;
            case MqttCommandType.REPORT_CPU_INFO:
                restartCommand(getmPubCpuInfoObserver(),mc);
                break;
            case MqttCommandType.REPORT_LOG:
                restartCommand(getmReportLogObserver(),mc);
                break;
            case MqttCommandType.REPORT_ALL_INFO:
                if (mc.getPubStatus() == 1){
                    return;
                }
                mMqttPresenter.pubTemp();
                mMqttPresenter.pubCpuInfo();
                mHttpPresenter.reportLog();
                mMqttPresenter.pubRunTime();
                break;
            case MqttCommandType.REPORT_RUN_TIME:
                restartCommand(getReportRunTimeObserver(),mc);
                break;
        }
    }


    /**
     * 重新执行上报命令
     * @param intervalObserver
     * @param mc
     */
    private void restartCommand(IntervalObserver intervalObserver, MqttCommand mc){
        //取消之前同命令的订阅
        if (intervalObserver != null){
            intervalObserver.setUnsubscribe(true);
        }
        if (mc.getPubStatus() == 1){
            return;
        }
        IntervalObserver observer = null;
        observer = mIntervalObserverFactory.createIntervalObserverFactory(intervalObserver);
        setIntervalObserver(observer);
        observer.onNext(-1l);
        Observable.interval(mc.getPubInterval(), TimeUnit.MINUTES)
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    private void setIntervalObserver(IntervalObserver observer) {
        if (observer instanceof ReportTempObserver){
            setmReportTempObserver((ReportTempObserver) observer);
        }else if (observer instanceof ReportLogObserver){
            setmReportLogObserver((ReportLogObserver) observer);
        }else if (observer instanceof PubCpuInfoObserver){
            setmPubCpuInfoObserver((PubCpuInfoObserver) observer);
        }else if(observer instanceof ReportRunTimeObserver){
            setReportRunTimeObserver((ReportRunTimeObserver) observer);
        }else if (observer instanceof PubDeviceInfoObserver){

        }
    }

    public PubCpuInfoObserver getmPubCpuInfoObserver() {
        if (mPubCpuInfoObserver == null) {
            mPubCpuInfoObserver = new PubCpuInfoObserver(mMqttPresenter);
        }
        return mPubCpuInfoObserver;
    }

    public  void setmPubCpuInfoObserver(PubCpuInfoObserver pubCpuInfoObserver) {
        mPubCpuInfoObserver = pubCpuInfoObserver;
    }

    public  ReportRunTimeObserver getReportRunTimeObserver() {
        return mReportRunTimeObserver;
    }

    public  void setReportRunTimeObserver(ReportRunTimeObserver reportRunTimeObserver) {
       mReportRunTimeObserver = reportRunTimeObserver;
    }

    public  ReportLogObserver getmReportLogObserver() {
        if (mReportLogObserver == null) {
            mReportLogObserver = new ReportLogObserver(mHttpPresenter);
        }
        return mReportLogObserver;
    }

    public  void setmReportLogObserver(ReportLogObserver reportLogObserver) {
        mReportLogObserver = reportLogObserver;
    }

    public ReportTempObserver getmReportTempObserver() {
        if (mReportTempObserver == null) {
            mReportTempObserver = new ReportTempObserver(mMqttPresenter);
        }
        return mReportTempObserver;
    }

    public void setmReportTempObserver(ReportTempObserver reportTempObserver) {
        mReportTempObserver = reportTempObserver;
    }
}
