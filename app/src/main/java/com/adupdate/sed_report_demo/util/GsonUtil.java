package com.adupdate.sed_report_demo.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GsonUtil {
    private static Gson gson =  new Gson();

    /**
     * 对象转json字符串
     * @param obj
     * @return
     */
    public static String toJsonString(Object obj){
        return gson.toJson(obj);
    }

    /**
     * json字符串转对象
     * @param tClass
     * @param json
     * @param <T>
     * @return
     */
    public static  <T> T toObject(Class<T> tClass,String json){
        T t = gson.fromJson(json,tClass);
        return t;
    }
}
