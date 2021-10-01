package com.marius.valeyou_admin.ui.activity.main;

import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.singninbean.SignInModel;
import com.marius.valeyou_admin.data.local.SharedPref;
import com.marius.valeyou_admin.data.remote.helper.NetworkErrorHandler;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.data.repo.WelcomeRepo;
import com.marius.valeyou_admin.di.base.viewmodel.BaseActivityViewModel;
import com.marius.valeyou_admin.util.event.SingleActionEvent;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;

import java.net.HttpURLConnection;

import javax.inject.Inject;

import dagger.android.support.DaggerApplication;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivityVM extends BaseActivityViewModel {

    public final SharedPref sharedPref;
    private final NetworkErrorHandler networkErrorHandler;
    final SingleActionEvent<MenuItem> obrNavClick = new SingleActionEvent<>();
    final SingleRequestEvent<Integer> score = new SingleRequestEvent<>();
    final SingleRequestEvent<Void> playerResponse = new SingleRequestEvent<>();
    public final WelcomeRepo welcomeRepo;



    private final DaggerApplication context;
    final SingleActionEvent<long[]> saveData = new SingleActionEvent<>();

    @Inject
    public MainActivityVM(DaggerApplication context, SharedPref sharedPref, NetworkErrorHandler networkErrorHandler,WelcomeRepo welcomeRepo) {
        this.context = context;
        this.sharedPref = sharedPref;
        this.networkErrorHandler = networkErrorHandler;
        this.welcomeRepo = welcomeRepo;
    }


    public BottomNavigationView.OnNavigationItemSelectedListener getNavListioner() {
        return item -> {
            obrNavClick.setValue(item);
            return true;
        };
    }


}
