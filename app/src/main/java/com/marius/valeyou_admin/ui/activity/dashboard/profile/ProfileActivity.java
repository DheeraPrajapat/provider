package com.marius.valeyou_admin.ui.activity.dashboard.profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.IdentityModel;
import com.marius.valeyou_admin.data.beans.base.MoreBean;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.notification.NotificationModel;
import com.marius.valeyou_admin.data.beans.profilebean.ProfileModel;
import com.marius.valeyou_admin.data.beans.reviews.MyReview;
import com.marius.valeyou_admin.data.beans.singninbean.SignInModel;
import com.marius.valeyou_admin.data.beans.singninbean.SubCategories;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityProfileBinding;
import com.marius.valeyou_admin.databinding.DialogDeactivateAccopuontBinding;
import com.marius.valeyou_admin.databinding.DialogDeleteAccouontBinding;
import com.marius.valeyou_admin.databinding.HolderBusinessHourBinding;
import com.marius.valeyou_admin.databinding.HolderCertificateItemBinding;
import com.marius.valeyou_admin.databinding.HolderEducationItemBinding;
import com.marius.valeyou_admin.databinding.HolderExperienceItemBinding;
import com.marius.valeyou_admin.databinding.HolderIdentityCardBinding;
import com.marius.valeyou_admin.databinding.HolderLanguageItemBinding;
import com.marius.valeyou_admin.databinding.HolderPortfolioItemsBinding;
import com.marius.valeyou_admin.databinding.HolderProfileItemBinding;
import com.marius.valeyou_admin.databinding.HolderReviewsBinding;
import com.marius.valeyou_admin.di.base.adapter.SimpleRecyclerViewAdapter;
import com.marius.valeyou_admin.di.base.dialog.BaseCustomDialog;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.ui.StripeConnectActivity;
import com.marius.valeyou_admin.ui.activity.RatingActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.changepassword.ChangePasswordFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.businesshours.BusinessHoursActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.certificate.AddCertificateActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.editprofile.EditProfileActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.education.AddEducationActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.experience.AddExperienceActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.indentity.IdentityActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.myservices.MyServicesActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.portfolio.AddPortfolioActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.providerlanguages.SelectLanguageActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.activity.main.MainActivity;
import com.marius.valeyou_admin.ui.activity.notification.NotificationActivity;
import com.marius.valeyou_admin.ui.activity.notification.NotificationAdapter;
import com.marius.valeyou_admin.ui.fragment.myjob.reschedule.RescheduleActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.opendetails.CurrentJobDetailsActivity;
import com.marius.valeyou_admin.util.Constants;
import com.marius.valeyou_admin.util.databinding.ImageViewBindingUtils;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

public class ProfileActivity extends AppActivity<ActivityProfileBinding, ProfileActivityVM> implements CustomPortfolioAdapter.ClickListner,CustomEducationAdapter.ClickListner,CustomExperienceAdapter.ClickListner{


