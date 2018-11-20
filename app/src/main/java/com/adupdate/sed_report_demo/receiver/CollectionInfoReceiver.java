package com.adupdate.sed_report_demo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.adupdate.sed_report_demo.aspect.annotation.Async;
import com.adupdate.sed_report_demo.app.App;
import com.adupdate.sed_report_demo.entity.IpAddress;
import com.adupdate.sed_report_demo.entity.TimeRecord;
import com.adupdate.sed_report_demo.service.ReporterService;
import com.adupdate.sed_report_demo.util.DateUtil;
import com.adupdate.sed_report_demo.util.NetUtil;
import com.adupdate.sed_report_demo.helper.SharedPreferencesHelper;
import com.adupdate.sed_report_demo.util.Trace;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class CollectionInfoReceiver extends BroadcastReceiver {
    private static final String TAG = "CollectionInfoReceiver";
    public static final String ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
    public static final String ACTION_SHUT_DOWN = "android.intent.action.ACTION_SHUTDOWN";
    public static final String ACTION_CHANGE_NETWORK = "android.net.conn.CONNECTIVITY_CHANGE";
    public static final String ACTION_REPORT_NEWEST_TOTAL_RUN_TIME_RECORD = "report_newest_total_run_time_record";

    @Inject
    SharedPreferencesHelper mSharedPreferencesHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        AndroidInjection.inject(this,context);
        String action = intent.getAction();
        Trace.d(TAG,"onReceive: " + action);
        switch (action){
            case ACTION_BOOT_COMPLETED:
                long bootTime = DateUtil.obtainBootTime();
                Trace.d(TAG,"boot time :" + DateUtil.simpleFormat(bootTime));
                bootTimeIpAddressRecord(context);
                break;
            case ACTION_SHUT_DOWN:
                long currentTime = DateUtil.obtainCurrentTime();
                Trace.d(TAG," shut down time:" + DateUtil.simpleFormat(currentTime));
                updateLastShutDownRecord(context);
                break;
            case ACTION_CHANGE_NETWORK:
                if (NetUtil.isConnect(context)) {
                    updateNewestIpAddress(context);
                }
            case ACTION_REPORT_NEWEST_TOTAL_RUN_TIME_RECORD:
                ReporterService.startReporterService(context,ReporterService.TASK_REPORT_NEWEST_TOTAL_RUN_TIME);
                break;
            default:
                break;
        }
    }




    /**
     * 记录开机时间和ip
     */
    @Async
    private void bootTimeIpAddressRecord(Context context){
        Trace.d(TAG,"接收到开机广播！！！");
        mSharedPreferencesHelper.putBootCompletedTotalRunTime();
        ReporterService.startReporterService(context,ReporterService.TASK_REPORT_NEWEST_DEVICE_INFO);
    }

    /**
     * 记录关机时间和使用时长
     */
    private void updateLastShutDownRecord(Context context){
       ReporterService.startReporterService(context,ReporterService.TASK_SHUT_DOWN);
    }

    /**
     * 记录IP
     * @param context
     */
    private void updateNewestIpAddress(Context context){
        ReporterService.startReporterService(context,ReporterService.TASK_UPDATE_IP);
    }

    /**
     * 记录ip地址
     * @Async表示异步执行该方法
     * @param currentTime
     * @param context
     */
    @Async
    public void insertIpAddress(String currentTime,Context context){
        IpAddress ipAddress = new IpAddress();
        String ip = NetUtil.getIPAddress(context);
        ipAddress.setTime(currentTime);
        ipAddress.setIp(ip);
    }




}
