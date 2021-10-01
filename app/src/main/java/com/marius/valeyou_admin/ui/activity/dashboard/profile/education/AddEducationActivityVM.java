package com.marius.valeyou_admin.ui.activity.dashboard.profile.education;

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
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddEducationActivityVM extends BaseActivityViewModel {
    public final SharedPref sharedPref;
    public final LiveLocationDetecter liveLocationDetecter;
    private final NetworkErrorHandler networkErrorHandler;
    private final WelcomeRepo welcomeRepo;
    public final SingleActionEvent<View> base_click = new SingleActionEvent<>();

    final SingleRequestEvent<SimpleApiResponse> addEducationEvent = new SingleRequestEvent<>();

    @Inject
    public AddEducationActivityVM(SharedPref sharedPref,
                                    LiveLocationDetecter liveLocationDetecter,
                                    NetworkErrorHandler networkErrorHandler,
                                    WelcomeRepo welcomeRepo) {

        this.sharedPref = sharedPref;
        this.networkErrorHandler = networkErrorHandler;
        this.liveLocationDetecter = liveLocationDetecter;
        this.welcomeRepo = welcomeRepo;

    }

    public void onClick(View view) {
        base_click.call(view);
    }



    public void addEducationApi(String auth_key, HashMap<String, String> map) {
        welcomeRepo.addEditEducationApi(auth_key,map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<SimpleApiResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                addEducationEvent.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(SimpleApiResponse addCertificateApiResponse) {

                if (addCertificateApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    addEducationEvent.setValue(Resource.success(null, addCertificateApiResponse.getMsg()));

                } else {

                    addEducationEvent.setValue(Resource.error(null, addCertificateApiResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                addEducationEvent.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }

    public void EducationApi(String auth_key, HashMap<String, String> map,int id) {
        welcomeRepo.EditEducationApi(auth_key,map,id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<SimpleApiResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                addEducationEvent.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(SimpleApiResponse addCertificateApiResponse) {

                if (addCertificateApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    addEducationEvent.setValue(Resource.success(null, addCertificateApiResponse.getMsg()));

                } else {

                    addEducationEvent.setValue(Resource.error(null, addCertificateApiResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                addEducationEvent.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }



}
