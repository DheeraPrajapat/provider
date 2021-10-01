package com.marius.valeyou_admin.ui.activity.dashboard.jobhistory;

import android.view.View;

import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.databinding.FragmentJobHistoryBinding;
import com.marius.valeyou_admin.di.base.adapter.PagerAdapter;
import com.marius.valeyou_admin.di.base.view.AppFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.jobhistory.currentjob.CurrentJobFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

public class JobHistoryFragment extends AppFragment<FragmentJobHistoryBinding,JobHistoryFragmentVM> {

    public static final String TAG = "JobHistoryFragment";

    public static Fragment newInstance() {
        return new JobHistoryFragment();
    }

    @Override
    protected BindingFragment<JobHistoryFragmentVM> getBindingFragment() {
        return new BindingFragment<>(R.layout.fragment_job_history,JobHistoryFragmentVM.class);
    }

    @Override
    protected void subscribeToEvents(JobHistoryFragmentVM vm) {
        vm.base_click.observe(this, new Observer<View>() {
            @Override
            public void onChanged(View view) {
                switch (view!=null?view.getId():0){
                    case R.id.ll_one:
                        binding.vpJob.setCurrentItem(0);
                        break;
                    case R.id.ll_two:
                        binding.vpJob.setCurrentItem(1);
                        break;
                }
            }
        });
        setViewPager();
    }

    private List<Fragment> fragmentList;
    private void setViewPager() {
        fragmentList = new ArrayList<>();
        fragmentList.add(CurrentJobFragment.newInstance(true));
        fragmentList.add(CurrentJobFragment.newInstance(false));
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager(), fragmentList);
        binding.vpJob.setAdapter(pagerAdapter);
        binding.vpJob.setCurrentItem(0);
        binding.vpJob.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        binding.setCheck(false);
                        break;
                    case 1:
                        binding.setCheck(true);
                        break;
                }
            }
        });
    }

}
