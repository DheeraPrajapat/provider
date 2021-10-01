package com.marius.valeyou_admin.ui.fragment.myjob.upcoming.opendetails.closejobs;

import android.os.Bundle;
import android.view.View;

import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.jobs.MyJobsModel;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.FragmentCloseJobsBinding;
import com.marius.valeyou_admin.databinding.FragmentUpcomingJobBinding;
import com.marius.valeyou_admin.di.base.view.AppFragment;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.UpComingJobFragmentVM;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.UpComingJobsAdapter;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

public class CloseJobsFragment extends AppFragment<FragmentCloseJobsBinding,CloseJobsFragmentVM> {

    private boolean check;

    public static Fragment newInstance(boolean check) {
        CloseJobsFragment comingJobFragment = new CloseJobsFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("check",check);
        comingJobFragment.setArguments(bundle);
        return comingJobFragment;
    }

    @Override
    protected BindingFragment<CloseJobsFragmentVM> getBindingFragment() {
        return new BindingFragment<>(R.layout.fragment_close_jobs,CloseJobsFragmentVM.class);
    }

    @Override
    protected void subscribeToEvents(CloseJobsFragmentVM vm) {
        check = getArguments().getBoolean("check");
        String authKey = vm.sharedPref.getUserData().getAuthKey();

        if (!authKey.isEmpty()) {
            vm.myJobsApi(authKey, "4");
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

    CloseJobsAdapter mAdapter = new CloseJobsAdapter(baseContext, new CloseJobsAdapter.ProductCallback() {
        @Override
        public void onItemClick(View v, MyJobsModel m) {
             /* Intent intent;
            if (check) {
                intent = CurrentJobDetailsActivity.newIntent(getActivity());
                startNewActivity(intent);
            } else {
                intent = JobDetailsActivity.newIntent(getActivity());
                startNewActivity(intent);
            }*/
        }
    });

}
