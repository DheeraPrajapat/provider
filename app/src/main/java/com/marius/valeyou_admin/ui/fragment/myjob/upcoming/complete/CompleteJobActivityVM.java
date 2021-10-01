package com.marius.valeyou_admin.ui.fragment.myjob.upcoming.complete;

import android.view.View;

import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.invoice.InvoiceModel;
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

public class CompleteJobActivityVM extends BaseActivityViewModel {
    public final SharedPref sharedPref;
    public final LiveLocationDetecter liveLocationDetecter;
    private final NetworkErrorHandler networkErrorHandler;
    private final WelcomeRepo welcomeRepo;
    public final SingleActionEvent<View> base_click = new SingleActionEvent<>();
    final SingleRequestEvent<InvoiceModel> invoiceEvent = new SingleRequestEvent<>();

    @Inject
    public CompleteJobActivityVM(SharedPref sharedPref,
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




    public void getInvoice(String auth_key,int post_id) {
        welcomeRepo.getInvoice(auth_key,post_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ApiResponse<InvoiceModel>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                invoiceEvent.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(ApiResponse<InvoiceModel> invoiceBean) {

                if (invoiceBean.getStatus() == HttpURLConnection.HTTP_OK) {

                    invoiceEvent.setValue(Resource.success(invoiceBean.getData(), invoiceBean.getMsg()));

                } else {

                    invoiceEvent.setValue(Resource.error(null, invoiceBean.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                invoiceEvent.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }

}
