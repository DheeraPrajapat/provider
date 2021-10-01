package com.marius.valeyou_admin.ui.fragment.myjob.upcoming.opendetails.closejobs;

import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.jobs.MyJobsModel;
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

public class CloseJobsFragmentVM extends BaseFragmentViewModel {

    public final SharedPref sharedPref;
    private final NetworkErrorHandler networkErrorHandler;
    public final LiveLocationDetecter liveLocationDetecter;
    private final WelcomeRepo welcomeRepo;
    final SingleActionEvent<Integer> passIntent = new SingleActionEvent<>();
    public final ObservableField<String> field_Player1 = new ObservableField<>();
    final SingleRequestEvent<List<MyJobsModel>> myJobsBean = new SingleRequestEvent<>();

    @Inject
    public CloseJobsFragmentVM(SharedPref sharedPref, NetworkErrorHandler networkErrorHandler, LiveLocationDetecter liveLocationDetecter, WelcomeRepo welcomeRepo) {
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
}