    List<ProfileModel.ProviderCategoriesBean> categoriesBean;
    ProfileModel model;
    List<IdentityModel> listOfIDentity;
    String catName="";
    List<ProfileModel.ProviderPortfoliosBean> portFolioList;
    List<ProfileModel.EducationBean> educationList;
    List<ProfileModel.ExperienceBean> experienceModelList;
    ProfileCategoryAdapter ProfileCategoryAdapter;


    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected BindingActivity<ProfileActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_profile, ProfileActivityVM.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void subscribeToEvents(ProfileActivityVM vm) {
        setToolbar();
        String authKey = vm.sharedPref.getUserData().getAuthKey();
        String provider_id = String.valueOf(vm.sharedPref.getUserData().getId());
        vm.profile(authKey,provider_id);
        vm.myReview(authKey);
        vm.getIdentity(authKey);



        binding.rvLanguage.setVisibility(View.VISIBLE);
        binding.IMGAddLanguages.setVisibility(View.VISIBLE);


        binding.rvEducation.setVisibility(View.VISIBLE);
        binding.IMGAddEducation.setVisibility(View.VISIBLE);
        binding.rvExperience.setVisibility(View.VISIBLE);
        binding.IMGAddExperience.setVisibility(View.VISIBLE);
        binding.rvPortfolio.setVisibility(View.VISIBLE);
        binding.IMGAddPortfolio.setVisibility(View.VISIBLE);
        binding.rvIdentityCard.setVisibility(View.GONE);
        binding.IMGAddIdentity.setVisibility(View.GONE);
        binding.rvCertificate.setVisibility(View.GONE);
        binding.IMGAddCertificate.setVisibility(View.GONE);
        binding.rvBusinessHour.setVisibility(View.GONE);
        binding.IMGBusinessHours.setVisibility(View.GONE);
        binding.rvReviews.setVisibility(View.GONE);
        binding.IMGAddReviews.setVisibility(View.GONE);
        binding.rvServices.setVisibility(View.GONE);
        binding.IMGAddServices.setVisibility(View.GONE);

        vm.deleteApiBean.observe(this, new SingleRequestEvent.RequestObserver<SimpleApiResponse>() {
            @Override
            public void onRequestReceived(@NonNull Resource<SimpleApiResponse> resource) {

                switch (resource.status) {
                    case LOADING:

                        break;
                    case SUCCESS:

                        vm.success.setValue(resource.message);
                        vm.profile(authKey,provider_id);
                        vm.getIdentity(authKey);

                        break;
                    case ERROR:

                        if (resource.message.equalsIgnoreCase("unauthorized")){
                            Intent intent1 = LoginActivity.newIntent(ProfileActivity.this);
                            startNewActivity(intent1, true, true);

                        }
                        vm.error.setValue(resource.message);

                        break;
                    case WARN:

                        vm.warn.setValue(resource.message);

                        break;
                }
            }
        });

        vm.accountSettingBean.observe(this, new SingleRequestEvent.RequestObserver<SimpleApiResponse>() {
            @Override
            public void onRequestReceived(@NonNull Resource<SimpleApiResponse> resource) {

                switch (resource.status) {
                    case LOADING:
                        showProgressDialog(R.string.wait);
                        break;
                    case SUCCESS:
                        dismissProgressDialog();
                        if (resource.message.equalsIgnoreCase("account deactivate successfully")){
                            vm.success.setValue(getResources().getString(R.string.deactivate_success));
                        }
                        sharedPref.deleteAll();
                        Intent intent1 = LoginActivity.newIntent(ProfileActivity.this);
                        startNewActivity(intent1, true, true);
                        finishAffinity();
                        break;
                    case ERROR:
                        dismissProgressDialog();
                        if (resource.message.equalsIgnoreCase(getResources().getString(R.string.unauthorised))){
                            sharedPref.deleteAll();
                            Intent intent = LoginActivity.newIntent(ProfileActivity.this);
                            startNewActivity(intent, true, true);

                        }
                        vm.error.setValue(resource.message);
                        break;
                    case WARN:
                        dismissProgressDialog();
                        vm.warn.setValue(resource.message);

                        break;
                }
            }
        });



        vm.identityBean.observe(this, new SingleRequestEvent.RequestObserver<List<IdentityModel>>() {
            @Override
            public void onRequestReceived(@NonNull Resource<List<IdentityModel>> resource) {

                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        setRecycleIdentityCard(resource.data);
                        break;
                    case ERROR:
                        vm.error.setValue(resource.message);
                        break;
                    case WARN:
                        vm.warn.setValue(resource.message);
                        break;
                }
            }
        });


