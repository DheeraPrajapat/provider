package com.marius.valeyou_admin.di.module;

import androidx.lifecycle.ViewModelProvider;

import com.marius.valeyou_admin.di.ViewModelFactory;

import dagger.Binds;
import dagger.Module;

@Module(includes = {
        ActivityViewModelModule.class,
        FragmentViewModelModule.class
})
public abstract class ViewModelModule {
    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
