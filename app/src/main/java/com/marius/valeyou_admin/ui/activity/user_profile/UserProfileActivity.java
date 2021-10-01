package com.marius.valeyou_admin.ui.activity.user_profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.profilebean.ProfileModel;
import com.marius.valeyou_admin.data.beans.userprofile.UserProfileModel;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityUserProfileBinding;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.ui.activity.RatingActivity;
import com.marius.valeyou_admin.ui.activity.RatingActivityVM;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.ProfileActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;

public class UserProfileActivity extends AppActivity<ActivityUserProfileBinding,UserProfileActivityVM> {

String userId;
    @Override
    protected BindingActivity<UserProfileActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_user_profile, UserProfileActivityVM.class);
    }

    @Override
    protected void subscribeToEvents(UserProfileActivityVM vm) {

        Intent in = getIntent();
        if (in!=null){
            userId = in.getStringExtra("id");
            vm.getUserProfile(userId);
        }

        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                supportFinishAfterTransition();
                onBackPressed(true);
            }
        });




        vm.profileBean.observe(this, new SingleRequestEvent.RequestObserver<UserProfileModel>() {
            @Override
            public void onRequestReceived(@NonNull Resource<UserProfileModel> resource) {
                switch (resource.status) {
                    case LOADING:
                       showProgressDialog(R.string.plz_wait);
                        break;
                    case SUCCESS:
                        dismissProgressDialog();
                        binding.setVariable(BR.bean,resource.data);
                        break;
                    case ERROR:
                        dismissProgressDialog();

                        if (resource.message.equalsIgnoreCase(getResources().getString(R.string.unauthorised))){
                            sharedPref.deleteAll();
                            Intent intent1 = LoginActivity.newIntent(UserProfileActivity.this);
                            startNewActivity(intent1, true, true);

                        }
                        vm.error.setValue(resource.message);

                        break;
                    case WARN:
                        dismissProgressDialog();
                        vm.warn.setValue(resource.message);
                        break;
                }
            }
        });

    }
}