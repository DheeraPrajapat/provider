package com.marius.valeyou_admin.ui.fragment.myjob.upcoming.closedetails;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.jobs.JobDetailModel;
import com.marius.valeyou_admin.data.beans.profilebean.ProfileModel;
import com.marius.valeyou_admin.data.beans.reviews.MyReview;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityJobDetailsBinding;
import com.marius.valeyou_admin.databinding.DialogDeleteAccouontBinding;
import com.marius.valeyou_admin.databinding.HolderReviewsBinding;
import com.marius.valeyou_admin.di.base.adapter.SimpleRecyclerViewAdapter;
import com.marius.valeyou_admin.di.base.dialog.BaseCustomDialog;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.ui.StripeConnectActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.ProfileActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.util.AppUtils;
import com.marius.valeyou_admin.util.databinding.ImageViewBindingUtils;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

public class JobDetailsActivity extends AppActivity<ActivityJobDetailsBinding, JobDetailsActivityVM> {

    int id;
    int fav;
    boolean isChecked = false;
    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity,JobDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected BindingActivity<JobDetailsActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_job_details,JobDetailsActivityVM.class);
    }
    @Override
    protected void subscribeToEvents(JobDetailsActivityVM vm) {

        Intent in = getIntent();
        if (in!=null){

            String authKey = vm.sharedPref.getUserData().getAuthKey();
            id = in.getIntExtra("id",0);
            vm.getJobDetaial(authKey,id);


        }

        binding.header.tvTitle.setText(getResources().getString(R.string.detail));
        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                finish(true);
            }
        });

        vm.base_click.observe(this, new Observer<View>() {
            @Override
            public void onChanged(View view) {
                switch (view.getId()){

                    case R.id.btn_cancel_bid:
                      vm.deleteBid(sharedPref.getUserData().getAuthKey(),id);
                        break;

                    case R.id.btn_submit:
                        String price = binding.etBidPrice.getText().toString();
                        String authKey = vm.sharedPref.getUserData().getAuthKey();
                        if (price.isEmpty()){
                            vm.info.setValue(getResources().getString(R.string.enter_price_for_bid));
                        }else if (binding.etBidDes.getText().toString().isEmpty()) {
                            vm.info.setValue(getResources().getString(R.string.enter_description));
                        }else{
                            vm.placeBid(authKey,id,Integer.parseInt(price),binding.etBidDes.getText().toString());
                        }

                        break;

                    case R.id.ic_fav:
                        String auth_key = vm.sharedPref.getUserData().getAuthKey();
                        if (fav == 2) {
                            vm.addToFav(auth_key, id, 1);
                        }else {
                            vm.addToFav(auth_key, id, 2);
                        }

                        break;
                }
            }
        });


        vm.deleteBidEvent.observe(this, new Observer<Resource<SimpleApiResponse>>() {
            @Override
            public void onChanged(Resource<SimpleApiResponse> resource) {
                switch (resource.status) {
                    case LOADING:
                        binding.setCheck(true);
                        break;
                    case SUCCESS:
                        binding.setCheck(false);
                       vm.success.setValue(resource.message);
                       onBackPressed();
                        break;
                    case ERROR:
                        binding.setCheck(false);

                        if (resource.message.equalsIgnoreCase("unauthorized")){
                            Intent intent1 = LoginActivity.newIntent(JobDetailsActivity.this);
                            startNewActivity(intent1, true, true);

                        }else{
                            vm.error.setValue(resource.message);
                        }
                        break;
                    case WARN:
                        binding.setCheck(false);
                        vm.warn.setValue(resource.message);
                        break;
                }
            }
        });


        vm.placeApiBean.observe(this, new Observer<Resource<SimpleApiResponse>>() {
            @Override
            public void onChanged(Resource<SimpleApiResponse> resource) {
                switch (resource.status) {
                    case LOADING:
                        binding.setCheck(true);
                        break;
                    case SUCCESS:
                        binding.setCheck(false);
                        SuccessBidAccount();

                        break;
                    case ERROR:
                        binding.setCheck(false);

                        if (resource.message.equalsIgnoreCase("unauthorized")){
                            Intent intent1 = LoginActivity.newIntent(JobDetailsActivity.this);
                            startNewActivity(intent1, true, true);

                        }else{
                            AlertDialog alertDialog = new AlertDialog.Builder(JobDetailsActivity.this).create();
                            alertDialog.setTitle("Alert");
                            alertDialog.setMessage("Please connect your payment method to bid");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                        break;
                    case WARN:
                        binding.setCheck(false);
                        vm.warn.setValue(resource.message);
                        break;
                }
            }
        });

        vm.addToFavApiBean.observe(this, new Observer<Resource<SimpleApiResponse>>() {
            @Override
            public void onChanged(Resource<SimpleApiResponse> resource) {
                switch (resource.status) {
                    case LOADING:
                        binding.setCheck(true);
                        break;
                    case SUCCESS:
                        binding.setCheck(false);

                        if (isChecked){
                          binding.icFav.setImageResource(R.drawable.ic_favorite_icon);
                          isChecked = false;
                        }else{
                            binding.icFav.setImageResource(R.drawable.ic_fav_icon);
                            isChecked = true;
                        }
                        break;
                    case ERROR:
                        binding.setCheck(false);
                        vm.error.setValue(resource.message);
                        if (resource.message.equalsIgnoreCase("unauthorized")){
                            Intent intent1 = LoginActivity.newIntent(JobDetailsActivity.this);
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




        vm.jobDetailBean.observe(this, new Observer<Resource<JobDetailModel>>() {
           @Override
           public void onChanged(Resource<JobDetailModel> resource) {
               switch (resource.status) {
                   case LOADING:
                      binding.setCheck(true);
                       break;
                   case SUCCESS:
                       binding.setCheck(false);
                       setRecyclerView(resource.data.getOrderImages());
                        binding.setVariable(BR.bean,resource.data);
                        fav = resource.data.getFav();
                        if (fav ==1){
                            isChecked = true;
                        }else{
                            isChecked = false;
                        }
                       String time = resource.data.getStartTime();
                       String finalTime = AppUtils.getDateTime(Long.parseLong(time));
                       binding.startDate.setText(finalTime);

                        if (resource.data.getOrderImages().size()>0) {
                            String image = resource.data.getOrderImages().get(0).getImages();
                            ImageViewBindingUtils.setImage(binding.jobImage,image);

                        }else{
                            binding.jobImage.setImageResource(R.drawable.new_placeholder);
                        }


                       break;
                   case ERROR:
                       binding.setCheck(false);
                       vm.error.setValue(resource.message);
                       if (resource.message.equalsIgnoreCase("unauthorised")){
                           Intent intent1 = LoginActivity.newIntent(JobDetailsActivity.this);
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

    private void setRecyclerView(List<JobDetailModel.OrderImagesBean> listOfImages) {
        if (listOfImages.size()>0) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            binding.rvOrderImages.setLayoutManager(layoutManager);
            binding.rvOrderImages.setAdapter(adapterImages);
            adapterImages.setList(listOfImages);
        }
    }


    SimpleRecyclerViewAdapter<JobDetailModel.OrderImagesBean, HolderReviewsBinding> adapterImages =
            new SimpleRecyclerViewAdapter(R.layout.holder_order_images, BR.bean, new SimpleRecyclerViewAdapter.SimpleCallback<JobDetailModel.OrderImagesBean>() {
                @Override
                public void onItemClick(View v, JobDetailModel.OrderImagesBean detailModel) {

                }
            });


    public static String convertDate(String dateInMilliseconds,String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }

    private BaseCustomDialog<DialogDeleteAccouontBinding> dialogSuccessBid;
    private void SuccessBidAccount() {
        dialogSuccessBid = new BaseCustomDialog<>(JobDetailsActivity.this, R.layout.bid_success_dialog, new BaseCustomDialog.Listener() {
            @Override
            public void onViewClick(View view) {
                if (view != null && view.getId() != 0) {
                    switch (view.getId()) {
                        case R.id.btn_submit:
                            dialogSuccessBid.dismiss();
                            onBackPressed();
                            break;
                    }
                }
            }
        });

        dialogSuccessBid.setCancelable(false);
        dialogSuccessBid.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialogSuccessBid.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialogSuccessBid.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogSuccessBid.show();

    }

}
