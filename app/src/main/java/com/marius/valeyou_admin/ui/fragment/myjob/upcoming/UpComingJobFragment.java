package com.marius.valeyou_admin.ui.fragment.myjob.upcoming;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.base.MoreBean;
import com.marius.valeyou_admin.data.beans.jobs.MyJobsModel;
import com.marius.valeyou_admin.data.beans.map.MapListModel;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.FragmentUpcomingJobBinding;
import com.marius.valeyou_admin.databinding.HolderUpcomingJobBinding;
import com.marius.valeyou_admin.di.base.adapter.SimpleRecyclerViewAdapter;
import com.marius.valeyou_admin.di.base.view.AppFragment;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.closedetails.JobDetailsActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.opendetails.CurrentJobDetailsActivity;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

public class UpComingJobFragment extends AppFragment<FragmentUpcomingJobBinding,UpComingJobFragmentVM> {

    private boolean check;

    public static Fragment newInstance(boolean check) {
        UpComingJobFragment comingJobFragment = new UpComingJobFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("check",check);
        comingJobFragment.setArguments(bundle);
        return comingJobFragment;
    }

    @Override
    protected BindingFragment<UpComingJobFragmentVM> getBindingFragment() {
        return new BindingFragment<>(R.layout.fragment_upcoming_job,UpComingJobFragmentVM.class);
    }

    @Override
    protected void subscribeToEvents(UpComingJobFragmentVM vm) {
        check = getArguments().getBoolean("check");
        String authKey = vm.sharedPref.getUserData().getAuthKey();

        if (!authKey.isEmpty()&&authKey!=null) {
            vm.myJobsApi(authKey, "0");
        }

        vm.myJobsBean.observe(this, new SingleRequestEvent.RequestObserver<List<MyJobsModel>>() {
            @Override
            public void onRequestReceived(@NonNull Resource<List<MyJobsModel>> resource) {
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
                        if (resource.message.equalsIgnoreCase("unauthorized")){
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

    private void setRecyclerView(List<MyJobsModel> myJobsModelsList) {
        if (myJobsModelsList.size()>0) {
            binding.rvUpcoming.setVisibility(View.VISIBLE);
            binding.txtNoJob.setVisibility(View.GONE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            binding.rvUpcoming.setLayoutManager(layoutManager);
            binding.rvUpcoming.setAdapter(mAdapter);
            mAdapter.setList(myJobsModelsList);
        }else{
            binding.rvUpcoming.setVisibility(View.GONE);
            binding.txtNoJob.setVisibility(View.VISIBLE);
        }
    }

    UpComingJobsAdapter mAdapter = new UpComingJobsAdapter(baseContext, new UpComingJobsAdapter.ProductCallback() {
        @Override
        public void onItemClick(View v, MyJobsModel m) {

            switch (v.getId()){
                case R.id.ll_job_item_click:

                    Intent intent = JobDetailsActivity.newIntent(getActivity());
                    intent.putExtra("id",m.getId());
                    startNewActivity(intent);


                    break;
            }

        }
    });

}
