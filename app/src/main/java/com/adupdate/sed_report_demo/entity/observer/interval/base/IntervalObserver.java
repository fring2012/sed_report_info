package com.adupdate.sed_report_demo.entity.observer.interval.base;

import com.adupdate.sed_report_demo.entity.observer.ObserverAbstract;
import com.adupdate.sed_report_demo.presenter.HttpPresenter;
import com.adupdate.sed_report_demo.presenter.MqttPresenter;

import javax.inject.Inject;

public class IntervalObserver extends ObserverAbstract<Long> {
    private boolean unsubscribe = false;

    @Override
    public void onNext(Long value) {
        super.onNext(value);
        if (unsubscribe){
            disposable.dispose();
            return;
        }
    }

    public boolean isUnsubscribe() {
        return unsubscribe;
    }

    public void setUnsubscribe(boolean unsubscribe) {
        this.unsubscribe = unsubscribe;
    }
}
