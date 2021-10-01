package com.marius.valeyou_admin.ui.activity.dashboard.profile.businesshours;

import android.view.View;

import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.local.SharedPref;
import com.marius.valeyou_admin.data.remote.helper.NetworkErrorHandler;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.data.repo.WelcomeRepo;
import com.marius.valeyou_admin.di.base.viewmodel.BaseActivityViewModel;
import com.marius.valeyou_admin.util.event.SingleActionEvent;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;
import com.marius.valeyou_admin.util.location.LiveLocationDetecter;

import java.net.HttpURLConnection;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BusinessHoursActivityVM extends BaseActivityViewModel {
    public final SharedPref sharedPref;
    public final LiveLocationDetecter liveLocationDetecter;
    private final NetworkErrorHandler networkErrorHandler;
    private final WelcomeRepo welcomeRepo;
    public final SingleActionEvent<View> base_click = new SingleActionEvent<>();

    final SingleRequestEvent<SimpleApiResponse> addBussinessHours = new SingleRequestEvent<>();

    @Inject
    public BusinessHoursActivityVM(SharedPref sharedPref, LiveLocationDetecter liveLocationDetecter, NetworkErrorHandler networkErrorHandler, WelcomeRepo welcomeRepo) {

        this.sharedPref = sharedPref;
        this.networkErrorHandler = networkErrorHandler;
        this.liveLocationDetecter = liveLocationDetecter;
        this.welcomeRepo = welcomeRepo;

    }

    public void onClick(View view) {
        base_click.call(view);
    }



    public void addBusienssHoursApi(String auth_key,String data,String type){
        welcomeRepo.addBussinessHoursApi(auth_key,data,type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<SimpleApiResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                addBussinessHours.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(SimpleApiResponse hoursApiResponse) {

                if (hoursApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    addBussinessHours.setValue(Resource.success(null, hoursApiResponse.getMsg()));

                } else {

                    addBussinessHours.setValue(Resource.error(null, hoursApiResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                addBussinessHours.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });

    }

}
