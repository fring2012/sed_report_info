package com.adupdate.sed_report_demo.util;

public class ParamUtil {



    /**
     *产生请求的序列号
     * @param clientid
     * @param timestamp unix时间戳
     * @return
     */
    public static String createSeqNo(String clientid,long timestamp){
        return "D_" + clientid + "_" +
                timestamp +
                random3Digit();
    }



    /**
     * 产生3位随机数
     * @return
     */
    public static int random3Digit(){
        return (int)(Math.random()*9 + 1)*100;
    }
}
