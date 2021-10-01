package com.marius.valeyou_admin.ui.activity.forgot;

import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.forgot.ForgotPasswordModel;
import com.marius.valeyou_admin.data.beans.singninbean.SignInModel;
import com.marius.valeyou_admin.data.local.SharedPref;
import com.marius.valeyou_admin.data.remote.helper.NetworkErrorHandler;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.data.repo.WelcomeRepo;
import com.marius.valeyou_admin.di.base.viewmodel.BaseActivityViewModel;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;
import com.marius.valeyou_admin.util.location.LiveLocationDetecter;

import java.net.HttpURLConnection;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ForgotPasswordActivityVM extends BaseActivityViewModel {

    private final SharedPref sharedPref;
    public final LiveLocationDetecter liveLocationDetecter;
    private final NetworkErrorHandler networkErrorHandler;
    private WelcomeRepo welcomeRepo;
    final SingleRequestEvent<ForgotPasswordModel> forgotPasswordEvent = new SingleRequestEvent<>();

    @Inject
    public ForgotPasswordActivityVM(SharedPref sharedPref, LiveLocationDetecter liveLocationDetecter, NetworkErrorHandler networkErrorHandler,WelcomeRepo welcomeRepo) {

        this.sharedPref = sharedPref;
        this.liveLocationDetecter = liveLocationDetecter;
        this.networkErrorHandler = networkErrorHandler;
        this.welcomeRepo = welcomeRepo;
    }

    public void forgot(String email){
        welcomeRepo.forgotPassword(email).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ApiResponse<ForgotPasswordModel>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                forgotPasswordEvent.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(ApiResponse<ForgotPasswordModel> forgotPasswrdDataApiResponse) {

                if (forgotPasswrdDataApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    forgotPasswordEvent.setValue(Resource.success(forgotPasswrdDataApiResponse.getData(), forgotPasswrdDataApiResponse.getMsg()));

                } else {

                    forgotPasswordEvent.setValue(Resource.error(null, forgotPasswrdDataApiResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                forgotPasswordEvent.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }
}
