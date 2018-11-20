package com.adupdate.sed_report_demo.entity.reportinfo;

import com.adupdate.sed_report_demo.entity.reportinfo.base.RequiredParams;
import com.google.gson.annotations.SerializedName;

public class ReportDeviceInfo extends RequiredParams{
    @SerializedName("bootTime")
    private String bootTime;

    @SerializedName("offTime")
    private String offTime;

    @SerializedName("totalRunTime")
    private long totalRunTime;

    @SerializedName("ipAddress")
    private String ipAddress;

    @SerializedName("reportTime")
    private String reportTime;

    @SerializedName("runTime")
    private Long runTime;


    public ReportDeviceInfo(){
        super();
    }

    public long getRunTime() {
        return runTime;
    }

    public void setRunTime(Long runTime) {
        this.runTime = runTime;
    }

    public String getBootTime() {
        return bootTime;
    }

    public void setBootTime(String bootTime) {
        this.bootTime = bootTime;
    }

    public String getOffTime() {
        return offTime;
    }

    public void setOffTime(String offTime) {
        this.offTime = offTime;
    }

    public long getTotalRunTime() {
        return totalRunTime;
    }

    public void setTotalRunTime(long totalRunTime) {
        this.totalRunTime = totalRunTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    @Override
    public String toString() {
        return "ReportDeviceInfo{" +
                "bootTime='" + bootTime + '\'' +
                ", offTime='" + offTime + '\'' +
                ", totalRunTime=" + totalRunTime +
                ", ipAddress='" + ipAddress + '\'' +
                ", reportTime='" + reportTime + '\'' +
                ", runTime=" + runTime +
                '}';
    }
}
