package com.marius.valeyou_admin.ui.activity.dashboard.jobhistory.currentjob;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.base.MoreBean;
import com.marius.valeyou_admin.databinding.FragmentCurrentJobBinding;
import com.marius.valeyou_admin.databinding.HolderUpcomingJobBinding;
import com.marius.valeyou_admin.di.base.adapter.SimpleRecyclerViewAdapter;
import com.marius.valeyou_admin.di.base.view.AppFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.jobhistory.currentjob.jobdetailsone.JobDetailsOneActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.closedetails.JobDetailsActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.opendetails.CurrentJobDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

public class CurrentJobFragment extends AppFragment<FragmentCurrentJobBinding,CurrentJobFragmentVM> {

    private boolean check;

    public static Fragment newInstance(boolean check) {
        CurrentJobFragment currentJobFragment = new CurrentJobFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("check",check);
        currentJobFragment.setArguments(bundle);
        return currentJobFragment;
    }

    @Override
    protected BindingFragment<CurrentJobFragmentVM> getBindingFragment() {
        return new BindingFragment<>(R.layout.fragment_current_job,CurrentJobFragmentVM.class);
    }

    @Override
    protected void subscribeToEvents(CurrentJobFragmentVM vm) {
        check = getArguments().getBoolean("check");
        setRecyclerView();
    }

    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.rvUpcoming.setLayoutManager(layoutManager);
        binding.rvUpcoming.setAdapter(adapter);
        adapter.setList(getList());
    }

    private List<MoreBean> getList() {
        List<MoreBean> moreBeans = new ArrayList<>();
        moreBeans.add(new MoreBean(1,"",1));
        moreBeans.add(new MoreBean(1,"",1));
        moreBeans.add(new MoreBean(1,"",1));
        moreBeans.add(new MoreBean(1,"",1));
        moreBeans.add(new MoreBean(1,"",1));
        moreBeans.add(new MoreBean(1,"",1));
        moreBeans.add(new MoreBean(1,"",1));
        moreBeans.add(new MoreBean(1,"",1));
        return moreBeans;
    }

    SimpleRecyclerViewAdapter<MoreBean, HolderUpcomingJobBinding> adapter = new SimpleRecyclerViewAdapter(R.layout.holder_upcoming_job, BR.bean, new SimpleRecyclerViewAdapter.SimpleCallback<MoreBean>() {
        @Override
        public void onItemClick(View v, MoreBean o) {
            Intent intent;
            if (check) {
                intent = JobDetailsOneActivity.newIntent(getActivity());
                startNewActivity(intent);
            } else {
                intent = JobDetailsActivity.newIntent(getActivity());
                startNewActivity(intent);
            }
        }
    });

}
