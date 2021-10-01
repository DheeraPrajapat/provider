package com.marius.valeyou_admin.ui.fragment.favourite;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.base.MoreBean;
import com.marius.valeyou_admin.data.beans.favourites.MyFavouritesModel;
import com.marius.valeyou_admin.data.beans.map.MapListModel;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.FragmentMymBinding;
import com.marius.valeyou_admin.databinding.HolderFavItemsBinding;
import com.marius.valeyou_admin.di.base.adapter.SimpleRecyclerViewAdapter;
import com.marius.valeyou_admin.di.base.view.AppFragment;
import com.marius.valeyou_admin.ui.activity.RatingActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.activity.main.MainActivity;
import com.marius.valeyou_admin.ui.fragment.IdentityVerficationActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.startjob.StartJobTimerActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.closedetails.JobDetailsActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.opendetails.CurrentJobDetailsActivity;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;

import java.util.ArrayList;
import java.util.List;

public class MyMFragment extends AppFragment<FragmentMymBinding, MyMFragmentVM> {
    public static final String TAG = "MyMFragment";
    private MainActivity mainActivity;

    public static Fragment newInstance() {
        return new MyMFragment();
    }

    @Override
    protected BindingFragment<MyMFragmentVM> getBindingFragment() {
        return new BindingFragment<>(R.layout.fragment_mym, MyMFragmentVM.class);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void subscribeToEvents(final MyMFragmentVM vm) {

        vm.getMyFavourites(vm.sharedPref.getUserData().getAuthKey());

        mainActivity = (MainActivity) getActivity();
        mainActivity.hideBottomNav(false);
        vm.base_click.observe(this, view -> {
            switch (view != null ? view.getId() : 0) {

            }
        });

        vm.favouritesListBean.observe(this, new SingleRequestEvent.RequestObserver<List<MyFavouritesModel>>() {
            @Override
            public void onRequestReceived(@NonNull Resource<List<MyFavouritesModel>> resource) {
                switch (resource.status) {
                    case LOADING:
                        binding.setCheck(true);
                        break;
                    case SUCCESS:
                        binding.setCheck(false);
                        setRecyclerView(resource.data);
                        break;
                    case ERROR:
                        binding.setCheck(false);
                        vm.error.setValue(resource.message);
                        if (resource.message.equalsIgnoreCase(getResources().getString(R.string.unauthorised))){
                           viewModel.sharedPref.deleteAll();
                            Intent intent1 = LoginActivity.newIntent(getActivity());
                            startNewActivity(intent1, true, true);

                        }
                        break;
                    case WARN:
                        binding.setCheck(false);
                        vm.warn.setValue(resource.message);
                        break;
                }
            }
        });


    }

    private void setRecyclerView(List<MyFavouritesModel> list) {
        if (list.size() > 0) {
            binding.txtNoRecord.setVisibility(View.GONE);
            binding.rvFav.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            binding.rvFav.setLayoutManager(layoutManager);
            binding.rvFav.setAdapter(adapter);
            adapter.setList(list);
        } else {
            binding.txtNoRecord.setVisibility(View.VISIBLE);
            binding.rvFav.setVisibility(View.GONE);
        }
    }

    SimpleRecyclerViewAdapter<MyFavouritesModel, HolderFavItemsBinding> adapter = new SimpleRecyclerViewAdapter<>(R.layout.holder_fav_items, BR.bean, new SimpleRecyclerViewAdapter.SimpleCallback<MyFavouritesModel>() {
        @Override
        public void onItemClick(View v, MyFavouritesModel myFavouritesModel) {
            switch (v.getId()) {

                case R.id.cv_fav:

                    if (myFavouritesModel.getOrder().getStatus() == 0) {

                        Intent intent = JobDetailsActivity.newIntent(getActivity());
                        intent.putExtra("id",myFavouritesModel.getJobId());
                        startNewActivity(intent);

                    }else if (myFavouritesModel.getOrder().getStatus() == 1) {

                        Intent intent = CurrentJobDetailsActivity.newIntent(getActivity());
                        intent.putExtra("id", myFavouritesModel.getJobId());
                        startNewActivity(intent);

                    } else if (myFavouritesModel.getOrder().getStatus() == 3) {

                        Intent intent = StartJobTimerActivity.newInstent(getActivity());
                        intent.putExtra("id", myFavouritesModel.getJobId());
                        startNewActivity(intent);

                    } else if (myFavouritesModel.getOrder().getStatus() == 4) {

                        Intent intent = StartJobTimerActivity.newInstent(getActivity());
                        intent.putExtra("id", myFavouritesModel.getJobId());
                        startNewActivity(intent);


                    } else if (myFavouritesModel.getOrder().getStatus() == 6) {

                        Intent intent = RatingActivity.newIntent(getActivity());
                        intent.putExtra("comeForm", "compelted");
                        intent.putExtra("id", myFavouritesModel.getJobId());
                        startNewActivity(intent);

                    }
                    break;
            }


        }
    });


}
