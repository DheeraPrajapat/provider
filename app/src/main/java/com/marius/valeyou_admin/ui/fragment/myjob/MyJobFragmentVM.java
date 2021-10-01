package com.marius.valeyou_admin.ui.fragment.myjob;

import com.marius.valeyou_admin.data.beans.NotificationBadgeModel;
import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.jobs.MyJobsModel;
import com.marius.valeyou_admin.data.beans.map.MapListModel;
import com.marius.valeyou_admin.data.beans.rejected_bids.RejectedModels;
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

import androidx.databinding.ObservableField;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyJobFragmentVM extends BaseFragmentViewModel{
    public final SharedPref sharedPref;
    private final NetworkErrorHandler networkErrorHandler;
    public final LiveLocationDetecter liveLocationDetecter;
    private final WelcomeRepo welcomeRepo;
    final SingleActionEvent<Integer> passIntent = new SingleActionEvent<>();
    public final ObservableField<String> field_Player1 = new ObservableField<>();
    final SingleRequestEvent<List<MyJobsModel>> myJobsBean = new SingleRequestEvent<>();
    final SingleRequestEvent<NotificationBadgeModel> notificationbadgeEventRequest = new SingleRequestEvent<>();
    final SingleRequestEvent<List<MapListModel>> mapListBean = new SingleRequestEvent<>();
    final SingleRequestEvent<List<RejectedModels>> rejectedBeanEvent = new SingleRequestEvent<>();
    @Inject
    public MyJobFragmentVM(SharedPref sharedPref, NetworkErrorHandler networkErrorHandler, LiveLocationDetecter liveLocationDetecter,WelcomeRepo welcomeRepo) {
        this.sharedPref = sharedPref;
        this.networkErrorHandler = networkErrorHandler;
        this.liveLocationDetecter = liveLocationDetecter;
        this.welcomeRepo = welcomeRepo;
    }

    public void myJobsApi(String auth_key,String status){
        welcomeRepo.myJobsApi(auth_key,status).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ApiResponse<List<MyJobsModel>>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                myJobsBean.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(ApiResponse<List<MyJobsModel>> jobsApiResponse) {

                if (jobsApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    myJobsBean.setValue(Resource.success(jobsApiResponse.getData(), jobsApiResponse.getMsg()));

                } else {

                    myJobsBean.setValue(Resource.error(null, jobsApiResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                myJobsBean.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

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

    public void getREjectedBids(String auth_key){

        welcomeRepo.getRejectedBids(auth_key).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ApiResponse<List<RejectedModels>>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                rejectedBeanEvent.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(ApiResponse<List<RejectedModels>> mapApiResponse) {

                if (mapApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    rejectedBeanEvent.setValue(Resource.success(mapApiResponse.getData(), mapApiResponse.getMsg()));

                } else {

                    rejectedBeanEvent.setValue(Resource.error(null, mapApiResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                rejectedBeanEvent.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }

}
