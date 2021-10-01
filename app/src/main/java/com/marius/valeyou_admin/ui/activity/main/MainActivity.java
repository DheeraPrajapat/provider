package com.marius.valeyou_admin.ui.activity.main;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.base.MoreBean;
import com.marius.valeyou_admin.data.beans.singninbean.SignInModel;
import com.marius.valeyou_admin.data.local.SharedPref;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityMainBinding;
import com.marius.valeyou_admin.databinding.DialogDeleteAccouontBinding;
import com.marius.valeyou_admin.databinding.HolderMoreBinding;
import com.marius.valeyou_admin.di.base.adapter.SimpleRecyclerViewAdapter;
import com.marius.valeyou_admin.di.base.dialog.BaseCustomDialog;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.di.fcm.Config;
import com.marius.valeyou_admin.ui.activity.dashboard.aboutus.AboutUsFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.changepassword.ChangePasswordFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.helpnsupport.HelpAndSupportFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.message.chatview.ChatActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.privacy_policy.PrivacyPolicyFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.ProfileActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.portfolio.AddPortfolioActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.terms.TermsFragment;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.activity.notification.NotificationActivity;
import com.marius.valeyou_admin.ui.activity.profileproject.ProfileProjectActivity;
import com.marius.valeyou_admin.ui.fragment.home.HomeFragment;
import com.marius.valeyou_admin.ui.fragment.message.ChatListFragment;
import com.marius.valeyou_admin.ui.fragment.more.MoreFragment;
import com.marius.valeyou_admin.ui.fragment.favourite.MyMFragment;
import com.marius.valeyou_admin.ui.fragment.myjob.MyJobFragment;
import com.marius.valeyou_admin.ui.fragment.newhome.NewHomeFragment;
import com.marius.valeyou_admin.ui.fragment.products.ProductFragment;
import com.marius.valeyou_admin.ui.fragment.restaurant.RestaurantFragment;
import com.marius.valeyou_admin.util.Constants;
import com.marius.valeyou_admin.util.databinding.ImageViewBindingUtils;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;
import com.marius.valeyou_admin.util.location.LiveLocationDetecter;
import com.marius.valeyou_admin.util.misc.BackStackManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppActivity<ActivityMainBinding, MainActivityVM> {
    public static final String TAG = "MainActivity";
    @Inject
    SharedPref sharedPref;
    private boolean exit = false;
    @Inject
    LiveLocationDetecter liveLocationDetecter;

    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected BindingActivity<MainActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_main, MainActivityVM.class);
    }

    @Override
    protected void subscribeToEvents(final MainActivityVM vm) {


        addSubFragmentWithoutCache(TAG,NewHomeFragment.newInstance());

        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                if (binding.header.imgBack.getAlpha() == 1)
                    backStepFragment();
            }
        });



        vm.base_click.observe(this, view -> {
            switch (view != null ? view.getId() : 0) {
                case R.id.ll_menu:
                    changeFragment(MyJobFragment.TAG);
                    break;
                case R.id.ll_fav:
                    changeFragment(MyMFragment.TAG);
                    break;
                case R.id.cv_home:
                    changeFragment(NewHomeFragment.TAG);
                    break;
                case R.id.ll_settings:
                    //changeFragment(MoreFragment.TAG);
                    Intent intent = AddPortfolioActivity.newIntent(this);
                    intent.putExtra("comeFrom","add");
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(MainActivity.this);
                    startActivity(intent,options.toBundle());
                    break;
                case R.id.ll_message:
                    changeFragment(ChatListFragment.TAG);
                    break;
            }
        });


        vm.obrNavClick.observe(this, menuItem -> {
            //  binding.header.ivProfile.setImageResource(R.drawable.ic_base_profile);
            switch (menuItem.getItemId()) {
                case R.id.nav_1:
                    changeFragment(HomeFragment.TAG);
                    break;
                case R.id.nav_2:
                    changeFragment(MyMFragment.TAG);
                    break;
                case R.id.nav_3:
                    changeFragment(ProductFragment.TAG);
                    break;
                case R.id.nav_4:
                    changeFragment(RestaurantFragment.TAG);
                    break;
                case R.id.nav_5:
                    changeFragment(MoreFragment.TAG);
                    break;

            }
        });

        BackStackManager.getInstance(this).setFragmentChangeListioner((tag, isSubFragment) -> {
            if (isSubFragment)
                showBackButton();
            else
                hideBackButton();
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        PackageInfo pinfo = null;
        try {

            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int versionNumber = pinfo.versionCode;
            String versionName = pinfo.versionName;
            binding.drawer.tvAppVersion.setText("App Version : "+versionNumber+"("+versionName+")");

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();

        }

        String image = viewModel.sharedPref.getUserData().getImage();
        ImageViewBindingUtils.setProfilePicture(binding.drawer.progileImg,image);
        String name = viewModel.sharedPref.getUserData().getFirstName()+" "+viewModel.sharedPref.getUserData().getLastName();
        binding.drawer.tvName.setText(name);

    }
    private boolean check = false;
    protected void changeFragment(@Nullable String tab) {
        switch (tab != null ? tab : NewHomeFragment.TAG) {
            case NewHomeFragment.TAG:
                check = true;
                setHeader("");
                BackStackManager.getInstance(this).pushFragments(R.id.container, NewHomeFragment.TAG, NewHomeFragment.newInstance(), tab != null);
                break;
            case MyMFragment.TAG:
                check = false;
                setHeader(getResources().getString(R.string.favourites));
                binding.header.tvTitle.setTextColor(getResources().getColor(R.color.white));
                BackStackManager.getInstance(this).pushFragments(R.id.container, MyMFragment.TAG, MyMFragment.newInstance(), true);
                break;
            case MyJobFragment.TAG:
                check = false;
                setHeader(getResources().getString(R.string.my_jobs));
                binding.header.tvTitle.setTextColor(getResources().getColor(R.color.white));
                BackStackManager.getInstance(this).pushFragments(R.id.container, MyJobFragment.TAG, MyJobFragment.newInstance(), true);
                break;
            case RestaurantFragment.TAG:
                check = false;
                setHeader("Restaurant");
                binding.header.tvTitle.setTextColor(getResources().getColor(R.color.white));
                BackStackManager.getInstance(this).pushFragments(R.id.container, RestaurantFragment.TAG, RestaurantFragment.newInstance(), true);
                break;
            case MoreFragment.TAG:
                check = false;
                setHeader("");
                binding.header.tvTitle.setTextColor(getResources().getColor(R.color.white));
                BackStackManager.getInstance(this).pushFragments(R.id.container, MoreFragment.TAG, MoreFragment.newInstance(), true);
                break;
            case ChatListFragment.TAG:
                check = false;
                setHeader(getResources().getString(R.string.message));
                binding.header.tvTitle.setTextColor(getResources().getColor(R.color.white));
                BackStackManager.getInstance(this).pushFragments(R.id.container, ChatListFragment.TAG, ChatListFragment.newInstance(), true);
                break;
        }
    }


    public void addSubFragment(@NonNull String tab, Fragment fragment) {
        BackStackManager.getInstance(this).pushSubFragments(R.id.container, tab, fragment, true);
    }

    public void addSubFragmentWithoutCache(@NonNull String tab, Fragment fragment) {
        BackStackManager.getInstance(this).pushSubFragmentsWithoutCache(R.id.container, tab, fragment, true);
    }

    public void hideBackButton() {
        binding.header.imgBack.animate().alpha(0);
    }

    public void showBackButton() {
        binding.header.imgBack.animate().alpha(1);
    }

    @Override
    public void onBackPressed() {
        if (backStepFragment()) {
            if (BackStackManager.getInstance(this).getCurrentTab() == "NewHomeFragment") {
                setExitDialog();
            }else{
                changeFragment(NewHomeFragment.TAG);
            }


        }



    }

    private boolean backStepFragment() {
        return BackStackManager.getInstance(this).removeFragment(R.id.container, true);
    }

    /*
     * show or hide bottom navigation bar
     **/
    public void hideBottomNav(boolean bool) {

    }

    /*
     * set header text
     **/
    public void setHeader(String header) {
        binding.header.tvTitle.setText(header);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        liveLocationDetecter.onActivityResult(requestCode, resultCode);
    }


    private BaseCustomDialog<DialogDeleteAccouontBinding> exitDialog;
    private void setExitDialog() {
        exitDialog = new BaseCustomDialog<>(MainActivity.this, R.layout.exit_dialog, new BaseCustomDialog.Listener() {
            @Override
            public void onViewClick(View view) {
                if (view != null && view.getId() != 0) {
                    switch (view.getId()) {
                        case R.id.btn_submit:
                            exitDialog.dismiss();
                            finish(true);
                            break;
                        case R.id.btn_cancel:
                            exitDialog.dismiss();
                            break;
                    }
                }
            }
        });

        exitDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        exitDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        exitDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        exitDialog.show();

    }



    public  void badgeCount(int count){
         if (count>0) {
             binding.bottomNav.tvBadge.setVisibility(View.VISIBLE);

            if (count>9){

                binding.bottomNav.tvBadge.setText(count +"+");

            }else{

                binding.bottomNav.tvBadge.setText("0"+count);

            }

        }else{

             binding.bottomNav.tvBadge.setVisibility(View.GONE);

        }
    }

}
