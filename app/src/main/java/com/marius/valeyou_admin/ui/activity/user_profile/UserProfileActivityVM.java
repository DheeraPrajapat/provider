package com.marius.valeyou_admin.ui.activity.user_profile;

import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.jobs.JobDetailModel;
import com.marius.valeyou_admin.data.beans.profilebean.ProfileModel;
import com.marius.valeyou_admin.data.beans.userprofile.UserProfileModel;
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

public class UserProfileActivityVM extends BaseActivityViewModel {
    public final SharedPref sharedPref;
    public final LiveLocationDetecter liveLocationDetecter;
    private final NetworkErrorHandler networkErrorHandler;
    private WelcomeRepo welcomeRepo;

    final SingleRequestEvent<UserProfileModel> profileBean = new SingleRequestEvent<>();
    @Inject
    public UserProfileActivityVM(SharedPref sharedPref, LiveLocationDetecter liveLocationDetecter, NetworkErrorHandler networkErrorHandler,WelcomeRepo welcomeRepo) {

        this.sharedPref = sharedPref;
        this.liveLocationDetecter = liveLocationDetecter;
        this.networkErrorHandler = networkErrorHandler;
        this.welcomeRepo = welcomeRepo;
    }


    public void getUserProfile(String user_id){
        welcomeRepo.getUserProfile(sharedPref.getUserData().getAuthKey(),user_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ApiResponse<UserProfileModel>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                profileBean.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(ApiResponse<UserProfileModel> profileApiResponse) {

                if (profileApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    profileBean.setValue(Resource.success(profileApiResponse.getData(), profileApiResponse.getMsg()));

                } else {

                    profileBean.setValue(Resource.error(null, profileApiResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                profileBean.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }
}
