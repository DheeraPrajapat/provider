package com.marius.valeyou_admin.ui.fragment.myjob.addwork.additional;

import android.view.View;

import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.jobs.JobDetailModel;
import com.marius.valeyou_admin.data.local.SharedPref;
import com.marius.valeyou_admin.data.remote.helper.NetworkErrorHandler;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.data.repo.WelcomeRepo;
import com.marius.valeyou_admin.di.base.viewmodel.BaseActivityViewModel;
import com.marius.valeyou_admin.util.event.SingleActionEvent;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;
import com.marius.valeyou_admin.util.location.LiveLocationDetecter;

import java.net.HttpURLConnection;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AdditionalActivityVM extends BaseActivityViewModel {
    public final SharedPref sharedPref;
    public final LiveLocationDetecter liveLocationDetecter;
    private final NetworkErrorHandler networkErrorHandler;
    private final WelcomeRepo welcomeRepo;
    public final SingleActionEvent<View> base_click = new SingleActionEvent<>();

    final SingleRequestEvent<JobDetailModel> jobDetailBean = new SingleRequestEvent<>();
    final SingleRequestEvent<ApiResponse<SimpleApiResponse>> deleteCostEvent = new SingleRequestEvent<>();
    @Inject
    public AdditionalActivityVM(SharedPref sharedPref,
                                    LiveLocationDetecter liveLocationDetecter,
                                    NetworkErrorHandler networkErrorHandler,
                                    WelcomeRepo welcomeRepo) {

        this.sharedPref = sharedPref;
        this.networkErrorHandler = networkErrorHandler;
        this.liveLocationDetecter = liveLocationDetecter;
        this.welcomeRepo = welcomeRepo;

    }


    public void onClick(View view) {
        base_click.call(view);
    }


    public void getJobDetaial(String auth_key,int post_id) {
        welcomeRepo.getJobDetailApi(auth_key, post_id).subscribeOn(Schedulers.io())
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

    public void deleteCost(String auth_key,int id) {
        welcomeRepo.delete_additional_cost(auth_key,id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<SimpleApiResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                deleteCostEvent.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(SimpleApiResponse response) {

                if (response.getStatus() == HttpURLConnection.HTTP_OK) {

                    deleteCostEvent.setValue(Resource.success(null, response.getMsg()));

                } else {

                    deleteCostEvent.setValue(Resource.success(null,response.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                deleteCostEvent.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }


}
