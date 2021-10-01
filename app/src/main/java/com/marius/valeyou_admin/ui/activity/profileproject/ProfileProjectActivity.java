package com.marius.valeyou_admin.ui.activity.profileproject;

import android.app.Activity;
import android.content.Intent;

import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.databinding.ActivityProfileProjectBinding;
import com.marius.valeyou_admin.di.base.view.AppActivity;

import androidx.lifecycle.Observer;

public class ProfileProjectActivity extends AppActivity<ActivityProfileProjectBinding,ProfileProjectActivityVM> {

    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity,ProfileProjectActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected BindingActivity<ProfileProjectActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_profile_project,ProfileProjectActivityVM.class);
    }

    @Override
    protected void subscribeToEvents(ProfileProjectActivityVM vm) {
        binding.header.tvTitle.setText(getResources().getString(R.string.add_portfolio));
        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                finish(true);
            }
        });
    }
}
