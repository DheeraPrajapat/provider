package com.marius.valeyou_admin.ui.activity.dashboard.message.chatview;

import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.local.SharedPref;
import com.marius.valeyou_admin.data.remote.helper.NetworkErrorHandler;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.data.repo.WelcomeRepo;
import com.marius.valeyou_admin.di.base.viewmodel.BaseActivityViewModel;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;
import com.marius.valeyou_admin.util.location.LiveLocationDetecter;

import java.net.HttpURLConnection;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ChatActivityVM extends BaseActivityViewModel {
    public final SharedPref sharedPref;
    private final NetworkErrorHandler networkErrorHandler;
    public final LiveLocationDetecter liveLocationDetecter;
    private final WelcomeRepo welcomeRepo;

    SingleRequestEvent<SimpleApiResponse> blockUnblockEvent = new SingleRequestEvent<>();
    @Inject
    public ChatActivityVM(SharedPref sharedPref, NetworkErrorHandler networkErrorHandler, LiveLocationDetecter liveLocationDetecter,WelcomeRepo welcomeRepo) {
        this.sharedPref = sharedPref;
        this.networkErrorHandler = networkErrorHandler;
        this.liveLocationDetecter = liveLocationDetecter;
        this.welcomeRepo = welcomeRepo;
    }

    public void blockUnblock(String user2Id,String apiType){
        welcomeRepo.blockUnblockUser(sharedPref.getUserData().getAuthKey(),user2Id,apiType).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<SimpleApiResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                blockUnblockEvent.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(SimpleApiResponse faqResponse) {

                if (faqResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    blockUnblockEvent.setValue(Resource.success(null, faqResponse.getMsg()));

                } else {

                    blockUnblockEvent.setValue(Resource.error(null, faqResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                blockUnblockEvent.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }
}
