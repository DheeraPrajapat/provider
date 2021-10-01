package com.marius.valeyou_admin.ui.activity.forgot.recoverpass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.databinding.ActivityRecoverPasswordBinding;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;

import androidx.lifecycle.Observer;

public class RecoverPasswordActivity extends AppActivity<ActivityRecoverPasswordBinding, RecoverPasswordActivityVM> {

    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, RecoverPasswordActivity.class);
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
    protected BindingActivity<RecoverPasswordActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_recover_password, RecoverPasswordActivityVM.class);
    }

    @Override
    protected void subscribeToEvents(RecoverPasswordActivityVM vm) {

        vm.base_click.observe(this, new Observer<View>() {
            @Override
            public void onChanged(View view) {
                switch (view != null ? view.getId() : 0) {
                    case R.id.btn_done:
                        Intent intent = LoginActivity.newIntent(RecoverPasswordActivity.this);
                        startNewActivity(intent);
                        finishAffinity();






                        break;
                }
            }
        });

    }


}
