package com.marius.valeyou_admin.ui.activity.welcome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import androidx.core.app.ActivityOptionsCompat;

import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.repo.WelcomeRepo;
import com.marius.valeyou_admin.databinding.ActivityWelcomeBinding;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.activity.main.MainActivity;
import com.marius.valeyou_admin.ui.activity.tourpage.TourActivity;

public class WelcomeActivity extends AppActivity<ActivityWelcomeBinding, WelcomeActivityVM> {

    @Override
    protected BindingActivity<WelcomeActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_welcome, WelcomeActivityVM.class);
    }

    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onConnectionRefresh(boolean connected) {
        if (connected) {
            Intent intent = TourActivity.newInstance(WelcomeActivity.this);
            viewModel.nextUi(intent, 1);
        }
    }


    @Override
    protected void subscribeToEvents(final WelcomeActivityVM vm) {

        if (viewModel.sharedPref.getUserData()!=null){

            Log.d("UserDetail : ",viewModel.sharedPref.getUserData().getId()+"==="+viewModel.sharedPref.getUserData().getAuthKey());

            Intent intent = MainActivity.newIntent(WelcomeActivity.this);
            viewModel.nextUi(intent, 1);

        }else{

            passIntent();

        }




        vm.clk_login.observe(this, aVoid -> {

        });

        vm.obr_nextui.observe(this, intent -> {
            startNewActivity(intent, true);
        });
    }

    private void passIntent() {
        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);

        if (!sharedpreferences.getBoolean("PreventToLaunch", false)) {

            Intent intent = TourActivity.newInstance(WelcomeActivity.this);
            viewModel.nextUi(intent, 1);

        }else{

            Intent intent = LoginActivity.newIntent(WelcomeActivity.this);
            viewModel.nextUi(intent, 1);

        }

    }


}

