package com.marius.valeyou_admin.ui.activity.dashboard.profile.portfolio.category;

import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.categoriesBean.CategoryDataBean;
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

public class PortfolioCategoryActivityVM  extends BaseActivityViewModel {

    public final SharedPref sharedPref;
    public final LiveLocationDetecter liveLocationDetecter;
    private final NetworkErrorHandler networkErrorHandler;
    private final WelcomeRepo welcomeRepo;
    final SingleRequestEvent<List<CategoryDataBean>> categoriesbeandata = new SingleRequestEvent<>();
    final SingleRequestEvent<SimpleApiResponse> profilebeandata = new SingleRequestEvent<>();
    final SingleRequestEvent<SimpleApiResponse> updatePriceEvent = new SingleRequestEvent<>();
    @Inject
    public PortfolioCategoryActivityVM( SharedPref sharedPref,LiveLocationDetecter liveLocationDetecter,NetworkErrorHandler networkErrorHandler,WelcomeRepo welcomeRepo) {
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



}
