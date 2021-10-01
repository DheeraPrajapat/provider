package com.marius.valeyou_admin.ui.fragment.myjob.noaccess;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.ComissionModel;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityNoAccessChargeBinding;
import com.marius.valeyou_admin.di.base.view.BaseActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.addwork.AddWorkActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.addwork.AddWorkActivityVM;

public class NoAccessChargeActivity extends BaseActivity<ActivityNoAccessChargeBinding,NoAccessChargeActivityVM> {

    int job_id;
    int total;
    public static Intent newIntent(Activity activity, int job_id, int total_amount) {
        Intent intent = new Intent(activity, NoAccessChargeActivity.class);
        intent.putExtra("id",job_id);
        intent.putExtra("total",total_amount);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected BindingActivity<NoAccessChargeActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_no_access_charge, NoAccessChargeActivityVM.class);
    }

    @Override
    protected void subscribeToEvents(NoAccessChargeActivityVM vm) {

         job_id = getIntent().getIntExtra("id",0);
          total = getIntent().getIntExtra("total",0);


          vm.getNoAccessChargeAmount();

        binding.header.tvTitle.setText("Add No Access Charge");

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
                    case R.id.sendBtn:

                        if (binding.etPrice.getText().toString().isEmpty()){
                            vm.error.setValue(getResources().getString(R.string.enter_price));
                        }else if (binding.etDes.getText().toString().isEmpty()){
                            vm.error.setValue(getResources().getString(R.string.enter_reason));
                        }else{
                            vm.addNoAccessCharge(vm.sharedPref.getUserData().getAuthKey(),
                                    String.valueOf(job_id),
                                    binding.etDes.getText().toString(),
                                    binding.etPrice.getText().toString());
                        }

                        break;
                }
            }
        });

        vm.addNoAccessChargeEvent.observe(this, new Observer<Resource<SimpleApiResponse>>() {
            @Override
            public void onChanged(Resource<SimpleApiResponse> simpleApiResponseResource) {
                switch (simpleApiResponseResource.status) {
                    case LOADING:
                        showProgressDialog(R.string.plz_wait);
                        break;
                    case SUCCESS:
                        dismissProgressDialog();
                        vm.success.setValue(simpleApiResponseResource.message);
                        finish(true);
                        break;

                    case ERROR:
                        dismissProgressDialog();
                        vm.error.setValue(simpleApiResponseResource.message);
                        if (simpleApiResponseResource.message.equalsIgnoreCase("unauthorised")) {

                            Intent intent1 = LoginActivity.newIntent(NoAccessChargeActivity.this);
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

        vm.getNoAccessChargeAmountEvent.observe(this, new Observer<Resource<ComissionModel>>() {
            @Override
            public void onChanged(Resource<ComissionModel> simpleApiResponseResource) {
                switch (simpleApiResponseResource.status) {
                    case LOADING:
                        showProgressDialog(R.string.plz_wait);
                        break;
                    case SUCCESS:
                        dismissProgressDialog();
                        int charge = Integer.parseInt(simpleApiResponseResource.data.getComission());

                       double finalcharge = charge*total/100f;

                       binding.etPrice.setText(String.format("%.2f", finalcharge));

                        break;

                    case ERROR:
                        dismissProgressDialog();
                        vm.error.setValue(simpleApiResponseResource.message);
                        if (simpleApiResponseResource.message.equalsIgnoreCase("unauthorised")) {

                            Intent intent1 = LoginActivity.newIntent(NoAccessChargeActivity.this);
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


    }
}