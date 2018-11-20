package com.adupdate.sed_report_demo.helper;

import android.content.Context;

import com.adupdate.sed_report_demo.app.App;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesHelper {
    private Properties prop;
    private Context mCx;

    public PropertiesHelper(Context context){
        mCx = context;
        prop = new Properties();
        try {
            String config_file = "CustomConfig.properties";
            InputStream in = mCx.getAssets().open(config_file);  //打开assets目录下的config.properties文件
            prop.load( new InputStreamReader(in,"utf-8"));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public String getValue(String key){
        String value  = prop.getProperty(key);
        return value;
    }


}
