package com.marius.valeyou_admin.ui.activity.dashboard.aboutus;

import android.content.Intent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.allcontent.AboutModel;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityAboutUsBinding;
import com.marius.valeyou_admin.di.base.view.AppFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.ProfileActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.activity.main.MainActivity;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

public class AboutUsFragment extends AppFragment<ActivityAboutUsBinding, AboutUsFragmentVM> {

    private MainActivity mainActivity;
    public static Fragment newInstance() {
        return new AboutUsFragment();
    }

    @Override
    protected BindingFragment<AboutUsFragmentVM> getBindingFragment() {
        return new BindingFragment<AboutUsFragmentVM>(R.layout.activity_about_us, AboutUsFragmentVM.class);
    }

    @Override
    protected void subscribeToEvents(AboutUsFragmentVM vm) {

        mainActivity = (MainActivity) getActivity();
        mainActivity.hideBottomNav(false);
            vm.aboutApi(3);

            vm.aboutBean.observe(this, new Observer<Resource<AboutModel>>() {
                @Override
                public void onChanged(Resource<AboutModel> aboutModelResource) {
                    switch (aboutModelResource.status) {
                        case LOADING:
                            binding.setCheck(true);
                            break;
                        case SUCCESS:
                            binding.setCheck(false);
                            String aboutStr = aboutModelResource.data.getAbout();
                            webViewContent(aboutStr);
                            break;
                        case ERROR:
                            binding.setCheck(false);
                            vm.error.setValue(aboutModelResource.message);

                            if (aboutModelResource.message.equalsIgnoreCase(getResources().getString(R.string.unauthorised))){
                                viewModel.sharedPref.deleteAll();
                                Intent intent1 = LoginActivity.newIntent(getActivity());
                                startNewActivity(intent1, true, true);

                            }

                            break;
                        case WARN:
                            binding.setCheck(false);
                            vm.warn.setValue(aboutModelResource.message);
                            break;
                    }
                }
            });
    }

    public void webViewContent(String about){
        binding.wvLoad.requestFocus();
        binding.wvLoad.getSettings().setLightTouchEnabled(true);
        binding.wvLoad.getSettings().setJavaScriptEnabled(true);
        binding.wvLoad.getSettings().setGeolocationEnabled(true);
        binding.wvLoad.setSoundEffectsEnabled(true);
        binding.wvLoad.loadData(about,
                "text/html", "UTF-8");
    }
}
