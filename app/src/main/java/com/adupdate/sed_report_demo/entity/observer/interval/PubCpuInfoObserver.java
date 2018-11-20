package com.adupdate.sed_report_demo.entity.observer.interval;

import com.adupdate.sed_report_demo.app.App;
import com.adupdate.sed_report_demo.entity.observer.interval.base.IntervalObserver;
import com.adupdate.sed_report_demo.presenter.MqttPresenter;

import javax.inject.Inject;

public class PubCpuInfoObserver extends IntervalObserver {
    private MqttPresenter mMqttPresenter;

    public PubCpuInfoObserver(MqttPresenter mqttPresenter) {
        mMqttPresenter = mqttPresenter;
    }

    @Override
    public void onNext(Long value) {
        super.onNext(value);
        mMqttPresenter.pubCpuInfo();
    }
}
