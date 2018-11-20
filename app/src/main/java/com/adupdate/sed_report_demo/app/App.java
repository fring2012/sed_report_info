package com.adupdate.sed_report_demo.app;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.IntentService;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.input.InputManager;
import android.hardware.usb.UsbManager;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.InputDevice;


import com.adupdate.sed_report_demo.BuildConfig;
import com.adupdate.sed_report_demo.dagger.component.DaggerAppComponent;
import com.adupdate.sed_report_demo.dagger.module.AppModule;
import com.adupdate.sed_report_demo.db.RoomDemoDatabase;
import com.adupdate.sed_report_demo.db.dao.TimetableDao;

import com.adupdate.sed_report_demo.entity.TimeRecord;
import com.adupdate.sed_report_demo.presenter.HttpPresenter;
import com.adupdate.sed_report_demo.presenter.MqttPresenter;
import com.adupdate.sed_report_demo.presenter.impl.HttpPresenterImpl;
import com.adupdate.sed_report_demo.presenter.impl.MqttPresenterImpl;
import com.adupdate.sed_report_demo.receiver.CollectionInfoReceiver;
import com.adupdate.sed_report_demo.receiver.UsbManagerReceiver;
import com.adupdate.sed_report_demo.service.ReporterService;
import com.adupdate.sed_report_demo.util.DateUtil;
import com.adupdate.sed_report_demo.util.Trace;

import java.net.URL;
import java.net.URLConnection;
import java.util.List;


import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.DaggerApplication;


public class App extends DaggerApplication {
    private static final String TAG = "App";

    @Inject
    public  TimetableDao timetableDao;
    @Inject
    public Context mCx;

    private static ContentResolver mCr;
    private static UsbManagerReceiver mUsbStateChangeReceiver;


    private static final long INTERVAL = DateUtil.MINUTE_TIME * 5;// 1h


    @Override
    public void onCreate() {
        super.onCreate();
        haveWriteExternalStorage();

        //initDao();
        seeTimeRecordList();

        detectUsbWithBroadcast();
        detectUsbDeviceWithInputManager();
        //getNetTime();

        mCr = getContentResolver();

        ReporterService.startReporterService(mCx, ReporterService.TASK_CLEAN_EXCEPTION_TIME_RECORD);

        //app启动任务
        ReporterService.startReporterService(mCx, ReporterService.TASK_START_UP);

        //定时发布设备运行总时间信息
        startReportNewestRecordAlarm(getApplicationContext());

    }


    /**
     * 判断外置存储的读写权限
     *
     * @return
     */
    public boolean haveWriteExternalStorage() {
        String permissionW = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String permissionR = Manifest.permission.READ_EXTERNAL_STORAGE;
        boolean result = (ContextCompat.checkSelfPermission(this, permissionW) == PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(this, permissionR) == PackageManager.PERMISSION_GRANTED);
        Trace.write_file(result);
        return result;
    }


    private void startReportNewestRecordAlarm(Context context) {
        Intent intent = new Intent(context, CollectionInfoReceiver.class);
//        Intent intent = new Intent();
//        intent.setComponent(new ComponentName("com.adupdate.sed_report_demo","" +
//                "com.adupdate.sed_report_demo.receiver.CollectionInfoReceiver"));
        intent.setAction(CollectionInfoReceiver.ACTION_REPORT_NEWEST_TOTAL_RUN_TIME_RECORD);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0
                , intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pendingIntent);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, 0 + INTERVAL, INTERVAL, pendingIntent);

    }


    private void seeTimeRecordList() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                timetableDao.deleteById(16);
                List<TimeRecord> timeRecords = timetableDao.getAll();
                for (int i = 0; i < timeRecords.size(); i++) {
                    Trace.d(TAG, timeRecords.get(i).toString());
                }
            }
        }.start();
    }

    /**
     * 获取自动获取时间设置状态
     * 1---设置为自动获取网络时间
     * 0---设置为自定义获取网络时间
     * @return
     */
    public static String getAutoTimeState() {
        return android.provider.Settings.System.getString(mCr, Settings.System.AUTO_TIME);
    }

    private void getNetTime() {

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    URL url = null;//取得资源对象
                    url = new URL(BuildConfig.httpUrl);
                    //url = new URL("http://www.ntsc.ac.cn");//中国科学院国家授时中心
                    //url = new URL("http://www.bjtime.cn");
                    URLConnection uc = url.openConnection();//生成连接对象
                    uc.connect(); //发出连接
                    long ld = uc.getDate(); //取得网站日期时间
                    Trace.d(TAG,DateUtil.simpleFormat(ld));
                    Trace.d(TAG,DateUtil.simpleFormat(System.currentTimeMillis()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private void detectUsbWithBroadcast() {
        Trace.d(TAG, "listenUsb: register");
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
        filter.addAction("android.hardware.usb.action.USB_STATE");

        registerReceiver(mUsbStateChangeReceiver, filter);
        Trace.d(TAG, "listenUsb: registered");
    }

    private void detectUsbDeviceWithInputManager() {
        InputManager im = (InputManager) getSystemService(INPUT_SERVICE);
        int[] devices = im.getInputDeviceIds();
        for (int id : devices) {
            InputDevice device = im.getInputDevice(id);
            Trace.d(TAG, "detectUsbDeviceWithInputManager: " + device.getName());
            //do something
        }
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().setModule(new AppModule(this)).create(this);
    }
}
