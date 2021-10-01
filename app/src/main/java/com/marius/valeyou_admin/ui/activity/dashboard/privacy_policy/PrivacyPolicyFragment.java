package com.marius.valeyou_admin.ui.activity.dashboard.privacy_policy;

import android.content.Intent;
import android.os.Bundle;

import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.allcontent.AboutModel;
import com.marius.valeyou_admin.data.beans.allcontent.PrivacyModel;
import com.marius.valeyou_admin.data.beans.allcontent.TermsModel;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityPrivacyPolicyBinding;
import com.marius.valeyou_admin.di.base.view.AppFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.ProfileActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.activity.main.MainActivity;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

public class PrivacyPolicyFragment extends AppFragment<ActivityPrivacyPolicyBinding, PrivacyPolicyFragmentVM> {
    public static final String TAG = "PrivacyPolicyFragment";

    public static Fragment newInstance() {
        return new PrivacyPolicyFragment();
    }

    @Override
    protected BindingFragment<PrivacyPolicyFragmentVM> getBindingFragment() {
        return new BindingFragment<>(R.layout.activity_privacy_policy, PrivacyPolicyFragmentVM.class);
    }

    @Override
    protected void subscribeToEvents(PrivacyPolicyFragmentVM vm) {


        vm.privacyApi(2);
        vm.termsApi(1);

        vm.privacyBean.observe(this, new Observer<Resource<PrivacyModel>>() {
            @Override
            public void onChanged(Resource<PrivacyModel> aboutModelResource) {
                switch (aboutModelResource.status) {
                    case LOADING:
                        showProgressDialog(R.string.plz_wait);
                        break;

                     case SUCCESS:
                        dismissProgressDialog();
                        String privacyStr = aboutModelResource.data.getPrivacy();
                        binding.setPrivacy(privacyStr);
                        break;

                    case ERROR:
                        dismissProgressDialog();
                        vm.error.setValue(aboutModelResource.message);

                        if (aboutModelResource.message.equalsIgnoreCase(getResources().getString(R.string.unauthorised))){
                            viewModel.sharedPref.deleteAll();
                            Intent intent1 = LoginActivity.newIntent(getActivity());
                            startNewActivity(intent1, true, true);

                        }
                        break;

                    case WARN:
                        dismissProgressDialog();
                        vm.warn.setValue(aboutModelResource.message);
                        break;
                }
            }
        });

        vm.termsBean.observe(this, new Observer<Resource<TermsModel>>() {
            @Override
            public void onChanged(Resource<TermsModel> termsModelResource) {
                switch (termsModelResource.status) {
                    case LOADING:
                        showProgressDialog(R.string.plz_wait);
                        break;
                    case SUCCESS:
                       dismissProgressDialog();
                        String termsStr = termsModelResource.data.getTerm();
                        binding.setTerms(termsStr);
                        break;
                    case ERROR:
                        dismissProgressDialog();
                        vm.error.setValue(termsModelResource.message);
                        if (termsModelResource.message.equalsIgnoreCase(getResources().getString(R.string.unauthorised))){
                            viewModel.sharedPref.deleteAll();
                            Intent intent1 = LoginActivity.newIntent(getActivity());
                            startNewActivity(intent1, true, true);

                        }
                        break;
                    case WARN:
                        dismissProgressDialog();
                        vm.warn.setValue(termsModelResource.message);
                        break;
                }
            }
        });

    }
}
