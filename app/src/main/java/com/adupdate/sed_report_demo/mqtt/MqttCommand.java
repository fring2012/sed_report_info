package com.adupdate.sed_report_demo.mqtt;

import com.adupdate.sed_report_demo.util.GsonUtil;

public class MqttCommand {
    //命令类型：1：上报温度 2：上报cpu信息 3：上报日志 4.上报所有信息
    private int commandType;
    //上报间隔（单位分钟）
    private int pubInterval;
    //上报开关（0：上报 1：不上报）
    private int pubStatus;

    public int getCommandType() {
        return commandType;
    }

    public void setCommandType(int commandType) {
        this.commandType = commandType;
    }

    public int getPubInterval() {
        return pubInterval;
    }

    public void setPubInterval(int pubInterval) {
        this.pubInterval = pubInterval;
    }

    public int getPubStatus() {
        return pubStatus;
    }

    public void setPubStatus(int pubStatus) {
        this.pubStatus = pubStatus;
    }

    @Override
    public String toString() {
        return "MqttCommand{" +
                "commandType=" + commandType +
                ", pubInterval=" + pubInterval +
                ", pubStatus=" + pubStatus +
                '}';
    }

    public static MqttCommand obtainMqttCommand(String json){
        return GsonUtil.toObject(MqttCommand.class,json);
    }
}
