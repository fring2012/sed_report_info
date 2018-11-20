package com.adupdate.sed_report_demo.entity.reportinfo.base;

import com.adupdate.sed_report_demo.entity.CustomDeviceInfo;
import com.adupdate.sed_report_demo.entity.DeviceInfo;
import com.google.gson.annotations.SerializedName;

public class RequiredParams {
    @SerializedName("mid")
    private String mid;
    //安卓版本号
    @SerializedName("androidVersion")
    private String androidVersion;
    //固件版本号
    @SerializedName("version")
    private String version ;

   public  RequiredParams(){

    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
