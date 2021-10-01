package com.marius.valeyou_admin.ui.activity.signup.uploaddocument;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.categoriesBean.CategoryDataBean;
import com.marius.valeyou_admin.data.beans.singninbean.SignInModel;
import com.marius.valeyou_admin.data.remote.helper.ApiUtils;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityUploadDocumentBinding;
import com.marius.valeyou_admin.databinding.DialogDoneUploadBinding;
import com.marius.valeyou_admin.databinding.DialogSetPriceBinding;
import com.marius.valeyou_admin.di.base.dialog.BaseCustomDialog;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.activity.main.MainActivity;
import com.marius.valeyou_admin.ui.activity.signup.signuptwo.selectcategory.SelectCategoryActivity;
import com.marius.valeyou_admin.util.AppUtils;
import com.marius.valeyou_admin.util.Constants;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;
import com.marius.valeyou_admin.util.location.LiveLocationDetecter;
import com.marius.valeyou_admin.util.location.LocCallback;
import com.marius.valeyou_admin.util.permission.PermissionHandler;
import com.marius.valeyou_admin.util.permission.Permissions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UploadDocumentActivity extends AppActivity<ActivityUploadDocumentBinding, UploadDocumentActivityVM> {
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    File file;
    List<CategoryDataBean>  categoryList;
    HashMap<String, String> map;
    Location mCurrentlocation;
    String auth_key;
    String list;

    HashMap<String,RequestBody> newMap = new HashMap<>();

    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, UploadDocumentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        Permissions.check(this,Manifest.permission.WRITE_EXTERNAL_STORAGE, 0, new PermissionHandler() {
            @Override
            public void onGranted() {
            }
        });

        Intent intent = getIntent();
        if (intent!=null){

            auth_key = intent.getStringExtra("auth_key");
            list =  intent.getStringExtra("categoryList");
            map = (HashMap<String, String>) intent.getSerializableExtra("signupMap");

            for (String s : map.keySet()) {
                RequestBody requestBody = RequestBody.create(MultipartBody.FORM, map.get(s));
                newMap.put(s, requestBody);
            }
        }


        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        binding.btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCAmeraPermision();
            }
        });
    }

    @Override
    protected BindingActivity<UploadDocumentActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_upload_document, UploadDocumentActivityVM.class);
    }

    @Override
    protected void subscribeToEvents(UploadDocumentActivityVM vm) {
        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                finish(true);
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
                        try {
                            locCallback.getApiException().startResolutionForResult(UploadDocumentActivity.this, LiveLocationDetecter.REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                    case PROMPT_CANCEL:
                        break;
                    case FOUND:
                        mCurrentlocation = locCallback.getLocation();
                        vm.liveLocationDetecter.removeLocationUpdates();
                        break;
                }

            }
        });

       vm.profilebeandata.observe(this, new Observer<Resource<ApiResponse<SignInModel>>>() {
           @Override
           public void onChanged(Resource<ApiResponse<SignInModel>> apiResponseResource) {
               switch (apiResponseResource.status) {
                   case LOADING:
                       showProgressDialog(R.string.wait);
                       break;
                   case SUCCESS:
                       dismissProgressDialog();
                       String socialId = sharedPref.get(Constants.SOCIAL_ID,"");
                       if (!socialId.equals("")) {
                           Intent intent = MainActivity.newIntent(UploadDocumentActivity.this);
                           startNewActivity(intent);
                           finishAffinity();
                       }else {
                           Intent intent = LoginActivity.newIntent(UploadDocumentActivity.this);
                           startNewActivity(intent);
                           finishAffinity();
                       }
                       break;
                   case ERROR:
                       dismissProgressDialog();
                       vm.error.setValue(apiResponseResource.message);
                       break;
                   case WARN:
                       dismissProgressDialog();
                       vm.warn.setValue(apiResponseResource.message);
                       break;
               }
           }
       });

    }

    private void signup(){
        hitSignupApi((viewModel));
    }

    private void openCAmeraPermision(){
        Permissions.check(this,Manifest.permission.CAMERA, 0, new PermissionHandler() {
            @Override
            public void onGranted() {
                gallaryIntent();
            }
        });
    }

    private void hitSignupApi(UploadDocumentActivityVM vm){

        String securityNumber =  binding.etSecurityNumber.getText().toString();

        if (securityNumber.isEmpty()){
            vm.error.setValue(getResources().getString(R.string.enter_security_number));
        } else if (file==null) {
            vm.error.setValue(getResources().getString(R.string.enter_license_number));
        }else{

            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            MultipartBody.Part licenseImage =
                    MultipartBody.Part.createFormData("driverLicence", file.getName(), requestFile);

            newMap.put(Constants.SELECTED_DATA, toRequestBody(list));
            newMap.put(Constants.SECURITY_NUMBER, toRequestBody(securityNumber));

            if (auth_key!=null) {
                vm.editProfile(auth_key, newMap, licenseImage);
            }else {
                vm.error.setValue("Something went wrong.");
            }

        }
    }


    public static RequestBody toRequestBody (String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body ;
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
                binding.ivFile.setImageURI(uri);



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
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
}
