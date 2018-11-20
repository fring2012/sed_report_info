package com.adupdate.sed_report_demo.factory;

import android.content.Context;

import com.adupdate.sed_report_demo.entity.observer.interval.PubCpuInfoObserver;
import com.adupdate.sed_report_demo.entity.observer.interval.PubDeviceInfoObserver;
import com.adupdate.sed_report_demo.entity.observer.interval.ReportLogObserver;
import com.adupdate.sed_report_demo.entity.observer.interval.ReportRunTimeObserver;
import com.adupdate.sed_report_demo.entity.observer.interval.ReportTempObserver;
import com.adupdate.sed_report_demo.entity.observer.interval.base.IntervalObserver;
import com.adupdate.sed_report_demo.presenter.HttpPresenter;
import com.adupdate.sed_report_demo.presenter.MqttPresenter;

import javax.inject.Inject;

import dagger.Component;

public class IntervalObserverFactory {
    @Inject
    MqttPresenter mMqttPresenter;

    @Inject
    Context mCx;

    @Inject
    HttpPresenter mHttpPresenter;

    public IntervalObserver createIntervalObserverFactory(IntervalObserver intervalObserver) {
        IntervalObserver observer = null;
        if (intervalObserver instanceof ReportTempObserver){
            observer = new ReportTempObserver(mMqttPresenter);
        }else if (intervalObserver instanceof ReportLogObserver){
            observer = new ReportLogObserver(mHttpPresenter);
        }else if (intervalObserver instanceof PubCpuInfoObserver){
            observer = new PubCpuInfoObserver(mMqttPresenter);
        }else if(intervalObserver instanceof ReportRunTimeObserver){
            observer = new ReportRunTimeObserver(mMqttPresenter);
        }else if (intervalObserver instanceof PubDeviceInfoObserver){

        }
        return observer;
    }
}
