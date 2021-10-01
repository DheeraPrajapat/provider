package com.marius.valeyou_admin.ui.activity.dashboard.profile.certificate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;

import com.google.gson.JsonElement;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.profilebean.ProfileModel;
import com.marius.valeyou_admin.data.remote.helper.ApiUtils;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityAddCertificateBinding;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.di.base.view.BaseActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.ProfileActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.portfolio.AddPortfolioActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.providerlanguages.SelectLanguageActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.providerlanguages.SelectLanguageActivityVM;
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
import java.util.HashMap;
import java.util.Map;

public class AddCertificateActivity extends AppActivity<ActivityAddCertificateBinding,AddCertificateActivityVM> {

    private static final int SELECT_PICTURE = 1;
    File file;
    String comeFrom;
    String certificateId;

    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, AddCertificateActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected BindingActivity<AddCertificateActivityVM> getBindingActivity() {
        return new BaseActivity.BindingActivity<>(R.layout.activity_add_certificate, AddCertificateActivityVM.class);
    }

    @Override
    protected void subscribeToEvents(AddCertificateActivityVM vm) {

        Intent intent = getIntent();
        if (intent!=null){
             comeFrom = intent.getStringExtra("comeFrom");
            if (comeFrom.equalsIgnoreCase("add")){
                binding.header.tvTitle.setText(getResources().getString(R.string.addcertificate));
            }else{
                binding.header.tvTitle.setText(getResources().getString(R.string.edit_certificate));
                ProfileModel.CertificatesBean certificatesData = intent.getParcelableExtra("certificateData");
                certificateId = String.valueOf(certificatesData.getId());
                binding.etTitle.setText(certificatesData.getTitle());
                binding.etDesc.setText(certificatesData.getDescription());

            }
        }



        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                finish(true);
            }
        });

        vm.addCertificateBean.observe(this, new SingleRequestEvent.RequestObserver<SimpleApiResponse>() {
            @Override
            public void onRequestReceived(@NonNull Resource<SimpleApiResponse> resource) {
                switch (resource.status) {
                    case LOADING:
                        showProgressDialog(R.string.wait);
                        break;
                    case SUCCESS:
                        dismissProgressDialog();
                        Intent intent = ProfileActivity.newIntent(AddCertificateActivity.this);
                        startNewActivity(intent);
                        finish();
                        break;
                    case ERROR:
                        dismissProgressDialog();
                        vm.error.setValue(resource.message);
                        if (resource.message.equalsIgnoreCase("unauthorized")){
                            Intent intent1 = LoginActivity.newIntent(AddCertificateActivity.this);
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
                    case R.id.et_photo:
                        AllowPermision();
                        break;

                    case R.id.cv_save:
                        String authKey = vm.sharedPref.getUserData().getAuthKey();
                        String title = binding.etTitle.getText().toString();
                        String desc = binding.etDesc.getText().toString();

                        Map<String, RequestBody> map =new HashMap<>();
                        map.put("title",toRequestBody(title));
                        map.put("description",toRequestBody(desc));

                        if (title.isEmpty()){
                            vm.info.setValue(getResources().getString(R.string.enter_title));
                        }else if (desc.isEmpty()){
                            vm.info.setValue(getResources().getString(R.string.enter_description));
                        }else if (file==null){
                            vm.info.setValue(getResources().getString(R.string.choose_your_certificate));
                        }else {

                            MultipartBody.Part image = ApiUtils.createMultipartBodySingle(file);

                            if (comeFrom.equalsIgnoreCase("add")) {

                                vm.addCertificateApi(authKey, map, image);

                            }else{

                                map.put("certificate_id",toRequestBody(certificateId));
                                vm.editCertificate(authKey, map, image);


                            }

                        }

                        break;

                    case R.id.cv_cancel:
                        onBackPressed();
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
                binding.etPhoto.setText(file.getName());



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

    public static RequestBody toRequestBody (String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body ;
    }

}
