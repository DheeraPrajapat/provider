package com.marius.valeyou_admin.di.module;


import com.marius.valeyou_admin.data.local.SharedPref;
import com.marius.valeyou_admin.data.remote.api.WelcomeApi;
import com.marius.valeyou_admin.data.remote.helper.NetworkErrorHandler;
import com.marius.valeyou_admin.data.repo.WelcomeRepo;
import com.marius.valeyou_admin.data.repo.WelcomeRepoImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {
    @Singleton
    @Provides
    static WelcomeRepo welcomeRepo(SharedPref sharedPref, WelcomeApi welcomeApi, NetworkErrorHandler networkErrorHandler) {

        WelcomeRepo repo = new WelcomeRepoImpl(sharedPref, welcomeApi, networkErrorHandler);

        return repo;
    }
}
