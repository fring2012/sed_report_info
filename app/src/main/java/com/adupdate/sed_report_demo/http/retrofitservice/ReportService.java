package com.adupdate.sed_report_demo.http.retrofitservice;


import com.adupdate.sed_report_demo.http.ResultBody;
import com.adupdate.sed_report_demo.http.RetrofitFactory;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ReportService {

    @POST("/register/{productId}")
    Observable<ResultBody> registerDevice(@Body Map<String,Object> map, @Path("productId")String productId);

    @Multipart
    @POST("/product/{productId}/{deviceId}/shadow/reportLog")
    Observable<ResultBody> reportLog(@Path("productId")String productId, @Path("deviceId")String deviceId,
                                     @Part("mid") RequestBody mid,
                                     @Part("androidVersion") RequestBody androidVersion,
                                     @Part("version") RequestBody version,
                                     @Part MultipartBody.Part file);

    class Build{
        public static ReportService build()  {
            return RetrofitFactory.createRetrofit().create(ReportService.class);
        }

        public static ReportService testBuild(){
            return RetrofitFactory.createTestRetrofit().create(ReportService.class);
        }
    }
}

