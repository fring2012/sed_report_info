package com.adupdate.sed_report_demo.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.abupdate.mqtt_libs.mqttv3.IMqttToken;
import com.adupdate.sed_report_demo.app.App;
import com.adupdate.sed_report_demo.app.constant.Constant;
import com.adupdate.sed_report_demo.db.dao.TimetableDao;
import com.adupdate.sed_report_demo.entity.DeviceInfo;
import com.adupdate.sed_report_demo.entity.TimeRecord;
import com.adupdate.sed_report_demo.http.ResultBody;
import com.adupdate.sed_report_demo.entity.observer.ObserverAbstract;
import com.adupdate.sed_report_demo.mqtt.MqttTool;
import com.adupdate.sed_report_demo.mqtt.constant.MqttConnectState;
import com.adupdate.sed_report_demo.mqtt.callback.base.MqttActionListener;
import com.adupdate.sed_report_demo.mqtt.callback.base.MqttConnectActionListener;
import com.adupdate.sed_report_demo.mqtt.callback.MqttMessageCallBack;
import com.adupdate.sed_report_demo.presenter.HttpPresenter;
import com.adupdate.sed_report_demo.presenter.MqttPresenter;
import com.adupdate.sed_report_demo.util.DateUtil;
import com.adupdate.sed_report_demo.util.NetUtil;
import com.adupdate.sed_report_demo.helper.SharedPreferencesHelper;
import com.adupdate.sed_report_demo.util.Trace;



import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DaggerIntentService;
import io.reactivex.Observer;



public class ReporterService extends DaggerIntentService{
    private static final String TAG = "ReporterService";
    public static final String TASK_START_UP = "start-up";//开机任务
    public static final String TASK_REPORT_NEWEST_DEVICE_INFO = "report_newest_device_info";//上报最新的设备信息
    public static final String TASK_CLEAN_EXCEPTION_TIME_RECORD = "clean_exception_time_record";//清理异常的信息
    public static final String TASK_ALL_RECORD = "task_all_record";//上报所有信息
    public static final String TASK_UPDATE_IP = "update_ip";
    public static final String TASK_SHUT_DOWN = "shut_down";
    public static final String TASK_REPORT_NEWEST_TOTAL_RUN_TIME = "report_newest_totalRunTime";
    public static final String TASK_BEFORE_DAWN_REPORT = "before_dawn_report";
    private static boolean isCleanNotReport = false;
    private static int recordTimeCount = 0;

    @Inject
    public Context mCx;

    @Inject
    public MqttPresenter mMqttPresenter;

    @Inject
    public HttpPresenter mHttpPresenter;

    @Inject
    public TimetableDao mTimetableDao;

    @Inject
    public MqttMessageCallBack mMqttMessageCallBack;

    @Inject
    DeviceInfo mDeviceInfo;

    @Inject
    MqttTool mMqttTool;

