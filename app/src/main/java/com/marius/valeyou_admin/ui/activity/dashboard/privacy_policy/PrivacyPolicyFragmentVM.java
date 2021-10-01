package com.marius.valeyou_admin.ui.activity.dashboard.privacy_policy;

import com.marius.valeyou_admin.data.beans.allcontent.AboutModel;
import com.marius.valeyou_admin.data.beans.allcontent.PrivacyModel;
import com.marius.valeyou_admin.data.beans.allcontent.TermsModel;
import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.local.SharedPref;
import com.marius.valeyou_admin.data.remote.helper.NetworkErrorHandler;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.data.repo.WelcomeRepo;
import com.marius.valeyou_admin.di.base.viewmodel.BaseFragmentViewModel;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;
import com.marius.valeyou_admin.util.location.LiveLocationDetecter;

import java.net.HttpURLConnection;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PrivacyPolicyFragmentVM extends BaseFragmentViewModel {
    public final SharedPref sharedPref;
    public final LiveLocationDetecter liveLocationDetecter;
    private final NetworkErrorHandler networkErrorHandler;
    private final WelcomeRepo welcomeRepo;

    final SingleRequestEvent<PrivacyModel> privacyBean = new SingleRequestEvent<>();
    final SingleRequestEvent<TermsModel> termsBean = new SingleRequestEvent<>();


    @Inject
    public PrivacyPolicyFragmentVM(SharedPref sharedPref,
                             LiveLocationDetecter liveLocationDetecter,
                             NetworkErrorHandler networkErrorHandler,
                             WelcomeRepo welcomeRepo) {
        this.sharedPref = sharedPref;
        this.networkErrorHandler = networkErrorHandler;
        this.liveLocationDetecter = liveLocationDetecter;
        this.welcomeRepo = welcomeRepo;

    }

    public void privacyApi(int type) {
        welcomeRepo.privacyApi(type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ApiResponse<PrivacyModel>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                privacyBean.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(ApiResponse<PrivacyModel> aboutModelApiResponse) {

                if (aboutModelApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    privacyBean.setValue(Resource.success(aboutModelApiResponse.getData(), aboutModelApiResponse.getMsg()));

                } else {

                    privacyBean.setValue(Resource.error(null, aboutModelApiResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                privacyBean.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }

    public void termsApi(int type) {
        welcomeRepo.termsApi(type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ApiResponse<TermsModel>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                termsBean.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(ApiResponse<TermsModel> aboutModelApiResponse) {

                if (aboutModelApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    termsBean.setValue(Resource.success(aboutModelApiResponse.getData(), aboutModelApiResponse.getMsg()));

                } else {

                    termsBean.setValue(Resource.error(null, aboutModelApiResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                termsBean.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }


}
