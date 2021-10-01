package com.marius.valeyou_admin.ui.activity.notification;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.base.MoreBean;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.notification.NotificationModel;
import com.marius.valeyou_admin.data.beans.singninbean.SocialSignIn;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityNotificationBinding;
import com.marius.valeyou_admin.databinding.HolderCancelJobDialogBinding;
import com.marius.valeyou_admin.databinding.HolderNotificationItemBinding;
import com.marius.valeyou_admin.databinding.HolderYesConfirmationBinding;
import com.marius.valeyou_admin.di.base.adapter.SimpleRecyclerViewAdapter;
import com.marius.valeyou_admin.di.base.dialog.BaseCustomDialog;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.di.base.view.AppFragment;
import com.marius.valeyou_admin.ui.activity.RatingActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.activity.main.MainActivity;
import com.marius.valeyou_admin.ui.activity.notification.notificationdetails.NotificationDetailsActivity;
import com.marius.valeyou_admin.ui.activity.signup.SignupActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.reschedule.RescheduleActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.startjob.StartJobTimerActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.opendetails.CurrentJobDetailsActivity;
import com.marius.valeyou_admin.util.Constants;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

public class NotificationActivity extends AppActivity<ActivityNotificationBinding, NotificationActivityVM> {
    public static final String TAG = "NotificationActivity";
    NotificationAdapter notificationAdapter;
    List<NotificationModel> notificationModelList;
    int pos;

    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected BindingActivity<NotificationActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_notification, NotificationActivityVM.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getNotification(viewModel.sharedPref.getUserData().getAuthKey());
    }

    @Override
    protected void subscribeToEvents(NotificationActivityVM vm) {
        binding.header.tvTitle.setText(getResources().getString(R.string.notifications));

        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                finish(true);
            }
        });

        vm.notificationbean.observe(this, new SingleRequestEvent.RequestObserver<List<NotificationModel>>() {
            @Override
            public void onRequestReceived(@NonNull Resource<List<NotificationModel>> resource) {
                switch (resource.status) {
                    case LOADING:
                        binding.setCheck(true);
                        break;
                    case SUCCESS:
                        binding.setCheck(false);
                        notificationModelList = resource.data;
                        setRecyclerView(notificationModelList);
                        break;
                    case ERROR:
                        binding.setCheck(false);
                        break;
                    case WARN:
                        binding.setCheck(false);
                        vm.warn.setValue(resource.message);
                        break;
                }
            }
        });


        vm.readNotificationEvent.observe(this, new Observer<Resource<SimpleApiResponse>>() {
            @Override
            public void onChanged(Resource<SimpleApiResponse> simpleApiResponseResource) {
                switch (simpleApiResponseResource.status) {
                    case LOADING:
                        binding.setCheck(true);
                        break;
                    case SUCCESS:

                        binding.setCheck(false);
                        vm.success.setValue(simpleApiResponseResource.message);
                        notificationModelList.get(pos).setIsSeen(1);
                        notificationAdapter.notifyItemChanged(pos);

                        break;

                    case ERROR:
                        binding.setCheck(false);
                        vm.success.setValue(simpleApiResponseResource.message);
                        notificationModelList.get(pos).setIsSeen(1);
                        notificationAdapter.notifyItemChanged(pos);
                        break;
                    case WARN:
                        binding.setCheck(false);
                        vm.warn.setValue(simpleApiResponseResource.message);
                        break;
                }
            }
        });

        vm.rescheduleconfirmationEvent.observe(this, new SingleRequestEvent.RequestObserver<SimpleApiResponse>() {
            @Override
            public void onRequestReceived(@NonNull Resource<SimpleApiResponse> resource) {
                switch (resource.status) {
                    case LOADING:
                        binding.setCheck(true);
                        break;
                    case SUCCESS:
                        binding.setCheck(false);
                        vm.success.setValue(resource.message);
                       finish(true);
                        break;
                    case WARN:
                        binding.setCheck(false);
                        vm.warn.setValue(resource.message);
                        break;
                    case ERROR:
                        binding.setCheck(false);
                        vm.error.setValue(resource.message);
                        break;
                }
            }
        });

        vm.changeStatusEvent.observe(this, new Observer<Resource<SimpleApiResponse>>() {
            @Override
            public void onChanged(Resource<SimpleApiResponse> resource) {
                switch (resource.status) {
                    case LOADING:
                        binding.setCheck(true);
                        break;
                    case SUCCESS:
                        binding.setCheck(false);

                        vm.success.setValue(resource.message);
                        finish(true);

                        break;
                    case ERROR:
                        binding.setCheck(false);

                        if (resource.message.equalsIgnoreCase("unauthorised")) {

                            Intent intent1 = LoginActivity.newIntent(NotificationActivity.this);
                            startNewActivity(intent1, true, true);

                        } else {

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




    }

    private void setRecyclerView(List<NotificationModel> list) {
        if (list.size() > 0) {
            binding.noTxt.setVisibility(View.GONE);
            binding.rvNotification.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            binding.rvNotification.setLayoutManager(layoutManager);
            notificationAdapter = new NotificationAdapter(NotificationActivity.this, list, new NotificationAdapter.Listner() {
                @Override
                public void onItemClick(View v, int position, NotificationModel model) {

                    switch (v.getId()) {


                        case R.id.yesBtn:

                            yesConfirmation(String.valueOf(model.getOrder().getId()));

                            break;

                        case R.id.suggestTime:

                            Intent reIntent = RescheduleActivity.newIntent(NotificationActivity.this, model.getOrder().getId());
                            startActivity(reIntent);

                            break;

                        case R.id.cancelJob:
                            cancelJobConfirmation(String.valueOf(model.getOrder().getId()));
                            break;

                        case R.id.tv_read:
                            for (int i= 0; i < notificationModelList.size(); i++) {
                                if (notificationModelList.get(i).getIsSeen() != 1) {
                                    pos = i;
                                    viewModel.readNotification(sharedPref.getUserData().getAuthKey(), "1", notificationModelList.get(i).getId() + "");
                                    notificationModelList.get(pos).setIsSeen(1);
                                    notificationAdapter.notifyItemChanged(pos);
                                }
                            }
                            break;

                        case R.id.cv_items:
                            pos = position;
                            viewModel.readNotification(sharedPref.getUserData().getAuthKey(), "1", model.getId() + "");
                            notificationModelList.get(pos).setIsSeen(1);
                            notificationAdapter.notifyItemChanged(pos);
                            if (model.getOrder().getStatus() == 9) {

                                Intent intent = RatingActivity.newIntent(NotificationActivity.this);
                                intent.putExtra("comeForm", "compelted");
                                intent.putExtra("id", model.getOrder().getId());
                                startNewActivity(intent);

                            }else{

                                Intent intent = CurrentJobDetailsActivity.newIntent(NotificationActivity.this);
                                intent.putExtra("id", model.getOrder().getId());
                                startNewActivity(intent);
                            }

                            break;
                    }

                }
            });
            binding.rvNotification.setAdapter(notificationAdapter);

        } else {

            binding.noTxt.setVisibility(View.VISIBLE);
            binding.rvNotification.setVisibility(View.GONE);

        }


    }


    private BaseCustomDialog<HolderYesConfirmationBinding> YesConfirmDoalog;

    private void yesConfirmation(String id) {
        YesConfirmDoalog = new BaseCustomDialog<>(NotificationActivity.this, R.layout.holder_yes_confirmation, new BaseCustomDialog.Listener() {
            @Override
            public void onViewClick(View view) {
                if (view != null && view.getId() != 0) {
                    switch (view.getId()) {
                        case R.id.btn_submit:
                            YesConfirmDoalog.dismiss();
                            viewModel.rescheduleConfirmationApi(id);
                            break;
                        case R.id.btn_cancel:
                            YesConfirmDoalog.dismiss();
                            break;

                        case R.id.iv_cancel:
                            YesConfirmDoalog.dismiss();
                            break;
                    }
                }
            }
        });
        YesConfirmDoalog.getWindow().setBackgroundDrawableResource(R.color.white_trans);
        YesConfirmDoalog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        YesConfirmDoalog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        YesConfirmDoalog.show();
    }



    private BaseCustomDialog<HolderCancelJobDialogBinding> cancelJobConfirmationDialog;

    private void cancelJobConfirmation(String id) {
        cancelJobConfirmationDialog = new BaseCustomDialog<>(NotificationActivity.this, R.layout.holder_cancel_job_dialog, new BaseCustomDialog.Listener() {
            @Override
            public void onViewClick(View view) {
                if (view != null && view.getId() != 0) {
                    switch (view.getId()) {
                        case R.id.btn_submit:
                            if (cancelJobConfirmationDialog.getBinding().etReason.getText().toString().isEmpty()){
                                viewModel.error.setValue("Please enter reason of cancelling");
                            }else {
                                cancelJobConfirmationDialog.dismiss();
                                viewModel.changeJobStatus(sharedPref.getUserData().getAuthKey(), id, "1", "2", cancelJobConfirmationDialog.getBinding().etReason.getText().toString());
                            }
                            break;
                        case R.id.btn_cancel:
                            cancelJobConfirmationDialog.dismiss();
                            break;

                        case R.id.iv_cancel:
                            cancelJobConfirmationDialog.dismiss();
                            break;
                    }
                }
            }
        });
        cancelJobConfirmationDialog.getWindow().setBackgroundDrawableResource(R.color.white_trans);
        cancelJobConfirmationDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        cancelJobConfirmationDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        cancelJobConfirmationDialog.show();
    }

}
