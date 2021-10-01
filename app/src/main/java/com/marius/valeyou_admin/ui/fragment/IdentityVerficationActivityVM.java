package com.marius.valeyou_admin.ui.fragment;

import android.view.View;

import com.marius.valeyou_admin.data.beans.IdentityModel;
import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.profilebean.ProfileModel;
import com.marius.valeyou_admin.data.beans.reviews.MyReview;
import com.marius.valeyou_admin.data.local.SharedPref;
import com.marius.valeyou_admin.data.remote.helper.NetworkErrorHandler;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.data.repo.WelcomeRepo;
import com.marius.valeyou_admin.di.base.viewmodel.BaseActivityViewModel;
import com.marius.valeyou_admin.util.event.SingleActionEvent;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;
import com.marius.valeyou_admin.util.location.LiveLocationDetecter;

import java.net.HttpURLConnection;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class IdentityVerficationActivityVM extends BaseActivityViewModel {
    public final SharedPref sharedPref;
    public final LiveLocationDetecter liveLocationDetecter;
    private final NetworkErrorHandler networkErrorHandler;
    private final WelcomeRepo welcomeRepo;
   // public final SingleActionEvent<View> base_click = new SingleActionEvent<>();

    final SingleRequestEvent<List<IdentityModel>> identityBean = new SingleRequestEvent<>();
    final SingleRequestEvent<SimpleApiResponse> deleteApiBean = new SingleRequestEvent<>();


    @Inject
    public IdentityVerficationActivityVM(SharedPref sharedPref,LiveLocationDetecter liveLocationDetecter,NetworkErrorHandler networkErrorHandle,WelcomeRepo welcomeRepo) {
        this.sharedPref =sharedPref;
        this.liveLocationDetecter = liveLocationDetecter;
        this.welcomeRepo = welcomeRepo;
        this.networkErrorHandler = networkErrorHandle;

    }

    public void getIdentity(String authKey){
        welcomeRepo.getIdentity(authKey).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ApiResponse<List<IdentityModel>>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                identityBean.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(ApiResponse<List<IdentityModel>> apiResponse) {

                if (apiResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    identityBean.setValue(Resource.success(apiResponse.getData(), apiResponse.getMsg()));

                } else {

                    identityBean.setValue(Resource.error(null, apiResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                identityBean.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }

    public void deleteIdentity(String authKey,int identity_id){
        welcomeRepo.deleteIdentity(authKey,identity_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<SimpleApiResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                deleteApiBean.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(SimpleApiResponse apiResponse) {

                if (apiResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    deleteApiBean.setValue(Resource.success(null, apiResponse.getMsg()));

                } else {

                    deleteApiBean.setValue(Resource.error(null, apiResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                deleteApiBean.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }

}
