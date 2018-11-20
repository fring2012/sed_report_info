package com.adupdate.sed_report_demo.ui.activity.code;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.adupdate.sed_report_demo.ui.activity.code.anno.QueryPermission;

import java.util.ArrayList;
import java.util.List;

public abstract class PermissionActivity extends BaseActivity{
    private static final String TAG = "PermissionActivity";

    @Override
    protected void onResume() {
        super.onResume();
        requestPermission();
    }

    public void requestPermission(){
        Class aClass = this.getClass();
        QueryPermission annotation = (QueryPermission) aClass.getAnnotation(QueryPermission.class);
        if (annotation == null){
            return;
        }
        String[] permissions = annotation.getPermissions();
        List<String> requestList = new ArrayList<>();
        Log.d(TAG, "requestPermission: " + permissions[0]);
        for (String permission : permissions){
            int result = ContextCompat.checkSelfPermission(this,permission);
            if (result != PackageManager.PERMISSION_GRANTED){
                requestList.add(permission);
            }
        }
        Log.d(TAG, "requestPermission: " +requestList.size());
        if (requestList.size() <= 0){
            return;
        }

        permissions = new String[requestList.size()];
        requestList.toArray(permissions);
        ActivityCompat.requestPermissions(this,permissions,1);
    }
}
