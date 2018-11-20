package com.adupdate.sed_report_demo.presenter.impl;

import com.abupdate.mqtt_libs.mqttv3.IMqttActionListener;
import com.abupdate.mqtt_libs.mqttv3.IMqttToken;
import com.adupdate.sed_report_demo.app.App;
import com.adupdate.sed_report_demo.app.constant.Constant;
import com.adupdate.sed_report_demo.db.dao.TimetableDao;
import com.adupdate.sed_report_demo.entity.CustomDeviceInfo;
import com.adupdate.sed_report_demo.entity.DeviceInfo;
import com.adupdate.sed_report_demo.entity.reportinfo.MqttRequestEntityFactory;
import com.adupdate.sed_report_demo.entity.reportinfo.ReportDeviceInfo;
import com.adupdate.sed_report_demo.entity.reportinfo.ReportInfoFactory;
import com.adupdate.sed_report_demo.entity.reportinfo.RunTimeInfo;
import com.adupdate.sed_report_demo.mqtt.MqttRequestEntity;
import com.adupdate.sed_report_demo.entity.TimeRecord;
import com.adupdate.sed_report_demo.mqtt.constant.MqttTopicEnum;
import com.adupdate.sed_report_demo.mqtt.MqttTool;
import com.adupdate.sed_report_demo.mqtt.callback.base.MqttActionListener;
import com.adupdate.sed_report_demo.presenter.MqttPresenter;
import com.adupdate.sed_report_demo.util.DateUtil;
import com.adupdate.sed_report_demo.util.Trace;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;


public class MqttPresenterImpl implements MqttPresenter {
    private static final String TAG = "MqttPresenterImpl";
    private static CountDownLatch mCountDownLatch;

    @Inject
    CustomDeviceInfo mCustomDeviceInfo;

    @Inject
    TimetableDao mTimetableDao;

    @Inject
    DeviceInfo mDeviceInfo;

    @Inject
    MqttRequestEntityFactory mMqttRequestEntityFactory;

    @Inject
    ReportInfoFactory mReportInfoFactory;

    @Inject
    MqttTool mMqttTool;

    @Override
    public void mqttConnect(IMqttActionListener mqttActionListener) {
        mMqttTool.mqttConnect(mqttActionListener);
    }

