package com.marius.valeyou_admin.ui.activity.login;

import android.view.View;

import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.singninbean.SignInModel;
import com.marius.valeyou_admin.data.beans.singninbean.SocialSignIn;
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

public class LoginActivityVM extends BaseActivityViewModel {

    public final SharedPref sharedPref;
    public final LiveLocationDetecter liveLocationDetecter;
    private final NetworkErrorHandler networkErrorHandler;
    private final WelcomeRepo welcomeRepo;
    public final SingleActionEvent<View> base_click = new SingleActionEvent<>();

    final SingleRequestEvent<SignInModel> userBean = new SingleRequestEvent<>();
    final SingleRequestEvent<SignInModel> socialBean = new SingleRequestEvent<>();
    final SingleRequestEvent<SimpleApiResponse> sendOTPEvent = new SingleRequestEvent<>();
    final SingleRequestEvent<SimpleApiResponse> verifyEmailEvent = new SingleRequestEvent<>();


    @Inject
    public LoginActivityVM(SharedPref sharedPref, LiveLocationDetecter liveLocationDetecter, NetworkErrorHandler networkErrorHandler, WelcomeRepo welcomeRepo) {

        this.sharedPref = sharedPref;
        this.liveLocationDetecter = liveLocationDetecter;
        this.networkErrorHandler = networkErrorHandler;
        this.welcomeRepo = welcomeRepo;
    }

    public void onClick(View view) {
        base_click.call(view);
    }


    public void signIn(Map<String, String> map) {
        welcomeRepo.signin(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ApiResponse<SignInModel>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                userBean.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(ApiResponse<SignInModel> signinDataApiResponse) {

                if (signinDataApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    userBean.setValue(Resource.success(signinDataApiResponse.getData(), signinDataApiResponse.getMsg()));

                } else {

                    userBean.setValue(Resource.error(null, signinDataApiResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                userBean.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }

    public void socialSignIn(Map<String, String> map) {
        welcomeRepo.socialSignIn(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ApiResponse<SignInModel>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                socialBean.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(ApiResponse<SignInModel> signinDataApiResponse) {

                if (signinDataApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    socialBean.setValue(Resource.success(signinDataApiResponse.getData(), signinDataApiResponse.getMsg()));

                } else {

                    socialBean.setValue(Resource.error(null, signinDataApiResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                socialBean.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }


    public void sendOTP(String email){
        welcomeRepo.sendOTP(email).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<SimpleApiResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                sendOTPEvent.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(SimpleApiResponse response) {

                if (response.getStatus() == HttpURLConnection.HTTP_OK) {

                    sendOTPEvent.setValue(Resource.success(null, response.getMsg()));

                } else {

                    sendOTPEvent.setValue(Resource.error(null, response.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                sendOTPEvent.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }

    public void verifyEmail(String email,String otp){
        welcomeRepo.verifyEmail(email,otp).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<SimpleApiResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                verifyEmailEvent.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(SimpleApiResponse response) {

                if (response.getStatus() == HttpURLConnection.HTTP_OK) {

                    verifyEmailEvent.setValue(Resource.success(null, response.getMsg()));

                } else {

                    verifyEmailEvent.setValue(Resource.error(null, response.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                verifyEmailEvent.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }
}
