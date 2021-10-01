package com.marius.valeyou_admin.ui.activity.forgot.recoverpass;

import android.view.View;

import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.change.ChangePasswordModel;
import com.marius.valeyou_admin.data.beans.singninbean.SignInModel;
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

public class RecoverPasswordActivityVM extends BaseActivityViewModel {

    private final SharedPref sharedPref;
    private final NetworkErrorHandler networkErrorHandler;
    private final WelcomeRepo welcomeRepo;
    public final SingleActionEvent<View> base_click = new SingleActionEvent<>();

    final SingleRequestEvent<ChangePasswordModel> changePasswordEvent = new SingleRequestEvent<>();

    @Inject
    public RecoverPasswordActivityVM(SharedPref sharedPref, NetworkErrorHandler networkErrorHandler, WelcomeRepo welcomeRepo) {

        this.sharedPref = sharedPref;
        this.networkErrorHandler = networkErrorHandler;
        this.welcomeRepo = welcomeRepo;
    }


    public void changePasswordApi(String authKey,String oldPassword,String newPassword){

        welcomeRepo.changePassword(authKey,oldPassword,newPassword).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ApiResponse<ChangePasswordModel>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                changePasswordEvent.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(ApiResponse<ChangePasswordModel> changePasswordApiResponse) {

                if (changePasswordApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    changePasswordEvent.setValue(Resource.success(changePasswordApiResponse.getData(), changePasswordApiResponse.getMsg()));

                } else {

                    changePasswordEvent.setValue(Resource.error(null, changePasswordApiResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                changePasswordEvent.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });

    }
}