    @Override
    public void mqttDisconnect() {
        mMqttTool.disconnect(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Trace.d(TAG,"disconnect onSuccess");
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                Trace.d(TAG,"disconnect onFailure");
                throwable.printStackTrace();
            }
        });
    }


    @Override
    public void pubDeviceLogin() {
        Long timestamp = System.currentTimeMillis();
        MqttRequestEntity re = mMqttRequestEntityFactory.createMqttRequestEntity();
        re.putBodyParams("timestamp",timestamp);
        re.putBodyParams("mid", mCustomDeviceInfo.mid);
        re.putBodyParams("version", mDeviceInfo.getFirmwareVersion());
        Trace.d(TAG, "pubDeviceLogin:" + re.toJsonString());
        mMqttTool.pub(MqttTopicEnum.DEVICE_LOGIN,re.toJsonString(),new MqttActionListener(){

        });
    }

    @Override
    public void subDeviceLoginResponse(IMqttActionListener mqttActionListener) {
        mMqttTool.sub(MqttTopicEnum.DEVICE_LOGIN_RESPONSE, mqttActionListener);
    }

    @Override
    public void pubDeviceLoginOut() {
        MqttRequestEntity re = mMqttRequestEntityFactory.createMqttRequestEntity();
        re.putBodyParams("type",1);
        Trace.d(TAG,"pubDeviceLoginOut:" + re.toJsonString());
        mMqttTool.pub(MqttTopicEnum.DEVICE_LOGIN_OUT,re.toJsonString(),new MqttActionListener(){

        });
    }

    @Override
    public void subDeviceLoginOutResponse(IMqttActionListener mqttActionListener) {
        mMqttTool.sub(MqttTopicEnum.DEVICE_LOGIN_RESPONSE,mqttActionListener);
    }

    @Override
    public void pubBootDeviceInfo(final TimeRecord timeRecord) {

    }

    @Override
    public void pubRunTimeDeviceInfo(final TimeRecord timeRecord) {
        Trace.d(TAG,"pubRunTimeDeviceInfo(TimeRecord timeRecord)");
        long reportTime = 0l;
        if (timeRecord.getReportTime() != 0) {
            //记录上报过哪一天的运行时间
            reportTime = DateUtil.obtainBelongBeforeDawnTime(timeRecord.getReportTime());
        }
        List<Long> hds = deviceInfoHowManyDays(timeRecord);
        int hdsSize = hds.size();
        Trace.d(TAG,"The TimeRecord contains " + hdsSize + " days!!");
        for (int i = 0; i < hds.size(); i++) {
            long beforeDawn = hds.get(i).longValue();

            if (beforeDawn > reportTime || reportTime == 0) {
                timeRecord.setReportTime(beforeDawn);
                timeRecord.setRunTime(countRunTime(beforeDawn));
                if (i < hdsSize - 1) {
                    CountDownLatch latch = new CountDownLatch(1);
                    pubRunTime(timeRecord, latch);
                    if (latch.getCount() != 0)
                        return;
                } else {
                    if (timeRecord.getOffTime() == 0){
                        return;
                    }
                    pubReportDeviceInfo(timeRecord);
                }

            }
        }

//        pubRunTimeDeviceInfoList(timeRecord,hds,startI);
    }

    @Override
    public void pubReportDeviceInfo(final TimeRecord timeRecord) {
        if (timeRecord.getIs_report() == 1){
            //如果已经上报了开机记录，而关机还没记录
            if (timeRecord.getOffTime() == 0){
                Trace.d(TAG,"offTime is null!");
                Trace.d(TAG, String.format(Constant.BOUNDARY_LINE_END,"pubReportDeviceInfo"));
                return;
            }
        }
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        MqttActionListener mqttActionListener = new MqttActionListener(){
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                super.onSuccess(iMqttToken);
                if (timeRecord.getOffTime() != 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            timeRecord.setIs_report(2);
                            Trace.d(TAG,"pub offTime is success!");
                            mTimetableDao.update(timeRecord);
                            countDownLatch.countDown();
                        }
                    }).start();
                } else if (timeRecord.getBootTime() != 0){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            timeRecord.setIs_report(1);
                            Trace.d(TAG,"pub bootTime is success！");
                            mTimetableDao.update(timeRecord);
                            countDownLatch.countDown();
                        }
                    }).start();
                }
                Trace.d(TAG,"onSuccess");
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                super.onFailure(iMqttToken, throwable);
                    countDownLatch.countDown();
            }
        };
        pubReportDeviceInfo(timeRecord,mqttActionListener);
        try {
            countDownLatch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Trace.d(TAG, String.format(Constant.BOUNDARY_LINE_END,"pubReportDeviceInfo"));
    }

    @Override
    public void subReportDeviceInfoResponse(IMqttActionListener mqttActionListener) {
        mMqttTool.sub(MqttTopicEnum.REPORT_DEVICE_INFO_RESPONSE, mqttActionListener);
    }

    @Override
    public void subCommandReceive(IMqttActionListener mqttActionListener) {
        mMqttTool.sub(MqttTopicEnum.COMMAND_RECEIVE,mqttActionListener);
    }

    @Override
    public void pubCommandReceiveStatus() {
        MqttRequestEntity mr = mMqttRequestEntityFactory.createMqttRequestEntity();
        mr.putBodyParams("mid",mCustomDeviceInfo.mid);
        mr.putBodyParams("timestamp",mr.getTimestamp());
        String message = mr.toJsonString();
        mMqttTool.pub(MqttTopicEnum.COMMAND_RECEIVE_STATUS,message,new MqttActionListener(){

        });
    }

    @Override
    public void pubCpuInfo() {
        Trace.d(TAG,String.format(Constant.BOUNDARY_LINE_BEGIN,"pub cpu info"));
        MqttRequestEntity mr = mMqttRequestEntityFactory.createMqttRequestEntity();
        mr.putBodyParams("deviceType", 2);
        mr.putBodyParams("reported",mReportInfoFactory.createCpuInfo());
        String message = mr.toJsonString();
        mMqttTool.pub(MqttTopicEnum.REPORT_DEVICE_INFO,message,new MqttActionListener(){
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                super.onSuccess(iMqttToken);
                Trace.d(TAG,"pubCpuInfo onSuccess");
                Trace.d(TAG,String.format(Constant.BOUNDARY_LINE_END,"pub cpu info"));
            }
        });
    }

    @Override
    public void pubTemp() {
        MqttRequestEntity mr = mMqttRequestEntityFactory.createMqttRequestEntity();
        mr.putBodyParams("deviceType", 2);
        mr.putBodyParams("reported",mReportInfoFactory.createTempInfo());
        String message = mr.toJsonString();
        mMqttTool.pub(MqttTopicEnum.REPORT_DEVICE_INFO,message,new MqttActionListener(){

        });
    }

    @Override
    public void pubRunTime() {
        MqttRequestEntity mr = mMqttRequestEntityFactory.createMqttRequestEntity();
        mr.putBodyParams("deviceType", 2);
        mr.putBodyParams("reported",new RunTimeInfo());
        String message = mr.toJsonString();
        mMqttTool.pub(MqttTopicEnum.REPORT_DEVICE_INFO,message,new MqttActionListener(){

        });
    }

    private void pubRunTime(final TimeRecord timeRecord,final CountDownLatch latch){
        MqttActionListener mqttActionListener = new MqttActionListener(){
            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                super.onFailure(iMqttToken, throwable);
            }

            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                super.onSuccess(iMqttToken);
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        mTimetableDao.update(timeRecord);
                        latch.countDown();
                    }
                }.start();
            }
        };
        pubReportDeviceInfo(timeRecord,mqttActionListener);
        try {
            latch.await(2,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Trace.d(TAG, String.format(Constant.BOUNDARY_LINE_END,"pubReportDeviceInfo"));
    }

    private void pubRunTimeDeviceInfoList(final TimeRecord timeRecord, final List<Long> hds, int i){
        if (timeRecord == null){
            return;
        }
        if (hds == null){
            return;
        }
        if (i >= hds.size()){
            return;
        }
        if (i == (hds.size() - 1)){
            if (timeRecord.getOffTime() == 0){
                return;
            }
        }
        long beforeDawn = hds.get(i);
        i++;
        Trace.d("11111",DateUtil.simpleFormat(beforeDawn));
        Trace.d("11111",":" + i);
        timeRecord.setReportTime(beforeDawn);
        timeRecord.setRunTime(countRunTime(beforeDawn));
        final ReportDeviceInfo rdi = mReportInfoFactory.createDeviceInfo(timeRecord);
        MqttRequestEntity re = mMqttRequestEntityFactory.createMqttRequestEntity();
        re.putBodyParams("deviceType",2);
        re.putBodyParams("reported",rdi);
        String message = re.toJsonString();
        final int finalI = i;
        mMqttTool.pub(MqttTopicEnum.REPORT_DEVICE_INFO, message, new MqttActionListener(){
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                super.onSuccess(iMqttToken);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (finalI < (hds.size())){
                            Trace.d("11111","update:" + timeRecord);
                            mTimetableDao.update(timeRecord);
                        }else {
                            //如果是最后一个数据，且offTime已经确定
                            if (timeRecord.getOffTime() !=0 ){
                                timeRecord.setIs_report(2);
                                mTimetableDao.update(timeRecord);
                            }
                        }
                        pubRunTimeDeviceInfoList(timeRecord,hds,finalI);
                    }
                }).start();
            }
        });

    }
    /**
     *计算制定日期的运行时间
     * @param belongTime 所属日期的任意时间截
     * @return
     */
    private long countRunTime(long belongTime){
        //获取所属日期的凌晨时间截
        long beforeDawn = DateUtil.obtainBelongBeforeDawnTime(belongTime);
        //获取所属日期截止的时间截
        long endTime = beforeDawn + DateUtil.DAY_TIME;
        List<TimeRecord> timeRecords = mTimetableDao.getDayRunTime(beforeDawn,endTime);
        long runTime = 0l;
        for (int i = 0; i < timeRecords.size(); i++) {
            TimeRecord timeRecord = timeRecords.get(i);
            long bootTime = timeRecord.getBootTime();
            long offTime = timeRecord.getOffTime();
            if (offTime == 0){
                offTime = System.currentTimeMillis();
            }
            long addTime;
            if (bootTime >= beforeDawn && offTime <= endTime){
                addTime = offTime - bootTime;
            }else if (bootTime < beforeDawn && offTime <=endTime){
                addTime = offTime - beforeDawn;
            }else if (bootTime >= beforeDawn && offTime > endTime){
                addTime = endTime - bootTime;
            }else {
                addTime = DateUtil.DAY_TIME;
            }
            if (addTime > 0) {
                runTime += addTime;
            }
        }
        Trace.d(TAG,"st:" + DateUtil.simpleFormat(beforeDawn) + "/" + "et:" + DateUtil.simpleFormat(endTime));
        Trace.d(TAG,timeRecords.size() + "/" + "runTime:" + runTime + "/" + "belongTime:" + DateUtil.simpleFormat(belongTime));
        return runTime;
    }

    /**
     * 返回一个开关机记录里包含多少天
     * @param timeRecord
     * @return
     */
    private List<Long> deviceInfoHowManyDays(TimeRecord timeRecord){
        List<Long> list = new ArrayList<>();
        long bootTime = timeRecord.getBootTime();
        long offTime = timeRecord.getOffTime();
        if (offTime == 0){
            offTime = System.currentTimeMillis();
        }

        long bootBeforeDawn = DateUtil.obtainBelongBeforeDawnTime(bootTime);
        long offBeforeDawn = DateUtil.obtainBelongBeforeDawnTime(offTime);
        list.add(bootBeforeDawn);
        if (bootBeforeDawn == offBeforeDawn){
            return list;
        }
        int size = (int) ((offBeforeDawn - bootBeforeDawn) / DateUtil.DAY_TIME) - 1;

        for (int i = 0; i < size; i++) {
            long beforeDawn = bootBeforeDawn + (i + 1) * DateUtil.DAY_TIME;
            list.add(beforeDawn);
        }
        list.add(offBeforeDawn);
        return list;
    }

    /**
     * 上报设备信息
     * @param timeRecord
    *
     */
    public void pubReportDeviceInfo(final TimeRecord timeRecord,MqttActionListener mqttActionListener) {
        Trace.d(TAG, String.format(Constant.BOUNDARY_LINE_BEGIN,"pubReportDeviceInfo"));
        if (timeRecord == null){
            Trace.d(TAG, "上传记录为空！！");
            Trace.d(TAG, String.format(Constant.BOUNDARY_LINE_END,"pubReportDeviceInfo"));
            return;
        }

        final ReportDeviceInfo rdi = mReportInfoFactory.createDeviceInfo(timeRecord);


        MqttRequestEntity re = mMqttRequestEntityFactory.createMqttRequestEntity();
        re.putBodyParams("deviceType",2);
        re.putBodyParams("reported",rdi);
        String message = re.toJsonString();
        mMqttTool.pub(MqttTopicEnum.REPORT_DEVICE_INFO, message, mqttActionListener);
        awaitCountDownLatch();
    }


    public static void countDown(){
        if (mCountDownLatch != null && mCountDownLatch.getCount() != 0){
            mCountDownLatch.countDown();
        }
    }

    private void awaitCountDownLatch(){
        mCountDownLatch = new CountDownLatch(1);
        try {
            mCountDownLatch.await(2,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
