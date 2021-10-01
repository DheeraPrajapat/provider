package com.marius.valeyou_admin.ui;

import android.util.Log;
import android.view.View;

import com.marius.valeyou_admin.data.beans.Aarin;
import com.marius.valeyou_admin.data.beans.Bank;
import com.marius.valeyou_admin.data.beans.CitiesModel;
import com.marius.valeyou_admin.data.beans.IdentityModel;
import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.categoriesBean.CategoryDataBean;
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

import org.json.JSONException;
import org.json.JSONObject;

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

public class StripeConnectActivityVM extends BaseActivityViewModel {

    public final SharedPref sharedPref;
    public final LiveLocationDetecter liveLocationDetecter;
    private final NetworkErrorHandler networkErrorHandler;
    private final WelcomeRepo welcomeRepo;
    public final SingleActionEvent<View> base_click = new SingleActionEvent<>();
    final SingleRequestEvent<Aarin> profileBean = new SingleRequestEvent<>();
    final SingleRequestEvent<List<Bank>> bancos = new SingleRequestEvent<>();
    final SingleRequestEvent<SimpleApiResponse> addAarin = new SingleRequestEvent<>();
    final SingleRequestEvent<Aarin> getAarin = new SingleRequestEvent<>();
    final SingleRequestEvent<SimpleApiResponse> editAarin = new SingleRequestEvent<>();
    @Inject
    public StripeConnectActivityVM(SharedPref sharedPref,LiveLocationDetecter liveLocationDetecter,NetworkErrorHandler networkErrorHandle,WelcomeRepo welcomeRepo) {
        this.sharedPref =sharedPref;
        this.liveLocationDetecter = liveLocationDetecter;
        this.welcomeRepo = welcomeRepo;
        this.networkErrorHandler = networkErrorHandle;

    }

    public void onClick(View view) {
        base_click.call(view);
    }

    public void profile(String auth_key,String AarinId) {
        welcomeRepo.getAarin(auth_key,AarinId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ApiResponse<Aarin>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                profileBean.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(ApiResponse<Aarin> profileApiResponse) {

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
    public void getBanks(){
        welcomeRepo.getBanks().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ApiResponse<List<Bank>>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                bancos.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(ApiResponse<List<Bank>> listaBancos) {

                if (listaBancos.getStatus() == HttpURLConnection.HTTP_OK) {

                    bancos.setValue(Resource.success(listaBancos.getData(), listaBancos.getMsg()));

                } else {

                    bancos.setValue(Resource.error(null, listaBancos.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                bancos.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }

    public void cadastrar(String auth_key, Map<String, RequestBody> map) {
        welcomeRepo.addAarin(auth_key,map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ApiResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                addAarin.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(ApiResponse profileApiResponse) {

                if (profileApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {
                    String message = "";
                    try {
                        Log.d("TESTE", String.valueOf(profileApiResponse.getData()));
                        JSONObject mainObject = new JSONObject(String.valueOf(profileApiResponse.getData()));
                        message = mainObject.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    addAarin.setValue(Resource.success(null, message));

                } else {

                    addAarin.setValue(Resource.error(null, profileApiResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                addAarin.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }

    public void editar(String auth_key, Map<String, RequestBody> map) {
        welcomeRepo.editAarin(auth_key,map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ApiResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                addAarin.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(ApiResponse profileApiResponse) {

                if (profileApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {
                    String message = "";
                    try {
                        Log.d("TESTE", String.valueOf(profileApiResponse.getData()));
                        JSONObject mainObject = new JSONObject(String.valueOf(profileApiResponse.getData()));
                        message = mainObject.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    addAarin.setValue(Resource.success(null, message));

                } else {

                    addAarin.setValue(Resource.error(null, profileApiResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                addAarin.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }

}
