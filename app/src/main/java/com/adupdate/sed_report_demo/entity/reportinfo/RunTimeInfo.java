package com.adupdate.sed_report_demo.entity.reportinfo;

import com.adupdate.sed_report_demo.entity.reportinfo.base.RequiredParams;
import com.adupdate.sed_report_demo.util.DateUtil;

public class RunTimeInfo extends RequiredParams{
    private long runTime;

    public RunTimeInfo(){
        runTime = DateUtil.obtainElapsedRealtime();
    }
    public long getRunTime() {
        return runTime;
    }

    public void setRunTime(long runTime) {
        this.runTime = runTime;
    }
}
