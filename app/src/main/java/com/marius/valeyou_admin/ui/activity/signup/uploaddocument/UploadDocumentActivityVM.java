package com.marius.valeyou_admin.ui.activity.signup.uploaddocument;

import android.view.View;

import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.singninbean.SignInModel;
import com.marius.valeyou_admin.data.local.SharedPref;
import com.marius.valeyou_admin.data.remote.helper.NetworkErrorHandler;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.data.repo.WelcomeRepo;
import com.marius.valeyou_admin.di.base.viewmodel.BaseActivityViewModel;
import com.marius.valeyou_admin.util.event.SingleActionEvent;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;
import com.marius.valeyou_admin.util.location.LiveLocationDetecter;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UploadDocumentActivityVM extends BaseActivityViewModel{

    public final SharedPref sharedPref;
    public final LiveLocationDetecter liveLocationDetecter;
    private final NetworkErrorHandler networkErrorHandler;
    private final WelcomeRepo welcomeRepo;
    public final SingleActionEvent<View> base_click = new SingleActionEvent<>();

    final SingleRequestEvent<SignInModel> userBean = new SingleRequestEvent<>();
    final SingleRequestEvent<ApiResponse<SignInModel>> profilebeandata = new SingleRequestEvent<>();
    @Inject
    public UploadDocumentActivityVM(SharedPref sharedPref,LiveLocationDetecter liveLocationDetecter,NetworkErrorHandler networkErrorHandler,WelcomeRepo welcomeRepo) {

        this.sharedPref = sharedPref;
        this.networkErrorHandler = networkErrorHandler;
        this.liveLocationDetecter = liveLocationDetecter;
        this.welcomeRepo = welcomeRepo;
    }



    public void editProfile(String auth_key, Map<String, RequestBody> map, MultipartBody.Part image){
        welcomeRepo.editProfile(auth_key, map,image).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ApiResponse<SignInModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                        compositeDisposable.add(d);
                        profilebeandata.setValue(Resource.loading(null));

                    }

                    @Override
                    public void onSuccess(ApiResponse<SignInModel> profileApiResponse) {

                        if (profileApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                            profilebeandata.setValue(Resource.success(null, profileApiResponse.getMsg()));

                        } else {

                            profilebeandata.setValue(Resource.warn(null, profileApiResponse.getMsg()));

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        profilebeandata.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));
                    }
                });
    }

}
