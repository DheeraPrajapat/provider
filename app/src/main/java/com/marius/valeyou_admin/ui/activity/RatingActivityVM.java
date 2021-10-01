package com.marius.valeyou_admin.ui.activity;

import android.widget.RatingBar;

import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.faq.FaqModel;
import com.marius.valeyou_admin.data.beans.jobs.JobDetailModel;
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

public class RatingActivityVM extends BaseActivityViewModel {
    public final SharedPref sharedPref;
    public final LiveLocationDetecter liveLocationDetecter;
    private final NetworkErrorHandler networkErrorHandler;
    private WelcomeRepo welcomeRepo;
    final SingleRequestEvent<SimpleApiResponse> ratingResponse = new SingleRequestEvent<>();
    final SingleRequestEvent<JobDetailModel> jobDetailBean = new SingleRequestEvent<>();

    @Inject
    public RatingActivityVM(SharedPref sharedPref, LiveLocationDetecter liveLocationDetecter, NetworkErrorHandler networkErrorHandler,WelcomeRepo welcomeRepo) {

        this.sharedPref = sharedPref;
        this.liveLocationDetecter = liveLocationDetecter;
        this.networkErrorHandler = networkErrorHandler;
        this.welcomeRepo = welcomeRepo;
    }

    public void rateUser(String auth_key,int userTo,int orderId, String rating, String description){
        welcomeRepo.rateUser(auth_key,userTo,orderId,rating,description).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<SimpleApiResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                ratingResponse.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(SimpleApiResponse faqResponse) {

                if (faqResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    ratingResponse.setValue(Resource.success(null, faqResponse.getMsg()));

                } else {

                    ratingResponse.setValue(Resource.error(null, faqResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                ratingResponse.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }


    public void getJobDetaial(String auth_key,int post_id) {
        welcomeRepo.getJobDetailApi(auth_key,post_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ApiResponse<JobDetailModel>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                jobDetailBean.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(ApiResponse<JobDetailModel> jobDetailApiResponse) {

                if (jobDetailApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    jobDetailBean.setValue(Resource.success(jobDetailApiResponse.getData(), jobDetailApiResponse.getMsg()));

                } else {

                    jobDetailBean.setValue(Resource.error(null, jobDetailApiResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                jobDetailBean.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }

}
