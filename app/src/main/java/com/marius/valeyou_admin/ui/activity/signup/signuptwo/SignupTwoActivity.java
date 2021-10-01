package com.marius.valeyou_admin.ui.activity.signup.signuptwo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.Adress;
import com.marius.valeyou_admin.data.beans.CitiesModel;
import com.marius.valeyou_admin.data.beans.StatesModel;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivitySignupTwoBinding;
import com.marius.valeyou_admin.databinding.HolderCitiesBinding;
import com.marius.valeyou_admin.databinding.HolderStatesBinding;
import com.marius.valeyou_admin.di.base.adapter.SimpleRecyclerViewAdapter;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.ui.StripeConnectActivity;
import com.marius.valeyou_admin.ui.activity.LocationActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.editprofile.EditProfileActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.activity.signup.SignupActivity;
import com.marius.valeyou_admin.ui.activity.signup.signuptwo.selectcategory.SelectCategoryActivity;
import com.marius.valeyou_admin.util.Constants;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.adapters.TextViewBindingAdapter;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SignupTwoActivity extends AppActivity<ActivitySignupTwoBinding, SignupTwoActivityVM> {

    Adress endereco;
    Map<String, String> hashMap;
    String auth_key;
    double lat,lng;
    String state_id="",city_id="";
    List<CitiesModel> citiesModelList;
    List<StatesModel> statesModelList;
    String cityName="";
    int stateId;
    Calendar today = Calendar.getInstance();
    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, SignupTwoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected BindingActivity<SignupTwoActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_signup_two, SignupTwoActivityVM.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        configTexts();
    }

    private void configTexts(){
        binding.etZipCode.setInputType(InputType.TYPE_CLASS_NUMBER);
        SimpleMaskFormatter zip = new SimpleMaskFormatter("NNNNN-NNN");
        MaskTextWatcher zim = new MaskTextWatcher(binding.etZipCode, zip);
        binding.etZipCode.addTextChangedListener(zim);
        binding.etZipCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(binding.etZipCode.getText().length()>=9){
                    viewModel.Adress(binding.etZipCode.getText().toString().replace("-",""));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void preencherCampos(){
        binding.etAddress.setText(endereco.getStreet());
        binding.etCity.setText(endereco.getCity());
        binding.etState.setText(endereco.getState());
        binding.etStreet.setText(endereco.getStreet());
    }

    @Override
    protected void subscribeToEvents(SignupTwoActivityVM vm) {

        Intent intent = getIntent();
        if (intent!=null){
            auth_key = intent.getStringExtra("auth_key");
        }

        //setStatesAdapter();
       // setCitiesAdapter();

      //  vm.getStates();


        binding.etAddress.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                binding.etAddress.getDetailsFor(place, new DetailsCallback() {
                    @Override
                    public void onSuccess(PlaceDetails placeDetails) {

                        lat = placeDetails.geometry.location.lat;
                        lng = placeDetails.geometry.location.lng;

                        Log.d("test", "details " + placeDetails);
                        for (AddressComponent component : placeDetails.address_components) {
                            for (AddressComponentType type : component.types) {
                                switch (type) {
                                    case STREET_NUMBER:
                                        binding.etHouseNumber.setText(component.long_name);
                                        break;
                                    case ROUTE:
                                        break;
                                    case NEIGHBORHOOD:
                                        break;
                                    case SUBLOCALITY_LEVEL_1:
                                        break;
                                    case SUBLOCALITY:
                                        binding.etStreet.setText(component.long_name);
                                        break;
                                    case LOCALITY:
                                        binding.etCity.setText(component.long_name);
                                        cityName = component.long_name;
                                        break;
                                    case ADMINISTRATIVE_AREA_LEVEL_1:
                                        binding.etState.setText(component.long_name);
                                        /*for (int i = 0;i<statesModelList.size();i++){
                                            if (component.long_name.contains(statesModelList.get(i).getName())){
                                                binding.etState.setText(statesModelList.get(i).getName());
                                                state_id = String.valueOf(statesModelList.get(i).getId());
                                                viewModel.getCities(statesModelList.get(i).getId());
                                            }
                                        }*/


                                        break;
                                    case ADMINISTRATIVE_AREA_LEVEL_2:
                                        break;
                                    case COUNTRY:
                                        break;
                                    case POSTAL_CODE:
                                        binding.etZipCode.setText(component.long_name);
                                        break;
                                    case POLITICAL:
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }
                });
            }
        });



        vm.AdressEvent.observe(this, new SingleRequestEvent.RequestObserver<Adress>() {
            @Override
            public void onRequestReceived(@NonNull Resource<Adress> resource) {
                switch (resource.status) {
                    case LOADING:
                        showProgressDialog(R.string.wait);
                        break;
                    case SUCCESS:
                        endereco = resource.data;
                        preencherCampos();
                        dismissProgressDialog();
                        break;
                    case ERROR:
                        dismissProgressDialog();
                        vm.error.setValue(resource.message);
                        if (resource.message.equalsIgnoreCase("unauthorized")){
                            Intent intent1 = LoginActivity.newIntent(SignupTwoActivity.this);
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
                        }

                        /*for (int i = 0;i<citiesModelList.size();i++) {
                            if (citiesModelList.get(i).getName().contains(cityName)) {
                                binding.etCity.setText(citiesModelList.get(i).getName());
                                city_id = String.valueOf(citiesModelList.get(i).getId());
                            }
                        }*/

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



        vm.statesEventRequest.observe(this, new SingleRequestEvent.RequestObserver<List<StatesModel>>() {
            @Override
            public void onRequestReceived(@NonNull Resource<List<StatesModel>> resource) {
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        statesModelList = resource.data;
                        if (statesModelList.size()>0){
                            adapter.setList(statesModelList);
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


        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                finish(true);
            }
        });

        vm.base_click.observe(this, new Observer<View>() {
            @Override
            public void onChanged(View view) {
                Intent intent;
                switch (view != null ? view.getId() : 0) {

                    case R.id.et_state:
                        break;

                    case R.id.et_city:
                        /*if (binding.rvCities.getVisibility() == View.VISIBLE){
                            binding.rvCities.setVisibility(View.GONE);
                        }else{
                            binding.rvCities.setVisibility(View.VISIBLE);
                            if (binding.rvStates.getVisibility()==View.VISIBLE){
                                binding.rvStates.setVisibility(View.GONE);
                            }
                        }*/
                        break;


                    case R.id.etMonth :
                        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(SignupTwoActivity.this,
                                new MonthPickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(int selectedMonth, int selectedYear) {
                                        binding.etMonth.setText((selectedMonth + 1) + "");
                                    }
                                }, today.get(Calendar.YEAR), today.get(Calendar.MONTH)
                        );
                        builder.showMonthOnly().build().show();
                        break;

                    case R.id.etYear :
                        MonthPickerDialog.Builder builder1 = new MonthPickerDialog.Builder(SignupTwoActivity.this,
                                new MonthPickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(int selectedMonth, int selectedYear) {
                                        binding.etYear.setText(selectedYear + "");
                                    }
                                }, today.get(Calendar.YEAR), today.get(Calendar.MONTH)
                        );
                        builder1.setMaxYear(today.get(Calendar.YEAR)).showYearOnly().build().show();
                        break;

                    case R.id.iv_location:
                        Intent intent1 = new Intent(SignupTwoActivity.this,LocationActivity.class);
                        startActivityForResult(intent1,1);
                        break;

                    case R.id.btnSignup:

                            hashMap = new HashMap<>();

                            String day = binding.etday.getText().toString();
                            String month = binding.etMonth.getText().toString();
                            String year = binding.etYear.getText().toString();
                            try{
                                if (Integer.parseInt(day) > 31 || Integer.parseInt(day) < 1){
                                    viewModel.error.setValue(getResources().getString(R.string.enter_date_of_birth));
                                    return;
                                }
                            }
                            catch (Exception e){
                                viewModel.error.setValue(getResources().getString(R.string.enter_date_of_birth));
                                return;
                            }
                            if (day.equalsIgnoreCase("")||day.equalsIgnoreCase("day")||month.equalsIgnoreCase("")||year.equalsIgnoreCase(""))  {
                               viewModel.error.setValue(getResources().getString(R.string.enter_date_of_birth));
                                return;
                            }

                            String dob = binding.etday.getText().toString()+"/"+binding.etMonth.getText().toString()+"/"+binding.etYear.getText().toString();

                            String houseNumber = binding.etHouseNumber.getText().toString();
                            String apartmentNumber = binding.etAppartmentNumber.getText().toString();
                            String street = binding.etStreet.getText().toString();
                            String zipCode = binding.etZipCode.getText().toString();
                            String city = binding.etCity.getText().toString();
                            String state = binding.etState.getText().toString();
                            String des = binding.etDescription.getText().toString();
                            String address = binding.etAddress.getText().toString();
                            String latitude = String.valueOf(lat);
                            String longtitude = String.valueOf(lng);

                            if (address.isEmpty()) {

                                vm.error.setValue(getResources().getString(R.string.please_select_address));

                            }else{

                                if (!dob.isEmpty()){
                                    hashMap.put(Constants.DOB,dob);
                                }

                                if (!city.isEmpty()){
                                    hashMap.put(Constants.CITY,city);
                                }

                                if (!state.isEmpty()){
                                    hashMap.put(Constants.STATE,state);
                                }

                                if (!houseNumber.isEmpty()){
                                    hashMap.put(Constants.HOUSE_NUMBER,houseNumber);
                                }

                                if (!apartmentNumber.isEmpty()){
                                    hashMap.put(Constants.APARTMENT,apartmentNumber);
                                }

                                if (!street.isEmpty()){
                                    hashMap.put(Constants.STREET,street);
                                }


                                if (!zipCode.isEmpty()){
                                    hashMap.put(Constants.ZIP_CODE,zipCode);
                                }


                                if (!des.isEmpty()){
                                    hashMap.put(Constants.DESCRIPTION,des);
                                }

                                if (!state_id.isEmpty()){
                                    hashMap.put("stateId",state_id);
                                }

                                if (!city_id.isEmpty()){
                                    hashMap.put("cityId",city_id);
                                }

                                hashMap.put("latitude",latitude);
                                hashMap.put("longitude",longtitude);
                                hashMap.put(Constants.ADDRESS,address);
                                intent = SelectCategoryActivity.newIntent(SignupTwoActivity.this);
                                intent.putExtra("signupMap", (Serializable) hashMap);
                                intent.putExtra("auth_key",auth_key);
                                startNewActivity(intent);

                            }
                        break;

                }

            }


            public Boolean checkDateFormat(String date){
                if (date == null || !date.matches("^(1[0-9]|0[1-9]|3[0-1]|2[1-9])/(0[1-9]|1[0-2])/[0-9]{4}$"))
                    return false;
                SimpleDateFormat format=new SimpleDateFormat("dd/mm/yyyy");
                try {
                    format.parse(date);
                    return true;
                }catch (ParseException e){
                    return false;
                }
            }
            private String compareDates(String date)  {
                String msg = "";
                SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
                Date strDate = null;
                try {
                    strDate = sdf.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (System.currentTimeMillis() <= strDate.getTime()) {
                    msg = "your are not alowed to enter future date. Please enter a valid birthday date.";
                }
                return msg;
            }


        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){

            case 1:
                String result=data.getStringExtra("address");
                binding.etAddress.setText(result);
                String city = data.getStringExtra("city");
                String state = data.getStringExtra("state");
                String zipcode = data.getStringExtra("zipcode");
                lat = data.getDoubleExtra("latitude",0);
                lng = data.getDoubleExtra("longtitude",0);
                binding.etCity.setText(city);
                binding.etZipCode.setText(zipcode);


                for (int i = 0;i<statesModelList.size();i++){
                    if (state.contains(statesModelList.get(i).getName())){
                        binding.etState.setText(statesModelList.get(i).getName());
                        state_id = String.valueOf(statesModelList.get(i).getId());
                        viewModel.getCities(statesModelList.get(i).getId());
                    }
                }


                break;
        }
    }
    private void setStatesAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvStates.setLayoutManager(layoutManager);
        binding.rvStates.setAdapter(adapter);
    }

    private void setCitiesAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvCities.setLayoutManager(layoutManager);
        binding.rvCities.setAdapter(cityadapter);
    }


    SimpleRecyclerViewAdapter<StatesModel, HolderStatesBinding> adapter =
            new SimpleRecyclerViewAdapter(R.layout.holder_states, BR.bean, new SimpleRecyclerViewAdapter.SimpleCallback<StatesModel>() {
                @Override
                public void onItemClick(View v, StatesModel bean) {
                    switch (v != null ? v.getId() : 0) {
                        case R.id.ll_states:
                            binding.etState.setText(bean.getName());
                            binding.rvStates.setVisibility(View.GONE);
                            state_id = String.valueOf(bean.getId());
                            viewModel.getCities(bean.getId());

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
                            binding.etCity.setText(bean.getName());
                            binding.rvCities.setVisibility(View.GONE);
                            break;
                    }
                }
            });

    public static RequestBody toRequestBody (String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body ;
    }


}
