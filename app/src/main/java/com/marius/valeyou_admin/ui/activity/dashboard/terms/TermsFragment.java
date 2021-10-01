package com.marius.valeyou_admin.ui.activity.dashboard.terms;

import android.content.Intent;

import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.allcontent.AboutModel;
import com.marius.valeyou_admin.data.beans.allcontent.TermsModel;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.FragmentTermsBinding;
import com.marius.valeyou_admin.di.base.view.AppFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.aboutus.AboutUsFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.aboutus.AboutUsFragmentVM;
import com.marius.valeyou_admin.ui.activity.forgot.ForgotPasswordActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

public class TermsFragment  extends AppFragment<FragmentTermsBinding,TermsFragmentVm> {

    public static final String TAG = "TermsFragment";

    public static Fragment newInstance() {
        return new TermsFragment();
    }


    @Override
    protected BindingFragment<TermsFragmentVm> getBindingFragment() {
        return new BindingFragment<TermsFragmentVm>(R.layout.fragment_terms, TermsFragmentVm.class);

    }

    @Override
    protected void subscribeToEvents(TermsFragmentVm vm) {
        vm.termsApi(1);

        vm.termsBean.observe(this, new Observer<Resource<TermsModel>>() {
            @Override
            public void onChanged(Resource<TermsModel> termsResource) {
                switch (termsResource.status) {
                    case LOADING:
                        binding.setCheck(true);
                        break;
                    case SUCCESS:
                        binding.setCheck(false);
                        String aboutStr = termsResource.data.getTerm();
                        webViewContent(aboutStr);
                        break;
                    case ERROR:
                        binding.setCheck(false);
                        vm.error.setValue(termsResource.message);
                        if (termsResource.message.equalsIgnoreCase("unauthorized")){
                            Intent intent1 = LoginActivity.newIntent(getActivity());
                            startNewActivity(intent1, true, true);

                }

                        break;
                    case WARN:
                        binding.setCheck(false);
                        vm.warn.setValue(termsResource.message);
                        break;
                }
            }
        });
    }


    public void webViewContent(String terms){
        binding.wvLoad.requestFocus();
        binding.wvLoad.getSettings().setLightTouchEnabled(true);
        binding.wvLoad.getSettings().setJavaScriptEnabled(true);
        binding.wvLoad.getSettings().setGeolocationEnabled(true);
        binding.wvLoad.setSoundEffectsEnabled(true);
        binding.wvLoad.loadData(terms,
                "text/html", "UTF-8");
    }
}