        vm.reviewBean.observe(this, new SingleRequestEvent.RequestObserver<List<MyReview>>() {
            @Override
            public void onRequestReceived(@NonNull Resource<List<MyReview>> resource) {
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        setRecyclerReview(resource.data);
                         break;
                    case ERROR:
                        vm.error.setValue(resource.message);

                        break;
                    case WARN:
                        vm.warn.setValue(resource.message);
                        break;
                }
            }
        });

        vm.profileBean.observe(this, new SingleRequestEvent.RequestObserver<ProfileModel>() {
            @Override
            public void onRequestReceived(@NonNull Resource<ProfileModel> resource) {
                switch (resource.status) {
                    case LOADING:
                        binding.setCheck(true);
                        break;
                    case SUCCESS:
                        binding.setCheck(false);
                        binding.setVariable(BR.profile,model);
                        model = resource.data;


                        if (resource.data.getVehicleType().equalsIgnoreCase("")){
                            binding.txtDR.setText(getResources().getString(R.string.no));
                            binding.vehType.setText(getResources().getString(R.string.no));
                        }else{
                            binding.txtDR.setText(getResources().getString(R.string.yes));
                            binding.vehType.setText(resource.data.getVehicleType());
                        }


                        portFolioList = model.getProviderPortfolios();
                        if (portFolioList.size()>2){
                            binding.txtViewMore.setVisibility(View.VISIBLE);
                        }else{
                            binding.txtViewMore.setVisibility(View.GONE);
                        }


                        educationList = model.getEducation();
                        if (educationList.size()>2){
                            binding.txtViewEducationMore.setVisibility(View.VISIBLE);
                        }else{
                            binding.txtViewEducationMore.setVisibility(View.GONE);
                        }


                        experienceModelList = model.getExperience();
                        if (experienceModelList.size()>2){
                            binding.txtExpViewMore.setVisibility(View.VISIBLE);
                        }else{
                            binding.txtExpViewMore.setVisibility(View.GONE);
                        }


                        binding.setVariable(BR.profile,resource.data);
                        categoriesBean = resource.data.getProviderCategories();
                        setRecyclerView(model.getProviderLanguages());
                        setRecyclerPortfolio(model.getProviderPortfolios(),false);
                        setRecycleCertificate(model.getCertificates());
                        setRecycleBusiness(model.getBusiness_hours());
                        setRecyclerEducation(model.getEducation(),false);
                        setRecyclerExperience(experienceModelList,false);
                        getCatNames(categoriesBean);

                        break;
                    case ERROR:
                        binding.setCheck(false);

                        if (resource.message.equalsIgnoreCase(getResources().getString(R.string.unauthorised))){
                            sharedPref.deleteAll();
                            Intent intent1 = LoginActivity.newIntent(ProfileActivity.this);
                            startNewActivity(intent1, true, true);

                        }
                        vm.error.setValue(resource.message);

                        break;
                    case WARN:
                        binding.setCheck(false);
                        vm.warn.setValue(resource.message);
                        break;
                }
            }
        });


        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                supportFinishAfterTransition();
                onBackPressed(true);
            }
        });
        vm.base_click.observe(this, new Observer<View>() {
            @Override
            public void onChanged(View view) {
                Intent intent;
                switch (view != null ? view.getId() : 0) {

                    case R.id.tv_two:
                        intent = EditProfileActivity.newIntent(ProfileActivity.this);
                        startNewActivity(intent);
                        break;
                    case R.id.cv_deactivate:
                        dialogDeactivateAccount();
                        break;
                    case R.id.cv_delete:
                        dialogDeleteAccount();
                        break;

                    case R.id.cv_change_password:
                        Intent in = ChangePasswordFragment.newIntent(ProfileActivity.this);
                        startNewActivity(in);
                        break;
                    case R.id.IMG_add_portfolio:
                       intent = AddPortfolioActivity.newIntent(ProfileActivity.this);
                        intent.putExtra("comeFrom","add");
                       startNewActivity(intent);
                        break;

                    case R.id.IMG_add_languages:
                        intent = SelectLanguageActivity.newIntent(ProfileActivity.this);
                        intent.putExtra("comeFrom","add");
                        startNewActivity(intent);
                        break;


                    case R.id.IMG_add_certificate:
                        intent = AddCertificateActivity.newIntent(ProfileActivity.this);
                        intent.putExtra("comeFrom","add");
                        startNewActivity(intent);
                        break;

                    case R.id.IMG_add_identity:
                        intent = IdentityActivity.newIntent(ProfileActivity.this);
                        intent.putExtra("comeFrom","add");
                        startNewActivity(intent);
                        break;

                    case R.id.IMG_business_hours:
                        intent = BusinessHoursActivity.newIntent(ProfileActivity.this);
                        startNewActivity(intent);
                        break;

                    case R.id.IMG_add_education:
                        intent = AddEducationActivity.newIntent(ProfileActivity.this);
                        intent.putExtra("comeFrom","add");
                        startNewActivity(intent);
                        break;


                    case R.id.IMG_add_experience:
                        intent = AddExperienceActivity.newIntent(ProfileActivity.this);
                        intent.putExtra("comeFrom","add");
                        startNewActivity(intent);
                        break;

                    case R.id.txt_languages:
                        if (binding.rvLanguage.getVisibility()!=View.VISIBLE){
                            binding.rvLanguage.setVisibility(View.VISIBLE);
                            binding.IMGAddLanguages.setVisibility(View.VISIBLE);
                        }else{
                            binding.rvLanguage.setVisibility(View.GONE);
                            binding.IMGAddLanguages.setVisibility(View.GONE);
                        }

                        break;

                    case R.id.txt_portfolio:
                        if (binding.rvPortfolio.getVisibility()!=View.VISIBLE){
                            binding.rvPortfolio.setVisibility(View.VISIBLE);
                            binding.IMGAddPortfolio.setVisibility(View.VISIBLE);
                            if (portFolioList.size()>2){
                                binding.txtViewMore.setVisibility(View.VISIBLE);
                            }else{
                                binding.txtViewMore.setVisibility(View.GONE);
                            }

                        }else{
                            binding.rvPortfolio.setVisibility(View.GONE);
                            binding.IMGAddPortfolio.setVisibility(View.GONE);
                            binding.txtViewMore.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.lbl_category:
                        if (binding.rvCategory.getVisibility()!= View.VISIBLE) {
                            binding.rvCategory.setVisibility(View.VISIBLE);
                        }else{
                            binding.rvCategory.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.rvCategory:
                        intent = MyServicesActivity.newIntent(ProfileActivity.this);
                       /* Bundle args = new Bundle();
                        args.putSerializable("categoriesBean",(Serializable)categoriesBean);
                        intent.putExtra("BUNDLE",args);*/
                        startNewActivity(intent);
                    case R.id.txt_business_hours:
                        if (binding.rvBusinessHour.getVisibility()!=View.VISIBLE){
                            binding.rvBusinessHour.setVisibility(View.VISIBLE);
                            binding.IMGBusinessHours.setVisibility(View.VISIBLE);
                        }else{
                            binding.rvBusinessHour.setVisibility(View.GONE);
                            binding.IMGBusinessHours.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.txt_identity:
                        if (binding.rvIdentityCard.getVisibility()!=View.VISIBLE){
                            binding.rvIdentityCard.setVisibility(View.VISIBLE);
                            binding.IMGAddIdentity.setVisibility(View.VISIBLE);
                        }else{
                            binding.rvIdentityCard.setVisibility(View.GONE);
                            binding.IMGAddIdentity.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.txt_certificates:
                        if (binding.rvCertificate.getVisibility()!=View.VISIBLE){
                            binding.rvCertificate.setVisibility(View.VISIBLE);
                            binding.IMGAddCertificate.setVisibility(View.VISIBLE);
                        }else{
                            binding.rvCertificate.setVisibility(View.GONE);
                            binding.IMGAddCertificate.setVisibility(View.GONE);
                        }

                        break;

                    case R.id.txt_reviews:
                        if (binding.rvReviews.getVisibility()!=View.VISIBLE){
                            binding.rvReviews.setVisibility(View.VISIBLE);
                        }else{
                            binding.rvReviews.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.txt_Education:
                        if (binding.rvEducation.getVisibility()!=View.VISIBLE){
                            binding.rvEducation.setVisibility(View.VISIBLE);
                            binding.IMGAddEducation.setVisibility(View.VISIBLE);

                            if (educationList.size()>2){
                                binding.txtViewEducationMore.setVisibility(View.VISIBLE);
                            }else{
                                binding.txtViewEducationMore.setVisibility(View.GONE);
                            }
                        }else{
                            binding.rvEducation.setVisibility(View.GONE);
                            binding.IMGAddEducation.setVisibility(View.GONE);
                            binding.txtViewEducationMore.setVisibility(View.GONE);
                        }
                        break;

                    case R.id.txt_experience:
                        if (binding.rvExperience.getVisibility()!= View.VISIBLE){
                            binding.rvExperience.setVisibility(View.VISIBLE);
                            binding.IMGAddExperience.setVisibility(View.VISIBLE);

                            if (experienceModelList.size()>2){
                                binding.txtExpViewMore.setVisibility(View.VISIBLE);
                            }else{
                                binding.txtExpViewMore.setVisibility(View.GONE);
                            }
                        }else{
                            binding.rvExperience.setVisibility(View.GONE);
                            binding.IMGAddExperience.setVisibility(View.GONE);
                            binding.txtExpViewMore.setVisibility(View.GONE);
                        }
                        break;

                    case R.id.ll_rating:
                        scrollToReviews();
                        break;

                    case R.id.txtViewMore:
                        if (binding.txtViewMore.getText().toString().equalsIgnoreCase("Show More")) {
                            setRecyclerPortfolio(portFolioList, true);
                            binding.txtViewMore.setText("Show Less");
                        }else{
                            setRecyclerPortfolio(portFolioList, false);
                            binding.txtViewMore.setText("Show More");
                        }
                        break;

                    case R.id.txtViewEducationMore:
                        if (binding.txtViewEducationMore.getText().toString().equalsIgnoreCase("Show More")) {
                            setRecyclerEducation(educationList, true);
                            binding.txtViewEducationMore.setText("Show Less");
                        }else{
                            setRecyclerEducation(educationList, false);
                            binding.txtViewEducationMore.setText("Show More");
                        }
                        break;

                    case R.id.txtExpViewMore:
                        if (binding.txtExpViewMore.getText().toString().equalsIgnoreCase("Show More")) {
                            setRecyclerExperience(experienceModelList, true);
                            binding.txtExpViewMore.setText("Show Less");
                        }else{
                            setRecyclerExperience(experienceModelList, false);
                            binding.txtExpViewMore.setText("Show More");
                        }
                        break;
                }
            }
        });




    }

    private void scrollToReviews(){
        binding.rvReviews.setVisibility(View.VISIBLE);
        binding.scrollView.fullScroll(View.FOCUS_DOWN);

    }

    private void getCatNames( List<ProfileModel.ProviderCategoriesBean> categoriesBean){
        for (int i = 0;i<categoriesBean.size();i++) {
            String namme = categoriesBean.get(i).getCategory().getName();
            if (namme != null) {
                if (categoriesBean.size() > 1) {
                    catName = catName + "/" + namme;
                } else {
                    catName = namme;
                }
            }
        }
        if (categoriesBean!=null){
            String categorias[] = catName.split("/");

            setCategoryView(categorias);

        }else{

            binding.rvCategory.setVisibility(View.GONE);

        }
    }

    private void setCategoryView(String[] list) {
        if (list.length > 0) {
            binding.rvCategory.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            binding.rvCategory.setLayoutManager(layoutManager);
            ProfileCategoryAdapter = new ProfileCategoryAdapter(ProfileActivity.this, list, new ProfileCategoryAdapter.Listner() {
                @Override
                public void onItemClick(View v) {
                    switch (v.getId()) {

                        case R.id.txt_category1:
                            Intent intent = MyServicesActivity.newIntent(ProfileActivity.this);
                            /* Bundle args = new Bundle();
                            args.putSerializable("categoriesBean",(Serializable)categoriesBean);
                            intent.putExtra("BUNDLE",args);*/
                            startNewActivity(intent);
                            break;


                        case R.id.txt_category2:
                            Intent intent2 = MyServicesActivity.newIntent(ProfileActivity.this);
                            /* Bundle args = new Bundle();
                            args.putSerializable("categoriesBean",(Serializable)categoriesBean);
                            intent.putExtra("BUNDLE",args);*/
                            startNewActivity(intent2);
                            break;


                        case R.id.txt_category3:
                            Intent intent3 = MyServicesActivity.newIntent(ProfileActivity.this);
                            /* Bundle args = new Bundle();
                            args.putSerializable("categoriesBean",(Serializable)categoriesBean);
                            intent.putExtra("BUNDLE",args);*/
                            startNewActivity(intent3);
                            break;

                    }
                }
            });
            binding.rvCategory.setAdapter(ProfileCategoryAdapter);

        } else {

//            binding.noTxt.setVisibility(View.VISIBLE);
            binding.rvCategory.setVisibility(View.GONE);

        }


    }

    private void setRecyclerView(List<ProfileModel.ProviderLanguagesBean> listOfLanguages) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvLanguage.setLayoutManager(layoutManager);
        binding.rvLanguage.setAdapter(adapterLanguage);
        adapterLanguage.setList(listOfLanguages);
    }

    private void setRecyclerPortfolio(List<ProfileModel.ProviderPortfoliosBean> listOfPortfolio,boolean isAll) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvPortfolio.setLayoutManager(layoutManager);
       CustomPortfolioAdapter portfolioAdapter = new CustomPortfolioAdapter(this,isAll,listOfPortfolio,this);
        binding.rvPortfolio.setAdapter(portfolioAdapter);

    }

    @Override
    public void onItemClick(View view, List<ProfileModel.ProviderPortfoliosBean> list, int position) {
        switch (view != null ? view.getId() : 0) {
            case R.id.iv_delete:

                String key = viewModel.sharedPref.getUserData().getAuthKey();
                dialogDeleteItem(key,list.get(position).getId(),"p");


                break;

            case R.id.iv_edit:
                Intent intent = AddPortfolioActivity.newIntent(ProfileActivity.this);
                intent.putExtra("comeFrom","edit");
                intent.putExtra("portfolioData",list.get(position));
                startNewActivity(intent);
                break;
        }
    }


    private void setRecycleIdentityCard(List<IdentityModel> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvIdentityCard.setLayoutManager(layoutManager);
        binding.rvIdentityCard.setAdapter(adapterIdentity);
        adapterIdentity.setList(list);
    }

    private void setRecycleCertificate(List<ProfileModel.CertificatesBean> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvCertificate.setLayoutManager(layoutManager);
        binding.rvCertificate.setAdapter(adapterCertificate);
        adapterCertificate.setList(list);
    }

    private void setRecycleBusiness(List<ProfileModel.BusinessHoursBean> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvBusinessHour.setLayoutManager(layoutManager);
        binding.rvBusinessHour.setAdapter(adapterBusinesses);
        adapterBusinesses.setList(list);
    }

    private void setRecyclerReview(List<MyReview> reviewList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvReviews.setLayoutManager(layoutManager);
        binding.rvReviews.setAdapter(adapterReview);
        adapterReview.setList(reviewList);
    }

    private void setRecyclerEducation(List<ProfileModel.EducationBean> list,boolean isEducationAll) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            binding.rvEducation.setLayoutManager(layoutManager);
            CustomEducationAdapter educationAdapter = new CustomEducationAdapter(this,isEducationAll,list,this);
            binding.rvEducation.setAdapter(educationAdapter);

    }

    @Override
    public void onEducationItemClick(View v, List<ProfileModel.EducationBean> list, int position) {
        switch (v.getId()){
            case R.id.iv_delete:

                String key = viewModel.sharedPref.getUserData().getAuthKey();
                dialogDeleteItem(key,list.get(position).getId(),"ed");

                break;

            case R.id.iv_edit:
                Intent intent = AddEducationActivity.newIntent(ProfileActivity.this);
                intent.putExtra("comeFrom","edit");
                intent.putExtra("educationBean",list.get(position));
                startNewActivity(intent);
                break;
        }
    }

    private void setRecyclerExperience(List<ProfileModel.ExperienceBean> list,boolean isExperinceAll) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvExperience.setLayoutManager(layoutManager);
        CustomExperienceAdapter customExperienceAdapter = new CustomExperienceAdapter(this,isExperinceAll,list,this);
        binding.rvExperience.setAdapter(customExperienceAdapter);
    }

    @Override
    public void onExperienceItemClick(View v, List<ProfileModel.ExperienceBean> list, int position) {
        switch (v.getId()){
            case R.id.iv_delete:

                String key = viewModel.sharedPref.getUserData().getAuthKey();
                dialogDeleteItem(key,list.get(position).getId(),"exp");



                break;

            case R.id.iv_edit:
                Intent intent = AddExperienceActivity.newIntent(ProfileActivity.this);
                intent.putExtra("comeFrom","edit");
                intent.putExtra("experienceBean",list.get(position));
                startNewActivity(intent);
                break;
        }
    }

    private List<MoreBean> getData() {
        List<MoreBean> beanList = new ArrayList<>();
        beanList.add(new MoreBean(1,"Language",1));
        beanList.add(new MoreBean(1,"Portfolio",1));
        return beanList;
    }

    private List<MoreBean> getBusinessHours() {
        List<MoreBean> beanList = new ArrayList<>();
        beanList.add(new MoreBean(1,"8:00 AM - 6:00 PM",1));
        beanList.add(new MoreBean(1,"8:00 AM - 6:00 PM",1));
        beanList.add(new MoreBean(1,"8:00 AM - 6:00 PM",1));
        beanList.add(new MoreBean(1,"8:00 AM - 6:00 PM",1));
        beanList.add(new MoreBean(1,"8:00 AM - 6:00 PM",1));
        beanList.add(new MoreBean(1,"8:00 AM - 6:00 PM",1));
        beanList.add(new MoreBean(1,"8:00 AM - 6:00 PM",1));
        return beanList;
    }

    private void setToolbar() {
        binding.tvTitle.setText("Profile");
        binding.tvTitle.setTextColor(getResources().getColor(R.color.white));
        binding.setCheck(true);
    }

    SimpleRecyclerViewAdapter<MoreBean, HolderProfileItemBinding> adapter =
            new SimpleRecyclerViewAdapter(R.layout.holder_profile_item, BR.bean, new SimpleRecyclerViewAdapter.SimpleCallback() {
                @Override
                public void onItemClick(View v, Object o) {

                }
            });




    SimpleRecyclerViewAdapter<ProfileModel.ProviderLanguagesBean, HolderLanguageItemBinding> adapterLanguage =
            new SimpleRecyclerViewAdapter(R.layout.holder_language_item, BR.language, new SimpleRecyclerViewAdapter.SimpleCallback<ProfileModel.ProviderLanguagesBean>() {
                @Override
                public void onItemClick(View view, ProfileModel.ProviderLanguagesBean o) {
                    switch (view != null ? view.getId() : 0) {
                        case R.id.iv_delete:

                            String key = viewModel.sharedPref.getUserData().getAuthKey();
                            dialogDeleteItem(key,o.getId(),"l");


                            break;

                        case R.id.iv_edit:
                            Intent intent = SelectLanguageActivity.newIntent(ProfileActivity.this);
                            intent.putExtra("comeFrom","edit");
                            intent.putExtra("languageData",o);
                            startNewActivity(intent);
                            break;
                    }


                }
            });

    SimpleRecyclerViewAdapter<ProfileModel.ProviderPortfoliosBean, HolderPortfolioItemsBinding> adapterPortfolio =
            new SimpleRecyclerViewAdapter(R.layout.holder_portfolio_items, BR.portfolio, new SimpleRecyclerViewAdapter.SimpleCallback<ProfileModel.ProviderPortfoliosBean>() {
                @Override
                public void onItemClick(View view, ProfileModel.ProviderPortfoliosBean o) {

                    switch (view != null ? view.getId() : 0) {
                        case R.id.iv_delete:

                            String key = viewModel.sharedPref.getUserData().getAuthKey();
                            dialogDeleteItem(key,o.getId(),"p");


                            break;

                        case R.id.iv_edit:
                            Intent intent = AddPortfolioActivity.newIntent(ProfileActivity.this);
                            intent.putExtra("comeFrom","edit");
                            intent.putExtra("portfolioData",o);
                            startNewActivity(intent);
                            break;
                    }

                }
            });


    SimpleRecyclerViewAdapter<IdentityModel, HolderIdentityCardBinding> adapterIdentity =
            new SimpleRecyclerViewAdapter(R.layout.holder_identity_card, BR.bean, new SimpleRecyclerViewAdapter.SimpleCallback<IdentityModel>() {
                @Override
                public void onItemClick(View v, IdentityModel o) {

                    switch (v.getId()){
                        case R.id.iv_delete:

                            String key = viewModel.sharedPref.getUserData().getAuthKey();
                            dialogDeleteItem(key,o.getId(),"i");


                            break;


                        case R.id.iv_edit:
                            Intent intent = IdentityActivity.newIntent(ProfileActivity.this);
                            intent.putExtra("comeFrom","edit");
                            intent.putExtra("IdentityData",o);
                            startNewActivity(intent);
                            break;
                    }

                }
            });

    SimpleRecyclerViewAdapter<ProfileModel.CertificatesBean, HolderCertificateItemBinding> adapterCertificate =
            new SimpleRecyclerViewAdapter(R.layout.holder_certificate_item, BR.certificate, new SimpleRecyclerViewAdapter.SimpleCallback<ProfileModel.CertificatesBean>() {
                @Override
                public void onItemClick(View view, ProfileModel.CertificatesBean o) {
                    switch (view != null ? view.getId() : 0) {
                        case R.id.iv_delete:

                            String key = viewModel.sharedPref.getUserData().getAuthKey();
                            dialogDeleteItem(key,o.getId(),"c");



                            break;

                        case R.id.iv_edit:
                            Intent intent = AddCertificateActivity.newIntent(ProfileActivity.this);
                            intent.putExtra("comeFrom","edit");
                            intent.putExtra("certificateData",o);
                            startNewActivity(intent);
                            break;

                    }

                }
            });

    SimpleRecyclerViewAdapter<ProfileModel.BusinessHoursBean, HolderBusinessHourBinding> adapterBusinesses =
            new SimpleRecyclerViewAdapter(R.layout.holder_business_hour, BR.bean, new SimpleRecyclerViewAdapter.SimpleCallback<ProfileModel.BusinessHoursBean>() {
                @Override
                public void onItemClick(View v, ProfileModel.BusinessHoursBean o) {

                }
            });

    SimpleRecyclerViewAdapter<MyReview, HolderReviewsBinding> adapterReview =
            new SimpleRecyclerViewAdapter(R.layout.holder_reviews, BR.bean, new SimpleRecyclerViewAdapter.SimpleCallback<MyReview>() {
                @Override
                public void onItemClick(View v, MyReview myReview) {

                }
            });



    private BaseCustomDialog<DialogDeactivateAccopuontBinding> dialogSuccess;

    private void dialogDeactivateAccount() {
        dialogSuccess = new BaseCustomDialog<>(ProfileActivity.this, R.layout.dialog_deactivate_accopuont, new BaseCustomDialog.Listener() {
            @Override
            public void onViewClick(View view) {
                if (view != null && view.getId() != 0) {
                    switch (view.getId()) {
                        case R.id.btn_submit:
                            dialogSuccess.dismiss();

                            viewModel.accountSetting(viewModel.sharedPref.getUserData().getAuthKey(),
                                    "0");
                            break;
                        case R.id.btn_cancel:
                            dialogSuccess.dismiss();
                            break;
                    }
                }
            }
        });
        dialogSuccess.getWindow().setBackgroundDrawableResource(R.color.transparance_whhite);
        dialogSuccess.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialogSuccess.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogSuccess.show();
    }

    private BaseCustomDialog<DialogDeleteAccouontBinding> dialogDelete;
    private void dialogDeleteAccount() {
        dialogDelete = new BaseCustomDialog<>(ProfileActivity.this, R.layout.dialog_delete_accouont, new BaseCustomDialog.Listener() {
            @Override
            public void onViewClick(View view) {
                if (view != null && view.getId() != 0) {
                    switch (view.getId()) {
                        case R.id.btn_submit:
                            dialogDelete.dismiss();
                            viewModel.accountSetting(viewModel.sharedPref.getUserData().getAuthKey(),
                                    "1");
                            break;
                        case R.id.btn_cancel:
                            dialogDelete.dismiss();
                            break;
                    }
                }
            }
        });

        dialogDelete.getWindow().setBackgroundDrawableResource(R.color.transparance_whhite);
        dialogDelete.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialogDelete.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogDelete.show();

    }


    private BaseCustomDialog<DialogDeleteAccouontBinding> deleteItemDialog;
    private void dialogDeleteItem(String key,int id,String type) {
        deleteItemDialog = new BaseCustomDialog<>(ProfileActivity.this, R.layout.delete_confirmation_dialog, new BaseCustomDialog.Listener() {
            @Override
            public void onViewClick(View view) {
                if (view != null && view.getId() != 0) {
                    switch (view.getId()) {
                        case R.id.btn_submit:

                            if (type.equalsIgnoreCase("p")){
                                viewModel.deletePortFolio(key,id);
                            }else if (type.equalsIgnoreCase("c")){
                                viewModel.deleteCertificate(key, id);
                            }else if (type.equalsIgnoreCase("l")){
                                viewModel.deleteLanguage(key,id);
                            }
                            else if (type.equalsIgnoreCase("i")){

                                viewModel.deleteIdentity(key,id);

                            }
                            else if (type.equalsIgnoreCase("ed")){

                                viewModel.deleteEducation(key,id);

                            } else if (type.equalsIgnoreCase("exp")){

                                viewModel.deleteExperience(key,id);

                            }

                            deleteItemDialog.dismiss();
                            break;
                        case R.id.btn_cancel:
                           deleteItemDialog.dismiss();
                            break;
                    }
                }
            }
        });

        deleteItemDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        deleteItemDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        deleteItemDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        deleteItemDialog.show();

    }

    @Override
    protected void onBackPressed(boolean animate) {
        supportFinishAfterTransition();
        super.onBackPressed(animate);
    }
}
