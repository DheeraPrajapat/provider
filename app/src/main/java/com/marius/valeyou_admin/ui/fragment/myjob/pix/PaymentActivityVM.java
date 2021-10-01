package com.marius.valeyou_admin.ui.fragment.myjob.pix;

import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.local.SharedPref;
import com.marius.valeyou_admin.data.remote.helper.NetworkErrorHandler;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.data.repo.WelcomeRepo;
import com.marius.valeyou_admin.di.base.viewmodel.BaseActivityViewModel;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;

import java.net.HttpURLConnection;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class PaymentActivityVM extends BaseActivityViewModel{
    private final SharedPref sharedPref;
    private final NetworkErrorHandler networkErrorHandler;
    private final WelcomeRepo welcomeRepo;
//    SingleRequestEvent<List<GetCardBean>> singleRequestEvent = new SingleRequestEvent<>();
    SingleRequestEvent<SimpleApiResponse> paymentEvent = new SingleRequestEvent<>();
    @Inject
    public PaymentActivityVM(SharedPref sharedPref, NetworkErrorHandler networkErrorHandler, WelcomeRepo welcomeRepo) {
        this.sharedPref = sharedPref;
        this.networkErrorHandler = networkErrorHandler;
        this.welcomeRepo = welcomeRepo;
    }


    public void stripePaymentApi(String orderId){
        welcomeRepo.getAarin(sharedPref.getUserData().getAuthKey(),orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SimpleApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                        paymentEvent.setValue(Resource.loading(null));
                    }

                    @Override
                    public void onSuccess(SimpleApiResponse listApiResponse) {
                        if (listApiResponse.getStatus() == HttpURLConnection.HTTP_OK){
                            paymentEvent.setValue(Resource.success(null,listApiResponse.getMsg()));
                        } else {
                            paymentEvent.setValue(Resource.warn(null,listApiResponse.getMsg()));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        if (e instanceof HttpException) {
                            HttpException exception = (HttpException) e;
                            if (exception.code() == 401){
                                paymentEvent.setValue(Resource.error(null, "Provider Stripe account not connected."));
                            } else {
                                paymentEvent.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));
                            }
                        } else {
                            paymentEvent.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));
                        }

                    }
                });
    }



}
