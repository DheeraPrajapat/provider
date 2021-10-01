package com.marius.valeyou_admin.ui.activity.dashboard.profile;

import android.view.View;

import com.marius.valeyou_admin.data.beans.IdentityModel;
import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.profilebean.ProfileModel;
import com.marius.valeyou_admin.data.beans.reviews.MyReview;
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
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProfileActivityVM extends BaseActivityViewModel {

    public final SharedPref sharedPref;
    public final LiveLocationDetecter liveLocationDetecter;
    private final NetworkErrorHandler networkErrorHandler;
    private final WelcomeRepo welcomeRepo;
    public final SingleActionEvent<View> base_click = new SingleActionEvent<>();

    final SingleRequestEvent<ProfileModel> profileBean = new SingleRequestEvent<>();
    final SingleRequestEvent<List<MyReview>> reviewBean = new SingleRequestEvent<>();
    final SingleRequestEvent<List<IdentityModel>> identityBean = new SingleRequestEvent<>();
    final SingleRequestEvent<SimpleApiResponse> deleteApiBean = new SingleRequestEvent<>();
    final SingleRequestEvent<SimpleApiResponse> accountSettingBean = new SingleRequestEvent<>();

    @Inject
    public ProfileActivityVM(SharedPref sharedPref,LiveLocationDetecter liveLocationDetecter,NetworkErrorHandler networkErrorHandle,WelcomeRepo welcomeRepo) {
        this.sharedPref =sharedPref;
        this.liveLocationDetecter = liveLocationDetecter;
        this.welcomeRepo = welcomeRepo;
        this.networkErrorHandler = networkErrorHandle;

    }

    public void onClick(View view) {
        base_click.call(view);
    }

    public void profile(String auth_key,String provider_id) {
        welcomeRepo.profile(auth_key,provider_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ApiResponse<ProfileModel>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                profileBean.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(ApiResponse<ProfileModel> profileApiResponse) {

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


    public void myReview(String auth_key) {
        welcomeRepo.myReviewApi(auth_key).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ApiResponse<List<MyReview>>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                reviewBean.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(ApiResponse<List<MyReview>> reviewApiResponse) {

                if (reviewApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    reviewBean.setValue(Resource.success(reviewApiResponse.getData(), reviewApiResponse.getMsg()));

                } else {

                    reviewBean.setValue(Resource.error(null, reviewApiResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                reviewBean.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }


    public void deletePortFolio(String authKey,int id){
        welcomeRepo.deletePortfolioApi(authKey,id).subscribeOn(Schedulers.io())
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

    public void deleteLanguage(String authKey,int lang_id){
        welcomeRepo.deleteLanguageApi(authKey,lang_id).subscribeOn(Schedulers.io())
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

    public void deleteCertificate(String authKey,int certificate_id){
        welcomeRepo.deleteCertificate(authKey,certificate_id).subscribeOn(Schedulers.io())
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

    public void deleteEducation(String authKey,int id){
        welcomeRepo.deleteEducation(authKey,id).subscribeOn(Schedulers.io())
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


    public void deleteExperience(String authKey,int id){
        welcomeRepo.deleteExperience(authKey,id).subscribeOn(Schedulers.io())
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


    public void accountSetting(String authKey,String type){
        welcomeRepo.accountSetting(authKey,type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<SimpleApiResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                accountSettingBean.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(SimpleApiResponse apiResponse) {

                if (apiResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    accountSettingBean.setValue(Resource.success(null, apiResponse.getMsg()));

                } else {

                    accountSettingBean.setValue(Resource.error(null, apiResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                accountSettingBean.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }
}






