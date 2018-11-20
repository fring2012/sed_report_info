package com.adupdate.sed_report_demo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.adupdate.sed_report_demo.util.Trace;

public class UsbManagerReceiver extends BroadcastReceiver {
    private static final String TAG = "UsbManagerReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Trace.d(TAG,intent.getAction());
    }
}
