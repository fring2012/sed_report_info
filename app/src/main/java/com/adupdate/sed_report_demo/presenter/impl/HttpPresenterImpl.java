package com.adupdate.sed_report_demo.presenter.impl;


import android.support.annotation.NonNull;

import com.adupdate.sed_report_demo.app.constant.Constant;
import com.adupdate.sed_report_demo.entity.CustomDeviceInfo;
import com.adupdate.sed_report_demo.entity.DeviceInfo;
import com.adupdate.sed_report_demo.entity.ProductInfo;
import com.adupdate.sed_report_demo.entity.observer.ObserverAbstract;
import com.adupdate.sed_report_demo.http.ResultBody;
import com.adupdate.sed_report_demo.http.retrofitservice.ReportService;
import com.adupdate.sed_report_demo.presenter.HttpPresenter;
import com.adupdate.sed_report_demo.util.Codec2;
import com.adupdate.sed_report_demo.util.GsonUtil;
import com.adupdate.sed_report_demo.util.Trace;
import com.adupdate.sed_report_demo.util.ZipUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class HttpPresenterImpl implements HttpPresenter {
    private static final String TAG = "HttpPresenterImpl";

    @Inject
    CustomDeviceInfo mCustomDeviceInfo;

    @Inject
    DeviceInfo mDeviceInfo;

    @Inject
    ProductInfo mProductInfo;

    @Override
    public void deviceRegister(Observer<ResultBody> observer) {
        if (observer == null){
            return;
        }
        Trace.d(TAG, String.format(Constant.BOUNDARY_LINE_BEGIN,"device register"));
        ReportService rs = ReportService.Build.build();
        Map<String,Object> map = new HashMap<>();
        Long timestamp = System.currentTimeMillis();
        String signStr = mCustomDeviceInfo.mid + mProductInfo.productId + timestamp;
        String sign = Codec2.getHmacMd5Str(signStr,mProductInfo.productSecret);
        map.put("mid",mCustomDeviceInfo.mid);
        map.put("oem",mCustomDeviceInfo.oem);
        map.put("models",mCustomDeviceInfo.models);
        map.put("platform",mCustomDeviceInfo.platform);
        map.put("deviceType",mCustomDeviceInfo.deviceType);
        map.put("timestamp",timestamp);
        map.put("sign",sign);
        map.put("sdkversion",mDeviceInfo.getAndroidVersion());
        map.put("appversion","1.0");
        map.put("version","0.0.8");
        map.put("networkType","WIFI");
        Observable<ResultBody> observable = rs.registerDevice(map,mProductInfo.productId);
        Trace.d(TAG,"request body : " + GsonUtil.toJsonString(map));

        observable
            .retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                        // 参数Observable<Throwable>中的泛型 = 上游操作符抛出的异常，可通过该条件来判断异常的类型
                        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(@NonNull Throwable throwable) throws Exception {
                                Trace.d(TAG,"Thread:" + Thread.currentThread().getName());
                                // 输出网络异常信息
                                Trace.e(TAG, "device register error : " + throwable.toString());
                                //等待10秒才选择重试
                                return Observable.just(1).delay(10, TimeUnit.SECONDS);
                            }
                        });
                    }
                }).subscribe(observer);
    }

    @Override
    public void reportLog() {
        Trace.d(TAG,String.format(Constant.BOUNDARY_LINE_BEGIN,"reportLog"));
        final File logTextFile = new File(Trace.getM_log_path());
        if (!logTextFile.exists()){
            Trace.d(TAG,"无日志文件");
            Trace.d(TAG, String.format(Constant.BOUNDARY_LINE_END,"reportLog"));
            return;
        }
        String fileName = ZipUtil.toZip(Trace.getM_log_path(),true);
        final File file = new File(ZipUtil.OUT_PATH + fileName + ".zip");
        if (file.exists()){
            Trace.d(TAG,"压缩成功!压缩文件：" + file.getName());
        }else {
            Trace.d(TAG,"压缩失败！");
            Trace.d(TAG, String.format(Constant.BOUNDARY_LINE_END,"reportLog"));
            return;
        }

        ReportService rs = ReportService.Build.testBuild();
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("logFile", file.getName(), requestFile);

        RequestBody mid =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), mCustomDeviceInfo.mid);

        RequestBody androidVersion =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"),mDeviceInfo.getAndroidVersion());

        RequestBody version =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"),mDeviceInfo.getFirmwareVersion());

        Observable<ResultBody> observable = rs.reportLog(mProductInfo.getProductId(), mDeviceInfo.getDeviceId(),
                mid,androidVersion,version,body);

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverAbstract<ResultBody>(){
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (file.exists()){
                            file.delete();
                        }
                        if (e != null)
                            e.printStackTrace();
                        Trace.d(TAG, String.format(Constant.BOUNDARY_LINE_END,"reportLog"));
                    }

                    @Override
                    public void onNext(ResultBody resultBody) {
                        super.onNext(resultBody);
                        Trace.d(TAG,resultBody.toString());
                        if (file.exists()){
                            file.delete();
                        }
                        int status = resultBody.getStatus();
                        if (status == 1000){
                            Trace.d(TAG,"日志上传成功！");
                            Trace.d(TAG, String.format(Constant.BOUNDARY_LINE_END,"reportLog"));
                            return;
                        }
                        Trace.d(TAG, String.format(Constant.BOUNDARY_LINE_END,"reportLog"));
                        Trace.d(TAG,"日志上传失败！");
                    }
                });

//                new Consumer<ResultBody>() {
//
//                    @Override
//                    public void accept(ResultBody resultBody) throws Exception {
//                        Trace.d(TAG,resultBody.toString());
//                        if (file.exists()){
//                            file.delete();
//                        }
//                        int status = resultBody.getStatus();
//                        if (status == 1000){
//                            Trace.d(TAG,"日志上传成功！");
////                            if (logTextFile.exists()){
////                                logTextFile.delete();
////                            }
//                            Trace.d(TAG, String.format(Constant.BOUNDARY_LINE_END,"reportLog"));
//                            return;
//                        }
//                        Trace.d(TAG, String.format(Constant.BOUNDARY_LINE_END,"reportLog"));
//                        Trace.d(TAG,"日志上传失败！");
//
//                    }
//                });
    }
}
