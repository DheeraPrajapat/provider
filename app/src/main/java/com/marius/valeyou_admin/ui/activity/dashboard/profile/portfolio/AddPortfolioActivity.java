package com.marius.valeyou_admin.ui.activity.dashboard.profile.portfolio;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.profilebean.ProfileModel;
import com.marius.valeyou_admin.data.beans.singninbean.SignInModel;
import com.marius.valeyou_admin.data.remote.helper.ApiUtils;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityAddPortfolioBinding;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.di.base.view.BaseActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.ProfileActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.portfolio.category.PortfolioCategoryActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.activity.main.MainActivity;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;
import com.marius.valeyou_admin.util.permission.PermissionHandler;
import com.marius.valeyou_admin.util.permission.Permissions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddPortfolioActivity extends AppActivity<ActivityAddPortfolioBinding,AddPortfolioActivityVM> {


    private int mYear, mMonth, mDay;
    private static final int SELECT_PICTURE = 1;
    File file;
    String comeFrom;
    String portfolioId;
    String json="";
    String subCat="",name="";

    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity,AddPortfolioActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected BaseActivity.BindingActivity<AddPortfolioActivityVM> getBindingActivity() {
        return new BaseActivity.BindingActivity<>(R.layout.activity_add_portfolio,AddPortfolioActivityVM.class);
    }

    @Override
    protected void subscribeToEvents(AddPortfolioActivityVM vm) {

        Intent intent = getIntent();
        if (intent!=null){
            comeFrom = intent.getStringExtra("comeFrom");
            if (comeFrom.equalsIgnoreCase("add")){
                binding.header.tvTitle.setText(getResources().getString(R.string.add_portfolio));
            }else{
                binding.header.tvTitle.setText(getResources().getString(R.string.edit_portfolio));
                ProfileModel.ProviderPortfoliosBean portfolioData = intent.getParcelableExtra("portfolioData");
                portfolioId = String.valueOf(portfolioData.getId());
                binding.etTitle.setText(portfolioData.getTitle());
                binding.etDesc.setText(portfolioData.getDescription());
                binding.etProjectPhoto.setText(portfolioData.getImage());
                if (portfolioData.getCategory()==null||portfolioData.getCategory().equalsIgnoreCase("")){
                    binding.etCategory.setText(getResources().getString(R.string.select_category));
                }else {
                    binding.etCategory.setText(portfolioData.getCategory());
                }


            }
        }

        binding.header.tvTitle.setTextColor(getResources().getColor(R.color.white));
        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                supportFinishAfterTransition();
                onBackPressed(true);
            }
        });

        vm.addPortfolioBean.observe(this, new SingleRequestEvent.RequestObserver<SimpleApiResponse>() {
            @Override
            public void onRequestReceived(@NonNull Resource<SimpleApiResponse> resource) {
                switch (resource.status) {
                    case LOADING:
                        showProgressDialog(R.string.wait);
                        break;
                    case SUCCESS:
                        dismissProgressDialog();
                        Intent intent = ProfileActivity.newIntent(AddPortfolioActivity.this);
                        startNewActivity(intent);
                        finish();
                        break;
                    case ERROR:
                        dismissProgressDialog();
                        vm.error.setValue(resource.message);
                        if (resource.message.equalsIgnoreCase("unauthorized")){
                            Intent intent1 = LoginActivity.newIntent(AddPortfolioActivity.this);
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

                    case R.id.et_category:

                        Intent intent1 = new Intent(AddPortfolioActivity.this, PortfolioCategoryActivity.class);
                        startActivityForResult(intent1,101);

                        break;

                    case R.id.cv_save:

                        String auth_key = vm.sharedPref.getUserData().getAuthKey();
                        String title = binding.etTitle.getText().toString();
                        String description = binding.etDesc.getText().toString();

                        Map<String, RequestBody> map =new HashMap<>();
                        map.put("title",toRequestBody(title));
                        map.put("description",toRequestBody(description));

                        if (title.isEmpty()){
                            vm.error.setValue(getResources().getString(R.string.enter_project_title));
                        }else if (description.isEmpty()){
                            vm.error.setValue(getResources().getString(R.string.enter_project_description));
                        }else if (binding.etCategory.getText().toString().equalsIgnoreCase("Select Category")){
                            vm.error.setValue(getResources().getString(R.string.please_select_category));
                        }  else if (file==null){
                            map.put("category",toRequestBody(binding.etCategory.getText().toString()));

                            if (!subCat.isEmpty()){
                                map.put("subcategory",toRequestBody(subCat));
                            }


                            if (comeFrom.equalsIgnoreCase("edit"))
                            { map.put("portfolio_id",toRequestBody(portfolioId));
                                vm.editPortfolio(auth_key,map,null);
                            }else {
                                vm.addPortfolio(auth_key, map, null);
                            }
                        }else {

                            map.put("category",toRequestBody(binding.etCategory.getText().toString()));

                            if (!subCat.isEmpty()){
                                map.put("subcategory",toRequestBody(subCat));
                            }

                            MultipartBody.Part image = ApiUtils.createMultipartBodySingle(file);
                            if (comeFrom.equalsIgnoreCase("edit"))
                            { map.put("portfolio_id",toRequestBody(portfolioId));
                                vm.editPortfolio(auth_key,map,image);
                            }else {
                                vm.addPortfolio(auth_key, map, image);
                            }
                        }

                        break;

                    case R.id.cv_cancel:
                        onBackPressed();
                        break;

                    case R.id.et_date:
                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);
                        datePickerDialog();

                        break;

                    case R.id.et_project_photo:
                        AllowPermision();
                        break;


                }
            }
        });
    }


    private void datePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        binding.etDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    private void AllowPermision(){
        Permissions.check(this, Manifest.permission.READ_EXTERNAL_STORAGE, 0, new PermissionHandler() {
            @Override
            public void onGranted() {
                gallaryIntent();
            }
        });
    }

private void gallaryIntent(){
    CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(this);
}

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                    Uri uri = result.getUri();
                    String frontpathString = getRealPathFromURI(uri);
                    file = new File(frontpathString);
                binding.etProjectPhoto.setText(file.getName());



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }else if (requestCode == 101){
            if (data!=null) {
                json = data.getStringExtra("json");
                 name = data.getStringExtra("name");
                 subCat = data.getStringExtra("subCat");
                binding.etCategory.setText(name);
            }
        }
    }

    public static RequestBody toRequestBody (String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body ;
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




}
