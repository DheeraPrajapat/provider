package com.marius.valeyou_admin.di.module.androidcomponent;

import com.marius.valeyou_admin.ui.StripeConnectActivity;
import com.marius.valeyou_admin.ui.activity.LocationActivity;
import com.marius.valeyou_admin.ui.activity.RatingActivity;
import com.marius.valeyou_admin.ui.activity.bids.PlacedBidsActivity;
import com.marius.valeyou_admin.ui.activity.block.BlockActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.changepassword.ChangePasswordFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.jobhistory.currentjob.jobdetailsone.JobDetailsOneActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.message.chatview.ChatActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.ProfileActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.businesshours.BusinessHoursActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.certificate.AddCertificateActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.editprofile.EditProfileActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.education.AddEducationActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.experience.AddExperienceActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.indentity.IdentityActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.myservices.MyServicesActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.portfolio.AddPortfolioActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.portfolio.category.PortfolioCategoryActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.providerlanguages.SelectLanguageActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.review.sendreview.SendReviewActivity;
import com.marius.valeyou_admin.ui.activity.faq.FaqActivity;
import com.marius.valeyou_admin.ui.activity.forgot.ForgotPasswordActivity;
import com.marius.valeyou_admin.ui.activity.forgot.recoverpass.RecoverPasswordActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.activity.main.MainActivity;
import com.marius.valeyou_admin.ui.activity.notification.NotificationActivity;
import com.marius.valeyou_admin.ui.activity.notification.notificationdetails.NotificationDetailsActivity;
import com.marius.valeyou_admin.ui.activity.profileproject.ProfileProjectActivity;
import com.marius.valeyou_admin.ui.activity.signup.SignupActivity;
import com.marius.valeyou_admin.ui.activity.signup.signuptwo.SignupTwoActivity;
import com.marius.valeyou_admin.ui.activity.signup.signuptwo.selectcategory.SelectCategoryActivity;
import com.marius.valeyou_admin.ui.activity.signup.uploaddocument.UploadDocumentActivity;
import com.marius.valeyou_admin.ui.activity.tourpage.TourActivity;
import com.marius.valeyou_admin.ui.activity.user_profile.UserProfileActivity;
import com.marius.valeyou_admin.ui.activity.welcome.WelcomeActivity;
import com.marius.valeyou_admin.ui.fragment.IdentityVerficationActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.addwork.AddWorkActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.addwork.additional.AdditionalActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.noaccess.NoAccessChargeActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.reschedule.RescheduleActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.startjob.StartJobTimerActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.closedetails.JobDetailsActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.complete.CompleteJobActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.opendetails.CurrentJobDetailsActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.opendetails.attachments.AttachmentsActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.opendetails.upload.UploadActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    // NOTE: UserApp  here

    @ContributesAndroidInjector
    abstract WelcomeActivity welcomeActivity();

    @ContributesAndroidInjector
    abstract LoginActivity loginActivity();

    @ContributesAndroidInjector
    abstract SignupActivity signupActivity();

    @ContributesAndroidInjector
    abstract ForgotPasswordActivity forgotPasswordActivity();

    @ContributesAndroidInjector
    abstract MainActivity mainActivity();

    @ContributesAndroidInjector
    abstract TourActivity tourActivity();

    @ContributesAndroidInjector
    abstract RecoverPasswordActivity recoverPasswordActivity();

    @ContributesAndroidInjector
    abstract SignupTwoActivity signupTwoActivity();

    @ContributesAndroidInjector
    abstract SelectCategoryActivity selectCategoryActivity();

    @ContributesAndroidInjector
    abstract JobDetailsActivity jobDetailsActivity();

    @ContributesAndroidInjector
    abstract CurrentJobDetailsActivity currentJobDetailsActivity();

    @ContributesAndroidInjector
    abstract JobDetailsOneActivity jobDetailsOneActivity();

    @ContributesAndroidInjector
    abstract SendReviewActivity sendReviewActivity();

    @ContributesAndroidInjector
    abstract NotificationDetailsActivity notificationDetailsActivity();

    @ContributesAndroidInjector
    abstract ChatActivity chatActivity();

    @ContributesAndroidInjector
    abstract ProfileActivity profileActivity();

    @ContributesAndroidInjector
    abstract EditProfileActivity editProfileActivity();

    @ContributesAndroidInjector
    abstract ChangePasswordFragment changePasswordFragment();

    @ContributesAndroidInjector
    abstract UploadDocumentActivity uploadDocumentActivity();

    @ContributesAndroidInjector
    abstract ProfileProjectActivity profileProjectActivity();

    @ContributesAndroidInjector
    abstract NotificationActivity notificationActivity();

    @ContributesAndroidInjector
    abstract StartJobTimerActivity startJobTimerActivity();

    @ContributesAndroidInjector
    abstract AddPortfolioActivity addPortfolioActivity();

    @ContributesAndroidInjector
    abstract SelectLanguageActivity selectLanguageActivity();

    @ContributesAndroidInjector
    abstract AddCertificateActivity addCertificateActivity();

    @ContributesAndroidInjector
    abstract MyServicesActivity myServicesActivity();


    @ContributesAndroidInjector
    abstract IdentityActivity identityActivity();

    @ContributesAndroidInjector
    abstract BusinessHoursActivity businessHoursActivity();

    @ContributesAndroidInjector
    abstract FaqActivity faqActivity();


    @ContributesAndroidInjector
    abstract CompleteJobActivity completeJobActivity();

    @ContributesAndroidInjector
    abstract AddEducationActivity addEducationActivity();

    @ContributesAndroidInjector
    abstract AddExperienceActivity addExperienceActivity();

    @ContributesAndroidInjector
    abstract LocationActivity locationActivity();

    @ContributesAndroidInjector
    abstract StripeConnectActivity stripeConnectActivity();

    @ContributesAndroidInjector
    abstract RatingActivity ratingActivity();

    @ContributesAndroidInjector
    abstract IdentityVerficationActivity identityVerficationActivity();

    @ContributesAndroidInjector
    abstract PlacedBidsActivity placedBidsActivity();

    @ContributesAndroidInjector
    abstract UserProfileActivity userProfileActivity();


    @ContributesAndroidInjector
    abstract PortfolioCategoryActivity portfolioCategoryActivity();

    @ContributesAndroidInjector
    abstract BlockActivity blockActivity();

    @ContributesAndroidInjector
    abstract com.marius.valeyou_admin.ui.activity.language.SelectLanguageActivity selectLanguageActivity2();


    @ContributesAndroidInjector
    abstract AddWorkActivity addWorkActivity();

    @ContributesAndroidInjector
    abstract NoAccessChargeActivity noAccessChargeActivity();

    @ContributesAndroidInjector
    abstract RescheduleActivity rescheduleActivity();

    @ContributesAndroidInjector
    abstract AdditionalActivity additionalActivity();

    @ContributesAndroidInjector
    abstract UploadActivity uploadActivity();

    @ContributesAndroidInjector
    abstract AttachmentsActivity attachmentsActivity();
}
