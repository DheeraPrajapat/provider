package com.marius.valeyou_admin.ui.activity.dashboard.aboutus;

import android.view.View;

import com.marius.valeyou_admin.data.beans.allcontent.AboutModel;
import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.local.SharedPref;
import com.marius.valeyou_admin.data.remote.helper.NetworkErrorHandler;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.data.repo.WelcomeRepo;
import com.marius.valeyou_admin.di.base.viewmodel.BaseFragmentViewModel;
import com.marius.valeyou_admin.util.event.SingleActionEvent;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;
import com.marius.valeyou_admin.util.location.LiveLocationDetecter;

import java.net.HttpURLConnection;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AboutUsFragmentVM extends BaseFragmentViewModel {
    public final SharedPref sharedPref;
    public final LiveLocationDetecter liveLocationDetecter;
    private final NetworkErrorHandler networkErrorHandler;
    private final WelcomeRepo welcomeRepo;

    final SingleRequestEvent<AboutModel> aboutBean = new SingleRequestEvent<>();


    @Inject
    public AboutUsFragmentVM(SharedPref sharedPref,
                             LiveLocationDetecter liveLocationDetecter,
                             NetworkErrorHandler networkErrorHandler,
                             WelcomeRepo welcomeRepo) {
        this.sharedPref = sharedPref;
        this.networkErrorHandler = networkErrorHandler;
        this.liveLocationDetecter = liveLocationDetecter;
        this.welcomeRepo = welcomeRepo;

    }

    public void aboutApi(int type) {
        welcomeRepo.aboutApi(type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ApiResponse<AboutModel>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                aboutBean.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(ApiResponse<AboutModel> aboutModelApiResponse) {

                if (aboutModelApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    aboutBean.setValue(Resource.success(aboutModelApiResponse.getData(), aboutModelApiResponse.getMsg()));

                } else {

                    aboutBean.setValue(Resource.error(null, aboutModelApiResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                aboutBean.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }


}
