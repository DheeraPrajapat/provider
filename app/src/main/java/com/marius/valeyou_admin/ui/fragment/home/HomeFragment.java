package com.marius.valeyou_admin.ui.fragment.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.facebook.login.LoginManager;
import com.facebook.share.Share;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.CitiesModel;
import com.marius.valeyou_admin.data.beans.LocationsBean;
import com.marius.valeyou_admin.data.beans.NotificationBadgeModel;
import com.marius.valeyou_admin.data.beans.StatesModel;
import com.marius.valeyou_admin.data.beans.base.MoreBean;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.business.BusinessHoursModel;
import com.marius.valeyou_admin.data.beans.categoriesBean.CategoryDataBean;
import com.marius.valeyou_admin.data.beans.map.MapListModel;
import com.marius.valeyou_admin.data.beans.singninbean.SignInModel;
import com.marius.valeyou_admin.data.beans.suggesstions.SuggesstionModel;
import com.marius.valeyou_admin.data.local.SharedPref;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.DialogDeactivateAccopuontBinding;
import com.marius.valeyou_admin.databinding.DialogDeleteAccouontBinding;
import com.marius.valeyou_admin.databinding.FragmentHomeBinding;
import com.marius.valeyou_admin.databinding.HolderFilterCategoryBinding;
import com.marius.valeyou_admin.databinding.HolderListItemsBinding;
import com.marius.valeyou_admin.databinding.HolderMapItemsBinding;
import com.marius.valeyou_admin.databinding.HolderMoreBinding;
import com.marius.valeyou_admin.databinding.HolderSetCategoryBinding;
import com.marius.valeyou_admin.databinding.HolderSetStateBinding;
import com.marius.valeyou_admin.databinding.HolderSuggestionsBinding;
import com.marius.valeyou_admin.di.base.adapter.SimpleRecyclerViewAdapter;
import com.marius.valeyou_admin.di.base.dialog.BaseCustomDialog;
import com.marius.valeyou_admin.di.base.view.AppFragment;
import com.marius.valeyou_admin.ui.StripeConnectActivity;
import com.marius.valeyou_admin.ui.activity.bids.PlacedBidsActivity;
import com.marius.valeyou_admin.ui.activity.block.BlockActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.aboutus.AboutUsFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.changepassword.ChangePasswordFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.helpnsupport.HelpAndSupportFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.jobhistory.currentjob.jobdetailsone.JobDetailsOneActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.privacy_policy.PrivacyPolicyFragment;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.ProfileActivity;
import com.marius.valeyou_admin.ui.activity.language.SelectLanguageActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.activity.main.MainActivity;
import com.marius.valeyou_admin.ui.activity.notification.NotificationActivity;
import com.marius.valeyou_admin.ui.fragment.IdentityVerficationActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.closedetails.JobDetailsActivity;
import com.marius.valeyou_admin.util.Constants;
import com.marius.valeyou_admin.util.databinding.ImageViewBindingUtils;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;
import com.marius.valeyou_admin.util.location.LiveLocationDetecter;
import com.marius.valeyou_admin.util.location.LocCallback;
import com.marius.valeyou_admin.util.permission.PermissionHandler;
import com.marius.valeyou_admin.util.permission.Permissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

public class HomeFragment extends AppFragment<FragmentHomeBinding, HomeFragmentVM> implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {
    public static final String TAG = "HomeFragment";
    private MainActivity mainActivity;
    String authKey;
    String stripeId;
    @Inject
    SharedPref sharedPref;
    private boolean isAccount = false;
    boolean flag = false;
    private Location mCurrentlocation;
    @Nullable
    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;
    List<MapListModel> projectsList;
    List<SuggesstionModel> suggestionsModelList;
    private BottomSheetBehavior bottomSheetBehavior;
    private List<MoreBean> filterCat;

    List<String> statesList;
    List<String> citiesList;
    List<StatesModel> statesModelList;
    List<CitiesModel> citiesModelList;
    String state_id = "";
    String city_id = "";
    private String cat_ids = "";
    private String state = "";
    String  min,max;
    String city;
    private List<CategoryDataBean> categoryListBeans;
    String cat_id="";
    String sub_cat_id="";
    String id = "";
    String distance= "";
    String end_price = "";
    String start_price = "";

