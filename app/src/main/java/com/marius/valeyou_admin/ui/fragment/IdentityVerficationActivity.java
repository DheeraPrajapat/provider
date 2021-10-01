package com.marius.valeyou_admin.ui.fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.IdentityModel;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.suggesstions.SuggesstionModel;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityIdentityBinding;
import com.marius.valeyou_admin.databinding.ActivityIdentityVerficationBinding;
import com.marius.valeyou_admin.databinding.DialogDeleteAccouontBinding;
import com.marius.valeyou_admin.databinding.HolderFilterCategoryBinding;
import com.marius.valeyou_admin.databinding.HolderIdentityCardBinding;
import com.marius.valeyou_admin.di.base.adapter.SimpleRecyclerViewAdapter;
import com.marius.valeyou_admin.di.base.dialog.BaseCustomDialog;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.ProfileActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.ProfileActivityVM;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.indentity.IdentityActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.fragment.home.HomeFragment;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;

import java.util.List;

public class IdentityVerficationActivity extends AppActivity<ActivityIdentityVerficationBinding,IdentityVerficationActivityVM> {

    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, IdentityVerficationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected BindingActivity<IdentityVerficationActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_identity_verfication, IdentityVerficationActivityVM.class);
    }

    @Override
    protected void subscribeToEvents(IdentityVerficationActivityVM vm) {
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
                    case R.id.imgAdd:
                        Intent intent = IdentityActivity.newIntent(IdentityVerficationActivity.this);
                        intent.putExtra("comeFrom","add");
                        startNewActivity(intent);
                        break;
                }
            }
        });

        vm.getIdentity(sharedPref.getUserData().getAuthKey());

        vm.identityBean.observe(this, new SingleRequestEvent.RequestObserver<List<IdentityModel>>() {
            @Override
            public void onRequestReceived(@NonNull Resource<List<IdentityModel>> resource) {

                switch (resource.status) {
                    case LOADING:
                        binding.setCheck(true);
                        break;
                    case SUCCESS:
                        binding.setCheck(false);
                        List<IdentityModel> identityModelList = resource.data;
                        if (identityModelList.size()>0) {
                            binding.noIdentity.setVisibility(View.GONE);
                            binding.rvIdentity.setVisibility(View.VISIBLE);
                            adapterIdentity.setList(identityModelList);
                        }else{
                            binding.noIdentity.setVisibility(View.VISIBLE);
                            binding.rvIdentity.setVisibility(View.GONE);
                        }
                        break;
                    case ERROR:
                        binding.setCheck(false);
                        vm.error.setValue(resource.message);
                        if (resource.message.equalsIgnoreCase(getResources().getString(R.string.unauthorised))){
                           sharedPref.deleteAll();
                            Intent intent1 = LoginActivity.newIntent(IdentityVerficationActivity.this);
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

        vm.deleteApiBean.observe(this, new SingleRequestEvent.RequestObserver<SimpleApiResponse>() {
            @Override
            public void onRequestReceived(@NonNull Resource<SimpleApiResponse> resource) {

                switch (resource.status) {
                    case LOADING:
                        binding.setCheck(true);
                        break;
                    case SUCCESS:
                        binding.setCheck(false);
                        vm.success.setValue(resource.message);
                        vm.getIdentity(sharedPref.getUserData().getAuthKey());

                        break;
                    case ERROR:
                        binding.setCheck(false);
                        if (resource.message.equalsIgnoreCase(getResources().getString(R.string.unauthorised))){
                            sharedPref.deleteAll();
                            Intent intent1 = LoginActivity.newIntent(IdentityVerficationActivity.this);
                            startNewActivity(intent1, true, true);

                        }
                        vm.error.setValue(resource.message);

                        break;
                    case WARN:
                        binding.setCheck(false);
                        vm.warn.setValue(resource.message);

                        break;
                }
            }
        });


        setUpAdapter();

    }


    SimpleRecyclerViewAdapter<IdentityModel, HolderIdentityCardBinding> adapterIdentity =
            new SimpleRecyclerViewAdapter(R.layout.holder_identity_card, BR.bean, new SimpleRecyclerViewAdapter.SimpleCallback<IdentityModel>() {
                @Override
                public void onItemClick(View v, IdentityModel o) {

                    switch (v.getId()){
                        case R.id.iv_delete:

                            String key = viewModel.sharedPref.getUserData().getAuthKey();
                            dialogDeleteItem(key,o.getId(),"i");


                            break;


                        case R.id.iv_edit:
                            Intent intent = IdentityActivity.newIntent(IdentityVerficationActivity.this);
                            intent.putExtra("comeFrom","edit");
                            intent.putExtra("IdentityData",o);
                            startNewActivity(intent);
                            break;
                    }

                }
            });

    private void setUpAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvIdentity.setLayoutManager(layoutManager);
        binding.rvIdentity.setAdapter(adapterIdentity);

    }


    private BaseCustomDialog<DialogDeleteAccouontBinding> deleteItemDialog;
    private void dialogDeleteItem(String key,int id,String type) {
        deleteItemDialog = new BaseCustomDialog<>(IdentityVerficationActivity.this, R.layout.delete_confirmation_dialog, new BaseCustomDialog.Listener() {
            @Override
            public void onViewClick(View view) {
                if (view != null && view.getId() != 0) {
                    switch (view.getId()) {
                        case R.id.btn_submit:


                                viewModel.deleteIdentity(key,id);


                            deleteItemDialog.dismiss();
                            break;
                        case R.id.btn_cancel:
                            deleteItemDialog.dismiss();
                            break;
                    }
                }
            }
        });

        deleteItemDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        deleteItemDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        deleteItemDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        deleteItemDialog.show();

    }

}