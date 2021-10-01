package com.marius.valeyou_admin.ui.activity.dashboard.helpnsupport;

import android.content.Intent;
import android.view.View;

import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.databinding.FragmentHelpSupportBinding;
import com.marius.valeyou_admin.di.base.view.AppFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.message.chatview.ChatActivity;
import com.marius.valeyou_admin.ui.activity.faq.FaqActivity;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

public class HelpAndSupportFragment extends AppFragment<FragmentHelpSupportBinding,HelpAndSupportFragmentVM> {

    public static final String TAG = "HelpAndSupportFragment";

    public static Fragment newInstance() {
        return new HelpAndSupportFragment();
    }

    @Override
    protected BindingFragment<HelpAndSupportFragmentVM> getBindingFragment() {
        return new BindingFragment<>(R.layout.fragment_help_support,HelpAndSupportFragmentVM.class);
    }

    @Override
    protected void subscribeToEvents(HelpAndSupportFragmentVM vm) {

        vm.base_click.observe(this, new Observer<View>() {
            @Override
            public void onChanged(View view) {
                switch (view.getId()){
                    case R.id.tv_faq:

                        Intent intent = FaqActivity.newIntent(getActivity());
                        startNewActivity(intent);


                        break;

                    case R.id.tv_chat:

                       // Intent intent1 = ChatActivity.newIntent(getActivity());
                       // startNewActivity(intent1);


                        break;
                }
            }
        });

    }
}
