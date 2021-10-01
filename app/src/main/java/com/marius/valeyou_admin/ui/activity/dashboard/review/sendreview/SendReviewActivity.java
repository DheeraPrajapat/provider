package com.marius.valeyou_admin.ui.activity.dashboard.review.sendreview;

import android.app.Activity;
import android.content.Intent;

import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.databinding.ActivitySendReviewBinding;
import com.marius.valeyou_admin.di.base.view.AppActivity;

import androidx.lifecycle.Observer;

public class SendReviewActivity extends AppActivity<ActivitySendReviewBinding, SendReviewActivityVM> {
    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, SendReviewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected BindingActivity<SendReviewActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_send_review, SendReviewActivityVM.class);
    }

    @Override
    protected void subscribeToEvents(SendReviewActivityVM vm) {

        binding.header.tvTitle.setText("Write a Review");
        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                finish(true);
            }
        });

    }
}
