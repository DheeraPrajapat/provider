package com.marius.valeyou_admin.di.component;


import com.marius.valeyou_admin.data.remote.api.WelcomeApi;
import com.marius.valeyou_admin.data.repo.WelcomeRepo;
import com.marius.valeyou_admin.di.module.ApiModule;
import com.marius.valeyou_admin.di.module.AppModule;
import com.marius.valeyou_admin.di.module.DataModule;
import com.marius.valeyou_admin.di.module.LocalModule;
import com.marius.valeyou_admin.di.module.ManagerModule;
import com.marius.valeyou_admin.di.module.NetworkModule;
import com.marius.valeyou_admin.di.module.RepositoryModule;
import com.marius.valeyou_admin.di.module.SystemModule;
import com.marius.valeyou_admin.di.module.ViewModelModule;
import com.marius.valeyou_admin.di.module.androidcomponent.AndroidComponentsModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.android.support.DaggerApplication;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        LocalModule.class,
        ManagerModule.class,
        ApiModule.class,
        DataModule.class,
        RepositoryModule.class,
        NetworkModule.class,
        SystemModule.class,
        AndroidComponentsModule.class,
        ViewModelModule.class,


})
interface AppComponent extends AndroidInjector<DaggerApplication> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<DaggerApplication> {

    }
}
