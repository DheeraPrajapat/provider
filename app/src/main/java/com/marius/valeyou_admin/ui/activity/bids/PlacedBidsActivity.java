package com.marius.valeyou_admin.ui.activity.bids;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.map.MapListModel;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityPlacedBidsBinding;
import com.marius.valeyou_admin.databinding.HolderListItemsBinding;
import com.marius.valeyou_admin.di.base.adapter.SimpleRecyclerViewAdapter;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.di.base.view.BaseActivity;
import com.marius.valeyou_admin.di.base.view.BaseFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.changepassword.ChangePasswordFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.ProfileActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.fragment.home.HomeFragmentVM;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.closedetails.JobDetailsActivity;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlacedBidsActivity extends AppActivity<ActivityPlacedBidsBinding, PlacedBidsActivityVM> implements PlacedBidsAdapter.ProductCallback {
    String test = "";
    List<MapListModel> projectsList;
    List<MapListModel> finalList = new ArrayList<>();


    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, PlacedBidsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected BindingActivity<PlacedBidsActivityVM> getBindingActivity() {
        return new BaseActivity.BindingActivity<>(R.layout.activity_placed_bids, PlacedBidsActivityVM.class);
    }

    @Override
    protected void subscribeToEvents(PlacedBidsActivityVM vm) {

        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                finish(true);
            }
        });


        vm.getMapListFilterApi(sharedPref.getUserData().getAuthKey(), "", "", "", "", "", "", "", "", "");
        vm.mapListBean.observe(this, new SingleRequestEvent.RequestObserver<List<MapListModel>>() {
            @Override
            public void onRequestReceived(@NonNull Resource<List<MapListModel>> resource) {
                switch (resource.status) {
                    case LOADING:
                        binding.setCheck(true);
                        break;
                    case SUCCESS:
                        binding.setCheck(false);
                        binding.setVariable(BR.bean, resource.data);
                        projectsList = resource.data;

                        for (int i = 0; i < projectsList.size(); i++) {
                            if (projectsList.get(i).getBid_price() != 0) {
                                MapListModel model = new MapListModel();
                                model.setBid_price(projectsList.get(i).getBid_price());
                                model.setId(projectsList.get(i).getId());
                                model.setDescription(projectsList.get(i).getDescription());
                                model.setJobType(projectsList.get(i).getJobType());
                                model.setDistance(projectsList.get(i).getDistance());
                                model.setTitle(projectsList.get(i).getTitle());
                                model.setOrderImages(projectsList.get(i).getOrderImages());
                                model.setUserId(projectsList.get(i).getUserId());

                                finalList.add(model);
                            }
                        }

                        if (finalList.size() > 0) {
                            binding.rvBids.setVisibility(View.VISIBLE);
                            binding.txtNoRecord.setVisibility(View.GONE);
                            setRecyclerViewList(finalList);
                        } else {
                            binding.rvBids.setVisibility(View.GONE);
                            binding.txtNoRecord.setVisibility(View.VISIBLE);
                        }


                        break;
                    case ERROR:
                        binding.setCheck(false);
                        vm.error.setValue(resource.message);
                        if (resource.message.equalsIgnoreCase(getResources().getString(R.string.unauthorised))) {
                            sharedPref.deleteAll();
                            Intent intent1 = LoginActivity.newIntent(PlacedBidsActivity.this);
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


    private void setRecyclerViewList(List<MapListModel> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        PlacedBidsAdapter placedBidsAdapter = new PlacedBidsAdapter(this, this, list);
        binding.rvBids.setLayoutManager(layoutManager);
        binding.rvBids.setAdapter(placedBidsAdapter);

    }

    @Override
    public void onItemClick(View v, List<MapListModel> moreBeans, int pos) {
        switch (v.getId()) {
            case R.id.ll_list_items:
                Intent intent = JobDetailsActivity.newIntent(PlacedBidsActivity.this);
                intent.putExtra("id", moreBeans.get(pos).getId());
                startNewActivity(intent);
                break;
        }

    }
}