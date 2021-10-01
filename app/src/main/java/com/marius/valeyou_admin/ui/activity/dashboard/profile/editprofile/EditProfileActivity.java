package com.marius.valeyou_admin.ui.activity.dashboard.profile.editprofile;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.Adress;
import com.marius.valeyou_admin.data.beans.CitiesModel;
import com.marius.valeyou_admin.data.beans.StatesModel;
import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.profilebean.ProfileModel;
import com.marius.valeyou_admin.data.beans.singninbean.SignInModel;
import com.marius.valeyou_admin.data.remote.helper.ApiUtils;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityEditProfileBinding;
import com.marius.valeyou_admin.databinding.DialogDeleteAccouontBinding;
import com.marius.valeyou_admin.databinding.HolderCitiesBinding;
import com.marius.valeyou_admin.databinding.HolderStatesBinding;
import com.marius.valeyou_admin.di.base.adapter.SimpleRecyclerViewAdapter;
import com.marius.valeyou_admin.di.base.dialog.BaseCustomDialog;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.ui.activity.LocationActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.ProfileActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.certificate.AddCertificateActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.activity.signup.signuptwo.SignupTwoActivity;
import com.marius.valeyou_admin.util.databinding.ImageViewBindingUtils;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;
import com.marius.valeyou_admin.util.permission.PermissionHandler;
import com.marius.valeyou_admin.util.permission.Permissions;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditProfileActivity extends AppActivity<ActivityEditProfileBinding, EditProfileActivityVM> {

    public static final int RESULT_GALLERY = 0;
    public static final int RESULT_ADDRESS = 1;
    File file;
    String firstname;
    String lastname;
    String email;
    String phone;
    String aboutus;
    Double lat;
    Double lng;
    String state_id="";
    String city_id = "";
    String cityName="";
    Calendar today = Calendar.getInstance();
    Adress endereco;

    List<StatesModel> statesModelList;
    List<CitiesModel> citiesModelList;

    boolean isFirstTimeDateSelection = true;


    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, EditProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected BindingActivity<EditProfileActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_edit_profile, EditProfileActivityVM.class);
    }

    private void configText(){
        SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
        MaskTextWatcher mask = new MaskTextWatcher(binding.etPhone, smf);
        binding.etPhone.addTextChangedListener(mask);
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
    protected void subscribeToEvents(EditProfileActivityVM vm) {
        binding.header.tvTitle.setTextColor(getResources().getColor(R.color.white));
        binding.header.tvTitle.setText(getResources().getString(R.string.edit_profile));
        binding.header.setCheck(true);
        binding.header.tvTwo.setText(getResources().getString(R.string.save));

        configText();
        firstname = viewModel.sharedPref.getUserData().getFirstName();
        lastname = viewModel.sharedPref.getUserData().getLastName();
        email = viewModel.sharedPref.getUserData().getEmail();
        phone = viewModel.sharedPref.getUserData().getPhone();
        aboutus = viewModel.sharedPref.getUserData().getDescription();
        String state = viewModel.sharedPref.getUserData().getState();
        String zipcode = viewModel.sharedPref.getUserData().getZipCode();
        String apartment = viewModel.sharedPref.getUserData().getAppartment();
        String houseNumber = viewModel.sharedPref.getUserData().getHouseNumber();
        String street= viewModel.sharedPref.getUserData().getStreet();
        String city = viewModel.sharedPref.getUserData().getCity();
        String dateofBirth = viewModel.sharedPref.getUserData().getDob();
        String address = viewModel.sharedPref.getUserData().getAddress();

        if (dateofBirth != null && !dateofBirth.equalsIgnoreCase("")) {
            String[] separated = dateofBirth.split("/");
            String day = separated[0];
            String month = separated[1];
            String year = separated[2];

            binding.etday.setText(day);
            binding.etMonth.setText(month);
            binding.etYear.setText(year);

        }

        String image = viewModel.sharedPref.getUserData().getImage();
        Log.d("saved_image : ",image);
        if (image!=null){
            ImageViewBindingUtils.setProfilePicture(binding.IMGUserProfile,image);
        }

        binding.etFirstName.setText(firstname);
        binding.etLastname.setText(lastname);
        binding.etEmail.setText(email);
        binding.etPhone.setText(phone);
        binding.etAbout.setText(aboutus);
        binding.etState.setText(state);
        binding.etZipCode.setText(zipcode);
        binding.etApartment.setText(apartment);
        binding.etHouseNumber.setText(houseNumber);
        binding.etStreet.setText(street);
        binding.etCity.setText(city);
        binding.etAddress.setText(address);

        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                finish(true);
            }
        });


        List<String> daysList = new ArrayList<>();
        daysList.add(0,"Day");
        for (int i = 1;i<=31;i++){
            daysList.add(""+i);
        }

        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, daysList);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.etDate.setAdapter(adp1);


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
                            Intent intent1 = LoginActivity.newIntent(EditProfileActivity.this);
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

        binding.etDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View arg1, int position, long id) {
                String day = adapterView.getAdapter().getItem(position).toString();
                if (isFirstTimeDateSelection) {
                    isFirstTimeDateSelection = true;
                }else{
                    binding.etday.setText(day);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });


        vm.profilebeandata.observe(this, new SingleRequestEvent.RequestObserver<ApiResponse<SignInModel>>() {
            @Override
            public void onRequestReceived(@NonNull Resource<ApiResponse<SignInModel>> resource) {
                switch (resource.status) {
                    case LOADING:
                        showProgressDialog(R.string.wait);
                        break;
                    case SUCCESS:
                        dismissProgressDialog();
                        Intent intent = ProfileActivity.newIntent(EditProfileActivity.this);
                        startNewActivity(intent);
                        finish();
                        break;
                    case ERROR:
                        dismissProgressDialog();
                        vm.error.setValue(resource.message);
                        if (resource.message.equalsIgnoreCase("unauthorized")){
                            Intent intent1 = LoginActivity.newIntent(EditProfileActivity.this);
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

        vm.base_click.observe(this, new Observer<View>() {
            @Override
            public void onChanged(View view) {
                switch (view != null ? view.getId() : 0) {

                    case R.id.etday:
                        isFirstTimeDateSelection = false;
                        binding.etDate.performClick();
                        break;

                    case R.id.etMonth :
                        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(EditProfileActivity.this,
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
                        MonthPickerDialog.Builder builder1 = new MonthPickerDialog.Builder(EditProfileActivity.this,
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
                        AllowLocationPermision();
                        break;

                    case R.id.cv_gallaryBtn:
                        AllowPermision();
                        break;

                    case R.id.et_state:
                       /* if (binding.rvStates.getVisibility() == View.VISIBLE){
                            binding.rvStates.setVisibility(View.GONE);
                        }else{
                            binding.rvStates.setVisibility(View.VISIBLE);
                            if (binding.rvCities.getVisibility()==View.VISIBLE){
                                binding.rvCities.setVisibility(View.GONE);
                            }
                        }*/
                        break;

                    case R.id.et_city:
                       /* if (binding.rvCities.getVisibility() == View.VISIBLE){
                            binding.rvCities.setVisibility(View.GONE);
                        }else{
                            binding.rvCities.setVisibility(View.VISIBLE);
                            if (binding.rvStates.getVisibility()==View.VISIBLE){
                                binding.rvStates.setVisibility(View.GONE);
                            }
                        }*/
                        break;

                    case R.id.tv_two:

                        String authKey = vm.sharedPref.getUserData().getAuthKey();
                        String firstname = binding.etFirstName.getText().toString();
                        String lastname = binding.etLastname.getText().toString();
                        String email = binding.etEmail.getText().toString();
                        String phone = binding.etPhone.getText().toString();
                        String about = binding.etAbout.getText().toString();
                        String houseNumber = binding.etHouseNumber.getText().toString();
                        String apartment = binding.etApartment.getText().toString();
                        String Street = binding.etStreet.getText().toString();
                        String zipcode = binding.etZipCode.getText().toString();
                        String state = binding.etState.getText().toString();
                        String city = binding.etCity.getText().toString();
                        String dob = binding.etday.getText().toString()+"/"+binding.etMonth.getText().toString()+"/"+binding.etYear.getText().toString();
                        String address = binding.etAddress.getText().toString();

                        String latitude = "";
                        String longitude = "";
                        if (lat!=null){
                             latitude  = String.valueOf(lat);
                        }else{
                            latitude = String.valueOf(sharedPref.getUserData().getLatitude());

                    }

                        if (lng!=null){
                            longitude = String.valueOf(lng);
                        }else{
                            longitude = String.valueOf(sharedPref.getUserData().getLongitude());
                        }



                        if (file==null){

                            HashMap<String, String> strMap = new HashMap<>();
                            strMap.put("first_name",firstname);
                            strMap.put("last_name",lastname);
                            strMap.put("email",email);
                            strMap.put("phone",phone);
                            strMap.put("description",about);
                            strMap.put("state",state);
                            strMap.put("houseNumber",houseNumber);
                            strMap.put("appartment",apartment);
                            strMap.put("street",Street);
                            strMap.put("zipCode",zipcode);
                            strMap.put("city",city);
                            strMap.put("dob",dob);
                            strMap.put("address",address);
                            strMap.put("latitude",latitude);
                            strMap.put("longitude",longitude);



                            vm.editProfileSTr(authKey,strMap);

                        }else{

                        Map<String, RequestBody> map = new HashMap<>();
                        map.put("first_name",toRequestBody(firstname));
                        map.put("last_name",toRequestBody(lastname));
                        map.put("email",toRequestBody(email));
                        map.put("phone",toRequestBody(phone));
                        map.put("description",toRequestBody(about));
                        map.put("state",toRequestBody(state));
                        map.put("houseNumber",toRequestBody(houseNumber));
                        map.put("appartment",toRequestBody(apartment));
                        map.put("street",toRequestBody(Street));
                        map.put("zipCode",toRequestBody(zipcode));
                        map.put("city",toRequestBody(city));
                            map.put("dob",toRequestBody(dob));
                            map.put("address",toRequestBody(address));
                            map.put("latitude",toRequestBody(latitude));
                            map.put("longitude",toRequestBody(longitude));


                            MultipartBody.Part image = ApiUtils.createMultipartBodySingle(file);
                            vm.editProfile(authKey,map,image);
                        }


                        break;

                }
            }
        });


        //setStatesAdapter();
       // setCitiesAdapter();
       // vm.getStates();


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
                                       /* for (int i = 0;i<statesModelList.size();i++){
                                            if (component.long_name.contains(statesModelList.get(i).getName())){
                                                binding.etState.setText(statesModelList.get(i).getName());
                                                state_id = String.valueOf(statesModelList.get(i).getId());
                                                viewModel.getCities(statesModelList.get(i).getId());
                                            }
                                        }
*/

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

                       /* for (int i = 0;i<citiesModelList.size();i++) {
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


    }


    private void AllowPermision(){
        Permissions.check(this, Manifest.permission.READ_EXTERNAL_STORAGE, 0, new PermissionHandler() {
            @Override
            public void onGranted() {
                gallaryIntent();
            }
        });
    }

    private void AllowLocationPermision(){
        Permissions.check(this, Manifest.permission.ACCESS_FINE_LOCATION, 0, new PermissionHandler() {
            @Override
            public void onGranted() {

                Intent intent = new Intent(EditProfileActivity.this,LocationActivity.class);
                startActivityForResult(intent, RESULT_ADDRESS);

            }
        });
    }




    private void gallaryIntent(){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);

    }

    public static RequestBody toRequestBody (String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body ;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Uri uri = result.getUri();
                binding.img.setImageURI(uri);
                String frontpathString = getRealPathFromURI(uri);
                file = new File(frontpathString);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }

        }else if (requestCode == RESULT_ADDRESS){
            if (data!=null) {

                String result1 = data.getStringExtra("address");
                binding.etAddress.setText(result1);
                String city = data.getStringExtra("city");
                String state = data.getStringExtra("state");
                String zipcode = data.getStringExtra("zipcode");
                lat = data.getDoubleExtra("latitude",0);
                lng = data.getDoubleExtra("longtitude",0);
                binding.etCity.setText(city);
                binding.etZipCode.setText(zipcode);

               /* for (int i = 0;i<statesModelList.size();i++){
                    if (state.contains(statesModelList.get(i).getName())){
                        binding.etState.setText(statesModelList.get(i).getName());
                        state_id = String.valueOf(statesModelList.get(i).getId());
                        viewModel.getCities(statesModelList.get(i).getId());
                    }
                }*/


            }

        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
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


}
