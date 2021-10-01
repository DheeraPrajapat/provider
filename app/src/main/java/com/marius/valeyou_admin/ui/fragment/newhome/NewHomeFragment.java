package com.marius.valeyou_admin.ui.fragment.newhome;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.CitiesModel;
import com.marius.valeyou_admin.data.beans.NotificationBadgeModel;
import com.marius.valeyou_admin.data.beans.StatesModel;
import com.marius.valeyou_admin.data.beans.base.MoreBean;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.categoriesBean.CategoryDataBean;
import com.marius.valeyou_admin.data.beans.singninbean.SignInModel;
import com.marius.valeyou_admin.data.beans.suggesstions.SuggesstionModel;
import com.marius.valeyou_admin.data.local.SharedPref;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.DialogDeactivateAccopuontBinding;
import com.marius.valeyou_admin.databinding.DialogDeleteAccouontBinding;
import com.marius.valeyou_admin.databinding.FragmentNewHomeBinding;
import com.marius.valeyou_admin.databinding.HolderCitiesBinding;
import com.marius.valeyou_admin.databinding.HolderFilterCategoryBinding;
import com.marius.valeyou_admin.databinding.HolderMoreBinding;
import com.marius.valeyou_admin.databinding.HolderSetCategoryBinding;
import com.marius.valeyou_admin.databinding.HolderStatesBinding;
import com.marius.valeyou_admin.databinding.LogoutDialogBinding;
import com.marius.valeyou_admin.di.base.adapter.SimpleRecyclerViewAdapter;
import com.marius.valeyou_admin.di.base.dialog.BaseCustomDialog;
import com.marius.valeyou_admin.di.base.view.AppFragment;
import com.marius.valeyou_admin.ui.StripeConnectActivity;
import com.marius.valeyou_admin.ui.activity.bids.PlacedBidsActivity;
import com.marius.valeyou_admin.ui.activity.block.BlockActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.aboutus.AboutUsFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.changepassword.ChangePasswordFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.helpnsupport.HelpAndSupportFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.privacy_policy.PrivacyPolicyFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.ProfileActivity;
import com.marius.valeyou_admin.ui.activity.language.SelectLanguageActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.activity.main.MainActivity;
import com.marius.valeyou_admin.ui.activity.notification.NotificationActivity;
import com.marius.valeyou_admin.ui.fragment.IdentityVerficationActivity;
import com.marius.valeyou_admin.ui.fragment.home.HomeFragment;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class NewHomeFragment extends AppFragment<FragmentNewHomeBinding, NewHomeFragmentVM> implements HomePageCategoryAdapter.ProductCallback {
    public static final String TAG = "NewHomeFragment";

    @Inject
    SharedPref sharedPref;
    String stripeId;
    private MainActivity mainActivity;
    private BottomSheetBehavior bottomSheetBehavior;
    boolean flag = false;
    List<String> statesList;
    private List<MoreBean> filterCat;
    private String cat_ids = "";
    private String state = "";
    String  min,max;
    List<String> citiesList;
    List<StatesModel> statesModelList;
    List<CitiesModel> citiesModelList;
    String city;
    private List<CategoryDataBean> categoryListBeans;
    String state_id = "";
    String city_id = "";

    private boolean isAccount = false;

    List<SuggesstionModel> suggestionsModelList = new ArrayList<>();

    public static Fragment newInstance() {
        return new NewHomeFragment();
    }

    @Override
    protected BindingFragment<NewHomeFragmentVM> getBindingFragment() {
        return new BindingFragment<>(R.layout.fragment_new_home, NewHomeFragmentVM.class);
    }

    @Override
    public void onStart() {
        super.onStart();

        binding.setBean(viewModel.sharedPref.getUserData());
        binding.nav.setBean(viewModel.sharedPref.getUserData());
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getCount();
    }

    @Override
    protected void subscribeToEvents(NewHomeFragmentVM vm) {

        Log.d("Sanjeev","===" +sharedPref.getUserData().getAuthKey() + "==" + sharedPref.getUserData().getId());

        mainActivity = (MainActivity) getActivity();
        mainActivity.hideBottomNav(false);
        mainActivity.hideBackButton();


        vm.getCategories(0);
        binding.setCheck(false);

        binding.bottomLayout.ftPrice.rangeSeekbar3.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                  min = String.valueOf(minValue);
                  max = String.valueOf(maxValue);
                binding.bottomLayout.ftPrice.startRate.setText(minValue+" Brl");
                binding.bottomLayout.ftPrice.endRate.setText(""+maxValue+" Brl");
            }
        });


        vm.base_click.observe(this, new Observer<View>() {
            @Override
            public void onChanged(View view) {
                switch (view.getId()) {
                    case R.id.notificationIcon:
                        Intent intent = NotificationActivity.newIntent(getActivity());
                        startNewActivity(intent);
                        break;
                    case R.id.cv_profile:
                        binding.drawerView.openDrawer(GravityCompat.START);
                        break;

                    case R.id.rl_profile:
                        binding.drawerView.openDrawer(GravityCompat.START);
                        break;
                    case R.id.iv_profile:
                        binding.drawerView.openDrawer(GravityCompat.START);
                        break;

                    case R.id.cv_pic:
                        binding.drawerView.openDrawer(GravityCompat.START);
                        break;

                    case R.id.iv_close:
                        binding.drawerView.closeDrawers();
                        break;

                    case R.id.view_profile:
                        Intent intent1 = ProfileActivity.newIntent(getActivity());
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation(getActivity(),binding.nav.profileImage,"profile");
                        startActivity(intent1,options.toBundle());
                        break;

                    case R.id.view_img:
                        Intent intent3 = ProfileActivity.newIntent(getActivity());
                        ActivityOptionsCompat options1 = ActivityOptionsCompat.
                                makeSceneTransitionAnimation(getActivity(),binding.nav.profileImage,"profile");
                        startActivity(intent3,options1.toBundle());
                        break;

                    case R.id.imageViewfilter:
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;

                    case R.id.iv_cancel:
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;

                    case R.id.apply:
                        // Apply Filter
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        applyFilter();
                        break;
                    case R.id.et_state:
                        flag = true;
//                        binding.bottomLayout.ftState.spinner.performClick();
                        break;


                    case R.id.et_city:
//                        binding.bottomLayout.ftState.citySpinner.performClick();
                        break;

                    case R.id.cv_remote_job:
                        binding.setCheck(false);
                        vm.getCategories(0);
                        break;
                    case R.id.cv_local_job:
                        vm.getCategories(1);
                        binding.setCheck(true);
                        break;

                    case R.id.map:
                        mainActivity.addSubFragmentWithoutCache(HomeFragment.TAG, HomeFragment.newInstance());
                        break;

                    case R.id.cv_my_account:
                        if (isAccount) {
                            isAccount = false;
                            binding.nav.llAccountsItems.setVisibility(View.GONE);

                        } else {
                            isAccount = true;
                            binding.nav.llAccountsItems.setVisibility(View.VISIBLE);

                        }

                        break;

                    case R.id.txt_identity_verification:
                        binding.drawerView.closeDrawers();
                        Intent intent2 = IdentityVerficationActivity.newIntent(getActivity());
                        startNewActivity(intent2);
                        break;

                    case R.id.txt_deactivate_account:
                        binding.drawerView.closeDrawers();
                        dialogDeactivateAccount();
                        break;

                    case R.id.cv_list:
                        //vm.getListOfCateegory(1, "");
                        binding.setList(true);
                        break;

                    case R.id.cv_map:
                        mainActivity.addSubFragmentWithoutCache(HomeFragment.TAG,HomeFragment.newInstance());
                        break;

                    case R.id.txt_delete_account:
                        binding.drawerView.closeDrawers();
                        dialogDeleteAccount();
                        break;

                    case R.id.txt_change_password:
                        binding.drawerView.closeDrawers();
                        Intent in = ChangePasswordFragment.newIntent(getActivity());
                        startNewActivity(in);
                        break;



                }
            }
        });

        vm.categoriesbeandata.observe(this, new SingleRequestEvent.RequestObserver<List<CategoryDataBean>>() {
            @Override
            public void onRequestReceived(@NonNull Resource<List<CategoryDataBean>> resource) {
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        categoryListBeans = resource.data;
                        ftAdapter.setList(resource.data);
                        setRecyclerView(categoryListBeans);
                        break;
                    case ERROR:
                        vm.error.setValue(resource.message);
                        if (resource.message.equalsIgnoreCase(getResources().getString(R.string.unauthorised))){
                            sharedPref.deleteAll();
                            Intent intent1 = LoginActivity.newIntent(getActivity());
                            startNewActivity(intent1, true, true);

                        }
                        break;
                    case WARN:
                        vm.warn.setValue(resource.message);
                        break;
                }
            }
        });

