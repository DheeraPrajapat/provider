package com.marius.valeyou_admin.di.module;

import android.content.Context;

import com.marius.valeyou_admin.BuildConfig;
import com.marius.valeyou_admin.di.qualifier.ApiDateFormat;
import com.marius.valeyou_admin.di.qualifier.ApiEndpoint;
import com.marius.valeyou_admin.di.qualifier.AppContext;
import com.marius.valeyou_admin.util.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.android.DaggerApplication;
import okhttp3.HttpUrl;

@Module
public class AppModule {
    @AppContext
    @Provides
    static Context context(DaggerApplication daggerApplication) {
        return daggerApplication;
    }

    @ApiEndpoint
    @Singleton
    @Provides
    static HttpUrl apiEndpoint() {
        return HttpUrl.parse(BuildConfig.BASE_URL);
    }


    @Singleton
    @Provides
    static HttpUrl placeEndpoint() {
        return HttpUrl.parse(Constants.GOOGLE_URL);
    }

    @ApiDateFormat
    @Singleton
    @Provides
    static String apiDateFormat() {
        return "yyyy-MM-dd HH:mm:ss";
    }


}
