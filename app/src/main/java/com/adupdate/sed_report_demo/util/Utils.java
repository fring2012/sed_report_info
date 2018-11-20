package com.adupdate.sed_report_demo.util;


import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.adupdate.sed_report_demo.app.App;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

public class Utils {
    private static final String TAG = "Utils";
    private static DecimalFormat df = new DecimalFormat("0");
    private final static String CUR_PATH = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq";//保存当前CPU频率
    private final static String CUR_PATH1= "/sys/devices/system/cpu/cpu1/cpufreq/scaling_cur_freq";
    private final static String  CPU_PRESENT = "/sys/devices/system/cpu/present";
    private final static String CPU_TEMP_PATH = "/sys/class/thermal/thermal_zone9/temp";

    @Inject
    Context mCx;

    /**
     * 判断build.prop文件中fota配置是否可用
     *
     * @return
     */
    public boolean buildConfigValid() {
        try {
            ClassLoader cl = mCx.getClassLoader();
            Class SystemProperties = cl.loadClass("android.os.SystemProperties");
            Class[] paramTypes = new Class[1];
            paramTypes[0] = String.class;

            Method get = SystemProperties.getMethod("get", paramTypes);

            String version = (String) get.invoke(SystemProperties, new Object[]{"ro.fota.version"});
            String oem = (String) get.invoke(SystemProperties, new Object[]{"ro.fota.oem"});
            String models = (String) get.invoke(SystemProperties, new Object[]{"ro.fota.device"});
            String platform = (String) get.invoke(SystemProperties, new Object[]{"ro.fota.platform"});
            String deviceType = (String) get.invoke(SystemProperties, new Object[]{"ro.fota.type"});

            if (!TextUtils.isEmpty(version) &&
                    !TextUtils.isEmpty(oem) &&
                    !TextUtils.isEmpty(models) &&
                    !TextUtils.isEmpty(platform) &&
                    !TextUtils.isEmpty(deviceType)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 获取 IMEI 码
     * <p>需添加权限
     * {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @return IMEI 码
     */
    public  String getIMEI() {
        TelephonyManager tm =
                (TelephonyManager) mCx.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null){
            String imei = tm.getDeviceId();
            if (!TextUtils.isEmpty(imei) && !"unknown".equalsIgnoreCase(imei)) {
                return imei;
            }
        }
        return "";
    }

    /**
     * 获取SN号
     *
     * @return SN 号
     */
    public static String getSN() {
        return Build.SERIAL;
    }

    public static String formatDoubleToInteger(Double d){
        return df.format(d);
    }


    /**
     * 获取系统内存信息
     * 系统剩余内存:info.availMem
     * 系统内存：info.totalMem
     * @param context
     * @return
     */
    public static ActivityManager.MemoryInfo getMemoryInfo(Context context){
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(info);
        return info;
    }

    /**
     * 获取cpu频率
     */
    public static long getCurFreq(){
        ShellUtils.CommandResult commandResult = ShellUtils.execCmd("cat /sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_cur_freq",true,true);
        if (commandResult.result == 0){
            return Long.valueOf(commandResult.successMsg);
        }else {
            Trace.d(TAG,commandResult.errorMsg);
            return -1;
        }
    }

    /**
     * 获取cpu使用率
     * @return
     */
    public static int getCpuUsage(){
        ShellUtils.CommandResult commandResult = ShellUtils.execCmd("top -n 1",false,true);
        if (commandResult.result == 0){
            String successMsg = commandResult.successMsg;
            List<String> strList = getSubUtil(successMsg,"[0-9]+");
            int sum = 0;
            for (int i = 0; i < 4; i++) {
                sum +=  Integer.valueOf(strList.get(i));
            }
            return sum;
        }else {
            Trace.d(TAG,commandResult.errorMsg);
            return 0;
        }
    }

    /**
     * 获取cpu温度
     * @return
     */
    public static int getCpuTemp() {
        int result = 0;
        Trace.d(TAG,"temp:" + getFileFirstLineText(CPU_TEMP_PATH));
        result = Integer.parseInt(getFileFirstLineText(CPU_TEMP_PATH));
        return result;
    }

    /**
     * 获取当前CPU频率
     * @return
     */
    public static long getCurCPU(){
        long result = 0;
        result = Long.parseLong(getFileFirstLineText(CUR_PATH));
        return result;
    }
    /**
     * 获取当前CPU频率
     * @return
     */
    public static long getCurCPU1(){
        long result = 0;
        result = Long.parseLong(getFileFirstLineText(CUR_PATH1));
        return result;
    }
    /**
     * 获取cpu核心数
     * @return
     */
    public static int getCpuPresent(){
        int result = 0;
        String present = getFileFirstLineText(CPU_PRESENT);
        result = Integer.parseInt(present.charAt(present.length()-1) + "");

        return result + 1;
    }

    private static String  getFileFirstLineText(String filePath){
        String firstLieText = null;
        FileReader fr = null;
        BufferedReader br = null;
        try{
            fr = new FileReader(filePath);
            br = new BufferedReader(fr);
            String text = br.readLine();
            firstLieText = text.trim();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            ShellUtils.closeIO(fr,br);
        }
        return firstLieText;
    }


    /**
     * 正则表达式匹配 rgex表达式
     * @param soap
     * @param rgex
     * @return
     */
    public static List<String> getSubUtil(String soap, String rgex){
        List<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式
        Matcher m = pattern.matcher(soap);
        while (m.find()) {
            list.add(m.group(0));
        }
        return list;
    }
}

