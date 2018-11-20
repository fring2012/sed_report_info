package com.adupdate.sed_report_demo.entity.observer.interval;

import com.adupdate.sed_report_demo.app.App;
import com.adupdate.sed_report_demo.entity.observer.interval.base.IntervalObserver;
import com.adupdate.sed_report_demo.presenter.MqttPresenter;

public class ReportRunTimeObserver extends IntervalObserver{
    private MqttPresenter mMqttPresenter;

    public ReportRunTimeObserver(MqttPresenter mqttPresenter) {
        mMqttPresenter = mqttPresenter;
    }

    @Override
    public void onNext(Long value) {
        super.onNext(value);
        mMqttPresenter.pubRunTime();
    }
}
