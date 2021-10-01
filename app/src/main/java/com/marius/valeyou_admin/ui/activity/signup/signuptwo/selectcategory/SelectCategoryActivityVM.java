package com.marius.valeyou_admin.ui.activity.signup.signuptwo.selectcategory;

import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.categoriesBean.CategoryDataBean;
import com.marius.valeyou_admin.data.beans.singninbean.SignInModel;
import com.marius.valeyou_admin.data.local.SharedPref;
import com.marius.valeyou_admin.data.remote.helper.NetworkErrorHandler;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.data.repo.WelcomeRepo;
import com.marius.valeyou_admin.di.base.viewmodel.BaseActivityViewModel;
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

public class SelectCategoryActivityVM extends BaseActivityViewModel{

    private final SharedPref sharedPref;
    public final LiveLocationDetecter liveLocationDetecter;
    private final NetworkErrorHandler networkErrorHandler;
    private final WelcomeRepo welcomeRepo;
    final SingleRequestEvent<ApiResponse<SignInModel>> profilebeandata = new SingleRequestEvent<>();
    final SingleRequestEvent<List<CategoryDataBean>> categoriesbeandata = new SingleRequestEvent<>();

    @Inject
    public SelectCategoryActivityVM( SharedPref sharedPref,LiveLocationDetecter liveLocationDetecter,NetworkErrorHandler networkErrorHandler,WelcomeRepo welcomeRepo) {
            this.sharedPref = sharedPref;
            this.liveLocationDetecter = liveLocationDetecter;
            this.networkErrorHandler = networkErrorHandler;
            this.welcomeRepo = welcomeRepo;
    }

    public void categoriesData(int type){
        welcomeRepo.categoriesApi(type).subscribeOn(Schedulers.io())
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


    public void editProfile(String auth_key, Map<String, String> map){
        welcomeRepo.editProfileStr(auth_key, map).subscribeOn(Schedulers.io())
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
