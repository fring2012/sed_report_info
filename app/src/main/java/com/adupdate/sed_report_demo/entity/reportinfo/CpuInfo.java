package com.adupdate.sed_report_demo.entity.reportinfo;

import android.app.ActivityManager;

import com.adupdate.sed_report_demo.app.App;
import com.adupdate.sed_report_demo.entity.CustomDeviceInfo;
import com.adupdate.sed_report_demo.entity.DeviceInfo;
import com.adupdate.sed_report_demo.entity.reportinfo.base.RequiredParams;
import com.adupdate.sed_report_demo.util.Utils;

public class CpuInfo extends RequiredParams{
    //private int messageId;
    private long freeMem;
    private long totalMem;
    private long curFreq;
    private int numCores;
    private int cpuUsage;




    public CpuInfo(){

    }

//    public int getMessageId() {
//        return messageId;
//    }
//
//    public void setMessageId(int messageId) {
//        this.messageId = messageId;
//    }

    public long getFreeMem() {
        return freeMem;
    }

    public void setFreeMem(long freeMem) {
        this.freeMem = freeMem;
    }

    public long getTotalMem() {
        return totalMem;
    }

    public void setTotalMem(long totalMem) {
        this.totalMem = totalMem;
    }

    public long getCurFreq() {
        return curFreq;
    }

    public void setCurFreq(long curFreq) {
        this.curFreq = curFreq;
    }

    public int getNumCores() {
        return numCores;
    }

    public void setNumCores(int numCores) {
        this.numCores = numCores;
    }

    public int getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(int cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

}
