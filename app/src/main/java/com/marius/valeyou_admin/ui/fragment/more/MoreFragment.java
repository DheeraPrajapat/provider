package com.marius.valeyou_admin.ui.fragment.more;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.base.MoreBean;
import com.marius.valeyou_admin.data.beans.singninbean.SignInModel;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.DialogDeleteAccouontBinding;
import com.marius.valeyou_admin.databinding.FragmentMoreBinding;
import com.marius.valeyou_admin.databinding.HolderMoreBinding;
import com.marius.valeyou_admin.di.base.adapter.SimpleRecyclerViewAdapter;
import com.marius.valeyou_admin.di.base.dialog.BaseCustomDialog;
import com.marius.valeyou_admin.di.base.view.AppFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.aboutus.AboutUsFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.changepassword.ChangePasswordFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.helpnsupport.HelpAndSupportFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.privacy_policy.PrivacyPolicyFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.ProfileActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.terms.TermsFragment;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.activity.main.MainActivity;
import com.marius.valeyou_admin.util.databinding.ImageViewBindingUtils;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;

import java.util.ArrayList;
import java.util.List;

public class MoreFragment extends AppFragment<FragmentMoreBinding, MoreFragmentVM> {
    public static final String TAG = "MoreFragment";
    private MainActivity mainActivity;


    public static Fragment newInstance() {
        return new MoreFragment();
    }

    @Override
    protected BindingFragment<MoreFragmentVM> getBindingFragment() {
        return new BindingFragment<>(R.layout.fragment_more, MoreFragmentVM.class);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void subscribeToEvents(final MoreFragmentVM vm) {
        mainActivity = (MainActivity) getActivity();
        mainActivity.hideBottomNav(false);
        setRecyclerViewMore();

        vm.base_click.observe(this, view -> {
            switch (view != null ? view.getId() : 0) {



            }
        });

        vm.userBean.observe(this, new SingleRequestEvent.RequestObserver<SignInModel>() {
            @Override
            public void onRequestReceived(@NonNull Resource<SignInModel> resource) {
                switch (resource.status) {
                    case LOADING:
                        binding.setCheck(true);
                        break;
                    case SUCCESS:
                        binding.setCheck(false);
                        vm.success.setValue(resource.message);
                        Intent intent1 = LoginActivity.newIntent(getActivity());
                        startNewActivity(intent1, true, true);
                        break;
                    case ERROR:
                        binding.setCheck(false);
                        vm.error.setValue(resource.message);
                        if (resource.message.equalsIgnoreCase("unauthorised")){
                            Intent intent = LoginActivity.newIntent(getActivity());
                            startNewActivity(intent, true, true);

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

    @Override
    public void onResume() {
        super.onResume();

        Log.d("auth_key",viewModel.sharedPref.getUserData().getAuthKey());

        binding.etName.setText(viewModel.sharedPref.getUserData().getFirstName()+" "+viewModel.sharedPref.getUserData().getLastName());

        String image = viewModel.sharedPref.getUserData().getImage();
        if (image!=null){
            ImageViewBindingUtils.setProfilePicture(binding.profileIMG,"http://3.17.254.50:4999/upload/"+image);
        }
    }

    private void setRecyclerViewMore() {
        binding.rvMore.setLayoutManager(new LinearLayoutManager(baseContext, RecyclerView.VERTICAL, false));
        SimpleRecyclerViewAdapter<MoreBean, HolderMoreBinding> adapter = new SimpleRecyclerViewAdapter<>(R.layout.holder_more, com.marius.valeyou_admin.BR.bean, new SimpleRecyclerViewAdapter.SimpleCallback<MoreBean>() {
            @Override
            public void onItemClick(View v, MoreBean moreBean) {
                Intent intent;
                switch (moreBean != null ? moreBean.getId() : 1) {
                    case 1:
                        intent = ProfileActivity.newIntent(getActivity());
                        startNewActivity(intent);
                        break;
                    case 2:
                        intent = ChangePasswordFragment.newIntent(getActivity());
                        startNewActivity(intent);
                        break;
                    case 3:
                        mainActivity.addSubFragment(TAG, PrivacyPolicyFragment.newInstance());
                        break;
                    case 4:
                        mainActivity.addSubFragment(TAG, TermsFragment.newInstance());
                        break;
                    case 5:
                        mainActivity.addSubFragment(TAG, AboutUsFragment.newInstance());
                        break;
                    case 6:
                        mainActivity.addSubFragment(TAG, HelpAndSupportFragment.newInstance());

                        break;
                    case 7:

                        dialogLogout();

                        break;
                }
            }
        });
        binding.rvMore.setAdapter(adapter);
        adapter.setList(getMoreData());
    }

    /* load data in list */
    private List<MoreBean> getMoreData() {
        List<MoreBean> menuBeanList = new ArrayList<>();
        menuBeanList.add(new MoreBean(1, "Profile", R.drawable.ic_profile_sidemenu_icon));
        menuBeanList.add(new MoreBean(2, "Change Password", R.drawable.ic_change_password_icon));
        menuBeanList.add(new MoreBean(3, "Privacy Policy", R.drawable.ic_privacy_policy_icon));
        menuBeanList.add(new MoreBean(4, "Terms & Conditions", R.drawable.ic_privacy_policy_icon));
        menuBeanList.add(new MoreBean(5, "About US", R.drawable.ic_about_us_icon));
        menuBeanList.add(new MoreBean(6, "Help And Support", R.drawable.ic_help_icon));
        menuBeanList.add(new MoreBean(7, "Logout", R.drawable.ic_logout_icon));
        return menuBeanList;
    }

    private BaseCustomDialog<DialogDeleteAccouontBinding> dialogLogout;
    private void dialogLogout() {
        dialogLogout = new BaseCustomDialog<>(getActivity(), R.layout.logout_dialog, new BaseCustomDialog.Listener() {
            @Override
            public void onViewClick(View view) {
                if (view != null && view.getId() != 0) {
                    switch (view.getId()) {
                        case R.id.btn_submit:

                            String id = String.valueOf(viewModel.sharedPref.getUserData().getId());
                            viewModel.logoutApi(id,viewModel.sharedPref.getUserData().getAuthKey());
                            dialogLogout.dismiss();
                            break;
                        case R.id.btn_cancel:
                            dialogLogout.dismiss();
                            break;
                    }
                }
            }
        });
        dialogLogout.getWindow().setBackgroundDrawableResource(R.color.transparance_whhite);
        dialogLogout.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialogLogout.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogLogout.show();
    }
}
