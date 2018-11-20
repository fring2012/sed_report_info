package com.adupdate.sed_report_demo.entity.reportinfo;

import com.adupdate.sed_report_demo.entity.CustomDeviceInfo;
import com.adupdate.sed_report_demo.entity.DeviceInfo;
import com.adupdate.sed_report_demo.entity.reportinfo.base.RequiredParams;

public class TempInfo extends RequiredParams{

    //private int messageId;
    private String temp = "29";



//    public int getMessageId() {
//        return messageId;
//    }
//
//    public void setMessageId(int messageId) {
//        this.messageId = messageId;
//    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

}
