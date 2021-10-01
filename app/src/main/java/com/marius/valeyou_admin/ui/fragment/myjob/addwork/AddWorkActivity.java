package com.marius.valeyou_admin.ui.fragment.myjob.addwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.WorkSource;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.AddWorkModel;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.map.MapListModel;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityAddWorkBinding;
import com.marius.valeyou_admin.databinding.HolderAddWorkDialogBinding;
import com.marius.valeyou_admin.databinding.HolderConfirmationCodeBinding;
import com.marius.valeyou_admin.di.base.dialog.BaseCustomDialog;
import com.marius.valeyou_admin.di.base.view.BaseActivity;
import com.marius.valeyou_admin.ui.activity.RatingActivity;
import com.marius.valeyou_admin.ui.activity.RatingActivityVM;
import com.marius.valeyou_admin.ui.activity.bids.PlacedBidsAdapter;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.opendetails.CurrentJobDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class AddWorkActivity extends BaseActivity<ActivityAddWorkBinding,AddWorkActivityVM> {

    List<AddWorkModel> workList = new ArrayList<>();
    AddWorkAdapter addWorkAdapter;
    int job_id;

    public static Intent newIntent(Activity activity, int job_id) {
        Intent intent = new Intent(activity, AddWorkActivity.class);
        intent.putExtra("id",job_id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected BindingActivity<AddWorkActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_add_work, AddWorkActivityVM.class);
    }

    @Override
    protected void subscribeToEvents(AddWorkActivityVM vm) {

         job_id = getIntent().getIntExtra("id",0);

        binding.header.tvTitle.setText("Add Extra Work");
        binding.header.setCheck(true);
        binding.header.tvTwo.setText("SAVE");

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
                    case R.id.btnAdd:

                        addWorkDialog();

                        break;

                    case R.id.tv_two:

                        String jsonData = getWorkJson(getList());
                        Log.d("JsonData"," : "+jsonData);

                        if (jsonData!=null||!jsonData.isEmpty()){

                            vm.addExtraWork(vm.sharedPref.getUserData().getAuthKey(), String.valueOf(job_id),jsonData);

                        }

                        break;
                }
            }
        });

        vm.addExtraWorkEvent.observe(this, new Observer<Resource<SimpleApiResponse>>() {
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

                            Intent intent1 = LoginActivity.newIntent(AddWorkActivity.this);
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

        setupAdapter(workList);

    }

    private BaseCustomDialog<HolderAddWorkDialogBinding> dialogAddWork;

    private void addWorkDialog() {
        dialogAddWork = new BaseCustomDialog<>(this, R.layout.holder_add_work_dialog, new BaseCustomDialog.Listener() {
            @Override
            public void onViewClick(View view) {
                if (view != null && view.getId() != 0) {
                    switch (view.getId()) {
                        case R.id.btn_submit:

                            String title = dialogAddWork.getBinding().tvTwo.getText().toString();
                            String price = dialogAddWork.getBinding().tvFour.getText().toString();

                            addWorkAdapter.updateList(new AddWorkModel(title,price));

                            dialogAddWork.dismiss();
                            break;

                        case R.id.btn_cancel:
                            dialogAddWork.dismiss();
                            break;
                    }
                }
            }
        });

        dialogAddWork.setCancelable(false);
        dialogAddWork.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialogAddWork.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialogAddWork.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogAddWork.show();

    }


    private void setupAdapter(List<AddWorkModel> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
         addWorkAdapter = new AddWorkAdapter(AddWorkActivity.this, list, new AddWorkAdapter.ItemCallBack() {
            @Override
            public void onItemClick(View v, List<AddWorkModel> list, int pos) {
                addWorkAdapter.remove(pos);
            }
        });
        binding.rvWork.setLayoutManager(layoutManager);
        binding.rvWork.setAdapter(addWorkAdapter);

    }

    private  List<AddWorkModel> getList(){
        return addWorkAdapter.getList();
    }

    public  String getWorkJson(List<AddWorkModel> myList) {
        Gson gson = new Gson();
        String jsonCartList = gson.toJson(myList);
        return jsonCartList;

    }

}