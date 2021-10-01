package com.marius.valeyou_admin.ui.activity.dashboard.profile.providerlanguages;

import android.view.View;

import com.google.gson.JsonElement;
import com.marius.valeyou_admin.data.beans.LanguagesBean;
import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.profilebean.LanguagesModel;
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
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SelectLanguageActivityVM extends BaseActivityViewModel {
    public final SharedPref sharedPref;
    public final LiveLocationDetecter liveLocationDetecter;
    private final NetworkErrorHandler networkErrorHandler;
    private final WelcomeRepo welcomeRepo;
    public final SingleActionEvent<View> base_click = new SingleActionEvent<>();

    final SingleRequestEvent<List<LanguagesBean>> languagesBean = new SingleRequestEvent<>();
    final SingleRequestEvent<JsonElement> addLanguageBean = new SingleRequestEvent<>();
    @Inject
    public SelectLanguageActivityVM(SharedPref sharedPref,LiveLocationDetecter liveLocationDetecter,NetworkErrorHandler networkErrorHandler,WelcomeRepo welcomeRepo) {

        this.sharedPref = sharedPref;
        this.networkErrorHandler = networkErrorHandler;
        this.liveLocationDetecter = liveLocationDetecter;
        this.welcomeRepo = welcomeRepo;

    }

    public void onClick(View view) {
        base_click.call(view);
    }


    public void getLanguages() {
        welcomeRepo.allLanguages().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ApiResponse<List<LanguagesBean>>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                languagesBean.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(ApiResponse<List<LanguagesBean>> languagesApiResponse) {

                if (languagesApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    languagesBean.setValue(Resource.success(languagesApiResponse.getData(), languagesApiResponse.getMsg()));

                } else {

                    languagesBean.setValue(Resource.error(null, languagesApiResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                languagesBean.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }

    public void addLanguage(String auth_key,String name,String type) {
        welcomeRepo.addLanguageApi(auth_key,name,type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ApiResponse<JsonElement>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                addLanguageBean.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(ApiResponse<JsonElement> languagesApiResponse) {

                if (languagesApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    addLanguageBean.setValue(Resource.success(languagesApiResponse.getData(), languagesApiResponse.getMsg()));

                } else {

                    addLanguageBean.setValue(Resource.error(null, languagesApiResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                addLanguageBean.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }

    public void editLanguage(String auth_key,int lang_id,String name,String type) {
        welcomeRepo.editLanguage(auth_key,lang_id,name,type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<SimpleApiResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                addLanguageBean.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(SimpleApiResponse languagesApiResponse) {

                if (languagesApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    addLanguageBean.setValue(Resource.error(null, languagesApiResponse.getMsg()));

                } else {

                    addLanguageBean.setValue(Resource.error(null, languagesApiResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                addLanguageBean.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }

}
