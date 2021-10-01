package com.marius.valeyou_admin.ui.activity.dashboard.profile.myservices;

import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.categoriesBean.CategoryDataBean;
import com.marius.valeyou_admin.data.beans.profilebean.ProfileModel;
import com.marius.valeyou_admin.data.local.SharedPref;
import com.marius.valeyou_admin.data.remote.helper.NetworkErrorHandler;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.data.repo.WelcomeRepo;
import com.marius.valeyou_admin.di.base.viewmodel.BaseActivityViewModel;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;
import com.marius.valeyou_admin.util.location.LiveLocationDetecter;

import java.net.HttpURLConnection;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyServicesActivityVM extends BaseActivityViewModel {
    public final SharedPref sharedPref;
    public final LiveLocationDetecter liveLocationDetecter;
    private final NetworkErrorHandler networkErrorHandler;
    private final WelcomeRepo welcomeRepo;
    final SingleRequestEvent<List<CategoryDataBean>> categoriesbeandata = new SingleRequestEvent<>();
    final SingleRequestEvent<SimpleApiResponse> profilebeandata = new SingleRequestEvent<>();
    final SingleRequestEvent<SimpleApiResponse> updatePriceEvent = new SingleRequestEvent<>();
    @Inject
    public MyServicesActivityVM( SharedPref sharedPref,LiveLocationDetecter liveLocationDetecter,NetworkErrorHandler networkErrorHandler,WelcomeRepo welcomeRepo) {
        this.sharedPref = sharedPref;
        this.liveLocationDetecter = liveLocationDetecter;
        this.networkErrorHandler = networkErrorHandler;
        this.welcomeRepo = welcomeRepo;
    }

    public void categoriesData(int type,String provider_id){
        welcomeRepo.categoriesApi(type,provider_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ApiResponse<List<CategoryDataBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                        categoriesbeandata.setValue(Resource.loading(null));
                    }

                    @Override
                    public void onSuccess(ApiResponse<List<CategoryDataBean>> categoriesApiResponse) {

                        if (categoriesApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                            categoriesbeandata.setValue(Resource.success(categoriesApiResponse.getData(), categoriesApiResponse.getMsg()));

                        } else {

                            categoriesbeandata.setValue(Resource.warn(null, categoriesApiResponse.getMsg()));

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        categoriesbeandata.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));
                    }
                });
    }

    public void editSerivces(String auth_key,String selected_data){
        welcomeRepo.editProfileServicesApi(auth_key,selected_data).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SimpleApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                        profilebeandata.setValue(Resource.loading(null));
                    }

                    @Override
                    public void onSuccess(SimpleApiResponse profileApiResponse) {

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


    public void updatePrice(String category_id, String price){
        welcomeRepo.updatePrice(sharedPref.getUserData().getAuthKey(),category_id,price).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SimpleApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                        updatePriceEvent.setValue(Resource.loading(null));
                    }

                    @Override
                    public void onSuccess(SimpleApiResponse categoriesApiResponse) {

                        if (categoriesApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                            updatePriceEvent.setValue(Resource.success(null, categoriesApiResponse.getMsg()));

                        } else {

                            updatePriceEvent.setValue(Resource.warn(null, categoriesApiResponse.getMsg()));

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        updatePriceEvent.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));
                    }
                });
    }


}