    public static Fragment newInstance() {
        return new HomeFragment();
    }

    public static Fragment newInstance(String cat_id, String sub_cat_id,String id,String distance,String state, String city, String state_id,String city_id,String start_price, String end_price) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("cat_id", cat_id);
        args.putString("sub_cat_id", sub_cat_id);
        args.putString("id", id);
        args.putString("distance", distance);
        args.putString("state", state);
        args.putString("city", city);
        args.putString("state_id", state_id);
        args.putString("city_id", city_id);
        args.putString("start_price", start_price);
        args.putString("end_price", end_price);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected BindingFragment<HomeFragmentVM> getBindingFragment() {
        return new BindingFragment<>(R.layout.fragment_home, HomeFragmentVM.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.setBean(viewModel.sharedPref.getUserData());
        binding.nav.setBean(viewModel.sharedPref.getUserData());
    }

    private void getCurrentLocation() {
        Permissions.check(baseContext, Manifest.permission.ACCESS_FINE_LOCATION, 0, new PermissionHandler() {
            @Override
            public void onGranted() {
                viewModel.liveLocationDetecter.startLocationUpdates();
                setMyLocation();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = getArguments();
        if (bundle!=null){
            binding.setCheck(false);

            cat_id = bundle.getString("cat_id");
            sub_cat_id = bundle.getString("sub_cat_id");
            id = bundle.getString("id");
            state = bundle.getString("state");
            distance = bundle.getString("distance");

            city = bundle.getString("city");
            state_id = bundle.getString("state_id");
            city_id = bundle.getString("city_id");
            start_price = bundle.getString("start_price");
            end_price = bundle.getString("end_price");

            viewModel.getMapListFilterApi(sharedPref.getUserData().getAuthKey(),id,cat_id,sub_cat_id,distance,state_id,city_id,"",start_price,end_price);

        }else{

            viewModel.getMapListFilterApi(sharedPref.getUserData().getAuthKey(),"","","","",state_id,city_id,"","","");
            binding.setCheck(true);

        }

        viewModel.getCount();
    }

    @Override
    protected void subscribeToEvents(final HomeFragmentVM vm) {
        authKey = vm.sharedPref.getUserData().getAuthKey();
        mainActivity = (MainActivity) getActivity();
        mainActivity.hideBottomNav(false);
        mainActivity.setHeader("");
        mainActivity.hideBackButton();
        binding.etSearch.addTextChangedListener(textWatcher);
        Log.d("Sanjeev :",vm.sharedPref.getUserData().getAuthKey()+"==="+vm.sharedPref.getUserData().getId());
        binding.setCheck(true);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_view);


        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }


        vm.base_click.observe(this, view -> {
            switch (view != null ? view.getId() : 0) {
                case R.id.notificationIcon:
                    Intent intent = NotificationActivity.newIntent(getActivity());
                    startNewActivity(intent);
                    break;
                case R.id.cv_list:
                    binding.setCheck(false);
                    break;
                case R.id.cv_map:
                    binding.setCheck(true);
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
                            makeSceneTransitionAnimation(getActivity(),binding.nav.viewImg,"profile");
                    startActivity(intent1,options.toBundle());
                    break;

                case R.id.view_img:
                    Intent intent3 = ProfileActivity.newIntent(getActivity());
                    ActivityOptionsCompat options1 = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(getActivity(),binding.nav.viewImg,"profile");
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
                    binding.bottomLayout.ftState.spinner.performClick();
                    break;


                case R.id.et_city:
                    binding.bottomLayout.ftState.citySpinner.performClick();
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
        });


        binding.bottomLayout.ftPrice.rangeSeekbar3.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                min = String.valueOf(minValue);
                max = String.valueOf(maxValue);
                binding.bottomLayout.ftPrice.startRate.setText(minValue+" Brl");
                binding.bottomLayout.ftPrice.endRate.setText(""+maxValue+" Brl");
            }
        });



        binding.bottomLayout.ftState.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (flag) {       // Here

                    binding.bottomLayout.ftState.etState.setText(binding.bottomLayout.ftState.spinner.getSelectedItem().toString());

                    for (i = 0; i < statesModelList.size(); i++) {
                        String name = statesModelList.get(i).getName();
                        if (name.equalsIgnoreCase(binding.bottomLayout.ftState.spinner.getSelectedItem().toString())) {
                            state_id = String.valueOf(statesModelList.get(i).getId());

                        }
                    }

                    vm.getCities(Integer.parseInt(state_id));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.bottomLayout.ftState.citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                binding.bottomLayout.ftState.etCity.setText(binding.bottomLayout.ftState.citySpinner.getSelectedItem().toString());


                for (i = 0; i < citiesModelList.size(); i++) {

                    String name = citiesModelList.get(i).getName();

                    if (name.equalsIgnoreCase(binding.bottomLayout.ftState.citySpinner.getSelectedItem().toString())) {

                        city_id = String.valueOf(citiesModelList.get(i).getId());

                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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


        vm.suggestionsEventRequest.observe(this, new Observer<Resource<List<SuggesstionModel>>>() {
            @Override
            public void onChanged(Resource<List<SuggesstionModel>> listResource) {
                switch (listResource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        //binding.typingText.setVisibility(View.GONE);
                        suggestionsModelList = listResource.data;
                        adapterSuggestions.setList(suggestionsModelList);

                        break;
                    case WARN:
                        vm.warn.setValue(listResource.message);
                        break;
                    case ERROR:
                        vm.error.setValue(listResource.message);
                        break;
                }
            }
        });

        vm.liveLocationDetecter.observe(this, new Observer<LocCallback>() {
            @Override
            public void onChanged(LocCallback locCallback) {
                switch (locCallback.getType()) {
                    case STARTED:
                        break;
                    case ERROR:
                        break;
                    case STOPPED:
                        break;
                    case OPEN_GPS:
                        viewModel.info.setValue("Prompt open");
                        try {
                            locCallback.getApiException().startResolutionForResult(getActivity(), LiveLocationDetecter.REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                    case PROMPT_CANCEL:
                        viewModel.error.setValue("prompt cancel");
                        break;
                    case FOUND:

                        mCurrentlocation = locCallback.getLocation();
                        mCurrentlocation = locCallback.getLocation();
                        sharedPref.put(Constants.LATITUDE, String.valueOf(mCurrentlocation.getLatitude()));
                        sharedPref.put(Constants.LONGITUDE, String.valueOf(mCurrentlocation.getLongitude()));
                        vm.liveLocationDetecter.removeLocationUpdates();
                        if (projectsList != null) {
                            setMarkerOnMap(projectsList);
                        }

                        break;
                }

            }
        });


        vm.mapListBean.observe(this, new SingleRequestEvent.RequestObserver<List<MapListModel>>() {
            @Override
            public void onRequestReceived(@NonNull Resource<List<MapListModel>> resource) {
                switch (resource.status) {
                    case LOADING:
                        showProgressDialog(R.string.plz_wait);
                        break;
                    case SUCCESS:
                        dismissProgressDialog();
                        getCurrentLocation();
                        projectsList = resource.data;
                        adapter.setList(projectsList);
                        //setMarkerOnMap(resource.data);
                        //setRecyclerView(resource.data);
                        setRecyclerViewList(resource.data);
                        //binding.setVariable(BR.bean,resource.data);
                        break;
                    case ERROR:
                        dismissProgressDialog();
                        vm.error.setValue(resource.message);
                        if (resource.message.equalsIgnoreCase(getResources().getString(R.string.unauthorised))){
                            sharedPref.deleteAll();
                            Intent intent1 = LoginActivity.newIntent(getActivity());
                            startNewActivity(intent1, true, true);

                        }
                        break;
                    case WARN:
                        dismissProgressDialog();
                        vm.warn.setValue(resource.message);
                        break;
                }
            }
        });


        binding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    binding.etSearch.clearFocus();
                    InputMethodManager in = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(  binding.etSearch.getWindowToken(), 0);
                    binding.rvSuggestions.setVisibility(View.GONE);
                    vm.getMapListFilterApi(vm.sharedPref.getUserData().getAuthKey(),"","","","","","",binding.etSearch.getText().toString(),"","");
                    return true;
                }
                return false;
            }
        });


        vm.statesEventRequest.observe(this, new SingleRequestEvent.RequestObserver<List<StatesModel>>() {
            @Override
            public void onRequestReceived(@NonNull Resource<List<StatesModel>> resource) {
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        statesList = new ArrayList<>();

                        statesModelList = resource.data;

                        for (int i = 0; i < resource.data.size(); i++) {
                            statesList.add(resource.data.get(i).getName());
                        }

                        statesList.add(0, getResources().getString(R.string.select_state));
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, statesList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.bottomLayout.ftState.spinner.setAdapter(adapter);

                        break;
                    case WARN:
                        vm.warn.setValue(resource.message);
                        break;
                    case ERROR:
                        vm.error.setValue(resource.message);
                        if (resource.message.equalsIgnoreCase(getResources().getString(R.string.unauthorised))){
                            sharedPref.deleteAll();
                            Intent intent1 = LoginActivity.newIntent(getActivity());
                            startNewActivity(intent1, true, true);

                        }
                        break;
                }
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
                        citiesList = new ArrayList<>();
                        for (int i = 0;i<resource.data.size();i++){
                            citiesList.add(resource.data.get(i).getName());
                        }
                        citiesList.add(0,getResources().getString(R.string.select_city));
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, citiesList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.bottomLayout.ftState.citySpinner.setAdapter(adapter);

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
                       // }
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





        vm.getStates();
        setBottomsheet();
        setFilterRecycler();
        setAdapterSuggestions();
        setRecyclerViewMore();


    }


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String str = charSequence.toString();
            Log.d("typing : ",str);

            if (str.length()>0){
                binding.rvSuggestions.setVisibility(View.VISIBLE);
                viewModel.searchSuggestions(str);
            }else{
                binding.rvSuggestions.setVisibility(View.GONE);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void setMarkerOnMap(List<MapListModel> list) {
        if (googleMap == null)
            return;
        googleMap.clear();
        if (list == null)
            list = new ArrayList<>();

        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (int i = 0; i < list.size(); i++) {
            Random aleatorio = new Random();
            Double randlat = aleatorio.nextDouble() / 20000;
            Double randonlng = aleatorio.nextDouble() / 20000;

            LatLng latLng = new LatLng(list.get(i).getLatitude() + randlat, list.get(i).getLongitude() + randonlng);
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("")
                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView("", 1))));
            builder.include(latLng);
        }
        // Set a listener for marker click.
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMapClickListener(this);
        //googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));

        CameraPosition pos = new CameraPosition.Builder()
                .target(new LatLng(mCurrentlocation.getLatitude(), mCurrentlocation.getLongitude()))
                .zoom(15)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(pos));
    }

