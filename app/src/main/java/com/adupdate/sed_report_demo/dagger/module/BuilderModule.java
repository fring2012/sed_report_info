package com.adupdate.sed_report_demo.dagger.module;

import com.adupdate.sed_report_demo.receiver.CollectionInfoReceiver;
import com.adupdate.sed_report_demo.service.ReporterService;
import com.adupdate.sed_report_demo.ui.activity.MainActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BuilderModule {
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity mainActivityInjector();

    @ContributesAndroidInjector(modules = ReporterServiceModule.class)
    abstract ReporterService reporterServiceInjector();

    @ContributesAndroidInjector(modules = CollectionInfoReceiverModule.class)
    abstract CollectionInfoReceiver collectionInfoReceiverInjector();
}
