package com.adupdate.sed_report_demo.dagger.component;

import android.content.Context;

import com.adupdate.sed_report_demo.app.App;
import com.adupdate.sed_report_demo.dagger.module.AppModule;
import com.adupdate.sed_report_demo.dagger.module.BuilderModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules =
        {BuilderModule.class,
         AppModule.class,
         AndroidSupportInjectionModule.class})
public interface AppComponent extends  AndroidInjector<App>{
    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<App>{
        public abstract Builder setModule(AppModule appModule);
    }
}
