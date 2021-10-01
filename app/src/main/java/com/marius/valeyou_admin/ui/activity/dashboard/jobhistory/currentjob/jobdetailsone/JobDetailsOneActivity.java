package com.marius.valeyou_admin.ui.activity.dashboard.jobhistory.currentjob.jobdetailsone;

import android.app.Activity;
import android.content.Intent;

import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.jobs.JobDetailModel;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityJobDetailsOneBinding;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.ProfileActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.certificate.AddCertificateActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.closedetails.JobDetailsActivity;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

public class JobDetailsOneActivity extends AppActivity<ActivityJobDetailsOneBinding,JobDetailsOneActivityVM>{

    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity,JobDetailsOneActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected BindingActivity<JobDetailsOneActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_job_details_one,JobDetailsOneActivityVM.class);
    }

    @Override
    protected void subscribeToEvents(JobDetailsOneActivityVM vm) {
        binding.header.tvTitle.setText("Current Job Details");
        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                finish(true);
            }
        });


    }
}
