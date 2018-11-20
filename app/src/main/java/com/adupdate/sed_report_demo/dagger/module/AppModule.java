package com.adupdate.sed_report_demo.dagger.module;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.adupdate.sed_report_demo.app.App;
import com.adupdate.sed_report_demo.db.RoomDemoDatabase;
import com.adupdate.sed_report_demo.db.dao.TimetableDao;
import com.adupdate.sed_report_demo.entity.CustomDeviceInfo;
import com.adupdate.sed_report_demo.entity.DeviceInfo;
import com.adupdate.sed_report_demo.entity.reportinfo.MqttRequestEntityFactory;
import com.adupdate.sed_report_demo.entity.reportinfo.ReportInfoFactory;
import com.adupdate.sed_report_demo.factory.IntervalObserverFactory;
import com.adupdate.sed_report_demo.factory.TopicFactory;
import com.adupdate.sed_report_demo.helper.PropertiesHelper;
import com.adupdate.sed_report_demo.helper.SharedPreferencesHelper;
import com.adupdate.sed_report_demo.mqtt.MqttTool;
import com.adupdate.sed_report_demo.presenter.HttpPresenter;
import com.adupdate.sed_report_demo.presenter.MqttPresenter;
import com.adupdate.sed_report_demo.presenter.impl.HttpPresenterImpl;
import com.adupdate.sed_report_demo.presenter.impl.MqttPresenterImpl;
import com.adupdate.sed_report_demo.service.ReporterService;
import com.adupdate.sed_report_demo.ui.activity.MainActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public  class AppModule {
    private Context mCx;
    public AppModule(App app) {
        mCx = app;
    }
    @Provides
    public Context provideContext() {
        return mCx;
    }

    @Provides
    public MqttPresenter provideMqttPresenter() {
        return new MqttPresenterImpl();
    }

    @Provides
    public HttpPresenter provideHttpPresenter() {
        return new HttpPresenterImpl();
    }

    @Singleton
    @Provides
    public TimetableDao provideTimetableDao() {
        RoomDemoDatabase roomDemoDatabase = Room.databaseBuilder(mCx, RoomDemoDatabase.class, RoomDemoDatabase.DB_NAME).build();
        return roomDemoDatabase.timetableDao();
    }

    @Singleton
    @Provides
    public CustomDeviceInfo provideCustomDeviceInfo() {
        return new CustomDeviceInfo();
    }

    @Singleton
    @Provides
    public DeviceInfo provideDeviceInfo() {
        return new DeviceInfo();
    }

    @Singleton
    @Provides
    public ReportInfoFactory provideReportInfoFactory() {
        return new ReportInfoFactory();
    }

    @Singleton
    @Provides
    public PropertiesHelper providePropertiesHelper() {
        return new PropertiesHelper(mCx);
    }

    @Singleton
    @Provides
    public MqttRequestEntityFactory provideMqttRequestEntityFactory() {
        return new MqttRequestEntityFactory();
    }

    @Singleton
    @Provides
    public MqttTool provideMqttTool() {
        return new MqttTool();
    }

    @Singleton
    @Provides
    public TopicFactory provideTopicFactory() {
        return new TopicFactory();
    }

    @Singleton
    @Provides
    public SharedPreferencesHelper provideSharedPreferencesHelper() {
        return new SharedPreferencesHelper();
    }

    @Provides
    public IntervalObserverFactory provideIntervalObserverFactory() {
        return new IntervalObserverFactory();
    }
}
