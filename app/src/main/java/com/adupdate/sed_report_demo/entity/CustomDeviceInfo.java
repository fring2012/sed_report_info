package com.adupdate.sed_report_demo.entity;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.adupdate.sed_report_demo.app.App;
import com.adupdate.sed_report_demo.helper.PropertiesHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.inject.Inject;

public class CustomDeviceInfo {
    private static volatile CustomDeviceInfo customDeviceInfo;

    @Inject
    public Context mCx;

    @Inject
    PropertiesHelper mPropertiesHelper;

    public CustomDeviceInfo(){
        obtainCustomDeviceInfo();
    }

    public CustomDeviceInfo setMid(String mid) {
        this.mid = mid;
        return this;
    }



    public CustomDeviceInfo setVersion(String version) {
        this.version = version;
        return this;
    }

    public CustomDeviceInfo setOem(String oem) {
        this.oem = oem;
        return this;
    }

    public CustomDeviceInfo setModels(String models) {
        this.models = models;
        return this;
    }

    public CustomDeviceInfo setPlatform(String platform) {
        this.platform = platform;
        return this;
    }

    public CustomDeviceInfo setDeviceType(String deviceType) {
        this.deviceType = deviceType;
        return this;
    }

    /**
     * 设备唯一标识码
     */
    public String mid = "kaii";


    /**
     * 设备版本号
     */
    public String version = "7.0";
    /**
     * 厂商信息，广升提供
     */
    public String oem = "2234";
    /**
     * 设备型号，同一个厂商下面不允许出现相同型号的设备。oem+ models组成一个项目
     */
    public String models = "MI3C";
    /**
     * 芯片平台信息，如MTK6582、SPRD8830、MSM9x15，广升给出平台列表
     */
    public String platform = "BCM21654";
    /**
     * 设备类型，如phone、box、pad、mifi等，广升给出类型列表1
     */
    public String deviceType = "radiophone";

    public  void obtainCustomDeviceInfo(){
        //sn号作为mid
        this.mid =
//                "LVXDSA13";
//                "DVCSSAD12";
//                "SED_LVSVCS345";
//                "SED_SRVSD53";
                //"XSDAWEC45";
                Build.SERIAL;
//        if (buildParamsIsEnough()){
//            obtainBuildParams();
//        }else {
        this.version = mPropertiesHelper.getValue("ro.fota.version");
        this.oem = mPropertiesHelper.getValue("ro.fota.oem");
        this.models = mPropertiesHelper.getValue("ro.fota.device");
        this.platform = mPropertiesHelper.getValue("ro.fota.platform");
        this.deviceType = mPropertiesHelper.getValue("ro.fota.type");
//        }

    }
    private void obtainBuildParams(){
        ClassLoader cl = mCx.getClassLoader();
        try {
            Class SystemProperties = cl.loadClass("android.os.SystemProperties");
            Class[] paramTypes = new Class[1];
            paramTypes[0] = String.class;
            Method get = SystemProperties.getMethod("get", paramTypes);
            this.version = (String) get.invoke(SystemProperties, new Object[]{"ro.fota.version"});
            this.oem = (String) get.invoke(SystemProperties, new Object[]{"ro.fota.oem"});
            this.models = (String) get.invoke(SystemProperties, new Object[]{"ro.fota.device"});
            this.platform = (String) get.invoke(SystemProperties, new Object[]{"ro.fota.platform"});
            this.deviceType = (String) get.invoke(SystemProperties, new Object[]{"ro.fota.type"});

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
    private boolean buildParamsIsEnough(){
        ClassLoader cl = mCx.getClassLoader();
        boolean enough = false;
        try {
            Class SystemProperties = cl.loadClass("android.os.SystemProperties");
            Class[] paramTypes = new Class[1];
            paramTypes[0] = String.class;
            Method get = SystemProperties.getMethod("get", paramTypes);
            String version = (String) get.invoke(SystemProperties, new Object[]{"ro.fota.version"});
            String oem = (String) get.invoke(SystemProperties, new Object[]{"ro.fota.oem"});
            String models = (String) get.invoke(SystemProperties, new Object[]{"ro.fota.device"});
            String platform = (String) get.invoke(SystemProperties, new Object[]{"ro.fota.platform"});
            String deviceType = (String) get.invoke(SystemProperties, new Object[]{"ro.fota.type"});
            if (!TextUtils.isEmpty(version) && !TextUtils.isEmpty(oem) && !TextUtils.isEmpty(models)
                    && !TextUtils.isEmpty(platform) && !TextUtils.isEmpty(deviceType)){
                enough = true;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return enough;
    }
}
