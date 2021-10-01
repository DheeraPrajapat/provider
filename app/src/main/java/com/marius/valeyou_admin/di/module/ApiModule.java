package com.marius.valeyou_admin.di.module;

import com.marius.valeyou_admin.data.remote.api.WelcomeApi;
import com.marius.valeyou_admin.di.qualifier.ApiEndpoint;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ApiModule {

    @Singleton
    @Provides
    static WelcomeApi welcomeApi(@ApiEndpoint Retrofit retrofit) {
        return retrofit.create(WelcomeApi.class);
    }


}
