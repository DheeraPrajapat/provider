package com.marius.valeyou_admin.ui.fragment.myjob.upcoming.complete;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;

import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.invoice.InvoiceModel;
import com.marius.valeyou_admin.data.beans.jobs.JobDetailModel;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityCompleteJobBinding;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.activity.main.MainActivity;
import com.marius.valeyou_admin.ui.activity.signup.uploaddocument.UploadDocumentActivityVM;
import com.marius.valeyou_admin.ui.fragment.myjob.startjob.StartJobTimerActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.opendetails.CurrentJobDetailsActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CompleteJobActivity extends AppActivity<ActivityCompleteJobBinding,CompleteJobActivityVM> {

    int id;

    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, CompleteJobActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected BindingActivity<CompleteJobActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_complete_job, CompleteJobActivityVM.class);

    }

    @Override
    protected void subscribeToEvents(CompleteJobActivityVM vm) {
        binding.header.tvTitle.setText(getResources().getString(R.string.detail));

        Intent intent = getIntent();
        if (intent!=null){

            id = intent.getIntExtra("id",0);
            String authKey = viewModel.sharedPref.getUserData().getAuthKey();

            Log.d("Invoice : ", id+"=="+authKey);

            viewModel.getInvoice(authKey,id);

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
            switch (view.getId()){
                case R.id.homeBtn:
                   finish(true);
                    break;
            }
            }
        });

        vm.invoiceEvent.observe(this, new Observer<Resource<InvoiceModel>>() {
            @Override
            public void onChanged(Resource<InvoiceModel> resource) {
                switch (resource.status) {
                    case LOADING:
                        binding.setCheck(true);
                        break;
                    case SUCCESS:
                        binding.setCheck(false);
                        binding.setVariable(BR.bean,resource.data);

                        if (resource.data.getType()==1){
                            binding.cvBillingDetail.setVisibility(View.VISIBLE);
                        }else{
                            binding.cvBillingDetail.setVisibility(View.GONE);
                        }
                        break;
                    case ERROR:
                        binding.setCheck(false);
                        vm.error.setValue(resource.message);
                        if (resource.message.equalsIgnoreCase("unauthorised")){

                            Intent intent1 = LoginActivity.newIntent(CompleteJobActivity.this);
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

    private String convertTimeStampToTime(long timestamp){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp * 1000L);
        String date = DateFormat.format("MMM dd, yyyy hh:mm a", cal).toString();
        return date;
    }

    private long timeDiference(String startJobTime, String endTimeJob,ActivityCompleteJobBinding completeJobBinding,Resource<JobDetailModel> resource){
        Date start = null;
        Date end = null;
        long diff = 0;
        try {

            start = new SimpleDateFormat("MMM dd, yyyy hh:mm a").parse(startJobTime);
            end = new SimpleDateFormat("MMM dd, yyyy hh:mm a").parse(endTimeJob);

             Date today = new Date();
             diff = end.getTime() - start.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return diff;
    }

/*

    int numOfDays = (int) (diff / (1000 * 60 * 60 * 24));
    int hours = (int) (diff / (1000 * 60 * 60));
    int minutes = (int) (hours / (60));
    int seconds = (int) (minutes / 60);

            completeJobBinding.hours.setText(hours);
            completeJobBinding.minutes.setText(minutes);
            completeJobBinding.seconds.setText(seconds);

    int bidPrice = resource.data.getBid_price();
            if (hours>0){
        if (minutes>10) {
            hours = hours+1;
            int price_total = bidPrice * hours;
            binding.grandTotal.setText(price_total);
            binding.subTotal.setText(price_total);
        }else{
            int price_total = bidPrice * hours;
            binding.grandTotal.setText(price_total);
            binding.subTotal.setText(price_total);
        }
    }else if (minutes>0&&minutes<60){
        binding.grandTotal.setText(bidPrice*1);
        binding.subTotal.setText(bidPrice*1);
    }
*/

}
