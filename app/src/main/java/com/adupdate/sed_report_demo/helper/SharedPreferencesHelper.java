package com.adupdate.sed_report_demo.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.adupdate.sed_report_demo.app.App;
import com.adupdate.sed_report_demo.mqtt.constant.MqttLoginState;
import com.adupdate.sed_report_demo.util.DateUtil;
import com.adupdate.sed_report_demo.util.Trace;

import java.util.Map;

import javax.inject.Inject;

import static android.content.ContentValues.TAG;

public class SharedPreferencesHelper {
    private  SharedPreferences sharedPreferences;
    private  final String TOTAL_RUN_TIME = "totalRunTime";  //总运行时间
    private final String TOTAL_RUN_TIME_RECORD_TIME = "totalRunTimeRecordTime";  //记录的时刻
    private final String BOOT_TOTAL_RUN_TIME_RECORD_TIME = "bootTotalRunTimeRecordTime";//记录开机的时刻
    private final String MQTT_LOGIN_STATE = "mqttLoginState";
    private  final String FILE_NAME = "record";
    private final String VERIFICATION_TIME = "verification_time";
    /*
     * 保存手机里面的名字
     */
    private static SharedPreferences.Editor editor;

    @Inject
    Context mCx;

    public SharedPreferencesHelper() {
        sharedPreferences = mCx.getSharedPreferences(FILE_NAME,
                mCx.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * 设置校验时间
     * @param currentTime
     */
    public void putVerificationTime(long currentTime){
        put(VERIFICATION_TIME,currentTime);
    }

    /**
     * 获取校验时间
     * @return
     */
    public long getVerificationTime(){
         return (Long) getSharedPreference(VERIFICATION_TIME,0l);
    }

    /**
     * 设置登录状态
     * @param state
     */
    public void putMqttLoginState(int state){
         put(MQTT_LOGIN_STATE,state);
    }
    /**
     * 获取登录状态
     */
    public int getMqttLoginState(){
        return (int) getSharedPreference(MQTT_LOGIN_STATE, MqttLoginState.NOT_LOGIN);
    }
    /**
     * 开机时机记录总运行时间
     */
    public long putBootCompletedTotalRunTime(){
        long totalRunTime = getTotalRunTime();
        Trace.d(TAG,"bootTotalRunTime : " + totalRunTime);
        return putBootTotalRunTimeRecordTime(totalRunTime);
    }
    /**
     * 记录总运行时间
     */
    public long putTotalRunTime(){
        long bootTotalRunTime = getBootTotalRunTimeRecordTime();
        Trace.d(TAG,"bootTotalRunTime : " + bootTotalRunTime);
        long addTime = DateUtil.obtainElapsedRealtime();
        return addTotalRunTime(addTime,bootTotalRunTime);
    }

    /**
     * 增加总运行时间
     * @param addTime
     * @param bootTotalRunTime
     * @return
     */
    public long addTotalRunTime(long addTime,long bootTotalRunTime){
        if (addTime < 0){  //防止调整时间的时候出现错误
            return getTotalRunTime();
        }
        bootTotalRunTime += addTime;
        putTotalRunTime(bootTotalRunTime);
        return bootTotalRunTime;
    }

    /**
     * 设置系统总运行时间
     * @param totalRunTime 统总运行时间
     */
    public void putTotalRunTime(long totalRunTime){
        putLong(TOTAL_RUN_TIME,totalRunTime);
    }
    /**
     * 获取系统运行时间
     */
    public long getTotalRunTime(){
        return  getSharedPreferenceLong(TOTAL_RUN_TIME,0);
    }

    /**
     * 记录开机时刻的总运行时间
     * @param bootTotalRunTime
     */
    private long putBootTotalRunTimeRecordTime(long bootTotalRunTime){
        putLong(BOOT_TOTAL_RUN_TIME_RECORD_TIME,bootTotalRunTime);
        return bootTotalRunTime;
    }

    /**
     * 获取机子开机时刻的总运行时间
     * @return
     */
    private long getBootTotalRunTimeRecordTime(){
        return getSharedPreferenceLong(BOOT_TOTAL_RUN_TIME_RECORD_TIME,0l);
    }
//    /**
//     * * 设置记录总运行时间的时刻
//     * @param recordTime
//     */
//    private void putTotalRunTimeRecordTime(long recordTime){
//        putLong(TOTAL_RUN_TIME_RECORD_TIME,recordTime);
//    }
//    /**
//     * 获取总运行时间记录的时刻
//     */
//    private long getTotalRunTimeRecordTime(){
//        return getSharedPreferenceLong(TOTAL_RUN_TIME_RECORD_TIME,0);
//    }


    public void putLong(String key,long value){
        editor.putLong(key, (Long) value);
        editor.commit();
    }
    public long getSharedPreferenceLong(String key,long defValue){
        return sharedPreferences.getLong(key, 0);
    }
    /**
     * 存储
     */
    public void put(String key, Object object) {
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        editor.commit();
    }

    /**
     * 获取保存的数据
     */
    public Object getSharedPreference(String key, Object defaultObject) {
        if (defaultObject instanceof String) {
            return sharedPreferences.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sharedPreferences.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sharedPreferences.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sharedPreferences.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sharedPreferences.getLong(key, (Long) defaultObject);
        } else {
            return sharedPreferences.getString(key, null);
        }
    }

    /**
     * 移除某个key值已经对应的值
     */
    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        editor.clear();
        editor.commit();
    }

    /**
     * 查询某个key是否存在
     */
    public Boolean contain(String key) {
        return sharedPreferences.contains(key);
    }

    /**
     * 返回所有的键值对
     */
    public Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }
}