//        binding.bottomLayout.ftState.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                if (flag) {       // Here
//
//                    binding.bottomLayout.ftState.etState.setText(binding.bottomLayout.ftState.spinner.getSelectedItem().toString());
//
//                    for (i = 0; i < statesModelList.size(); i++) {
//                        String name = statesModelList.get(i).getName();
//                        if (name.equalsIgnoreCase(binding.bottomLayout.ftState.spinner.getSelectedItem().toString())) {
//
//                            state_id = String.valueOf(statesModelList.get(i).getId());
//
//                        }
//                    }
//
//                    vm.getCities(Integer.parseInt(state_id));
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

//        binding.bottomLayout.ftState.citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                binding.bottomLayout.ftState.etCity.setText(binding.bottomLayout.ftState.citySpinner.getSelectedItem().toString());
//
//
//                for (i = 0; i < citiesModelList.size(); i++) {
//
//                    String name = citiesModelList.get(i).getName();
//
//                    if (name.equalsIgnoreCase(binding.bottomLayout.ftState.citySpinner.getSelectedItem().toString())) {
//
//                        city_id = String.valueOf(citiesModelList.get(i).getId());
//
//                    }
//                }
//
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        vm.accountSettingBean.observe(this, new SingleRequestEvent.RequestObserver<SimpleApiResponse>() {
            @Override
            public void onRequestReceived(@NonNull Resource<SimpleApiResponse> resource) {

                switch (resource.status) {
                    case LOADING:
                        showProgressDialog(R.string.wait);
                        break;
                    case SUCCESS:
                        dismissProgressDialog();
                       // if (resource.message.equalsIgnoreCase("account deactivate successfully")){
                            vm.success.setValue(resource.message);
                      //  }
                        sharedPref.deleteAll();
                        Intent intent1 = LoginActivity.newIntent(getActivity());
                        startNewActivity(intent1, true, true);
                        getActivity().finishAffinity();
                        break;
                    case ERROR:
                        dismissProgressDialog();
                        if (resource.message.equalsIgnoreCase(getResources().getString(R.string.unauthorised))){
                            sharedPref.deleteAll();
                            Intent intent = LoginActivity.newIntent(getActivity());
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


        vm.notificationbadgeEventRequest.observe(this, new SingleRequestEvent.RequestObserver<NotificationBadgeModel>() {
            @Override
            public void onRequestReceived(@NonNull Resource<NotificationBadgeModel> resource) {
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        mainActivity.badgeCount(resource.data.getCount());

                        stripeId = resource.data.getStripeId();

                        if (resource.data.getNotification()>0){

                            binding.notificationCount.setVisibility(View.VISIBLE);
                            if (resource.data.getNotification()>9){
                                binding.notificationCount.setText("9+");
                            }else{
                                binding.notificationCount.setText(resource.data.getNotification()+"");
                            }

                        }else{
                            binding.notificationCount.setVisibility(View.GONE);
                        }

                        break;
                    case WARN:
                        vm.warn.setValue(resource.message);
                        break;
                    case ERROR:
                        vm.error.setValue(resource.message);
                        break;
                }
            }
        });


//        vm.statesEventRequest.observe(this, new SingleRequestEvent.RequestObserver<List<StatesModel>>() {
//            @Override
//            public void onRequestReceived(@NonNull Resource<List<StatesModel>> resource) {
//                switch (resource.status) {
//                    case LOADING:
//                        break;
//                    case SUCCESS:
//                        statesModelList = resource.data;
//                        if (statesModelList.size()>0) {
//                            stateadapter.setList(statesModelList);
//                        }else{
//                            Toast.makeText(mainActivity, "No State Found", Toast.LENGTH_SHORT).show();
//                        }
//
//                        break;
//                    case WARN:
//                        vm.warn.setValue(resource.message);
//                        break;
//                    case ERROR:
//                        vm.error.setValue(resource.message);
//                        break;
//                }
//            }
//        });

        binding.bottomLayout.ftState.etState.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input = charSequence.toString();
                if (input.length()>0){
                    binding.bottomLayout.ftState.rvStates.setVisibility(View.VISIBLE);
                    vm.getStates(input);
                }else{
                    binding.bottomLayout.ftState.rvStates.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        vm.statesEventRequest.observe(this, new SingleRequestEvent.RequestObserver<List<StatesModel>>() {
            @Override
            public void onRequestReceived(@NonNull Resource<List<StatesModel>> resource) {
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        statesModelList = resource.data;
                        if (statesModelList.size()>0) {
                            stateadapter.setList(statesModelList);
                        }else{
                            Toast.makeText(mainActivity, "No State Found", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case WARN:
                        vm.warn.setValue(resource.message);
                        break;
                    case ERROR:
                        vm.error.setValue(resource.message);
                        break;
                }
            }
        });

        binding.bottomLayout.ftState.etCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input = charSequence.toString();
                if (input.length()>0){
                    binding.bottomLayout.ftState.rvCities.setVisibility(View.VISIBLE);
                    vm.getCities(Integer.parseInt(state_id),input);
                }else{
                    binding.bottomLayout.ftState.rvCities.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        vm.citiesEventRequest.observe(this, new SingleRequestEvent.RequestObserver<List<CitiesModel>>() {
            @Override
            public void onRequestReceived(@NonNull Resource<List<CitiesModel>> resource) {
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:

                        citiesModelList = resource.data;
                        if (citiesModelList.size()>0) {
                            cityadapter.setList(citiesModelList);
                        }else {
                            Toast.makeText(mainActivity, "No City Found", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case WARN:
                        vm.warn.setValue(resource.message);
                        break;
                    case ERROR:
                        vm.error.setValue(resource.message);
                        break;
                }
            }
        });

//        vm.citiesEventRequest.observe(this, new SingleRequestEvent.RequestObserver<List<CitiesModel>>() {
//            @Override
//            public void onRequestReceived(@NonNull Resourc e<List<CitiesModel>> resource) {
//                switch (resource.status) {
//                    case LOADING:
//                        break;
//                    case SUCCESS:
//
//                        citiesModelList = resource.data;
//                        citiesList = new ArrayList<>();
//                        for (int i = 0;i<resource.data.size();i++){
//                            citiesList.add(resource.data.get(i).getName());
//                        }
//                        citiesList.add(0,"Select City");
//                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, citiesList);
//                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        binding.bottomLayout.ftState.citySpinner.setAdapter(adapter);
//
//                        break;
//                    case WARN:
//                        vm.warn.setValue(resource.message);
//                        break;
//                    case ERROR:
//                        vm.error.setValue(resource.message);
//                        if (resource.message.equalsIgnoreCase(getResources().getString(R.string.unauthorised))){
//                            sharedPref.deleteAll();
//                            Intent intent1 = LoginActivity.newIntent(getActivity());
//                            startNewActivity(intent1, true, true);
//
//                        }
//                        break;
//                }
//            }
//        });
        vm.citiesEventRequest.observe(this, new SingleRequestEvent.RequestObserver<List<CitiesModel>>() {
            @Override
            public void onRequestReceived(@NonNull Resource<List<CitiesModel>> resource) {
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:

                        citiesModelList = resource.data;
                        if (citiesModelList.size()>0) {
                            cityadapter.setList(citiesModelList);
                        }else {
                            Toast.makeText(mainActivity, "No City Found", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case WARN:
                        vm.warn.setValue(resource.message);
                        break;
                    case ERROR:
                        vm.error.setValue(resource.message);
                        break;
                }
            }
        });





        vm.suggestionsEventRequest.observe(this, new Observer<Resource<List<SuggesstionModel>>>() {
            @Override
            public void onChanged(Resource<List<SuggesstionModel>> listResource) {
                switch (listResource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        binding.typingText.setVisibility(View.GONE);
                        suggestionsModelList = listResource.data;
                        adapterSuggestions.setList(suggestionsModelList);

                        break;
                    case WARN:
                        vm.warn.setValue(listResource.message);
                        break;
                    case ERROR:
                        vm.error.setValue(listResource.message);
                        if (listResource.message.equalsIgnoreCase(getResources().getString(R.string.unauthorised))){
                            sharedPref.deleteAll();
                            Intent intent1 = LoginActivity.newIntent(getActivity());
                            startNewActivity(intent1, true, true);

                        }
                        break;
                }
            }
        });

        vm.userBean.observe(this, new SingleRequestEvent.RequestObserver<SignInModel>() {
            @Override
            public void onRequestReceived(@NonNull Resource<SignInModel> resource) {
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:

                        vm.sharedPref.deleteAll();
                        LoginManager.getInstance().logOut();
                        vm.success.setValue(resource.message);
                        Intent intent1 = LoginActivity.newIntent(getActivity());
                        startNewActivity(intent1, true, true);
                        getActivity().finishAffinity();

                        break;
                    case ERROR:
                        vm.error.setValue(resource.message);
                        if (resource.message.equalsIgnoreCase(getResources().getString(R.string.unauthorised))){
                            sharedPref.deleteAll();
                            Intent intent = LoginActivity.newIntent(getActivity());
                            startNewActivity(intent, true, true);

                        }

                        break;
                    case WARN:
                        vm.warn.setValue(resource.message);
                        break;
                }
            }
        });

        binding.etSearch.addTextChangedListener(textWatcher);




        setBottomsheet();
        vm.getStates("");
//        moveViewWithDrawer();
        setRecyclerViewMore();
        setFilterRecycler();
        setAdapterSuggestions();
        setStatesAdapter();
        setCitiesAdapter();

    }

    private void setStatesAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.bottomLayout.ftState.rvStates.setLayoutManager(layoutManager);
        binding.bottomLayout.ftState.rvStates.setAdapter(stateadapter);
    }

    private void setCitiesAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.bottomLayout.ftState.rvCities.setLayoutManager(layoutManager);
        binding.bottomLayout.ftState.rvCities.setAdapter(cityadapter);
    }

    SimpleRecyclerViewAdapter<StatesModel, HolderStatesBinding> stateadapter =
            new SimpleRecyclerViewAdapter(R.layout.holder_states, BR.bean, new SimpleRecyclerViewAdapter.SimpleCallback<StatesModel>() {
                @Override
                public void onItemClick(View v, StatesModel bean) {
                    switch (v != null ? v.getId() : 0) {
                        case R.id.ll_states:
                            binding.bottomLayout.ftState.etState.setText(bean.getName());
                            binding.bottomLayout.ftState.rvStates.setVisibility(View.GONE);
                            state_id = String.valueOf(bean.getId());
                            viewModel.getCities(bean.getId(),"");
                            binding.bottomLayout.ftState.etCity.setEnabled(true);
                            break;
                    }
                }
            });

    SimpleRecyclerViewAdapter<CitiesModel, HolderCitiesBinding> cityadapter =
            new SimpleRecyclerViewAdapter(R.layout.holder_cities, BR.bean, new SimpleRecyclerViewAdapter.SimpleCallback<CitiesModel>() {
                @Override
                public void onItemClick(View v, CitiesModel bean) {
                    switch (v != null ? v.getId() : 0) {
                        case R.id.ll_states:
                            city_id = String.valueOf(bean.getId());
                            binding.bottomLayout.ftState.etCity.setText(bean.getName());
                            binding.bottomLayout.ftState.rvCities.setVisibility(View.GONE);

                            break;
                    }
                }
            });


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String str = charSequence.toString();
            binding.typingText.setVisibility(View.VISIBLE);
            binding.typingText.setText("Searching for " + '"' + str + '"');
            if (str.length() > 0) {
                binding.rvSuggestions.setVisibility(View.VISIBLE);
                viewModel.searchSuggestions(str);
            } else {
                binding.typingText.setVisibility(View.GONE);
                suggestionsModelList.clear();
                binding.rvSuggestions.setVisibility(View.GONE);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void setAdapterSuggestions() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.rvSuggestions.setLayoutManager(layoutManager);
        binding.rvSuggestions.setAdapter(adapterSuggestions);
    }

    SimpleRecyclerViewAdapter<SuggesstionModel, HolderFilterCategoryBinding> adapterSuggestions =
            new SimpleRecyclerViewAdapter<>(R.layout.holder_new_home_suggestions, BR.bean, new SimpleRecyclerViewAdapter.SimpleCallback<SuggesstionModel>() {
                @Override
                public void onItemClick(View v, SuggesstionModel moreBean) {

                    if (moreBean.getSearch_type() == 0) {

                        mainActivity.addSubFragmentWithoutCache(HomeFragment.TAG, HomeFragment.newInstance("", "", String.valueOf(moreBean.getId()), "", state,city,state_id,city_id,"",""));

                    } else {

                        mainActivity.addSubFragmentWithoutCache(HomeFragment.TAG, HomeFragment.newInstance(String.valueOf(moreBean.getId()), "", "", "", state,city,state_id,city_id,"",""));

                    }

                    binding.rvSuggestions.setVisibility(View.GONE);

                }
            });

    private void setFilterRecycler() {
        binding.bottomLayout.setType(3);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.bottomLayout.rvFilterCat.setLayoutManager(layoutManager);
        binding.bottomLayout.rvFilterCat.setAdapter(adapterCat);
        filterCat = getListData();
        adapterCat.setList(filterCat);
    }

    private List<MoreBean> getListData() {
        List<MoreBean> beanList = new ArrayList<>();
       // beanList.add(new MoreBean(1, "Price", 1));
        beanList.add(new MoreBean(3, getResources().getString(R.string.distance), 1));
        beanList.add(new MoreBean(4, getResources().getString(R.string.region), 0));
        return beanList;
    }

    SimpleRecyclerViewAdapter<MoreBean, HolderFilterCategoryBinding> adapterCat =
            new SimpleRecyclerViewAdapter<>(R.layout.holder_filter_category, BR.bean, new SimpleRecyclerViewAdapter.SimpleCallback<MoreBean>() {
                @Override
                public void onItemClick(View v, MoreBean moreBean) {
                    for (int j = 0; j < filterCat.size(); j++) {
                        if (filterCat.get(j).getId() == moreBean.getId()) {
                            filterCat.get(j).setImage(1);
                        } else {
                            filterCat.get(j).setImage(0);
                        }
                    }
                    adapterCat.setList(filterCat);
                    binding.bottomLayout.setType(moreBean.getId());
                }
            });

    //bottomsheet
    private void setBottomsheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.llOne);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                switch (i) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                float s = (1f - v / 2f);
            }
        });
    }


    private void setRecyclerViewMore() {
        binding.nav.rvMore.setLayoutManager(new LinearLayoutManager(baseContext, RecyclerView.VERTICAL, false));
        SimpleRecyclerViewAdapter<MoreBean, HolderMoreBinding> adapter_more = new SimpleRecyclerViewAdapter<>(R.layout.holder_more, BR.bean, new SimpleRecyclerViewAdapter.SimpleCallback<MoreBean>() {
            @Override
            public void onItemClick(View v, MoreBean shopBean) {
                binding.drawerView.closeDrawers();
                Intent intent;
                switch (shopBean.getId()) {

                    case 1:
                        if (stripeId == null || stripeId.isEmpty()){
                            Intent intent1 = StripeConnectActivity.newIntent(getActivity());
                            intent1.putExtra("comeFrom","add");
                            startNewActivity(intent1);
                        }else{
                            Intent intent1 = StripeConnectActivity.newIntent(getActivity());
                            intent1.putExtra("comeFrom","edit");
                            intent1.putExtra("id",stripeId);
                            startNewActivity(intent1);
                        }

                        break;

                    case 2:

                        Intent notificationIntent = NotificationActivity.newIntent(getActivity());
                        startNewActivity(notificationIntent);

                        break;

                    case 3:

                        Intent langInt = SelectLanguageActivity.newIntent(getActivity());
                        startNewActivity(langInt);

                        break;

                    case 4:

                        Intent blockIntent = BlockActivity.newIntent(getActivity());
                        startNewActivity(blockIntent);

                        break;

                    case 5:
                        mainActivity.addSubFragment(TAG, PrivacyPolicyFragment.newInstance());
                        break;
                    case 6:
                        mainActivity.addSubFragment(TAG, HelpAndSupportFragment.newInstance());
                        break;
                    case 7:
                        mainActivity.addSubFragment(TAG, AboutUsFragment.newInstance());
                        break;
                    case 8:
                        dialogLogout();
                        break;

                }
            }
        });
        binding.nav.rvMore.setAdapter(adapter_more);
        adapter_more.setList(getMoreData());
    }

    private List<MoreBean> getMoreData() {
        List<MoreBean> menuBeanList = new ArrayList<>();
        menuBeanList.add(new MoreBean(1,getResources().getString(R.string.stripe_text),R.drawable.ic_about_us_icon));
        menuBeanList.add(new MoreBean(2, getResources().getString(R.string.notifications), R.drawable.ic_notification_icon));
       // menuBeanList.add(new MoreBean(3, getResources().getString(R.string.select_language), R.drawable.ic_language));
        menuBeanList.add(new MoreBean(4, getResources().getString(R.string.blocked_contacts), R.drawable.ic_block));
        menuBeanList.add(new MoreBean(5, getResources().getString(R.string.terms), R.drawable.ic_privacy_policy_icon));
        menuBeanList.add(new MoreBean(6, getResources().getString(R.string.help_support), R.drawable.ic_help_icon));
        menuBeanList.add(new MoreBean(7, getResources().getString(R.string.about_us), R.drawable.ic_about_us_icon));
        menuBeanList.add(new MoreBean(8, getResources().getString(R.string.logout_account), R.drawable.ic_logout_icon));
        return menuBeanList;
    }

    //filter adapter
    SimpleRecyclerViewAdapter<CategoryDataBean, HolderSetCategoryBinding> ftAdapter =
            new SimpleRecyclerViewAdapter<>(R.layout.holder_set_category, BR.bean, new SimpleRecyclerViewAdapter.SimpleCallback<CategoryDataBean>() {
                @Override
                public void onItemClick(View v, CategoryDataBean moreBean) {
                    for (int j = 0; j < categoryListBeans.size(); j++) {
                        if (categoryListBeans.get(j).getId() == moreBean.getId()) {
                            if (moreBean.isSelected()) {
                                categoryListBeans.get(j).setSelected(false);
                            } else {
                                categoryListBeans.get(j).setSelected(true);
                            }
                        }
                    }
                    ftAdapter.setList(categoryListBeans);
                }
            });


    private void setRecyclerView(List<CategoryDataBean> list) {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        HomePageCategoryAdapter adapter = new HomePageCategoryAdapter(getContext(), this::onItemClick, list);
        binding.rvCategories.setLayoutManager(layoutManager);
        binding.rvCategories.setAdapter(adapter);
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
                            viewModel.logoutApi(id, viewModel.sharedPref.getUserData().getAuthKey());
                            dialogLogout.dismiss();
                            break;
                        case R.id.btn_cancel:
                            dialogLogout.dismiss();
                            break;
                    }
                }
            }
        });
        dialogLogout.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialogLogout.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialogLogout.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogLogout.show();
    }


    public void showDialog(Activity activity, List<CategoryDataBean.SubCategoriesBean> subCategoriesBean) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.sub_cat_custom_layout);

        RecyclerView recycler_view = dialog.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        SubCatAdapter subCatAdapter = new SubCatAdapter(activity, subCategoriesBean);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setAdapter(subCatAdapter);
        Button cancel = dialog.findViewById(R.id.cancel);
        Button done = dialog.findViewById(R.id.done);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sub_cat_ids = "";
                String cat_id = "";
                for (int i = 0; i < subCategoriesBean.size(); i++) {
                    if (subCategoriesBean.get(i).isCheck()) {
                        if (sub_cat_ids.isEmpty()) {
                            sub_cat_ids = String.valueOf(subCategoriesBean.get(i).getId());
                        } else {
                            sub_cat_ids = sub_cat_ids + "," + subCategoriesBean.get(i).getId();
                        }
                    }
                }
                cat_id = String.valueOf(subCategoriesBean.get(0).getCategoryId());

                mainActivity.addSubFragmentWithoutCache(HomeFragment.TAG, HomeFragment.newInstance(cat_id, sub_cat_ids, "", "", state,city,state_id,city_id,"",""));

                dialog.cancel();


            }
        });

        dialog.show();
    }


    private void applyFilter() {
        cat_ids = "";
        String distance = String.valueOf(binding.bottomLayout.ftDistance.distanceSlider.getProgress() * 10);
        if (distance.equalsIgnoreCase("0")) {
            distance = "";
        }

        state = binding.bottomLayout.ftState.etState.getText().toString();
        city = binding.bottomLayout.ftState.etCity.getText().toString();
        if (state.equalsIgnoreCase("select state")) {
            state = "";
        }
        if (city.equalsIgnoreCase("select city")) {
            city = "";
        }

        mainActivity.addSubFragmentWithoutCache(HomeFragment.TAG, HomeFragment.newInstance(cat_ids, "", "", distance, state,city,state_id,city_id,"",""));

    }


    @Override
    public void onItemClick(View v, List<CategoryDataBean> moreBeans, int pos) {

        List<CategoryDataBean.SubCategoriesBean> subCategoriesBean = moreBeans.get(pos).getSubCategories();

        if (moreBeans.get(pos).getType() == 0) {

            if (moreBeans.get(pos).getSubCategories().size() > 0) {

                showDialog(getActivity(), subCategoriesBean);

            } else {

                mainActivity.addSubFragmentWithoutCache(HomeFragment.TAG, HomeFragment.newInstance(String.valueOf(moreBeans.get(pos).getId()), "", "", "", state,city,state_id,city_id,"",""));

            }

        } else {
            mainActivity.addSubFragmentWithoutCache(HomeFragment.TAG, HomeFragment.newInstance(String.valueOf(moreBeans.get(pos).getId()), "", "", "", state,city,state_id,city_id,"",""));

        }
    }

    private BaseCustomDialog<DialogDeactivateAccopuontBinding> dialogSuccess;

    private void dialogDeactivateAccount() {
        dialogSuccess = new BaseCustomDialog<>(getActivity(), R.layout.dialog_deactivate_accopuont, new BaseCustomDialog.Listener() {
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
        dialogDelete = new BaseCustomDialog<>(getActivity(), R.layout.dialog_delete_accouont, new BaseCustomDialog.Listener() {
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
}