    private GoogleMap.OnCameraIdleListener onCameraIdleListener = () -> {

    };

    private GoogleMap.OnCameraMoveStartedListener onCameraMoveStartedListener = i -> {

    };

    @Override
    public void onMapClick(LatLng latLng) {

        Log.i("HelloLATLON", "latidute" + latLng.latitude + ":::long" + latLng.longitude);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        try {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        } catch (Exception e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        setMyLocation();
    }


    private void setMyLocation() {
        if (googleMap != null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    private Bitmap getMarkerBitmapFromView(String distance, int pos) {
        View customMarkerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    private void setRecyclerView(List<MapListModel> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        binding.rvList.setLayoutManager(layoutManager);
        binding.rvList.setAdapter(adapter);
        adapter.setList(list);
    }

    SimpleRecyclerViewAdapter<MapListModel, HolderMapItemsBinding> adapter = new SimpleRecyclerViewAdapter(R.layout.holder_map_items, BR.bean, new SimpleRecyclerViewAdapter.SimpleCallback<MapListModel>() {
        @Override
        public void onItemClick(View v, MapListModel o) {

            switch (v.getId()) {
                case R.id.ll_item:

                    Intent intent = JobDetailsActivity.newIntent(getActivity());
                    intent.putExtra("id",o.getId());
                    startNewActivity(intent);

                    break;
            }

        }
    });


    private void setRecyclerViewList(List<MapListModel> list) {
        if (list.size()>0) {
            binding.rvListData.setVisibility(View.VISIBLE);
            binding.txtNoRecord.setVisibility(View.GONE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            binding.rvListData.setLayoutManager(layoutManager);
            binding.rvListData.setAdapter(adapterList);
            adapterList.setList(list);
        }else{
            binding.rvListData.setVisibility(View.GONE);
            binding.txtNoRecord.setVisibility(View.VISIBLE);
        }
    }

    SimpleRecyclerViewAdapter<MapListModel, HolderListItemsBinding> adapterList = new SimpleRecyclerViewAdapter(R.layout.holder_list_items, BR.bean, new SimpleRecyclerViewAdapter.SimpleCallback<MapListModel>() {
        @Override
        public void onItemClick(View v, MapListModel moreBean) {
            switch (v.getId()) {
                case R.id.ll_list_items:
                    Intent intent = JobDetailsActivity.newIntent(getActivity());
                    intent.putExtra("id",moreBean.getId());
                    startNewActivity(intent);

                    break;
            }


        }
    });


    private void setAdapterSuggestions() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.rvSuggestions.setLayoutManager(layoutManager);
        binding.rvSuggestions.setAdapter(adapterSuggestions);
    }

    SimpleRecyclerViewAdapter<SuggesstionModel, HolderFilterCategoryBinding> adapterSuggestions =
            new SimpleRecyclerViewAdapter<>(R.layout.holder_new_home_suggestions, BR.bean, new SimpleRecyclerViewAdapter.SimpleCallback<SuggesstionModel>() {
                @Override
                public void onItemClick(View v, SuggesstionModel moreBean) {

                    switch (v.getId()){

                        case R.id.rl_one:
                            if (moreBean.getSearch_type() == 0) {

                               // viewModel.getMapListFilterApi(sharedPref.getUserData().getAuthKey(), String.valueOf(moreBean.getId()),"","","","","","","","");
                                mainActivity.addSubFragmentWithoutCache(HomeFragment.TAG, HomeFragment.newInstance("", "", String.valueOf(moreBean.getId()), "", state,city,state_id,city_id,"",""));

                            } else {
                                mainActivity.addSubFragmentWithoutCache(HomeFragment.TAG, HomeFragment.newInstance(String.valueOf(moreBean.getId()), "", "", "", state,city,state_id,city_id,"",""));
                                //viewModel.getMapListFilterApi(sharedPref.getUserData().getAuthKey(), "",String.valueOf(moreBean.getId()),"","","","","","","");

                            }

                            binding.rvSuggestions.setVisibility(View.GONE);

                            break;
                    }




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

//                    case 3:
//
//                        Intent langIntent = SelectLanguageActivity.newIntent(getActivity());
//                        startNewActivity(langIntent);
//
//                        break;


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
        menuBeanList.add(new MoreBean(3, getResources().getString(R.string.languages), R.drawable.ic_language));
        menuBeanList.add(new MoreBean(4, getResources().getString(R.string.blocked_contacts), R.drawable.ic_block));
        menuBeanList.add(new MoreBean(5, getResources().getString(R.string.terms), R.drawable.ic_privacy_policy_icon));
        menuBeanList.add(new MoreBean(6, getResources().getString(R.string.help_support), R.drawable.ic_help_icon));
        menuBeanList.add(new MoreBean(7, getResources().getString(R.string.about_us), R.drawable.ic_about_us_icon));
        menuBeanList.add(new MoreBean(8, getResources().getString(R.string.logout_account), R.drawable.ic_logout_icon));
        return menuBeanList;
    }


    private void applyFilter() {
        cat_ids = "";
        String distance = String.valueOf(binding.bottomLayout.ftDistance.distanceSlider.getProgress() * 10);
        if (distance.equalsIgnoreCase("0")) {
            distance = "";
        }
        state = binding.bottomLayout.ftState.etState.getText().toString();
        city = binding.bottomLayout.ftState.etCity.getText().toString();
        if (state.equalsIgnoreCase(getResources().getString(R.string.select_state))) {
            state = "";
        }
        if (city.equalsIgnoreCase(getResources().getString(R.string.select_city))) {
            city = "";
        }
        mainActivity.addSubFragmentWithoutCache(HomeFragment.TAG, HomeFragment.newInstance(cat_ids, "", "", distance, state,city,state_id,city_id,"",""));

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
