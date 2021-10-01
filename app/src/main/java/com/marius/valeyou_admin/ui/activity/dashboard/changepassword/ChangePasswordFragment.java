package com.marius.valeyou_admin.ui.activity.dashboard.changepassword;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.change.ChangePasswordModel;
import com.marius.valeyou_admin.data.beans.singninbean.SignInModel;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.FragmentChangePasswordBinding;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.activity.main.MainActivity;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

public class ChangePasswordFragment extends AppActivity<FragmentChangePasswordBinding, ChangePasswordFragmentVM> {
    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, ChangePasswordFragment.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }
    //Convert to activity

    @Override
    protected BindingActivity<ChangePasswordFragmentVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.fragment_change_password, ChangePasswordFragmentVM.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
    }

    @Override
    protected void subscribeToEvents(ChangePasswordFragmentVM vm) {
        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                finish(true);
            }
        });

        vm.changePasswordEvent.observe(this, new SingleRequestEvent.RequestObserver<ChangePasswordModel>() {
            @Override
            public void onRequestReceived(@NonNull Resource<ChangePasswordModel> resource) {
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

                        if (resource.message.equalsIgnoreCase("bad request")){
                            vm.error.setValue(getResources().getString(R.string.old_password_dosent_match));
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

        vm.base_click.observe(this, new Observer<View>() {
            @Override
            public void onChanged(View view) {
                if (view == null)
                    return;
                switch (view != null ? view.getId() : 0) {

                    case R.id.btn_done:

                        String oldPassword = binding.etOldPassword.getText().toString();
                        String newPassword = binding.etNewPassword.getText().toString();
                        String cnfPassword = binding.etCnfPassword.getText().toString();

                        if (oldPassword.isEmpty()){
                            vm.info.setValue("Enter your old password!");
                        }else if (newPassword.isEmpty()){
                            vm.info.setValue("Enter your new password!");
                        }else if (newPassword.length()<6){
                            vm.info.setValue(getResources().getString(R.string.enter_min_password));
                        }else if (!cnfPassword.matches(newPassword)){
                            vm.info.setValue(getResources().getString(R.string.both_password_must_be_matched));
                        }else{
                            vm.changePasswordApi(vm.sharedPref.getUserData().getAuthKey(),
                                    oldPassword,newPassword);
                        }
                        break;


                }
            }
        });
    }
}
