package com.marius.valeyou_admin.ui.fragment.myjob.addwork.additional;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.jobs.JobDetailModel;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityAdditionalBinding;
import com.marius.valeyou_admin.databinding.HolderAdditionalWorkListBinding;
import com.marius.valeyou_admin.di.base.adapter.SimpleRecyclerViewAdapter;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.addwork.AddWorkActivity;

public class AdditionalActivity extends AppActivity<ActivityAdditionalBinding,AdditionalActivityVM> {
    int job_id;
    JobDetailModel model;

    public static Intent newIntent(Activity activity, int job_id) {
        Intent intent = new Intent(activity, AdditionalActivity.class);
        intent.putExtra("id",job_id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected BindingActivity<AdditionalActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_additional, AdditionalActivityVM.class);
    }
    @Override
    protected void subscribeToEvents(AdditionalActivityVM vm) {
        job_id = getIntent().getIntExtra("id",0);
        binding.header.tvTitle.setText("Additional Cost");
        vm.getJobDetaial(sharedPref.getUserData().getAuthKey(),job_id);
        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                finish(true);
            }
        });

        vm.base_click.observe(this, new Observer<View>() {
            @Override
            public void onChanged(View view) {

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
                        binding.setVariable(BR.bean, resource.data);
                        model = resource.data;

                        if (model.getAdditional_works().size()>0) {

                            adapter.setList(model.getAdditional_works());

                        }

                        break;
                    case ERROR:
                        dismissProgressDialog();
                        vm.error.setValue(resource.message);
                        if (resource.message.equalsIgnoreCase("unauthorised")) {

                            Intent intent1 = LoginActivity.newIntent(AdditionalActivity.this);
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

        vm.deleteCostEvent.observe(this, new Observer<Resource<ApiResponse<SimpleApiResponse>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<SimpleApiResponse>> simpleApiResponseResource) {
                switch (simpleApiResponseResource.status) {
                    case LOADING:
                        showProgressDialog(R.string.plz_wait);
                        break;
                    case SUCCESS:
                        dismissProgressDialog();
                        vm.success.setValue(simpleApiResponseResource.message);
                        vm.getJobDetaial(sharedPref.getUserData().getAuthKey(),job_id);
                        break;

                    case ERROR:
                        dismissProgressDialog();
                        vm.error.setValue(simpleApiResponseResource.message);
                        if (simpleApiResponseResource.message.equalsIgnoreCase("unauthorised")) {

                            Intent intent1 = LoginActivity.newIntent(AdditionalActivity.this);
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


        setRecyclerView();

    }

    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(AdditionalActivity.this);
        binding.rvWork.setLayoutManager(layoutManager);
        binding.rvWork.setAdapter(adapter);
    }


    SimpleRecyclerViewAdapter<JobDetailModel.AdditionalWorksBean, HolderAdditionalWorkListBinding> adapter = new SimpleRecyclerViewAdapter(R.layout.holder_additional_work_list, BR.bean,
            new SimpleRecyclerViewAdapter.SimpleCallback<JobDetailModel.AdditionalWorksBean>() {
                @Override
                public void onItemClick(View v, JobDetailModel.AdditionalWorksBean moreBean) {

                    switch (v.getId()) {
                        case R.id.delete_btn:
                            viewModel.deleteCost(sharedPref.getUserData().getAuthKey(),moreBean.getId());
                            break;

                    }

                }
            });

}