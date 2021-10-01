package com.marius.valeyou_admin.ui.fragment.favourite;

import androidx.databinding.ObservableField;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.favourites.MyFavouritesModel;
import com.marius.valeyou_admin.data.beans.map.MapListModel;
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

public class MyMFragmentVM extends BaseFragmentViewModel {

    public final SharedPref sharedPref;
    private final NetworkErrorHandler networkErrorHandler;
    public final LiveLocationDetecter liveLocationDetecter;
    private final WelcomeRepo welcomeRepo;
    final SingleActionEvent<Integer> passIntent = new SingleActionEvent<>();
    public final ObservableField<String> field_Player1 = new ObservableField<>();

    final SingleRequestEvent<List<MyFavouritesModel>> favouritesListBean = new SingleRequestEvent<>();

    @Inject
    public MyMFragmentVM(SharedPref sharedPref, NetworkErrorHandler networkErrorHandler, LiveLocationDetecter liveLocationDetecter,WelcomeRepo welcomeRepo) {
        this.sharedPref = sharedPref;
        this.networkErrorHandler = networkErrorHandler;
        this.liveLocationDetecter = liveLocationDetecter;
        this.welcomeRepo = welcomeRepo;
    }
    public void getMyFavourites(String auth_key){
        welcomeRepo.myFavouritesApi(auth_key).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ApiResponse<List<MyFavouritesModel>>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                favouritesListBean.setValue(Resource.loading(null));
            }

            @Override
            public void onSuccess(ApiResponse<List<MyFavouritesModel>> mapApiResponse) {

                if (mapApiResponse.getStatus() == HttpURLConnection.HTTP_OK) {

                    favouritesListBean.setValue(Resource.success(mapApiResponse.getData(), mapApiResponse.getMsg()));

                } else {

                    favouritesListBean.setValue(Resource.error(null, mapApiResponse.getMsg()));

                }
            }

            @Override
            public void onError(Throwable e) {

                favouritesListBean.setValue(Resource.error(null, networkErrorHandler.getErrMsg(e)));

            }
        });
    }
}
