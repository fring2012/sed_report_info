package com.adupdate.sed_report_demo.entity.observer.interval;

import com.adupdate.sed_report_demo.app.App;
import com.adupdate.sed_report_demo.entity.observer.interval.base.IntervalObserver;
import com.adupdate.sed_report_demo.presenter.HttpPresenter;

public class ReportLogObserver extends IntervalObserver {
    private HttpPresenter mHttpPresenter;

    public ReportLogObserver(HttpPresenter httpPresenter) {
        mHttpPresenter = httpPresenter;
    }
    @Override
    public void onNext(Long value) {
        super.onNext(value);
        mHttpPresenter.reportLog();
    }
}
