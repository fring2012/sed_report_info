package com.adupdate.sed_report_demo.presenter;

import com.adupdate.sed_report_demo.http.ResultBody;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.functions.Consumer;

public interface HttpPresenter {
    /**
     * http设备注册
     */
    void deviceRegister(Observer<ResultBody> observer);

    /**
     * http文件上传
     */
    void reportLog();
}
