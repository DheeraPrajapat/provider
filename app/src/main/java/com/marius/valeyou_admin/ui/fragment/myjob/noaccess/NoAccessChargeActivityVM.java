package com.marius.valeyou_admin.ui.fragment.myjob.noaccess;

import com.marius.valeyou_admin.data.beans.ComissionModel;
import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.local.SharedPref;
import com.marius.valeyou_admin.data.remote.helper.NetworkErrorHandler;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.data.repo.WelcomeRepo;
import com.marius.valeyou_admin.di.base.view.BaseActivity;
import com.marius.valeyou_admin.di.base.viewmodel.BaseActivityViewModel;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;
import com.marius.valeyou_admin.util.location.LiveLocationDetecter;

import java.net.HttpURLConnection;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NoAccessChargeActivityVM extends BaseActivityViewModel {
    public final SharedPref sharedPref;
    public final LiveLocationDetecter liveLocationDetecter;
    private final NetworkErrorHandler networkErrorHandler;
    private WelcomeRepo welcomeRepo;
    final SingleRequestEvent<SimpleApiResponse> addNoAccessChargeEvent = new SingleRequestEvent<>();
    final SingleRequestEvent<ComissionModel> getNoAccessChargeAmountEvent = new SingleRequestEvent<>();


    @Inject
    public NoAccessChargeActivityVM(SharedPref sharedPref, LiveLocationDetecter liveLocationDetecter, NetworkErrorHandler networkErrorHandler,WelcomeRepo welcomeRepo) {

        this.sharedPref = sharedPref;
        this.liveLocationDetecter = liveLocationDetecter;
        this.networkErrorHandler = networkErrorHandler;
        this.welcomeRepo = welcomeRepo;
    }
    public void addNoAccessCharge(String auth_key,String order_id,String des, String price) {
        welcomeRepo.addNoAccessCharge(auth_key,order_id,des,price).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<SimpleApiResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                addNoAccessChargeEvent.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(SimpleApiResponse response) {

                if (response.getStatus() == HttpURLConnection.HTTP_OK) {

                    addNoAccessChargeEvent.setValue(Resource.success(null, response.getMsg()));

                } else {

                    addNoAccessChargeEvent.setValue(Resource.success(null,response.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                addNoAccessChargeEvent.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }

    public void getNoAccessChargeAmount() {
        welcomeRepo.getComission().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ApiResponse<ComissionModel>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                addNoAccessChargeEvent.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(ApiResponse<ComissionModel> response) {

                if (response.getStatus() == HttpURLConnection.HTTP_OK) {

                    getNoAccessChargeAmountEvent.setValue(Resource.success(response.getData(), response.getMsg()));

                } else {

                    getNoAccessChargeAmountEvent.setValue(Resource.success(null,response.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                getNoAccessChargeAmountEvent.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }


}
