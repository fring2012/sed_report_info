package com.adupdate.sed_report_demo.entity.observer.interval;

import android.content.Context;

import com.adupdate.sed_report_demo.app.App;
import com.adupdate.sed_report_demo.entity.observer.interval.base.IntervalObserver;
import com.adupdate.sed_report_demo.mqtt.MqttTool;
import com.adupdate.sed_report_demo.mqtt.constant.MqttConnectState;
import com.adupdate.sed_report_demo.service.ReporterService;

import java.util.concurrent.Callable;

import javax.inject.Inject;

public class PubDeviceInfoObserver extends IntervalObserver {
    private Context mCx;
    public PubDeviceInfoObserver(Context context) {
        mCx = context;
    }

    @Override
    public void onNext(Long value) {
        super.onNext(value);
        ReporterService.startReporterService(mCx,ReporterService.TASK_REPORT_NEWEST_TOTAL_RUN_TIME);
    }
}
