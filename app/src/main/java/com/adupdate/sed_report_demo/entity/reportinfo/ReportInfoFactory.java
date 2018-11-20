package com.adupdate.sed_report_demo.entity.reportinfo;

import android.app.ActivityManager;
import android.content.Context;

import com.adupdate.sed_report_demo.app.App;
import com.adupdate.sed_report_demo.entity.CustomDeviceInfo;
import com.adupdate.sed_report_demo.entity.DeviceInfo;
import com.adupdate.sed_report_demo.entity.TimeRecord;
import com.adupdate.sed_report_demo.entity.reportinfo.base.RequiredParams;
import com.adupdate.sed_report_demo.util.DateUtil;
import com.adupdate.sed_report_demo.helper.SharedPreferencesHelper;
import com.adupdate.sed_report_demo.util.Utils;

import java.text.DecimalFormat;

import javax.inject.Inject;

public class ReportInfoFactory {
    @Inject
    Context mCx;

    @Inject
    CustomDeviceInfo mCustomDeviceInfo;

    @Inject
    DeviceInfo mDeviceInfo;

    @Inject
    SharedPreferencesHelper mSharedPreferencesHelper;

    public ReportDeviceInfo createDeviceInfo(TimeRecord timeRecord){
        timeRecord.setMid(mCustomDeviceInfo.mid);
        ReportDeviceInfo rdi = new ReportDeviceInfo();
        initRequireParams(rdi);
        if (timeRecord != null) {
            if (timeRecord.getBootTime() != 0) {
                rdi.setBootTime(DateUtil.simpleFormat(timeRecord.getBootTime()));
            }
            if (timeRecord.getOffTime() != 0) {
                rdi.setOffTime(DateUtil.simpleFormat(timeRecord.getOffTime()));
            }
            rdi.setTotalRunTime(mSharedPreferencesHelper.putTotalRunTime());
            rdi.setIpAddress(timeRecord.getIpAddress());
            if (timeRecord.getReportTime() !=0 ) {
                rdi.setReportTime(DateUtil.simpleFormat(timeRecord.getReportTime()));
            }
            if(timeRecord.getRunTime()!=0){
                rdi.setRunTime(timeRecord.getRunTime());
            }else {
                rdi.setRunTime(null);
            }

        }

        return rdi;
    }

    public  CpuInfo createCpuInfo(){
        CpuInfo cpuInfo = new CpuInfo();
        ActivityManager.MemoryInfo info = Utils.getMemoryInfo(mCx);
        cpuInfo.setFreeMem(info.availMem);
        cpuInfo.setTotalMem(info.totalMem);
        cpuInfo.setCurFreq(Utils.getCurCPU());
        cpuInfo.setNumCores(Utils.getCpuPresent());
        cpuInfo.setCpuUsage(Utils.getCpuUsage());
        return cpuInfo;
    }

    public  TempInfo createTempInfo(){
        TempInfo tempInfo = new TempInfo();
        long currentBeforeDawn = DateUtil.obtainBeforeDawnTime();
        String timeString = DateUtil.simpleFormat(currentBeforeDawn,DateUtil.DATE_PATTERN);
        String[] timeArray = timeString.split("-");
        int month = Integer.valueOf(timeArray[1]);
        int random = Integer.valueOf(timeArray[2]) / 3;
        Double temp = 0d;
        if (month <= 2){
            temp = 5d;
        }else if (month <= 5){
            temp = 25d;
        }else if (month <= 9){
            temp = 30d;
        }else if(month <= 11){
            temp  = 25d;
        }else {
            temp = 15d;
        }
        temp += Math.sin(3.1415926 * random/2) * random;
        DecimalFormat dFormat=new DecimalFormat("#.0");
        String tempString= dFormat.format(temp);
        temp = Double.valueOf(tempString);
        tempInfo.setTemp(temp.toString());
        return tempInfo;
    }

    /**
     * 初始化必要参数
     * @param requiredParams
     */
    public void initRequireParams(RequiredParams requiredParams) {
        requiredParams.setAndroidVersion(mDeviceInfo.getAndroidVersion());
        requiredParams.setMid(mCustomDeviceInfo.mid);
        requiredParams.setVersion(mDeviceInfo.getFirmwareVersion());
    }
}
