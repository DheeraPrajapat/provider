package com.marius.valeyou_admin.ui.fragment.myjob.upcoming.opendetails.attachments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.jobs.JobDetailModel;
import com.marius.valeyou_admin.data.remote.api.Urls;
import com.marius.valeyou_admin.data.remote.helper.ApiUtils;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityAttachmentsBinding;
import com.marius.valeyou_admin.databinding.HolderAttachmentsBinding;
import com.marius.valeyou_admin.databinding.HolderUploadedImagesBinding;
import com.marius.valeyou_admin.di.base.adapter.SimpleRecyclerViewAdapter;
import com.marius.valeyou_admin.di.base.view.BaseActivity;
import com.marius.valeyou_admin.di.base.viewmodel.BaseActivityViewModel;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.activity.view_image.ViewImageActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.opendetails.upload.UploadActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.opendetails.upload.UploadActivityVM;
import com.marius.valeyou_admin.util.Constants;
import com.marius.valeyou_admin.util.FilePath;
import com.marius.valeyou_admin.util.permission.PermissionHandler;
import com.marius.valeyou_admin.util.permission.Permissions;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AttachmentsActivity extends BaseActivity<ActivityAttachmentsBinding,AttachmentsActivityVM> {

    String order_id;
    int job_status;
    public static Intent newIntent(Activity activity, int id) {
        Intent intent = new Intent(activity, AttachmentsActivity.class);
        intent.putExtra("job_id",id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected BaseActivity.BindingActivity<AttachmentsActivityVM> getBindingActivity() {
        return new BaseActivity.BindingActivity<>(R.layout.activity_attachments, AttachmentsActivityVM.class);
    }

    @Override
    protected void subscribeToEvents(AttachmentsActivityVM vm) {
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
                        getDocument();
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

                            Intent intent1 = LoginActivity.newIntent(AttachmentsActivity.this);
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
                            adapterattachments.setList(imagesList);
                            binding.noData.setVisibility(View.GONE);

                        }else{
                            binding.noData.setVisibility(View.VISIBLE);
                        }

                        break;
                    case ERROR:
                        binding.setCheck(false);

                        vm.error.setValue(resource.message);

                        if (resource.message.equalsIgnoreCase("unauthorised")) {

                            Intent intent1 = LoginActivity.newIntent(AttachmentsActivity.this);
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

    SimpleRecyclerViewAdapter<JobDetailModel.ProviderWorkImagesBean, HolderAttachmentsBinding> adapterattachments =
            new SimpleRecyclerViewAdapter(R.layout.holder_attachments, BR.bean, new SimpleRecyclerViewAdapter.SimpleCallback<JobDetailModel.ProviderWorkImagesBean>() {
                @Override
                public void onItemClick(View v, JobDetailModel.ProviderWorkImagesBean o) {

                    switch (v.getId()){
                        case R.id.dwonload_attachment:
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(AttachmentsActivity.this);
                            builder1.setMessage("Are you sure you want to download this file?");
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            downloadFile(o.getImage());
                                        }
                                    });

                            builder1.setNegativeButton(
                                    "No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();


                            break;
                    }

                }
            });

    private void setUpAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvImages.setLayoutManager(layoutManager);
        binding.rvImages.setAdapter(adapterattachments);

    }

    private void getDocument()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int req, int result, Intent data)
    {
        // TODO Auto-generated method stub
        super.onActivityResult(req, result, data);
        if (result == RESULT_OK)
        {
            Uri fileuri = data.getData();
            try {
                String path = FilePath.getPath(this,fileuri);
                File file = new File(path);
                MultipartBody.Part image = ApiUtils.createMultipartBodySingle(file);

                viewModel.addWorkImages(viewModel.sharedPref.getUserData().getAuthKey(),
                        toRequestBody(order_id),toRequestBody("0"),toRequestBody("1"),image);
            }catch (Exception e){
                Toast.makeText(this, "This Pdf Not Supported!", Toast.LENGTH_SHORT).show();
            }


        }
    }

    public static RequestBody toRequestBody (String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body ;
    }

    private void downloadFile(String file){
        Permissions.check(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, 0, new PermissionHandler() {
            @Override
            public void onGranted() {
                Toast.makeText(AttachmentsActivity.this, "downloading...", Toast.LENGTH_SHORT).show();
                File direct =
                        new File(Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                                .getAbsolutePath() + "/" + "Provider/attachemtns");


                if (!direct.exists()) {
                    direct.mkdir();
                }

                DownloadManager dm = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
                Uri downloadUri = Uri.parse(Urls.MEDIA_URL+file);
                DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false)
                        .setTitle("attachment"+System.currentTimeMillis())
                        .setMimeType("application/pdf")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
                                File.separator + "attachment" + File.separator + System.currentTimeMillis());

                dm.enqueue(request);
            }
        });
    }

}