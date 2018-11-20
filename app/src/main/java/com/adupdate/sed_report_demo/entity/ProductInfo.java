package com.adupdate.sed_report_demo.entity;

import com.adupdate.sed_report_demo.BuildConfig;
import com.adupdate.sed_report_demo.app.App;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ProductInfo {
    private ProductInfo(){
        obtainProductInfo();
    }

    /**
     * 项目唯一标识码
     */
    public String productId = "1534472784";

    /**
     * 项目加密码
     */
    public String productSecret = "8142305b34fd49f0bb74144d7b8ce7c6";

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductSecret() {
        return productSecret;
    }

    public void setProductSecret(String productSecret) {
        this.productSecret = productSecret;
    }

    public void obtainProductInfo(){
        productId = BuildConfig.productId;
        productSecret = BuildConfig.productSecret;
    }
}
