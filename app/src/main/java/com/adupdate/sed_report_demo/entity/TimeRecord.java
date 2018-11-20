package com.adupdate.sed_report_demo.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


import com.adupdate.sed_report_demo.util.DateUtil;
import com.google.gson.annotations.Expose;

@Entity(tableName = "tb_timetable")
public class TimeRecord{
    @PrimaryKey(autoGenerate = true)
    private int id;

    //开机时间
    @ColumnInfo(name = "boot_time")
    private long bootTime;

    //关机时间
    @ColumnInfo(name = "off_time")
    private long offTime;

    //本次开机设备运行总时间
    @ColumnInfo(name = "run_this_time")
    private long runThisTime;

    //ip地址
    @ColumnInfo(name = "ip_address")
    private String ipAddress;

    //是否上报了,0 未上报 1 上报了开机时间 2 上报了开关机时间
    @Expose(serialize = false)
    @ColumnInfo(name = "is_report")
    private int is_report = 0;

    @ColumnInfo(name = "mid")
    private String mid;
    //记录的时间
    @ColumnInfo(name = "record_time")
    private long recordTime;

    //上报了本时段的那个日期
    @ColumnInfo(name = "report_time")
    private long reportTime;

    private long runTime;

    public TimeRecord(){
        if (offTime == 0) {
            runThisTime = DateUtil.obtainElapsedRealtime();
        }else {
            runThisTime = offTime - bootTime;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getBootTime() {
        return bootTime;
    }

    public void setBootTime(long bootTime) {
        this.bootTime = bootTime;
    }

    public long getOffTime() {
        return offTime;
    }

    public void setOffTime(long offTime) {
        this.offTime = offTime;
    }

    public long getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(long recordTime) {
        //this.currentTime = currentTime;
        this.recordTime =  recordTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
//        if (SharedPreferencesHelper.getInstance().getReportIpAddress().equals(ipAddress)){
//            this.ipAddress = null;
//            return;
//        }
        this.ipAddress = ipAddress;
    }

    public long getRunThisTime() {
        return runThisTime;
    }

    public void setRunThisTime(long runThisTime) {
        this.runThisTime = runThisTime;
    }

    public int getIs_report() {
        return is_report;
    }

    public void setIs_report(int is_report) {
        this.is_report = is_report;
    }



    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public long getReportTime() {
        return reportTime;
    }

    public void setReportTime(long reportTime) {
        this.reportTime = reportTime;
    }

    public long getRunTime() {
        return runTime;
    }

    public void setRunTime(long runTime) {
        this.runTime = runTime;
    }

    @Override
    public String toString() {
        return "TimeRecord{" +
                "id=" + id +
                ", bootTime=" + DateUtil.simpleFormat(bootTime) +
                ", offTime=" + DateUtil.simpleFormat(offTime) +
                ", runThisTime=" + runThisTime +
                ", ipAddress='" + ipAddress + '\'' +
                ", is_report=" + is_report +
                ", mid='" + mid + '\'' +
                ", recordTime=" + DateUtil.simpleFormat(recordTime) +
                ", reportTime=" + DateUtil.simpleFormat(reportTime) +
                ", runTime=" + runTime /(1000 * 60) + "秒" +
                '}';
    }
}
