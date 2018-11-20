package com.adupdate.sed_report_demo.ui.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.adupdate.sed_report_demo.R;

import com.adupdate.sed_report_demo.app.App;
import com.adupdate.sed_report_demo.aspect.annotation.MethodNameLog;

import com.adupdate.sed_report_demo.entity.CustomDeviceInfo;
import com.adupdate.sed_report_demo.helper.SharedPreferencesHelper;
import com.adupdate.sed_report_demo.presenter.MqttPresenter;
import com.adupdate.sed_report_demo.ui.activity.code.PermissionActivity;
import com.adupdate.sed_report_demo.ui.activity.code.anno.QueryPermission;
import com.adupdate.sed_report_demo.util.Trace;
import com.adupdate.sed_report_demo.util.Utils;

import javax.inject.Inject;

import butterknife.BindView;


@QueryPermission(getPermissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE})
public class MainActivity extends PermissionActivity{
    private static final String TAG = "MainActivity";

    @BindView(R.id.txt_logcat)
    public TextView logcatTxt;

    @BindView(R.id.edt_ota)
    public TextView otaEdt;

    @BindView(R.id.edt_curcpu)
    public EditText curcpuEdt;

    @BindView(R.id.edt_memory)
    public EditText memoryEdt;

    @BindView(R.id.edt_cpuTemp)
    public EditText cpuTempEdt;

    @BindView(R.id.edt_version)
    public EditText versionEdt;

    @BindView(R.id.edt_mid)
    public EditText midEdt;

    @BindView(R.id.edt_totalRunTime)
    public EditText totalRunTimeEdt;

    @BindView(R.id.edt_runTime)
    public EditText runTimeEdt;

    @BindView(R.id.edt_ip)
    public EditText ipEdt;

    @BindView(R.id.edt_bootTime)
    public EditText bootTimeEdt;

    @BindView(R.id.edt_offTime)
    public EditText offTimeEdt;

    @BindView(R.id.edt_activationTime)
    public EditText activationTimeEdt;

    @BindView(R.id.edt_ttySn)
    public EditText ttySnEdt;

    @BindView(R.id.edt_usblist)
    public EditText usblistEdt;

    @Inject
    CustomDeviceInfo mCustomDeviceInfo;

    @Inject
    SharedPreferencesHelper mSharedPreferencesHelper;

    @MethodNameLog
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @MethodNameLog
    public void test(){

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    private void initViewText() {
        otaEdt.setText("当前版本为最新版本");
        curcpuEdt.setText(Utils.getCurCPU() + "kHZ");
        memoryEdt.setText(Utils.getMemoryInfo(this).availMem + "(字节)");
        cpuTempEdt.setText(Utils.getCpuTemp() + "℃");
        versionEdt.setText("0.0.8");
        midEdt.setText(mCustomDeviceInfo.mid);
        totalRunTimeEdt.setText("运行总时间：" + mSharedPreferencesHelper.getTotalRunTime() + "ms");

//        runTimeEdt.setText("当日运行总时间: " + App.getmMqttPresenter().);
    }
}
