package com.marius.valeyou_admin.ui.activity.faq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.base.MoreBean;
import com.marius.valeyou_admin.data.beans.faq.FaqModel;
import com.marius.valeyou_admin.data.beans.notification.NotificationModel;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityFaqBinding;
import com.marius.valeyou_admin.databinding.HolderFaqBinding;
import com.marius.valeyou_admin.di.base.adapter.SimpleRecyclerViewAdapter;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.di.base.view.BaseFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.helpnsupport.HelpAndSupportFragmentVM;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivityVM;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;

import java.util.ArrayList;
import java.util.List;

public class FaqActivity extends AppActivity<ActivityFaqBinding,FaqActivityVM> {


    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, FaqActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected BindingActivity<FaqActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_faq, FaqActivityVM.class);

    }

    @Override
    protected void subscribeToEvents(FaqActivityVM vm) {

        vm.getFaq(vm.sharedPref.getUserData().getAuthKey());

        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                finish(true);
            }
        });


        vm.faqbean.observe(this, new SingleRequestEvent.RequestObserver<List<FaqModel>>() {
            @Override
            public void onRequestReceived(@NonNull Resource<List<FaqModel>> resource) {
                switch (resource.status) {
                    case LOADING:
                        binding.setCheck(true);
                        break;
                    case SUCCESS:
                        binding.setCheck(false);
                        setRecyclerFaq(resource.data);

                        break;
                    case ERROR:
                        binding.setCheck(false);

                        if (resource.message.equalsIgnoreCase("Bad Request")) {
                            vm.error.setValue("Incorrect Email Or Password");
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



    }


    private void setRecyclerFaq(List<FaqModel> list) {
        binding.rvFaq.setLayoutManager(new LinearLayoutManager(FaqActivity.this));
        binding.rvFaq.setAdapter(adapter);
        adapter.setCatList(list);
    }


    faqAdapter adapter = new faqAdapter(this, new faqAdapter.CheckCallback() {
        @Override
        public void onItemClick(View v, int position, FaqModel model, HolderFaqBinding binding) {
            if (model.isClicked()){
                model.setClicked(false);
                binding.tvDescription.setVisibility(View.GONE);
                rotate(binding.arrowImage,-90F);
            }else{
                model.setClicked(true);
                binding.tvDescription.setVisibility(View.VISIBLE);
                rotate(binding.arrowImage,90F);
            }

        }
    });

    private void rotate(ImageView imageView,float value){
        float deg = imageView.getRotation() + value;
        imageView.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());

    }



}
