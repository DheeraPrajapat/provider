package com.marius.valeyou_admin.di.module;

import androidx.lifecycle.ViewModel;

import com.marius.valeyou_admin.di.mapkey.ViewModelKey;
import com.marius.valeyou_admin.ui.activity.LocationActivityVM;
import com.marius.valeyou_admin.ui.activity.dashboard.aboutus.AboutUsFragmentVM;
import com.marius.valeyou_admin.ui.activity.dashboard.helpnsupport.HelpAndSupportFragmentVM;
import com.marius.valeyou_admin.ui.activity.dashboard.jobhistory.JobHistoryFragmentVM;
import com.marius.valeyou_admin.ui.activity.dashboard.jobhistory.currentjob.CurrentJobFragmentVM;
import com.marius.valeyou_admin.ui.activity.dashboard.privacy_policy.PrivacyPolicyFragmentVM;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.certificate.AddCertificateActivityVM;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.portfolio.AddPortfolioActivityVM;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.providerlanguages.SelectLanguageActivityVM;
import com.marius.valeyou_admin.ui.activity.dashboard.review.ReviewFragmentVM;
import com.marius.valeyou_admin.ui.activity.dashboard.terms.TermsFragmentVm;
import com.marius.valeyou_admin.ui.activity.tourpage.tourfragment.TourFragmentVM;
import com.marius.valeyou_admin.ui.fragment.home.HomeFragmentVM;
import com.marius.valeyou_admin.ui.fragment.message.ChatListFragmentVM;
import com.marius.valeyou_admin.ui.fragment.more.MoreFragmentVM;
import com.marius.valeyou_admin.ui.fragment.favourite.MyMFragmentVM;
import com.marius.valeyou_admin.ui.fragment.myjob.MyJobFragmentVM;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.UpComingJobFragmentVM;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.opendetails.closejobs.CloseJobsFragmentVM;
import com.marius.valeyou_admin.ui.fragment.newhome.NewHomeFragmentVM;
import com.marius.valeyou_admin.ui.fragment.products.ProductFragmentVM;
import com.marius.valeyou_admin.ui.fragment.restaurant.RestaurantFragmentVM;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class FragmentViewModelModule {
    // NOTE: customize here

    @Binds
    @IntoMap
    @ViewModelKey(HomeFragmentVM.class)
    abstract ViewModel HomeFragmentVM(HomeFragmentVM vm);

    @Binds
    @IntoMap
    @ViewModelKey(AddPortfolioActivityVM.class)
    abstract ViewModel addPortfolioViewModel(AddPortfolioActivityVM vm);

    @Binds
    @IntoMap
    @ViewModelKey(SelectLanguageActivityVM.class)
    abstract ViewModel selectLanguageViewModel(SelectLanguageActivityVM vm);


    @Binds
    @IntoMap
    @ViewModelKey(AddCertificateActivityVM.class)
    abstract ViewModel addCertificateViewModel(AddCertificateActivityVM vm);


    @Binds
    @IntoMap
    @ViewModelKey(MyMFragmentVM.class)
    abstract ViewModel MyMFragmentVM(MyMFragmentVM vm);

    @Binds
    @IntoMap
    @ViewModelKey(ProductFragmentVM.class)
    abstract ViewModel ProductFragmentVM(ProductFragmentVM vm);

    @Binds
    @IntoMap
    @ViewModelKey(RestaurantFragmentVM.class)
    abstract ViewModel RestaurantFragmentVM(RestaurantFragmentVM vm);

    @Binds
    @IntoMap
    @ViewModelKey(MoreFragmentVM.class)
    abstract ViewModel MoreFragmentVM(MoreFragmentVM vm);

    @Binds
    @IntoMap
    @ViewModelKey(TourFragmentVM.class)
    abstract ViewModel TourFragmentVM(TourFragmentVM vm);

    @Binds
    @IntoMap
    @ViewModelKey(ChatListFragmentVM.class)
    abstract ViewModel ChatListFragmentVM(ChatListFragmentVM vm);

    @Binds
    @IntoMap
    @ViewModelKey(MyJobFragmentVM.class)
    abstract ViewModel MyJobFragmentVM(MyJobFragmentVM vm);

    @Binds
    @IntoMap
    @ViewModelKey(UpComingJobFragmentVM.class)
    abstract ViewModel UpComingJobFragmentVM(UpComingJobFragmentVM vm);

    @Binds
    @IntoMap
    @ViewModelKey(JobHistoryFragmentVM.class)
    abstract ViewModel JobHistoryFragmentVM(JobHistoryFragmentVM vm);

    @Binds
    @IntoMap
    @ViewModelKey(CurrentJobFragmentVM.class)
    abstract ViewModel CurrentJobFragmentVM(CurrentJobFragmentVM vm);

    @Binds
    @IntoMap
    @ViewModelKey(ReviewFragmentVM.class)
    abstract ViewModel ReviewFragmentVM(ReviewFragmentVM vm);

    @Binds
    @IntoMap
    @ViewModelKey(PrivacyPolicyFragmentVM.class)
    abstract ViewModel PrivacyPolicyFragmentVM(PrivacyPolicyFragmentVM vm);

    @Binds
    @IntoMap
    @ViewModelKey(AboutUsFragmentVM.class)
    abstract ViewModel AboutUsFragmentVM(AboutUsFragmentVM vm);

    @Binds
    @IntoMap
    @ViewModelKey(HelpAndSupportFragmentVM.class)
    abstract ViewModel HelpAndSupportFragmentVM(HelpAndSupportFragmentVM vm);

    @Binds
    @IntoMap
    @ViewModelKey(TermsFragmentVm.class)
    abstract ViewModel TermsFragmentVm(TermsFragmentVm vm);

    @Binds
    @IntoMap
    @ViewModelKey(CloseJobsFragmentVM.class)
    abstract ViewModel CloseJobsFragmentVM(CloseJobsFragmentVM vm);

    @Binds
    @IntoMap
    @ViewModelKey(LocationActivityVM.class)
    abstract ViewModel LocationActivityVM(LocationActivityVM vm);

    @Binds
    @IntoMap
    @ViewModelKey(NewHomeFragmentVM.class)
    abstract ViewModel NewHomeFragmentVM(NewHomeFragmentVM vm);



}
