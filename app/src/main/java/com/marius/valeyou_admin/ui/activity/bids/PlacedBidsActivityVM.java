package com.marius.valeyou_admin.ui.activity.bids;

import androidx.databinding.ObservableField;

import com.marius.valeyou_admin.data.beans.NotificationBadgeModel;
import com.marius.valeyou_admin.data.beans.StatesModel;
import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.categoriesBean.CategoryDataBean;
import com.marius.valeyou_admin.data.beans.map.MapListModel;
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

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PlacedBidsActivityVM extends BaseActivityViewModel {
    public final SharedPref sharedPref;
    private final NetworkErrorHandler networkErrorHandler;
    public final LiveLocationDetecter liveLocationDetecter;
    private final WelcomeRepo welcomeRepo;
    final SingleRequestEvent<List<MapListModel>> mapListBean = new SingleRequestEvent<>();

    @Inject
    public PlacedBidsActivityVM(SharedPref sharedPref, NetworkErrorHandler networkErrorHandler, LiveLocationDetecter liveLocationDetecter,WelcomeRepo welcomeRepo) {
        this.sharedPref = sharedPref;
        this.networkErrorHandler = networkErrorHandler;
        this.liveLocationDetecter = liveLocationDetecter;
        this.welcomeRepo = welcomeRepo;
    }

    public void getMapListFilterApi(String auth_key,String id,String category_id,String sub_cat,String distance, String state_id,String city_id,String search,String start_price, String end_price){

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


}
