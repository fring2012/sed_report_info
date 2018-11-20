package com.adupdate.sed_report_demo.app.constant;

public class Error {
    //成功
    public static final int SUCCESS = 1000;


    //项目id不合法
    public static final int PRODUCTID_IS_INVALID = 1001;
    //项目不存在
    public static final int PROJECT_NOT_EXIST = 1002;
    //参数不合法
    public static final int PARAM_INVALID = 1003;
    //参数缺失
    public static final int PARAM_IS_LACK = 1004;
    //系统异常
    public static final int SYSTEM_ERROR = 1005;
    //json解析异常
    public static final int JSON_PARSING_EXCEPTION = 1006;
    //参数不符合规则
    public static final int PARAM_IS_NOT_CONFORM_TO_RULE = 1007;
    //MID长度错误
    public static final int MID_LENGTH_ERROR = 1008;
    //AES加密错误
    public static final int AES_ENCRYPTION_ERROR = 1009;


    //==================================================================================
    //sign错误
    public static final int SIGN_ERROR = 2001;


    //当前为最新版本
    public static final int CHECK_CURRENT_IS_LAST_VERSION = 2101;
    //设备未注册
    public static final int CHECK_DEVICE_IS_NOT_REGISTER = 2103;
    //该项目没有项目组
    public static final int PROJECT_HAS_NO_PROJECT_TEAM = 2104;


    //下载状态非法
    public static final int DOWNLOAD_STATIS_IS_NO_LEGAL = 2201;
    //deltalD不存在
    public static final int DELTALID_IS_NOT_EXIST = 2202;


    //deltalD不存在
    public static final int DELTALID_NOT_EXIST = 2301;
    //升级状态值非法
    public static final int UPGTADE_STATUS_IS_NOT_LEGAL = 2302;


    //文件类型错误
    public static final int FILE_TYPE_WRONG = 2401;
    //文件上传失败
    public static final int FILE_UPLOAD_FAILED = 2402;
    //文件超过限定大小
    public static final int FILE_EXCEEDS_LIMIT_SIZE = 2403;
    //文件不存在
    public static final int FILE_DOES_NOT_EXIST = 2404;


    //设备超出电子围栏范围
    public static final int DEVICE_OUT_OF_RANGE = 3001;


    //设备被禁用
    public static final int DEVICE_IS_DISABLED = 4001;


    //========================================Mqtt error =========================================
    public static final short REASON_CODE_FAILED_AUTHENTICATION = 4;
    public static final short REASON_CODE_NOT_AUTHORIZED = 5;


    public static String errorInfo(int error){
        String errorInfo = "";
        switch (error){
            case SUCCESS: errorInfo = "success";
                return errorInfo;
            case PRODUCTID_IS_INVALID: errorInfo = "The productId is invalid!";
                return errorInfo;
            case PROJECT_NOT_EXIST: errorInfo = "The project does not exist!";
                return errorInfo;
            case PARAM_INVALID: errorInfo = "The param is invalid!";
                return errorInfo;
            case PARAM_IS_LACK: errorInfo = "params is lack!";
                return errorInfo;
            case SYSTEM_ERROR: errorInfo = "The system is error!";
                return errorInfo;
            case JSON_PARSING_EXCEPTION: errorInfo = "JSON parsing exception!";
                return errorInfo;
            case PARAM_IS_NOT_CONFORM_TO_RULE: errorInfo = "param is not conform to rule!";
                return errorInfo;
            case MID_LENGTH_ERROR: errorInfo = "length error of mid!";
                return errorInfo;
            case AES_ENCRYPTION_ERROR: errorInfo = "ARS加密错误！";
                return errorInfo;
            case SIGN_ERROR: errorInfo = "The sign is error!";
                return errorInfo;
            case CHECK_CURRENT_IS_LAST_VERSION: errorInfo = "No new updates were found!";
                return errorInfo;
            case CHECK_DEVICE_IS_NOT_REGISTER: errorInfo = "device is not register!";
                return errorInfo;
            case PROJECT_HAS_NO_PROJECT_TEAM: errorInfo = "The project has no project team!";
                return errorInfo;
            case DOWNLOAD_STATIS_IS_NO_LEGAL: errorInfo = "The download status is not legal!";
                return errorInfo;
            case DELTALID_IS_NOT_EXIST: errorInfo = "The deltaID is not exist!";
                return errorInfo;
            case DELTALID_NOT_EXIST: errorInfo = "The deltaID is not exist!";
                return errorInfo;
            case UPGTADE_STATUS_IS_NOT_LEGAL: errorInfo = "The upgrade status is not legal!";
                return errorInfo;
            case FILE_TYPE_WRONG: errorInfo = "The file type is wrong!";
                return errorInfo;
            case FILE_UPLOAD_FAILED: errorInfo = "File upload failed!";
                return errorInfo;
            case FILE_EXCEEDS_LIMIT_SIZE: errorInfo = "File exceeds the limit size!";
                return errorInfo;
            case FILE_DOES_NOT_EXIST: errorInfo = "File does not exist!";
                return errorInfo;
            case DEVICE_OUT_OF_RANGE: errorInfo = "The device is out of range!";
                return errorInfo;
            case DEVICE_IS_DISABLED: errorInfo = "The device is disabled!";
                return errorInfo;
            default:errorInfo = "UNKNOWN ERROR!";
                return errorInfo;
        }
    }


}
