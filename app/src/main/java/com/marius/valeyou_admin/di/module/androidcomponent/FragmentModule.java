package com.marius.valeyou_admin.di.module.androidcomponent;


import com.marius.valeyou_admin.ui.activity.dashboard.aboutus.AboutUsFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.helpnsupport.HelpAndSupportFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.jobhistory.JobHistoryFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.jobhistory.currentjob.CurrentJobFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.privacy_policy.PrivacyPolicyFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.review.ReviewFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.terms.TermsFragment;
import com.marius.valeyou_admin.ui.activity.notification.NotificationActivity;
import com.marius.valeyou_admin.ui.activity.tourpage.tourfragment.TourFragment;
import com.marius.valeyou_admin.ui.fragment.home.HomeFragment;
import com.marius.valeyou_admin.ui.fragment.message.ChatListFragment;
import com.marius.valeyou_admin.ui.fragment.more.MoreFragment;
import com.marius.valeyou_admin.ui.fragment.favourite.MyMFragment;
import com.marius.valeyou_admin.ui.fragment.myjob.MyJobFragment;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.UpComingJobFragment;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.opendetails.closejobs.CloseJobsFragment;
import com.marius.valeyou_admin.ui.fragment.newhome.NewHomeFragment;
import com.marius.valeyou_admin.ui.fragment.products.ProductFragment;
import com.marius.valeyou_admin.ui.fragment.restaurant.RestaurantFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {
    // NOTE: user  here

    @ContributesAndroidInjector
    abstract HomeFragment homeFragment();

    @ContributesAndroidInjector
    abstract MyMFragment myMFragment();

    @ContributesAndroidInjector
    abstract ProductFragment productFragment();

    @ContributesAndroidInjector
    abstract RestaurantFragment restaurantFragment();

    @ContributesAndroidInjector
    abstract MoreFragment moreFragment();

    @ContributesAndroidInjector
    abstract TourFragment tourFragment();

    @ContributesAndroidInjector
    abstract ChatListFragment chatListFragment();

    @ContributesAndroidInjector
    abstract MyJobFragment myJobFragment();

    @ContributesAndroidInjector
    abstract UpComingJobFragment upComingJobFragment();

    @ContributesAndroidInjector
    abstract JobHistoryFragment jobHistoryFragment();

    @ContributesAndroidInjector
    abstract CurrentJobFragment currentJobFragment();

    @ContributesAndroidInjector
    abstract ReviewFragment reviewFragment();

    @ContributesAndroidInjector
    abstract PrivacyPolicyFragment privacyPolicyFragment();

    @ContributesAndroidInjector
    abstract AboutUsFragment aboutUsFragment();

    @ContributesAndroidInjector
    abstract HelpAndSupportFragment helpAndSupportFragment();

    @ContributesAndroidInjector
    abstract TermsFragment termsFragment();

    @ContributesAndroidInjector
    abstract CloseJobsFragment closeJobsFragment();

    @ContributesAndroidInjector
    abstract NewHomeFragment newHomeFragment();

}
