package com.adupdate.sed_report_demo.entity;

import android.content.Context;

import com.adupdate.sed_report_demo.app.App;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.inject.Inject;

public class DeviceInfo {
    @Inject
    Context mCx;
    public DeviceInfo(){
        obtainDeviceVersion();
    }

    private String deviceId;
    private String deviceSecret;
    private String androidVersion;
    private String  firmwareVersion;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceSecret() {
        return deviceSecret;
    }

    public void setDeviceSecret(String deviceSecret) {
        this.deviceSecret = deviceSecret;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "deviceId='" + deviceId + '\'' +
                ", deviceSecret='" + deviceSecret + '\'' +
                ", androidVersion='" + androidVersion + '\'' +
                ", firmwareVersion='" + firmwareVersion + '\'' +
                '}';
    }


    /**
     * 获取固件版本号和安卓版本号
     */
    public  void obtainDeviceVersion(){
        ClassLoader cl = mCx.getClassLoader();
        try {
            Class SystemProperties = cl.loadClass("android.os.SystemProperties");
            Class[] paramTypes = new Class[1];
            paramTypes[0] = String.class;
            Method get = SystemProperties.getMethod("get", paramTypes);
            androidVersion = (String) get.invoke(SystemProperties, new Object[]{"ro.build.version.sdk"});
            firmwareVersion = (String) get.invoke(SystemProperties, new Object[]{"ro.build.display.id"});
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}
