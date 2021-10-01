package com.marius.valeyou_admin.ui.fragment.myjob.upcoming.opendetails;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.AddWorkModel;
import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.base.MoreBean;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.jobs.JobDetailModel;
import com.marius.valeyou_admin.data.beans.singninbean.SignInModel;
import com.marius.valeyou_admin.data.beans.startendjob.StartEndModel;
import com.marius.valeyou_admin.data.remote.helper.ApiUtils;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.AccessDialogBinding;
import com.marius.valeyou_admin.databinding.ActivityCurrentJobDetailsBinding;
import com.marius.valeyou_admin.databinding.ArrivedPopupBinding;
import com.marius.valeyou_admin.databinding.DialogDeleteAccouontBinding;
import com.marius.valeyou_admin.databinding.DialogEndJobBinding;
import com.marius.valeyou_admin.databinding.HolderAddWorkDialogBinding;
import com.marius.valeyou_admin.databinding.HolderCancelJobDialogBinding;
import com.marius.valeyou_admin.databinding.HolderConfirmationCodeBinding;
import com.marius.valeyou_admin.databinding.HolderJobsShedulerBinding;
import com.marius.valeyou_admin.databinding.HolderUpdateBidBinding;
import com.marius.valeyou_admin.databinding.NoAccessDialogBinding;
import com.marius.valeyou_admin.di.base.adapter.SimpleRecyclerViewAdapter;
import com.marius.valeyou_admin.di.base.dialog.BaseCustomDialog;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.ui.activity.RatingActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.message.chatview.ChatActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.activity.main.MainActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.addwork.AddWorkActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.addwork.additional.AdditionalActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.noaccess.NoAccessChargeActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.reschedule.RescheduleActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.startjob.StartJobTimerActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.closedetails.JobDetailsActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.opendetails.attachments.AttachmentsActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.opendetails.upload.UploadActivity;
import com.marius.valeyou_admin.util.AppUtils;
import com.marius.valeyou_admin.util.databinding.ImageViewBindingUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

public class CurrentJobDetailsActivity extends AppActivity<ActivityCurrentJobDetailsBinding, CurrentJobDetailsActivityVM> {

    int job_id;
    String auth_key;
    String confirmationCode;
    int job_type;
    String des, endprice;
    int total_amount;

    int id;
    int job_status;
    JobDetailModel model;

    int bid_price;

    String ON_ARRIVING = "3";
    String ON_ARRIVED = "4";
    String CODE_VERIFIED = "6";
    String START_JOB = "7";
    String END_JOB = "8";
    String COMPLETE_BY_USER = "9";

