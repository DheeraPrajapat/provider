package com.marius.valeyou_admin.ui.fragment.more;

import androidx.databinding.ObservableField;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import com.google.gson.JsonObject;
import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.profilebean.ProfileModel;
import com.marius.valeyou_admin.data.beans.singninbean.SignInModel;
import com.marius.valeyou_admin.data.local.SharedPref;
import com.marius.valeyou_admin.data.remote.helper.NetworkErrorHandler;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.data.repo.WelcomeRepo;
import com.marius.valeyou_admin.di.base.viewmodel.BaseFragmentViewModel;
import com.marius.valeyou_admin.util.event.SingleActionEvent;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;

import java.net.HttpURLConnection;

import javax.inject.Inject;

public class MoreFragmentVM extends BaseFragmentViewModel {

    public final SharedPref sharedPref;
    private final NetworkErrorHandler networkErrorHandler;
    final SingleActionEvent<Integer> passIntent = new SingleActionEvent<>();
    public final ObservableField<String> field_Player1 = new ObservableField<>();
    public final WelcomeRepo welcomeRepo;
    final SingleRequestEvent<SignInModel> userBean = new SingleRequestEvent<>();

    @Inject
    public MoreFragmentVM(SharedPref sharedPref, NetworkErrorHandler networkErrorHandler, WelcomeRepo welcomeRepo) {
        this.sharedPref = sharedPref;
        this.networkErrorHandler = networkErrorHandler;
        this.welcomeRepo = welcomeRepo;
    }


    public void logoutApi(String provider_id,String auth_key){
        welcomeRepo.logout(provider_id,auth_key).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ApiResponse<SignInModel>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                userBean.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(ApiResponse<SignInModel> signinDataApiResponse) {

                if (signinDataApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    sharedPref.deleteAll();
                    userBean.setValue(Resource.success(signinDataApiResponse.getData(), signinDataApiResponse.getMsg()));

                } else {

                    userBean.setValue(Resource.error(null, signinDataApiResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                userBean.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }


}
