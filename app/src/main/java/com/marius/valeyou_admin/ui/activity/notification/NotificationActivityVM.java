package com.marius.valeyou_admin.ui.activity.notification;

import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.forgot.ForgotPasswordModel;
import com.marius.valeyou_admin.data.beans.notification.NotificationModel;
import com.marius.valeyou_admin.data.local.SharedPref;
import com.marius.valeyou_admin.data.remote.helper.NetworkErrorHandler;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.data.repo.WelcomeRepo;
import com.marius.valeyou_admin.di.base.viewmodel.BaseActivityViewModel;
import com.marius.valeyou_admin.di.base.viewmodel.BaseFragmentViewModel;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;
import com.marius.valeyou_admin.util.location.LiveLocationDetecter;

import java.net.HttpURLConnection;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NotificationActivityVM extends BaseActivityViewModel {
    public final SharedPref sharedPref;
    public final LiveLocationDetecter liveLocationDetecter;
    private final NetworkErrorHandler networkErrorHandler;
    private WelcomeRepo welcomeRepo;
    final SingleRequestEvent<List<NotificationModel>> notificationbean = new SingleRequestEvent<>();
    final SingleRequestEvent<SimpleApiResponse> readNotificationEvent = new SingleRequestEvent<>();
    final SingleRequestEvent<SimpleApiResponse> rescheduleconfirmationEvent = new SingleRequestEvent<>();
    final SingleRequestEvent<SimpleApiResponse> changeStatusEvent = new SingleRequestEvent<>();

    @Inject
    public NotificationActivityVM(SharedPref sharedPref, LiveLocationDetecter liveLocationDetecter, NetworkErrorHandler networkErrorHandler,WelcomeRepo welcomeRepo) {

        this.sharedPref = sharedPref;
        this.liveLocationDetecter = liveLocationDetecter;
        this.networkErrorHandler = networkErrorHandler;
        this.welcomeRepo = welcomeRepo;
    }

    public void getNotification(String auth_key){
        welcomeRepo.getNotifications(auth_key).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ApiResponse<List<NotificationModel>>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                notificationbean.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(ApiResponse<List<NotificationModel>> notificationResponse) {

                if (notificationResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    notificationbean.setValue(Resource.success(notificationResponse.getData(), notificationResponse.getMsg()));

                } else {

                    notificationbean.setValue(Resource.error(null, notificationResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                notificationbean.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }

    public void readNotification(String auth_key,String type, String id){
        welcomeRepo.readNotification(auth_key,type,id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<SimpleApiResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                readNotificationEvent.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(SimpleApiResponse notificationResponse) {

                if (notificationResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    readNotificationEvent.setValue(Resource.error(null, notificationResponse.getMsg()));

                } else {

                    readNotificationEvent.setValue(Resource.error(null, notificationResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                readNotificationEvent.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }

    public void rescheduleConfirmationApi(String order_id) {
        welcomeRepo.confirmRescheduleJob(sharedPref.getUserData().getAuthKey(),order_id,"1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SimpleApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                        rescheduleconfirmationEvent.setValue(Resource.loading(null));
                    }

                    @Override
                    public void onSuccess(SimpleApiResponse simpleApiResponse) {
                        if (simpleApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {
                            rescheduleconfirmationEvent.setValue(Resource.success(simpleApiResponse, simpleApiResponse.getMsg()));
                        } else {
                            rescheduleconfirmationEvent.setValue(Resource.warn(null, simpleApiResponse.getMsg()));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        rescheduleconfirmationEvent.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));
                    }
                });
    }

    public void changeJobStatus(String auth_key,String order_id,String type, String status,String reason) {
        welcomeRepo.changeStatus(auth_key,order_id,type,status,reason).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<SimpleApiResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                changeStatusEvent.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(SimpleApiResponse response) {

                if (response.getStatus() == HttpURLConnection.HTTP_OK) {

                    changeStatusEvent.setValue(Resource.success(null, response.getMsg()));

                } else {

                    changeStatusEvent.setValue(Resource.success(null,response.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                changeStatusEvent.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }


}
