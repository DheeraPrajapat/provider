package com.marius.valeyou_admin.ui.fragment.home;

import androidx.databinding.ObservableField;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import com.marius.valeyou_admin.data.beans.CitiesModel;
import com.marius.valeyou_admin.data.beans.NotificationBadgeModel;
import com.marius.valeyou_admin.data.beans.StatesModel;
import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.categoriesBean.CategoryDataBean;
import com.marius.valeyou_admin.data.beans.map.MapListModel;
import com.marius.valeyou_admin.data.beans.profilebean.ProfileModel;
import com.marius.valeyou_admin.data.beans.singninbean.SignInModel;
import com.marius.valeyou_admin.data.beans.suggesstions.SuggesstionModel;
import com.marius.valeyou_admin.data.local.SharedPref;
import com.marius.valeyou_admin.data.remote.helper.NetworkErrorHandler;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.data.repo.WelcomeRepo;
import com.marius.valeyou_admin.di.base.viewmodel.BaseFragmentViewModel;
import com.marius.valeyou_admin.util.event.SingleActionEvent;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;
import com.marius.valeyou_admin.util.location.LiveLocationDetecter;

import java.net.HttpURLConnection;
import java.util.List;

import javax.inject.Inject;

public class HomeFragmentVM  extends BaseFragmentViewModel {

    public final SharedPref sharedPref;
    private final NetworkErrorHandler networkErrorHandler;
    public final LiveLocationDetecter liveLocationDetecter;
    private final WelcomeRepo welcomeRepo;
    final SingleActionEvent<Integer> passIntent = new SingleActionEvent<>();
    public final ObservableField<String> field_Player1 = new ObservableField<>();
    final SingleRequestEvent<List<MapListModel>> mapListBean = new SingleRequestEvent<>();
    final SingleRequestEvent<List<SuggesstionModel>> suggestionsEventRequest = new SingleRequestEvent<>();
    final SingleRequestEvent<List<CategoryDataBean>> categoriesbeandata = new SingleRequestEvent<>();
    final SingleRequestEvent<SimpleApiResponse> filterbeandata = new SingleRequestEvent<>();
    final SingleRequestEvent<NotificationBadgeModel> notificationbadgeEventRequest = new SingleRequestEvent<>();
    public SingleRequestEvent<List<StatesModel>> statesEventRequest = new SingleRequestEvent<>();
    public SingleRequestEvent<List<CitiesModel>> citiesEventRequest = new SingleRequestEvent<>();
    final SingleRequestEvent<SimpleApiResponse> accountSettingBean = new SingleRequestEvent<>();
    final SingleRequestEvent<SignInModel> userBean = new SingleRequestEvent<>();

    @Inject
    public HomeFragmentVM(SharedPref sharedPref, NetworkErrorHandler networkErrorHandler, LiveLocationDetecter liveLocationDetecter,WelcomeRepo welcomeRepo) {
        this.sharedPref = sharedPref;
        this.networkErrorHandler = networkErrorHandler;
        this.liveLocationDetecter = liveLocationDetecter;
        this.welcomeRepo = welcomeRepo;
    }

    public void getMapListFilterApi(String auth_key,String id,String category_id,String sub_cat,String distance, String state_id, String city_id, String search,String start_price, String end_price){

        welcomeRepo.filterApi(auth_key,id,category_id,sub_cat,distance,state_id,city_id,search,start_price,end_price).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ApiResponse<List<MapListModel>>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                mapListBean.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(ApiResponse<List<MapListModel>> mapApiResponse) {

                if (mapApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    mapListBean.setValue(Resource.success(mapApiResponse.getData(), mapApiResponse.getMsg()));

                } else {

                    mapListBean.setValue(Resource.error(null, mapApiResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                mapListBean.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }

    public void searchSuggestions(String search) {
        welcomeRepo.getAllSuggestions(sharedPref.getUserData().getAuthKey(),search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ApiResponse<List<SuggesstionModel>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                        suggestionsEventRequest.setValue(Resource.loading(null));
                    }

                    @Override
                    public void onSuccess(ApiResponse<List<SuggesstionModel>> simpleApiResponse) {
                        if (simpleApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {
                            suggestionsEventRequest.setValue(Resource.success(simpleApiResponse.getData(), simpleApiResponse.getMsg()));
                        } else {
                            suggestionsEventRequest.setValue(Resource.warn(null, simpleApiResponse.getMsg()));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        suggestionsEventRequest.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));
                    }
                });
    }


    public void getCount(){
        welcomeRepo.getCount(sharedPref.getUserData().getAuthKey()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ApiResponse<NotificationBadgeModel>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                notificationbadgeEventRequest.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(ApiResponse<NotificationBadgeModel> faqResponse) {

                if (faqResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    notificationbadgeEventRequest.setValue(Resource.success(faqResponse.getData(), faqResponse.getMsg()));

                } else {

                    notificationbadgeEventRequest.setValue(Resource.error(null, faqResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                notificationbadgeEventRequest.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }


    public void getStates(){
        welcomeRepo.getStates().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ApiResponse<List<StatesModel>>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                statesEventRequest.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(ApiResponse<List<StatesModel>> faqResponse) {

                if (faqResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    statesEventRequest.setValue(Resource.success(faqResponse.getData(), faqResponse.getMsg()));

                } else {

                    statesEventRequest.setValue(Resource.error(null, faqResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                statesEventRequest.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }

    public void getCities(int id){
        welcomeRepo.getCities(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ApiResponse<List<CitiesModel>>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                citiesEventRequest.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(ApiResponse<List<CitiesModel>> faqResponse) {

                if (faqResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    citiesEventRequest.setValue(Resource.success(faqResponse.getData(), faqResponse.getMsg()));

                } else {

                    citiesEventRequest.setValue(Resource.error(null, faqResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                citiesEventRequest.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

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

    public void logoutApi(String provider_id,String auth_key){
        welcomeRepo.logout(provider_id,auth_key).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ApiResponse<SignInModel>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                userBean.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(ApiResponse<SignInModel> signinDataApiResponse) {

                if (signinDataApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {
                    sharedPref.deleteAll();
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

}
