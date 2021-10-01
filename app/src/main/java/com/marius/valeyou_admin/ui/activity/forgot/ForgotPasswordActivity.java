package com.marius.valeyou_admin.ui.activity.forgot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.forgot.ForgotPasswordModel;
import com.marius.valeyou_admin.data.beans.singninbean.SignInModel;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityForgotpasswordBinding;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.ui.activity.forgot.recoverpass.RecoverPasswordActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.activity.main.MainActivity;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;

import java.net.HttpURLConnection;

import androidx.annotation.NonNull;

public class ForgotPasswordActivity extends AppActivity<ActivityForgotpasswordBinding, ForgotPasswordActivityVM> {

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+.+[a-z]+";
    @Override
    protected BindingActivity<ForgotPasswordActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_forgotpassword, ForgotPasswordActivityVM.class);
    }

    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, ForgotPasswordActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
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
    protected void subscribeToEvents(final ForgotPasswordActivityVM vm) {
        vm.base_click.observe(this, view -> {
            if (view == null)
                return;
            switch (view != null ? view.getId() : 0) {
                case R.id.btn_done:
                    if (binding.etEmail.getText().toString().isEmpty()){

                        vm.info.setValue(getResources().getString(R.string.enter_registered_email));

                    } if (!binding.etEmail.getText().toString().matches(emailPattern)){

                    vm.info.setValue(getResources().getString(R.string.valid_email));

                }else {

                        vm.forgot(binding.etEmail.getText().toString());

                    }

                    break;
            }
        });

        vm.forgotPasswordEvent.observe(this, new SingleRequestEvent.RequestObserver<ForgotPasswordModel>() {
            @Override
            public void onRequestReceived(@NonNull Resource<ForgotPasswordModel> resource) {
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
                            Intent intent1 = LoginActivity.newIntent(ForgotPasswordActivity.this);
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
    }
}
