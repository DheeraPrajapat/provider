package com.marius.valeyou_admin.ui.fragment.myjob.startjob;

import android.app.Activity;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.jobs.JobDetailModel;
import com.marius.valeyou_admin.data.beans.startendjob.StartEndModel;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityStartJobTimerBinding;
import com.marius.valeyou_admin.databinding.DialogDeleteAccouontBinding;
import com.marius.valeyou_admin.di.base.dialog.BaseCustomDialog;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.ui.activity.RatingActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.message.chatview.ChatActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.activity.main.MainActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.complete.CompleteJobActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.opendetails.CurrentJobDetailsActivity;
import com.marius.valeyou_admin.util.databinding.ImageViewBindingUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.lifecycle.Observer;

public class StartJobTimerActivity extends AppActivity<ActivityStartJobTimerBinding, StartJobTimerActivityVM> {

    int id;
    JobDetailModel modelData;


    public static Intent newInstent(Activity activity) {
        Intent intent = new Intent(activity, StartJobTimerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected BindingActivity<StartJobTimerActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_start_job_timer, StartJobTimerActivityVM.class);
    }

    @Override
    protected void subscribeToEvents(StartJobTimerActivityVM vm) {
        binding.header.tvTitle.setText(getResources().getString(R.string.current_job_detail));

        Intent intent = getIntent();
        if (intent != null) {

            id = intent.getIntExtra("id", 0);
            String authKey = viewModel.sharedPref.getUserData().getAuthKey();
            viewModel.getJobDetaial(authKey, id);

        }


        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                finish(true);
            }
        });

        vm.base_click.observe(this, new Observer<View>() {
            @Override
            public void onChanged(View view) {
                switch (view.getId()) {
                    case R.id.btn_end_job:
                        if (binding.btnEndJob.getText().toString().equalsIgnoreCase("end job")) {

                            dialogStartJob();

                        }else{

                            vm.info.setValue(getResources().getString(R.string.job_is_in_review));

                        }
                        break;
                    case R.id.btn_chat:

                        break;
                }
            }
        });


        vm.startJobApiBean.observe(this, new Observer<Resource<SimpleApiResponse>>() {
            @Override
            public void onChanged(Resource<SimpleApiResponse> apiResponseResource) {
                switch (apiResponseResource.status) {
                    case LOADING:
                        binding.setCheck(true);
                        break;
                    case SUCCESS:
                        binding.setCheck(false);
                        binding.btnEndJob.setText("In Review");
                        break;
                    case ERROR:
                        binding.setCheck(false);
                        vm.error.setValue(apiResponseResource.message);
                        if (apiResponseResource.message.equalsIgnoreCase("unauthorised")) {

                            vm.sharedPref.deleteAll();
                            Intent intent1 = LoginActivity.newIntent(StartJobTimerActivity.this);
                            startNewActivity(intent1, true, true);
                            finishAffinity();

                        }
                        break;
                    case WARN:
                        binding.setCheck(false);
                        vm.warn.setValue(apiResponseResource.message);
                        break;
                }
            }
        });


        vm.jobDetailBean.observe(this, new Observer<Resource<JobDetailModel>>() {
            @Override
            public void onChanged(Resource<JobDetailModel> resource) {
                switch (resource.status) {
                    case LOADING:
                        binding.setCheck(true);
                        break;
                    case SUCCESS:

                        binding.setCheck(false);
                        modelData = resource.data;
                        binding.setVariable(BR.bean, resource.data);

                        if (resource.data.getOrderCategories().size()>0){
                            binding.txtCategory.setText(resource.data.getOrderCategories().get(0).getCategory().getName());
                        }else{
                            binding.txtCategory.setText("No");

                        }

                        if (resource.data.getStatus() == 3) {
                            binding.statusText.setText("Ongoing");
                            binding.btnEndJob.setText("End Job");
                        }else if (resource.data.getStatus() == 4){
                            binding.statusText.setText("In Review");
                            binding.btnEndJob.setText("In Review");
                        }


                        if (resource.data.getOrderImages().size() > 0) {
                            String image = resource.data.getOrderImages().get(0).getImages();
                            ImageViewBindingUtils.setImage(binding.jobImage,  image);

                        }

                        long startJobTimeStamp = Long.parseLong(resource.data.getStartjobDate());
                        String startJobTime = convertTimeStampToTime(startJobTimeStamp);
                        binding.startTime.setText(startJobTime);


                        break;
                    case ERROR:
                        binding.setCheck(false);
                        vm.error.setValue(resource.message);
                        if (resource.message.equalsIgnoreCase(getResources().getString(R.string.unauthorised))){
                            sharedPref.deleteAll();
                            Intent intent1 = LoginActivity.newIntent(StartJobTimerActivity.this);
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


    private String convertTimeStampToTime(long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp * 1000L);
        String date = DateFormat.format("MMM dd, yyyy hh:mm a", cal).toString();
        return date;
    }


    private BaseCustomDialog<DialogDeleteAccouontBinding> dialogStartEndJob;

    private void dialogStartJob() {
        dialogStartEndJob = new BaseCustomDialog<>(StartJobTimerActivity.this, R.layout.dialog_end_job, new BaseCustomDialog.Listener() {
            @Override
            public void onViewClick(View view) {
                if (view != null && view.getId() != 0) {
                    switch (view.getId()) {
                        case R.id.btn_submit:
                            String authKey = viewModel.sharedPref.getUserData().getAuthKey();
                            String end = (System.currentTimeMillis() / 1000) + "";
                            viewModel.endJob(authKey, id, 4, end);

                            dialogStartEndJob.dismiss();
                            break;
                        case R.id.btn_cancel:
                            dialogStartEndJob.dismiss();
                            break;
                    }
                }
            }
        });
        dialogStartEndJob.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialogStartEndJob.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialogStartEndJob.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogStartEndJob.show();
    }


}
