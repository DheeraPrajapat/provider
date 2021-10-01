package com.marius.valeyou_admin.ui.activity.dashboard.profile.certificate;

import android.view.View;

import com.google.gson.JsonElement;
import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.profilebean.ProfileModel;
import com.marius.valeyou_admin.data.local.SharedPref;
import com.marius.valeyou_admin.data.remote.helper.NetworkErrorHandler;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.data.repo.WelcomeRepo;
import com.marius.valeyou_admin.di.base.viewmodel.BaseActivityViewModel;
import com.marius.valeyou_admin.util.event.SingleActionEvent;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;
import com.marius.valeyou_admin.util.location.LiveLocationDetecter;

import java.net.HttpURLConnection;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddCertificateActivityVM extends BaseActivityViewModel {

    public final SharedPref sharedPref;
    public final LiveLocationDetecter liveLocationDetecter;
    private final NetworkErrorHandler networkErrorHandler;
    private final WelcomeRepo welcomeRepo;
    public final SingleActionEvent<View> base_click = new SingleActionEvent<>();

    final SingleRequestEvent<SimpleApiResponse> addCertificateBean = new SingleRequestEvent<>();

    @Inject
    public AddCertificateActivityVM(SharedPref sharedPref,
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



    public void addCertificateApi(String auth_key, Map<String, RequestBody> map, MultipartBody.Part image) {
        welcomeRepo.addCertificateApi(auth_key,map,image).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<SimpleApiResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                addCertificateBean.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(SimpleApiResponse addCertificateApiResponse) {

                if (addCertificateApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    addCertificateBean.setValue(Resource.success(null, addCertificateApiResponse.getMsg()));

                } else {

                    addCertificateBean.setValue(Resource.error(null, addCertificateApiResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                addCertificateBean.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }



    public void editCertificate(String auth_key, Map<String, RequestBody> map, MultipartBody.Part image) {
        welcomeRepo.editCertificate(auth_key,map,image).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<SimpleApiResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                addCertificateBean.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(SimpleApiResponse addCertificateApiResponse) {

                if (addCertificateApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    addCertificateBean.setValue(Resource.success(null, addCertificateApiResponse.getMsg()));

                } else {

                    addCertificateBean.setValue(Resource.error(null, addCertificateApiResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                addCertificateBean.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }



}
