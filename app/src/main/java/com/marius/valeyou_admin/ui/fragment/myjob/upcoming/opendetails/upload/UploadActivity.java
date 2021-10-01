package com.marius.valeyou_admin.ui.fragment.myjob.upcoming.opendetails.upload;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.IdentityModel;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.jobs.JobDetailModel;
import com.marius.valeyou_admin.data.remote.api.Urls;
import com.marius.valeyou_admin.data.remote.helper.ApiUtils;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityUploadBinding;
import com.marius.valeyou_admin.databinding.HolderIdentityCardBinding;
import com.marius.valeyou_admin.databinding.HolderUploadedImagesBinding;
import com.marius.valeyou_admin.di.base.adapter.SimpleRecyclerViewAdapter;
import com.marius.valeyou_admin.di.base.view.BaseActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.indentity.IdentityActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.activity.view_image.ViewImageActivity;
import com.marius.valeyou_admin.ui.fragment.IdentityVerficationActivity;
import com.marius.valeyou_admin.ui.fragment.IdentityVerficationActivityVM;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.opendetails.CurrentJobDetailsActivity;
import com.marius.valeyou_admin.util.AppUtils;
import com.marius.valeyou_admin.util.databinding.ImageViewBindingUtils;
import com.marius.valeyou_admin.util.permission.PermissionHandler;
import com.marius.valeyou_admin.util.permission.Permissions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UploadActivity extends BaseActivity<ActivityUploadBinding,UploadActivityVM> {

String order_id;
int job_status;
    public static Intent newIntent(Activity activity,int id) {
        Intent intent = new Intent(activity, UploadActivity.class);
        intent.putExtra("job_id",id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected BaseActivity.BindingActivity<UploadActivityVM> getBindingActivity() {
        return new BaseActivity.BindingActivity<>(R.layout.activity_upload, UploadActivityVM.class);
    }

    @Override
    protected void subscribeToEvents(UploadActivityVM vm) {

        int job_id = getIntent().getIntExtra("job_id",0);
        vm.getJobDetaial(vm.sharedPref.getUserData().getAuthKey(),job_id);

        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                finish(true);
            }
        });

        vm.base_click.observe(this, new Observer<View>() {
            @Override
            public void onChanged(View view) {
                switch (view.getId()){
                    case R.id.imgAdd:
                        openCAmeraPermision();
                        break;
                }
            }
        });

        vm.uploadEvent.observe(this, new Observer<Resource<SimpleApiResponse>>() {
            @Override
            public void onChanged(Resource<SimpleApiResponse> simpleApiResponseResource) {
                switch (simpleApiResponseResource.status) {
                    case LOADING:
                        binding.setCheck(true);
                        break;
                    case SUCCESS:
                        binding.setCheck(false);
                        vm.success.setValue(simpleApiResponseResource.message);
                        vm.getJobDetaial(vm.sharedPref.getUserData().getAuthKey(),job_id);
                        break;
                    case ERROR:
                        binding.setCheck(false);
                        vm.error.setValue(simpleApiResponseResource.message);
                        if (simpleApiResponseResource.message.equalsIgnoreCase("unauthorized")) {

                            Intent intent1 = LoginActivity.newIntent(UploadActivity.this);
                            startNewActivity(intent1, true, true);

                        }
                        break;

                    case WARN:
                        binding.setCheck(false);
                        vm.warn.setValue(simpleApiResponseResource.message);
                        break;
                }
            }
        });

        vm.jobDetailBean.observe(this, new Observer<Resource<JobDetailModel>>() {
            @Override
            public void onChanged(Resource<JobDetailModel> resource) {
                switch (resource.status) {
                    case LOADING:
                        binding.setCheck(true);
                        break;
                    case SUCCESS:
                        binding.setCheck(false);
                        binding.setVariable(BR.bean, resource.data);
                       order_id = String.valueOf(resource.data.getId());

                       job_status = resource.data.getStatus();

                       List<JobDetailModel.ProviderWorkImagesBean> imagesList = resource.data.getProvider_work_images();

                       if (imagesList.size()>0){
                           adapterWorkImages.setList(imagesList);
                           binding.noData.setVisibility(View.GONE);

                       }else{
                           binding.noData.setVisibility(View.VISIBLE);
                       }

                        break;
                    case ERROR:
                        binding.setCheck(false);
                        vm.error.setValue(resource.message);

                        if (resource.message.equalsIgnoreCase("unauthorised")) {

                            Intent intent1 = LoginActivity.newIntent(UploadActivity.this);
                            startNewActivity(intent1, true, true);

                        }
                        break;

                    case WARN:
                        binding.setCheck(false);
                        vm.warn.setValue(resource.message);
                        break;
                }
            }
        });

        setUpAdapter();


    }

    private void openCAmeraPermision(){
        Permissions.check(this, Manifest.permission.CAMERA, 0, new PermissionHandler() {
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
                File file = new File(frontpathString);
                MultipartBody.Part image = ApiUtils.createMultipartBodySingle(file);
                if (job_status < 7){

                    viewModel.addWorkImages(viewModel.sharedPref.getUserData().getAuthKey(),
                            toRequestBody(order_id),toRequestBody("0"),toRequestBody("1"),image);

            }else {

                viewModel.addWorkImages(viewModel.sharedPref.getUserData().getAuthKey(),
                        toRequestBody(order_id),toRequestBody("1"),toRequestBody("1"),image);
            }

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

    SimpleRecyclerViewAdapter<JobDetailModel.ProviderWorkImagesBean, HolderUploadedImagesBinding> adapterWorkImages =
            new SimpleRecyclerViewAdapter(R.layout.holder_uploaded_images, BR.bean, new SimpleRecyclerViewAdapter.SimpleCallback<JobDetailModel.ProviderWorkImagesBean>() {
                @Override
                public void onItemClick(View v, JobDetailModel.ProviderWorkImagesBean o) {

                    switch (v.getId()){
                        case R.id.images:
                            Intent intent = new Intent(UploadActivity.this, ViewImageActivity.class);
                            intent.putExtra("url", Urls.MEDIA_URL+o.getImage());
                            ActivityOptionsCompat options = ActivityOptionsCompat.
                                    makeSceneTransitionAnimation(UploadActivity.this, v, "image");
                            startActivity(intent,options.toBundle());
                            break;
                    }

                }
            });

    private void setUpAdapter() {

        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        binding.rvImages.setLayoutManager(layoutManager);
        binding.rvImages.setAdapter(adapterWorkImages);

    }




}

