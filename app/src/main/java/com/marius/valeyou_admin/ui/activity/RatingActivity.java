package com.marius.valeyou_admin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.jobs.JobDetailModel;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityRatingBinding;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.ui.activity.faq.FaqActivity;
import com.marius.valeyou_admin.ui.activity.faq.FaqActivityVM;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.startjob.StartJobTimerActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.closedetails.JobDetailsActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.complete.CompleteJobActivity;
import com.marius.valeyou_admin.util.AppUtils;
import com.marius.valeyou_admin.util.databinding.ImageViewBindingUtils;

public class RatingActivity extends AppActivity<ActivityRatingBinding, RatingActivityVM> {

    String name,image;
    int userTo;
    int id;
    JobDetailModel jobDetailModel;
    String comeFrom;

    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, RatingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected BindingActivity<RatingActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_rating, RatingActivityVM.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent in  = getIntent();
        if (in!=null){

           id = in.getIntExtra("id",0);
            viewModel.getJobDetaial(sharedPref.getUserData().getAuthKey(),id);


        }

    }

    @Override
    protected void subscribeToEvents(RatingActivityVM vm) {

        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                finish(true);
            }
        });

        vm.jobDetailBean.observe(this, new Observer<Resource<JobDetailModel>>() {
            @Override
            public void onChanged(Resource<JobDetailModel> resource) {
                switch (resource.status) {
                    case LOADING:
                        showProgressDialog(R.string.plz_wait);
                        break;
                    case SUCCESS:
                        dismissProgressDialog();
                        binding.setVariable(BR.bean,resource.data);
                        jobDetailModel = resource.data;
                        String time = resource.data.getStartTime();
                        String convertedTime = AppUtils.getDateTime(Long.parseLong(time));
                        binding.startDate.setText(convertedTime);

                       /* String endTime = resource.data.getEndjobDate();
                        String convertedEndTime = AppUtils.getDateTime(Long.parseLong(endTime));
                        binding.endDate.setText(convertedEndTime);*/
                        binding.endDate.setVisibility(View.GONE);

                        if ((resource.data.getOrderCategories().size()>0)) {
                        binding.txtCategory.setText(resource.data.getOrderCategories().get(0).getCategory().getName());
                    }else{
                            binding.txtCategory.setText(getResources().getString(R.string.no));
                        }

                        if (resource.data.getOrderImages().size()>0) {

                            String image = resource.data.getOrderImages().get(0).getImages();
                            ImageViewBindingUtils.setImage(binding.jobImage,image);

                        }else{
                            binding.jobImage.setImageResource(R.drawable.new_placeholder);
                        }

                        if (resource.data.getUser().getImage()!=null){
                            ImageViewBindingUtils.setProfilePicture(binding.userImage,resource.data.getUser().getImage());
                        }


                        if (jobDetailModel.getUser_rating().getId()!=null){
                            binding.cvUserRating.setVisibility(View.VISIBLE);
                            binding.userName.setText(jobDetailModel.getUser().getFirstName()+" "+jobDetailModel.getUser().getLastName());
                            binding.userRateDes.setText(jobDetailModel.getUser_rating().getDescription());
                            binding.userRating.setRating(Float.parseFloat(jobDetailModel.getUser_rating().getRatings()));
                            ImageViewBindingUtils.setProfileUrl(binding.userImg,jobDetailModel.getUser().getImage());

                        }else{
                            binding.cvUserRating.setVisibility(View.GONE);
                        }

                        if (jobDetailModel.getProvider_rating().getId()!=null){
                            binding.llRating.setVisibility(View.GONE);
                            binding.cvMyRating.setVisibility(View.VISIBLE);
                            binding.myName.setText(sharedPref.getUserData().getFirstName()+" "+sharedPref.getUserData().getLastName()+"(Me)");
                            binding.myRateDes.setText(jobDetailModel.getProvider_rating().getDescription());
                            binding.myRating.setRating(Float.parseFloat(jobDetailModel.getProvider_rating().getRatings()));
                            ImageViewBindingUtils.setProfileUrl(binding.myImage,sharedPref.getUserData().getImage());

                        }else{
                            binding.cvMyRating.setVisibility(View.GONE);
                            binding.llRating.setVisibility(View.VISIBLE);
                        }


                        break;
                    case ERROR:
                        dismissProgressDialog();
                        vm.error.setValue(resource.message);
                        if (resource.message.equalsIgnoreCase(getResources().getString(R.string.unauthorised))){
                            sharedPref.deleteAll();
                            Intent intent1 = LoginActivity.newIntent(RatingActivity.this);
                            startNewActivity(intent1, true, true);

                        }
                        break;
                    case WARN:
                        dismissProgressDialog();
                        vm.warn.setValue(resource.message);
                        break;
                }
            }
        });

        vm.ratingResponse.observe(this, new Observer<Resource<SimpleApiResponse>>() {
            @Override
            public void onChanged(Resource<SimpleApiResponse> simpleApiResponseResource) {
                switch (simpleApiResponseResource.status) {
                    case LOADING:
                      showProgressDialog(R.string.plz_wait);
                        break;
                    case SUCCESS:
                       dismissProgressDialog();

                       vm.success.setValue(simpleApiResponseResource.message);
                        Intent intent = CompleteJobActivity.newIntent(RatingActivity.this);
                        intent.putExtra("id",id);
                        startNewActivity(intent, true);
                        finish();

                        break;
                    case ERROR:
                        dismissProgressDialog();
                        vm.error.setValue(simpleApiResponseResource.message);
                        break;
                    case WARN:
                        dismissProgressDialog();
                        vm.warn.setValue(simpleApiResponseResource.message);
                        break;
                }
            }
        });

        vm.base_click.observe(this, new Observer<View>() {
            @Override
            public void onChanged(View view) {
                switch (view.getId()){
                    case R.id.btn_submit:

                        if (binding.rbRating.getRating()>0){

                            vm.rateUser(sharedPref.getUserData().getAuthKey(),jobDetailModel.getUser().getId(),jobDetailModel.getId(),binding.rbRating.getRating()+"",binding.tvDescription.getText().toString());

                        }else{
                            vm.error.setValue("Please add your rating.");
                        }

                        break;

                    case R.id.btn_invoice:

                        Intent intent = CompleteJobActivity.newIntent(RatingActivity.this);
                        intent.putExtra("id",id);
                        startNewActivity(intent);

                        break;

                }
            }
        });
    }
}