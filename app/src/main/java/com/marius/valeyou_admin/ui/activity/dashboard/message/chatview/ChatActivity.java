package com.marius.valeyou_admin.ui.activity.dashboard.message.chatview;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.share.Share;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.chat.MessageModel;
import com.marius.valeyou_admin.data.beans.chat.UsersModel;
import com.marius.valeyou_admin.data.beans.jobs.JobDetailModel;
import com.marius.valeyou_admin.data.local.SharedPref;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityChatBinding;
import com.marius.valeyou_admin.databinding.DialogDeactivateAccopuontBinding;
import com.marius.valeyou_admin.databinding.HolderBlockAccountBinding;
import com.marius.valeyou_admin.di.base.dialog.BaseCustomDialog;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.di.socket.SocketManager;
import com.marius.valeyou_admin.ui.activity.user_profile.UserProfileActivity;
import com.marius.valeyou_admin.util.Constants;
import com.marius.valeyou_admin.util.databinding.ImageViewBindingUtils;
import com.marius.valeyou_admin.util.permission.PermissionHandler;
import com.marius.valeyou_admin.util.permission.Permissions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ChatActivity extends AppActivity<ActivityChatBinding, ChatActivityVM> implements SocketManager.Observer {
    public SocketManager mSocketManager;
    UsersModel data;
    JobDetailModel jobDetalData;
    List<MessageModel> messagesList;
    MessageModel model;
    ChatAdapter adapter;
    public int currentpage = 1;
    List<MessageModel> messageModelsList = new ArrayList<>();
    String encodingStr;
    String ext = "";
    int msgType;
    String otherUserId;
    String otherUserImage;
    String otherUserName;
    @Inject
    SharedPref sharedPref;
    String timestamp;
    int i_block, he_block;

    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected BindingActivity<ChatActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_chat, ChatActivityVM.class);
    }

    @Override
    protected void subscribeToEvents(ChatActivityVM vm) {
        binding.header.tvTitle.setText(getResources().getString(R.string.message));

        if (mSocketManager == null) {

            mSocketManager = new SocketManager(this, this, sharedPref);

        }

        if (mSocketManager.getmSocket() == null) {

            mSocketManager.init();

        }

        Intent intent = getIntent();

        if (intent != null) {
            String comeFrom = intent.getStringExtra("comeFrom");
            Log.e("comeFrom",comeFrom);
            if (comeFrom.equalsIgnoreCase("chat")) {

                data = intent.getParcelableExtra("userData");
                Log.e("ASDSADASDAS",data.getChat().getMessage());
                otherUserId = String.valueOf(data.getChat().getUser().getId());
                otherUserImage = data.getChat().getUser().getImage();
                otherUserName = data.getChat().getUser().getName();


            } else if (comeFrom.equalsIgnoreCase("job")) {
                //jobDetalData = intent.getParcelableExtra("user_data");

                otherUserId = intent.getStringExtra("otherUserId");
                otherUserName = intent.getStringExtra("otherUserName");
                otherUserImage = intent.getStringExtra("otherUserImage");
            }

            binding.tvName.setText(otherUserName);
            ImageViewBindingUtils.setProfileUrl(binding.imgUser, otherUserImage);


        }

        binding.header.tvTitle.setTextColor(getResources().getColor(R.color.white));

        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                supportFinishAfterTransition();
                onBackPressed(true);
            }
        });

        vm.blockUnblockEvent.observe(this, new Observer<Resource<SimpleApiResponse>>() {
            @Override
            public void onChanged(Resource<SimpleApiResponse> simpleApiResponseResource) {
                switch (simpleApiResponseResource.status) {
                    case LOADING:
                        showProgressDialog(R.string.plz_wait);
                        break;
                    case SUCCESS:
                        dismissProgressDialog();

                        if (i_block == 1) {
                            binding.etChat.setText("");
                            binding.ivSend.setVisibility(View.VISIBLE);
                            binding.etChat.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                            binding.etChat.setTextColor(getResources().getColor(R.color.black));
                            binding.etChat.setEnabled(true);
                            binding.galary.setVisibility(View.VISIBLE);
                            binding.tvBlock.setText(getResources().getString(R.string.block));
                            i_block = 0;
                            Toast.makeText(ChatActivity.this, getResources().getString(R.string.you_unblocked)+" " + otherUserName, Toast.LENGTH_SHORT).show();

                        } else {
                            i_block = 1;
                            binding.etChat.setText(getResources().getString(R.string.you_blocked)+" " + otherUserName);
                            binding.etChat.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            binding.etChat.setTextColor(getResources().getColor(R.color.red));
                            binding.ivSend.setVisibility(View.GONE);
                            binding.etChat.setEnabled(false);
                            binding.tvBlock.setText(getResources().getString(R.string.unblock));
                            binding.galary.setVisibility(View.GONE);
                            Toast.makeText(ChatActivity.this, getResources().getString(R.string.you_blocked)+" " + otherUserName, Toast.LENGTH_SHORT).show();

                        }
                        break;
                    case WARN:
                        dismissProgressDialog();
                        vm.warn.setValue(simpleApiResponseResource.message);
                        break;
                    case ERROR:
                        dismissProgressDialog();
                        vm.error.setValue(simpleApiResponseResource.message);
                        break;
                }
            }
        });


        vm.base_click.observe(this, new Observer<View>() {
            @Override
            public void onChanged(View view) {
                switch (view != null ? view.getId() : 0) {

                    case R.id.galary :
                        AllowCameraPermision();
                        break;

                    case R.id.tv_block:
                        if (i_block == 0) {
                            blockAccount();
                        } else {
                            vm.blockUnblock(String.valueOf(otherUserId), "0");
                        }
                        break;

                    case R.id.img_user:

                        if (i_block == 0 && he_block == 0) {
                            Intent intent = new Intent(ChatActivity.this, UserProfileActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("id", otherUserId);
                            ActivityOptionsCompat options = ActivityOptionsCompat.
                                    makeSceneTransitionAnimation(ChatActivity.this, binding.imgUser, "user_profile");
                            startActivity(intent,options.toBundle());
                        } else if (i_block == 1 && he_block == 0) {
                            Toast.makeText(ChatActivity.this, getResources().getString(R.string.you_blocked_this_user), Toast.LENGTH_SHORT).show();
                        } else if (i_block == 0 && he_block == 1) {
                            Toast.makeText(ChatActivity.this, getResources().getString(R.string.your_have_blocked), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ChatActivity.this, getResources().getString(R.string.your_have_blocked), Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case R.id.iv_send:

                        timestamp = (System.currentTimeMillis() / 1000) + "";

                        if (binding.etChat.getText().toString().isEmpty()) {
                            vm.info.setValue("Type your message");
                        } else {
                            msgType = 0;
                            JSONObject jsonObject = getJsonData();
                            if (jsonObject != null) {
                                mSocketManager.sendMessage(jsonObject);
                                binding.etChat.setText("");
                            }
                        }
                        break;
                }
            }
        });


        getMessagesList();
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMessagesList();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    private void setupAdater(List<MessageModel> list) {
        int id = sharedPref.getUserData().getId();
        adapter = new ChatAdapter(ChatActivity.this, list, id, sharedPref.getUserData().getImage());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        /*linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);*/
        Collections.reverse(list);
        binding.rvMessageList.setLayoutManager(linearLayoutManager);

        if (list.size() > 2) {
            binding.rvMessageList.smoothScrollToPosition(adapter.getItemCount() - 1);
        } else {
            binding.rvMessageList.smoothScrollToPosition(adapter.getItemCount());
        }
        binding.rvMessageList.setAdapter(adapter);

    }


    private void getMessagesList() {
        try {
            binding.swipeRefreshLayout.setRefreshing(true);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.SENDERID, sharedPref.getUserData().getId());
            jsonObject.put(Constants.RECIEVERID, otherUserId);
            jsonObject.put(Constants.PAGE, "");
            jsonObject.put(Constants.LIMIT, "");
            jsonObject.put(Constants.TYPE, 1);
            jsonObject.put(Constants.APP_TYPE, 0);
            mSocketManager.getMessageList(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void getChatList(String event, JSONArray args) {

    }

    @Override
    public void getMessages(String event, JSONArray args) {
        binding.swipeRefreshLayout.setRefreshing(false);
        Type type = TypeToken.getParameterized(ArrayList.class, MessageModel.class).getType();
        messagesList = new Gson().fromJson(args.toString(), type);


        if (messagesList == null) {
            messagesList = new ArrayList<>();
        } else {

            if (messagesList.size() > 0) {

                i_block = messagesList.get(messagesList.size()-1).getI_block();
                he_block = messagesList.get(messagesList.size()-1).getHe_block();

                if (i_block == 1 && he_block == 0) {
                    binding.etChat.setText(getResources().getString(R.string.you_blocked)+" " + otherUserName);
                    binding.etChat.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    binding.etChat.setTextColor(getResources().getColor(R.color.red));
                    binding.tvBlock.setVisibility(View.VISIBLE);
                    binding.ivSend.setVisibility(View.GONE);
                    binding.galary.setVisibility(View.GONE);
                    binding.etChat.setEnabled(false);
                    binding.tvBlock.setText(getResources().getString(R.string.unblock));
                } else if (i_block == 0 && he_block == 1) {
                    binding.etChat.setText(otherUserName + " "+getResources().getString(R.string.blocked_you));
                    binding.etChat.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    binding.etChat.setTextColor(getResources().getColor(R.color.red));
                    binding.ivSend.setVisibility(View.GONE);
                    binding.galary.setVisibility(View.GONE);
                    binding.tvBlock.setVisibility(View.GONE);
                    binding.etChat.setEnabled(false);
                } else if (i_block == 0 && he_block == 0) {
                    binding.etChat.setText("");
                    binding.etChat.setEnabled(true);
                    binding.tvBlock.setVisibility(View.VISIBLE);
                    binding.tvBlock.setText(getResources().getString(R.string.block));
                    binding.ivSend.setVisibility(View.VISIBLE);
                    binding.galary.setVisibility(View.VISIBLE);
                }
            }


            setupAdater(messagesList);
        }

    }


    @Override
    public void sendMessageResponse(String event, JSONObject args) {
        dismissProgressDialog();
        model = new Gson().fromJson(args.toString(), MessageModel.class);
        if (adapter != null) {

            if (messagesList.size() > 0) {
                adapter.setList(model);
            } else {
                messagesList.add(model);
                setupAdater(messagesList);
            }

            binding.rvMessageList.smoothScrollToPosition(adapter.getItemCount() - 1);

        }
    }


    private JSONObject getJsonData() {
        showProgressDialog(R.string.sending);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.SENDERID, sharedPref.getUserData().getId());
            jsonObject.put(Constants.RECIEVERID, otherUserId);
            if (msgType==0) {
                jsonObject.put(Constants.MESSAGE, binding.etChat.getText().toString());
            }else{
                jsonObject.put(Constants.MESSAGE,encodingStr);

            }
            jsonObject.put(Constants.EXTENSION,ext);
            jsonObject.put(Constants.USERNAME, sharedPref.getUserData().getFirstName() + " " + sharedPref.getUserData().getLastName());
            jsonObject.put(Constants.USERIMAGE, sharedPref.getUserData().getImage());
            jsonObject.put(Constants.RECIEVERNAME, otherUserName);
            jsonObject.put(Constants.RECIEVERIMAGE, otherUserImage);
            jsonObject.put(Constants.MSGTYPE, msgType);
            jsonObject.put(Constants.TIMESTAMP, timestamp);
            jsonObject.put(Constants.TYPE, 1);
            jsonObject.put(Constants.APP_TYPE, 0);

            return jsonObject;
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSocketManager != null) {
            mSocketManager.disconnect();
        }
    }


    private BaseCustomDialog<HolderBlockAccountBinding> BlockAccountDialog;

    private void blockAccount() {
        BlockAccountDialog = new BaseCustomDialog<>(ChatActivity.this, R.layout.holder_block_account, new BaseCustomDialog.Listener() {
            @Override
            public void onViewClick(View view) {
                if (view != null && view.getId() != 0) {
                    switch (view.getId()) {
                        case R.id.btn_submit:
                            BlockAccountDialog.dismiss();

                            viewModel.blockUnblock(String.valueOf(otherUserId), "1");

                            break;
                        case R.id.btn_cancel:
                            BlockAccountDialog.dismiss();
                            break;

                        case R.id.iv_cancel:
                            BlockAccountDialog.dismiss();
                            break;
                    }


                }
            }
        });
        BlockAccountDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        BlockAccountDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        BlockAccountDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        BlockAccountDialog.show();
    }

    @Override
    protected void onBackPressed(boolean animate) {
        super.onBackPressed(animate);
        supportFinishAfterTransition();
    }

    private void AllowCameraPermision() {
        Permissions.check(this, Manifest.permission.CAMERA, 0, new PermissionHandler() {
            @Override
            public void onGranted() {
                galaryCameraIntent();
            }
        });
    }

    private void galaryCameraIntent() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri imageUri = result.getUri();
                String frontpathString = getRealPathFromURI(imageUri);
                encodingStr = getFileToByte(frontpathString);
                msgType = 1;
                ext = getMimeType(this,imageUri);

                if (encodingStr!=null){
                    JSONObject jsonObject = getJsonData();
                    if (jsonObject!=null) {
                        mSocketManager.sendMessage(jsonObject);
                    }
                }

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


    public static String getFileToByte(String filePath){
        Bitmap bmp = null;
        ByteArrayOutputStream bos = null;
        byte[] bt = null;
        String encodeString = null;
        try{
            bmp = BitmapFactory.decodeFile(filePath);
            bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bt = bos.toByteArray();
            encodeString = Base64.encodeToString(bt, Base64.DEFAULT);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return encodeString;
    }

    public static String getMimeType(Context context, Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }

}
