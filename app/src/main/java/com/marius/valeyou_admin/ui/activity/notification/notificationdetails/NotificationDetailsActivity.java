package com.marius.valeyou_admin.ui.activity.notification.notificationdetails;

import android.app.Activity;
import android.content.Intent;

import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.databinding.ActivityNotificationDetailsBinding;
import com.marius.valeyou_admin.di.base.view.AppActivity;

import androidx.lifecycle.Observer;


public class NotificationDetailsActivity extends AppActivity<ActivityNotificationDetailsBinding, NotificationDetailsActivityVM> {
    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, NotificationDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected BindingActivity<NotificationDetailsActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_notification_details, NotificationDetailsActivityVM.class);
    }

    @Override
    protected void subscribeToEvents(NotificationDetailsActivityVM vm) {
        binding.header.tvTitle.setText("New Job Detail");
        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                finish(true);
            }
        });
    }
}