    int ispayment;

    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, CurrentJobDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected BindingActivity<CurrentJobDetailsActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_current_job_details, CurrentJobDetailsActivityVM.class);
    }

    private String convertTimeStampToTime(long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp * 1000L);
        String date = DateFormat.format("MMM dd, yyyy hh:mm a", cal).toString();
        return date;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getIntExtra("id", 0);
            if (id != 0) {
                auth_key = viewModel.sharedPref.getUserData().getAuthKey();
                viewModel.getJobDetaial(auth_key, id);
            }
        }

    }

    @Override
    protected void subscribeToEvents(CurrentJobDetailsActivityVM vm) {
        binding.header.tvTitle.setText(getResources().getString(R.string.current_job_detail));
        AppUtils.hideSoftKeyboard(this);

        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                supportFinishAfterTransition();
                onBackPressed(true);
            }
        });

        vm.base_click.observe(this, new Observer<View>() {
            @Override
            public void onChanged(View view) {
                Intent intent;
                switch (view.getId()) {

                    case R.id.cancel_job:

                        cancel_job_dialog();

                        break;

                    case R.id.additionalCost:
                        if (job_status == 9) {

                        } else {
                            Intent intent1 = AdditionalActivity.newIntent(CurrentJobDetailsActivity.this, id);
                            startNewActivity(intent1);
                        }
                        break;

                    case R.id.btn_start_end_job:

                        if (job_type == 0) {


                            if (ispayment == 0) {

                                vm.info.setValue("Payment is not done by user.");

                            } else if (ispayment == 2) {

                                if (job_status == 1) {
                                    // vm.info.setValue("Payment is  done by user.");
                                    dialogStartJob();
                                } else if (job_status == 7) {
                                    endJobDialog();
                                } else if (job_status == 8) {
                                    vm.info.setValue("Waiting For Review");
                                } else if (job_status == 9) {

                                    Intent ratingIntent = RatingActivity.newIntent(CurrentJobDetailsActivity.this);
                                    ratingIntent.putExtra("id", id);
                                    startNewActivity(ratingIntent);

                                }

                            }

                        } else if (job_type == 1) {

                            if (job_status == 1) {

                                vm.changeJobStatus(auth_key, String.valueOf(job_id), "1", ON_ARRIVING,"");

                            } else if (job_status == 2) {


                            } else if (job_status == 3) {

                                arrivedPopup();

                            } else if (job_status == 4) {

                                grantedAccessPopup();


                            } else if (job_status == 6) {


                                dialogStartJob();


                            } else if (job_status == 7) {

                                endJobDialog();

                            } else if (job_status == 8) {

                                if (ispayment == 0) {
                                    vm.info.setValue("Job Completed! Wating for payment");
                                } else {
                                    vm.info.setValue("Waiting for user review.");
                                }

                            } else if (job_status == 10) {

                                vm.info.setValue("Payment still not done by user.");

                            } else if (job_status == 9) {

                                Intent ratingIntent = RatingActivity.newIntent(CurrentJobDetailsActivity.this);
                                ratingIntent.putExtra("id", id);
                                startNewActivity(ratingIntent);

                            }

                        }
                        break;


                    case R.id.btn_update:

                        if (job_status == 7||job_status ==8||job_status==9||job_status==10||job_status==11) {

                            vm.info.setValue("You cannot edit the bid price now.");


                        } else {


                            updateBidDialog(String.valueOf(bid_price));

                        }


                        break;

                    case R.id.btn_add_extra:

                        if (job_status == 7) {

                            startNewActivity(AddWorkActivity.newIntent(CurrentJobDetailsActivity.this, job_id));

                        } else {

                            vm.info.setValue("You can not add exrta work");


                        }

                        break;


                    case R.id.btnAddImages:
                        Intent uploadIntent = UploadActivity.newIntent(CurrentJobDetailsActivity.this,job_id);
                        startActivity(uploadIntent);
                        break;

                    case R.id.uploadAttachments:
                        Intent attachIntent = AttachmentsActivity.newIntent(CurrentJobDetailsActivity.this,job_id);
                        startActivity(attachIntent);
                        break;


                    case R.id.reschedule_job:

                        if (job_status == 7 || job_status == 8 || job_status == 9) {

                            vm.info.setValue("You can not reschedule this job now.");

                        } else if (job_status == 10) {

                            if (model.getNo_access_charges().size() > 0) {
                                if (model.getIsSchedule() == 0) {

                                    startNewActivity(RescheduleActivity.newIntent(CurrentJobDetailsActivity.this, job_id));

                                } else if (model.getIsSchedule() == 1) {

                                    vm.info.setValue("User has sent you reschedule request. Please check your notifications");

                                } else if (model.getIsSchedule() == 2) {

                                    vm.info.setValue("Reschedule Request Pending");

                                }
                            }

                        } else {

                            if (model.getIsSchedule() == 0) {

                                startNewActivity(RescheduleActivity.newIntent(CurrentJobDetailsActivity.this, job_id));

                            } else if (model.getIsSchedule() == 1) {

                                vm.info.setValue("User has sent you reschedule request. Please check your notifications");

                            } else if (model.getIsSchedule() == 2) {

                                vm.info.setValue("Reschedule Request Pending");

                            }


                        }

                        break;

                    case R.id.iv_chat:

                        Intent in = ChatActivity.newIntent(CurrentJobDetailsActivity.this);
                        in.putExtra("comeFrom", "job");
                        in.putExtra("otherUserId", String.valueOf(model.getUser().getId()));
                        in.putExtra("otherUserName", model.getUser().getFirstName() + " " + model.getUser().getLastName());
                        in.putExtra("otherUserImage", model.getUser().getImage());
                        startNewActivity(in);

                        break;

                }
            }
        });

        vm.placeApiBean.observe(this, new Observer<Resource<SimpleApiResponse>>() {
            @Override
            public void onChanged(Resource<SimpleApiResponse> simpleApiResponseResource) {
                switch (simpleApiResponseResource.status) {
                    case LOADING:
                        showProgressDialog(R.string.plz_wait);
                        break;
                    case SUCCESS:
                        dismissProgressDialog();
                        vm.success.setValue(simpleApiResponseResource.message);
                        vm.getJobDetaial(auth_key, job_id);
                        break;

                    case ERROR:
                        dismissProgressDialog();
                        vm.error.setValue(simpleApiResponseResource.message);
                        if (simpleApiResponseResource.message.equalsIgnoreCase("unauthorised")) {

                            Intent intent1 = LoginActivity.newIntent(CurrentJobDetailsActivity.this);
                            startNewActivity(intent1, true, true);

                        }
                        break;
                    case WARN:
                        dismissProgressDialog();
                        vm.warn.setValue(simpleApiResponseResource.message);
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
                        binding.setVariable(BR.bean, resource.data);
                        model = resource.data;
                        job_id = resource.data.getId();
                        job_status = resource.data.getStatus();
                        bid_price = resource.data.getBid_price();
                        endprice = String.valueOf(resource.data.getEndPrice());
                        des = resource.data.getDescription();
                        confirmationCode = resource.data.getConfirmationCode();
                        job_type = resource.data.getJobType();
                        ispayment = resource.data.getIsPayment();
                        adapterJobScheduler.setList(resource.data.getOrder_activities());
                        binding.additionalCost.setText("Extra Cost : " + model.getTotal_additional_amount() + " Brl");


                        if (bid_price==0){
                            total_amount = Integer.parseInt(endprice);
                        }else{
                            total_amount = bid_price;
                        }

                        if (resource.data.getIsSchedule() == 0) {
                            if (resource.data.getDate() != null) {
                                String date = AppUtils.getDateTime(Long.parseLong(resource.data.getDate()));
                                binding.txtDate.setText(date);
                            }

                        } else {

                            String date = AppUtils.getDateTime(Long.parseLong(resource.data.getReScheduleDate()));
                            binding.txtDate.setText(date);

                        }

                        if (resource.data.getOrderImages().size() > 0) {
                            String image = resource.data.getOrderImages().get(0).getImages();
                            ImageViewBindingUtils.setImage(binding.jobImage, image);

                        } else {
                            binding.jobImage.setImageResource(R.drawable.new_placeholder);
                        }

                         if (bid_price == 0) {
                                binding.btnUpdate.setVisibility(View.GONE);
                            } else {
                                binding.btnUpdate.setVisibility(View.VISIBLE);
                            }

                        if (resource.data.getJobType() == 0) {

                            binding.options.setVisibility(View.GONE);

                            if (resource.data.getIsPayment() == 0) {

                                binding.btnStartEndJob.setText(getResources().getString(R.string.payment_pending));

                            } else if (resource.data.getIsPayment() == 1) {

                                binding.btnStartEndJob.setText(getResources().getString(R.string.start));

                            } else if (resource.data.getStatus() == 2) {
                                binding.btnStartEndJob.setText(getResources().getString(R.string.cancelled));
                                binding.btnStartEndJob.setBackgroundColor(getResources().getColor(R.color.red));
                            } else if (resource.data.getStatus() == 7) {

                                binding.uploadAttachments.setVisibility(View.VISIBLE);
                                binding.btnStartEndJob.setText(getResources().getString(R.string.endjob));

                            } else if (resource.data.getStatus() == 8) {
                                binding.btnStartEndJob.setText(getResources().getString(R.string.waiting_for_review));

                            } else if (resource.data.getStatus() == 9) {
                                binding.btnStartEndJob.setText(getResources().getString(R.string.view_rating));
                                binding.options.setVisibility(View.GONE);
                            }

                        } else if (resource.data.getJobType() == 1) {

                            if (resource.data.getStatus() == 1) {
                                binding.cancelJob.setVisibility(View.VISIBLE);
                                binding.btnStartEndJob.setText(getResources().getString(R.string.on_the_way));
                                binding.options.setVisibility(View.VISIBLE);
                            } else if (resource.data.getStatus() == 2) {
                                binding.btnStartEndJob.setVisibility(View.GONE);
                                binding.cancelJob.setVisibility(View.GONE);
                                binding.options.setVisibility(View.GONE);
                            } else if (resource.data.getStatus() == 3) {
                                binding.btnStartEndJob.setText(getResources().getString(R.string.arrived)+"?");
                                binding.cancelJob.setVisibility(View.VISIBLE);
                                binding.options.setVisibility(View.VISIBLE);
                            } else if (resource.data.getStatus() == 4) {
                                binding.btnStartEndJob.setText(getResources().getString(R.string.granted_access));
                                binding.cancelJob.setVisibility(View.VISIBLE);
                                binding.options.setVisibility(View.VISIBLE);
                            } else if (resource.data.getStatus() == 6) {
                                binding.btnAddImages.setVisibility(View.VISIBLE);
                                binding.btnStartEndJob.setText(getResources().getString(R.string.start));
                                binding.cancelJob.setVisibility(View.VISIBLE);
                                binding.options.setVisibility(View.VISIBLE);
                            } else if (resource.data.getStatus() == 7) {
                                binding.btnAddImages.setVisibility(View.VISIBLE);
                                binding.btnStartEndJob.setText(getResources().getString(R.string.endjob));
                                binding.cancelJob.setVisibility(View.VISIBLE);
                                binding.options.setVisibility(View.VISIBLE);
                            } else if (resource.data.getStatus() == 8) {
                                binding.btnAddImages.setVisibility(View.VISIBLE);
                                binding.options.setVisibility(View.VISIBLE);

                                if (ispayment == 0) {
                                    binding.btnStartEndJob.setText(getResources().getString(R.string.waiting_for_payment));
                                } else {
                                    binding.btnStartEndJob.setText(getResources().getString(R.string.waiting_for_review));

                                }
                                binding.cancelJob.setVisibility(View.GONE);

                            } else if (resource.data.getStatus() == 9) {
                                binding.btnAddImages.setVisibility(View.GONE);
                                binding.btnStartEndJob.setText(getResources().getString(R.string.view_rating));
                                binding.options.setVisibility(View.GONE);
                                binding.cancelJob.setVisibility(View.GONE);
                            } else if (resource.data.getStatus() == 10) {
                                binding.btnAddImages.setVisibility(View.GONE);
                                if (model.getNo_access_charges().size() > 0) {

                                    if (model.getNo_access_charges().get(0).getPaid() == 0) {

                                        binding.btnStartEndJob.setText(getResources().getString(R.string.waiting_for_payment));
                                        binding.btnStartEndJob.setVisibility(View.VISIBLE);
                                        binding.options.setVisibility(View.VISIBLE);

                                    } else {

                                        binding.btnStartEndJob.setVisibility(View.GONE);
                                        binding.options.setVisibility(View.VISIBLE);
                                        binding.rescheduleJob.setVisibility(View.VISIBLE);
                                        binding.btnAddExtra.setVisibility(View.GONE);
                                        binding.btnUpdate.setVisibility(View.GONE);
                                        binding.cancelJob.setVisibility(View.VISIBLE);

                                    }

                                }


                            } else {

                                binding.btnStartEndJob.setVisibility(View.GONE);

                            }




                        }

                        break;
                    case ERROR:
                        binding.setCheck(false);
                        vm.error.setValue(resource.message);
                        if (resource.message.equalsIgnoreCase("unauthorised")) {

                            Intent intent1 = LoginActivity.newIntent(CurrentJobDetailsActivity.this);
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

        vm.changeStatusEvent.observe(this, new Observer<Resource<ApiResponse<SimpleApiResponse>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<SimpleApiResponse>> resource) {
                switch (resource.status) {
                    case LOADING:
                        binding.setCheck(true);
                        break;
                    case SUCCESS:
                        binding.setCheck(false);

                        vm.success.setValue(resource.message);
                        vm.getJobDetaial(auth_key, job_id);

                        break;
                    case ERROR:
                        binding.setCheck(false);

                        if (resource.message.equalsIgnoreCase("unauthorised")) {

                            Intent intent1 = LoginActivity.newIntent(CurrentJobDetailsActivity.this);
                            startNewActivity(intent1, true, true);

                        } else {

                            AlertDialog alertDialog = new AlertDialog.Builder(CurrentJobDetailsActivity.this).create();
                            alertDialog.setTitle("Alert");
                            alertDialog.setMessage(resource.message);
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


        vm.checkConfirmationCodeEvent.observe(this, new Observer<Resource<ApiResponse<SimpleApiResponse>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<SimpleApiResponse>> resource) {
                switch (resource.status) {
                    case LOADING:
                        binding.setCheck(true);
                        break;
                    case SUCCESS:
                        binding.setCheck(false);

                        vm.success.setValue(resource.message);
                        vm.getJobDetaial(auth_key, job_id);

                        break;
                    case ERROR:
                        binding.setCheck(false);

                        if (resource.message.equalsIgnoreCase("unauthorised")) {

                            Intent intent1 = LoginActivity.newIntent(CurrentJobDetailsActivity.this);
                            startNewActivity(intent1, true, true);

                        } else {

                            AlertDialog alertDialog = new AlertDialog.Builder(CurrentJobDetailsActivity.this).create();
                            alertDialog.setTitle("Alert");
                            alertDialog.setMessage(resource.message);
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


        setJobShedulerStatus();

    }


    private BaseCustomDialog<DialogDeleteAccouontBinding> dialogStartEndJob;

    private void dialogStartJob() {
        dialogStartEndJob = new BaseCustomDialog<>(CurrentJobDetailsActivity.this, R.layout.dialog_start_job, new BaseCustomDialog.Listener() {
            @Override
            public void onViewClick(View view) {
                if (view != null && view.getId() != 0) {
                    switch (view.getId()) {
                        case R.id.btn_submit:
                            dialogStartEndJob.dismiss();
                            viewModel.changeJobStatus(auth_key, String.valueOf(job_id), "1", START_JOB,"");
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

    private BaseCustomDialog<HolderConfirmationCodeBinding> dialogConfirmationCode;

    private void confirmationDialog(String confirmationCode) {
        dialogConfirmationCode = new BaseCustomDialog<>(this, R.layout.holder_confirmation_code, new BaseCustomDialog.Listener() {
            @Override
            public void onViewClick(View view) {
                if (view != null && view.getId() != 0) {
                    switch (view.getId()) {
                        case R.id.btn_submit:

                            if (dialogConfirmationCode.getBinding().tvTwo.getText().toString().equalsIgnoreCase(confirmationCode)) {

                                dialogConfirmationCode.dismiss();
                                viewModel.changeJobStatus(auth_key, String.valueOf(job_id), "1", CODE_VERIFIED,"");

                            } else {

                                viewModel.error.setValue(getResources().getString(R.string.invalid_confirmation_code));

                            }

                            break;

                        case R.id.btn_cancel:
                            dialogConfirmationCode.dismiss();
                            break;
                    }
                }
            }
        });

        dialogConfirmationCode.setCancelable(false);
        dialogConfirmationCode.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialogConfirmationCode.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialogConfirmationCode.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogConfirmationCode.show();

    }

    private BaseCustomDialog<AccessDialogBinding> dialogGrantedAccess;

    private void grantedAccessPopup() {
        dialogGrantedAccess = new BaseCustomDialog<>(this, R.layout.access_dialog, new BaseCustomDialog.Listener() {
            @Override
            public void onViewClick(View view) {
                if (view != null && view.getId() != 0) {
                    switch (view.getId()) {
                        case R.id.btn_submit:
                            confirmationDialog(confirmationCode);
                            dialogGrantedAccess.dismiss();

                            break;

                        case R.id.btn_cancel:
                            dialogGrantedAccess.dismiss();

                            noAccessCharngeDialog(total_amount);

                            break;
                    }
                }
            }
        });

        dialogGrantedAccess.setCancelable(false);
        dialogGrantedAccess.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialogGrantedAccess.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialogGrantedAccess.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogGrantedAccess.show();

    }

    private BaseCustomDialog<ArrivedPopupBinding> dialogArrived;

    private void arrivedPopup() {
        dialogArrived = new BaseCustomDialog<>(this, R.layout.arrived_popup, new BaseCustomDialog.Listener() {
            @Override
            public void onViewClick(View view) {
                if (view != null && view.getId() != 0) {
                    switch (view.getId()) {
                        case R.id.btn_submit:
                            dialogArrived.dismiss();
                            viewModel.changeJobStatus(auth_key, String.valueOf(job_id), "1", ON_ARRIVED,"");
                            break;

                        case R.id.btn_cancel:
                            dialogArrived.dismiss();
                            break;
                    }
                }
            }
        });

        dialogArrived.setCancelable(false);
        dialogArrived.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialogArrived.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialogArrived.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogArrived.show();

    }


    private BaseCustomDialog<HolderUpdateBidBinding> dilaogUpdate;

    private void updateBidDialog(String bidPrice) {
        dilaogUpdate = new BaseCustomDialog<>(this, R.layout.holder_update_bid, new BaseCustomDialog.Listener() {
            @Override
            public void onViewClick(View view) {
                if (view != null && view.getId() != 0) {
                    switch (view.getId()) {
                        case R.id.btn_update:

                            int price = Integer.parseInt(dilaogUpdate.getBinding().bidPrice.getText().toString().trim());

                            if (dilaogUpdate.getBinding().bidPrice.getText().toString().isEmpty()) {
                                viewModel.error.setValue(getResources().getString(R.string.please_enter_bid_price));
                            } else {
                                dilaogUpdate.dismiss();
                                viewModel.updateBid(auth_key, job_id, price);

                            }

                            break;

                        case R.id.btn_cancel:
                            dilaogUpdate.dismiss();
                            break;
                    }
                }
            }
        });

        dilaogUpdate.getBinding().bidPrice.setText(bidPrice);
        dilaogUpdate.setCancelable(false);
        dilaogUpdate.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dilaogUpdate.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dilaogUpdate.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dilaogUpdate.show();

    }

    private void setJobShedulerStatus() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvjobSheduler.setLayoutManager(layoutManager);
        binding.rvjobSheduler.setAdapter(adapterJobScheduler);
    }


    SimpleRecyclerViewAdapter<JobDetailModel.OrderActivitesBean, HolderJobsShedulerBinding> adapterJobScheduler =
            new SimpleRecyclerViewAdapter(R.layout.holder_jobs_sheduler, BR.bean, new SimpleRecyclerViewAdapter.SimpleCallback<JobDetailModel.OrderActivitesBean>() {
                @Override
                public void onItemClick(View v, JobDetailModel.OrderActivitesBean moreBean) {


                }
            });


    private BaseCustomDialog<DialogEndJobBinding> dialogEndJob;

    private void endJobDialog() {
        dialogEndJob = new BaseCustomDialog<>(CurrentJobDetailsActivity.this, R.layout.dialog_end_job, new BaseCustomDialog.Listener() {
            @Override
            public void onViewClick(View view) {
                if (view != null && view.getId() != 0) {
                    switch (view.getId()) {
                        case R.id.btn_submit:
                            viewModel.changeJobStatus(auth_key, String.valueOf(job_id), "1", END_JOB,"");
                            dialogEndJob.dismiss();
                            break;
                        case R.id.btn_cancel:
                            dialogEndJob.dismiss();
                            break;
                    }
                }
            }
        });
        dialogEndJob.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialogEndJob.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialogEndJob.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogEndJob.show();
    }


    private BaseCustomDialog<NoAccessDialogBinding> noAccessDialog;

    private void noAccessCharngeDialog(int total) {
        noAccessDialog = new BaseCustomDialog<>(this, R.layout.no_access_dialog, new BaseCustomDialog.Listener() {
            @Override
            public void onViewClick(View view) {
                if (view != null && view.getId() != 0) {
                    switch (view.getId()) {

                        case R.id.btn_submit:

                            noAccessDialog.dismiss();
                            startNewActivity(NoAccessChargeActivity.newIntent(CurrentJobDetailsActivity.this, job_id,total));

                            break;

                        case R.id.btn_cancel:

                            noAccessDialog.dismiss();

                            break;

                        case R.id.imgClose:
                            noAccessDialog.dismiss();
                            break;
                    }
                }
            }
        });

        noAccessDialog.setCancelable(true);
        noAccessDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        noAccessDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        noAccessDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        noAccessDialog.show();

    }

    private BaseCustomDialog<HolderCancelJobDialogBinding> cancelJobDialog;

    private void cancel_job_dialog() {
        cancelJobDialog = new BaseCustomDialog<>(this, R.layout.holder_cancel_job_dialog, new BaseCustomDialog.Listener() {
            @Override
            public void onViewClick(View view) {
                if (view != null && view.getId() != 0) {
                    switch (view.getId()) {
                        case R.id.btn_submit:
                            if (cancelJobDialog.getBinding().etReason.getText().toString().isEmpty()){
                                viewModel.error.setValue("Please enter reason of cancelling job");

                            }else{

                                cancelJobDialog.dismiss();
                                viewModel.changeJobStatus(auth_key, String.valueOf(job_id), "1", "2",cancelJobDialog.getBinding().etReason.getText().toString());

                            }

                            break;

                        case R.id.btn_cancel:
                            cancelJobDialog.dismiss();
                            break;

                    }
                }
            }
        });

        cancelJobDialog.setCancelable(true);
        cancelJobDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        cancelJobDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        cancelJobDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        cancelJobDialog.show();

    }


}