    @Inject
    SharedPreferencesHelper mSharedPreferencesHelper;

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String task = intent.getStringExtra("task");
        Trace.d(TAG,"task:" + task);
        switch (task){
            case TASK_START_UP:
                deviceRegister(getDeviceRegisterObserver());
                break;
            case TASK_REPORT_NEWEST_DEVICE_INFO:
                reportNewestDeviceInfo();
                break;
            case TASK_REPORT_NEWEST_TOTAL_RUN_TIME:
                reportNewestTotalRunTime();
                break;
            case TASK_CLEAN_EXCEPTION_TIME_RECORD:
                cleanExceptionTimeRecord();
                break;
            case TASK_ALL_RECORD:
                //reportNewestDeviceInfo();
                mMqttPresenter.pubCpuInfo();
                mMqttPresenter.pubTemp();
                //mHttpPresenter.reportLog();
                break;
            case TASK_UPDATE_IP:
                updateNewestIpAddress();
                break;
            case TASK_SHUT_DOWN:
                long currentTime = System.currentTimeMillis();
                updateLastShutDownRecord(currentTime,mCx,intent);
                break;

        }
    }

    /**
     * 上报设备运行时间
     */
    private void reportNewestTotalRunTime(){
        TimeRecord timeRecord = obtainNewestTimeRecord();
        timeRecord.setRecordTime(System.currentTimeMillis());
        mTimetableDao.update(timeRecord);
        recordTimeCount ++;

        if (MqttTool.MQTT_CONNECT_STATE == MqttConnectState.CONNECT) {
            pubNotReportTimeRecord();
            long currentBeforeDawn = DateUtil.obtainBeforeDawnTime();
            long bootBeforeDawn = DateUtil.obtainBelongBeforeDawnTime(timeRecord.getBootTime());
            if (timeRecord.getIs_report() == 0 && isCleanNotReport) {
                Trace.d(TAG,"补上开机未上报任务！！");
                mMqttPresenter.pubReportDeviceInfo(timeRecord);
            }else if(currentBeforeDawn > bootBeforeDawn && isCleanNotReport){
                long reportTime = timeRecord.getReportTime();
                if ((currentBeforeDawn - DateUtil.DAY_TIME) > reportTime){
                    Trace.d(TAG,"reportTime obsolete !");
                    mMqttPresenter.pubRunTimeDeviceInfo(timeRecord);
                }
            }
            if(recordTimeCount >= 30){
                recordTimeCount = 0;
                mMqttPresenter.pubReportDeviceInfo(new TimeRecord());
            }
        }
    }

    /**
     * 上报设备实时信息
     */
    private void reportNewestDeviceInfo(){
        pubNotReportTimeRecord();
        TimeRecord timeRecord = obtainNewestTimeRecord();
        timeRecord.setRecordTime(System.currentTimeMillis());
        mTimetableDao.update(timeRecord);

        //记录总运行时间
        mSharedPreferencesHelper.putTotalRunTime();
        if (MqttTool.MQTT_CONNECT_STATE == MqttConnectState.CONNECT) {
            if (isCleanNotReport)
                mMqttPresenter.pubReportDeviceInfo(timeRecord);
        }
    }
    /**
     * 记录关机时间
     * @param currentTime
     * @param context
     */
    private void updateLastShutDownRecord(final long currentTime,Context context,Intent intent){
        pubNotReportTimeRecord();
        TimeRecord timeRecord  = obtainNewestTimeRecord();
        timeRecord.setOffTime(currentTime);

        mTimetableDao.update(timeRecord);

        Trace.d(TAG,"关机记录信息：" + timeRecord);

        if (MqttTool.MQTT_CONNECT_STATE == MqttConnectState.CONNECT){
            mMqttPresenter.pubRunTimeDeviceInfo(timeRecord);
        }
        mSharedPreferencesHelper.putTotalRunTime();
        mSharedPreferencesHelper.putBootCompletedTotalRunTime();
//        mMqttPresenter.mqttDisconnect();
        stopService(intent);
    }

    /**
     * 修改ip地址
     */
    private void updateNewestIpAddress(){
        TimeRecord timeRecord = obtainNewestTimeRecord();
        String newestIp = NetUtil.getIPAddress(mCx);
        Trace.d(TAG,"update ip:" + newestIp);

        if (TextUtils.isEmpty(newestIp)) {
            return;
        }
        if (newestIp.equals(timeRecord.getIpAddress())) {
            return;
        }
        timeRecord.setIpAddress(newestIp);
        mTimetableDao.update(timeRecord);

    }


    /**
     * 设备注册
     * @param observer
     */
    public void deviceRegister(Observer<ResultBody> observer){
        mMqttTool.initMqtt(mCx,new MqttMessageCallBack());
        mHttpPresenter.deviceRegister(observer);
    }


    /**
     * mqtt链接
     */
    private void mqttConnect(){
        mMqttPresenter.mqttConnect(new MqttConnectActionListener(){
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                super.onSuccess(iMqttToken);
                Trace.d(TAG,"connect success!");
                Trace.d(TAG, String.format(Constant.BOUNDARY_LINE_END,"mqttConnect"));
                //mqtt订阅登录返回
                mMqttPresenter.subDeviceLoginResponse(new MqttActionListener(){
                    @Override
                    public void onSuccess(IMqttToken iMqttToken) {
                        super.onSuccess(iMqttToken);
                        //mqtt发布设备登录信息
                        mMqttPresenter.pubDeviceLogin();
                    }
                });
                //订阅服务器命令接收
                mMqttPresenter.subCommandReceive(new MqttActionListener());
            }
        });
    }

    /**
     * 开启服务
     * @param context
     * @param task
     */
    public static void startReporterService(Context context,String task){
        Intent intent = new Intent(context,ReporterService.class);
        intent.putExtra("task",task);
        context.startService(intent);
    }


    private Observer getDeviceRegisterObserver(){
        Observer deviceRegisterObserver = new ObserverAbstract<ResultBody>() {

            @Override
            public void onNext(ResultBody resultBody) {
                Trace.d(TAG,resultBody.toString());
                if (1000 != resultBody.getStatus()){
                    Trace.d(TAG,"device register error! " + resultBody.getMsg());
                    Trace.d(TAG, String.format(Constant.BOUNDARY_LINE_END,"device register"));
                    return;
                }
                //获取deviceId和deviceSecret
                mDeviceInfo.setDeviceId(resultBody.getData().get("deviceId"));
                //获取deviceSecret
                mDeviceInfo.setDeviceSecret(resultBody.getData().get("deviceSecret"));
                Trace.d(TAG, String.format(Constant.BOUNDARY_LINE_END,"device register"));
                mqttConnect();
            }
        };
        return deviceRegisterObserver;
    }


    /**
     * 清理异常记录
     */
    private void cleanExceptionTimeRecord(){
        //获取开机时间
        long bootTime = DateUtil.obtainBootTime();

        List<TimeRecord> timeRecords = mTimetableDao.getOffTimeUnrecorded(bootTime);
        int size = timeRecords.size();
        Trace.d(TAG,"异常未记录offTime：" + size + "条！ 开机时间：" + bootTime);
        for (int i = 0; i < size; i++) {
            TimeRecord timeRecord = timeRecords.get(i);
            timeRecord.setOffTime(timeRecord.getRecordTime());
            timeRecord.setRunThisTime(timeRecord.getOffTime() - timeRecord.getBootTime());
            mTimetableDao.update(timeRecord);
        }
        pubNotReportTimeRecord();
    }

    /**
     * 上报未上报的异常记录
     */
    private void pubNotReportTimeRecord(){
        if (!isCleanNotReport){
            Trace.d(TAG,"正在检查异常未上报记录！");
            List<TimeRecord> timeRecordList = mTimetableDao.getNotReportTimetable();
            int size = timeRecordList.size();
            if (size == 0){
                Trace.d(TAG,"无异常未上报记录！");
                long currentBootTime = DateUtil.obtainBootTime();
                long currentBeforeDawn = DateUtil.obtainBelongBeforeDawnTime(currentBootTime);

                mTimetableDao.cleanReportTimetable(currentBeforeDawn);
                isCleanNotReport = true;
            }else {
                Trace.d(TAG,"异常未上报记录:" + size + "条！");
                if (MqttTool.MQTT_CONNECT_STATE != MqttConnectState.CONNECT){
                    Trace.d(TAG,"MqttConnectState lost!");
                    return;
                }
                for (int i = 0; i < size; i++) {
                    TimeRecord timeRecord = timeRecordList.get(i);
                    if (MqttTool.MQTT_CONNECT_STATE != MqttConnectState.CONNECT){
                        Trace.d(TAG,"MqttConnectState lost!");
                        break;
                    }
                    if (i >= (size -1)){
                        mMqttPresenter.pubRunTimeDeviceInfo(timeRecord);
                    }else {
                        long bootBeforeDawn = DateUtil.obtainBelongBeforeDawnTime(timeRecord.getBootTime());
                        long lastBootBeforeDawn = DateUtil.obtainBelongBeforeDawnTime(timeRecordList.get(i+1).getBootTime());
                        if (bootBeforeDawn == lastBootBeforeDawn){
                            //无需上报当日运行时间等待下一个时间记录上报
                            mMqttPresenter.pubReportDeviceInfo(timeRecord);
                        }else {
                            mMqttPresenter.pubRunTimeDeviceInfo(timeRecord);
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取最新记录信息
     */
    private TimeRecord obtainNewestTimeRecord(){
        long bootTime = DateUtil.obtainBootTime();
//        TimeRecord timeRecord = App.getTimetableDao().getNewestTimetable();
        TimeRecord timeRecord = mTimetableDao.getNewestTimetable(bootTime);
        if (timeRecord == null){
//            if (NetUtil.isConnect(mCx)){
//                nowTime = DateUtil.obtainNetTime();
//            }
//            if (nowTime != 0){
//                bootTime = DateUtil.obtainBootTime(nowTime);
//            }
            Trace.e(TAG,timeRecord+ "");
            Trace.e(TAG,"reportNewestDeviceInfo! 数据库无最新数据！");
            timeRecord = new TimeRecord();
            timeRecord.setBootTime(DateUtil.obtainBootTime());
            timeRecord.setIpAddress(NetUtil.getIPAddress(mCx));
            timeRecord.setRecordTime(System.currentTimeMillis());
            mTimetableDao.insert(timeRecord);
            timeRecord = mTimetableDao.getNewestTimetable(bootTime);
        }
        return timeRecord;
    }



    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ReporterService(String name) {
        super(name);
    }

    public ReporterService(){
        super("ReporterService");
    }


}
