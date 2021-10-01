package com.marius.valeyou_admin.ui.activity.login;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Location;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.singninbean.SignInModel;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityLoginBinding;
import com.marius.valeyou_admin.databinding.OtpBinding;
import com.marius.valeyou_admin.di.base.dialog.BaseCustomDialog;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.ui.activity.forgot.ForgotPasswordActivity;
import com.marius.valeyou_admin.ui.activity.language.SelectLanguageActivity;
import com.marius.valeyou_admin.ui.activity.main.MainActivity;
import com.marius.valeyou_admin.ui.activity.signup.SignupActivity;
import com.marius.valeyou_admin.ui.activity.signup.signuptwo.SignupTwoActivity;
import com.marius.valeyou_admin.util.Constants;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;
import com.marius.valeyou_admin.util.location.LiveLocationDetecter;
import com.marius.valeyou_admin.util.location.LocCallback;
import com.marius.valeyou_admin.util.permission.PermissionHandler;
import com.marius.valeyou_admin.util.permission.Permissions;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import static android.content.ContentValues.TAG;

public class LoginActivity extends AppActivity<ActivityLoginBinding, LoginActivityVM> {
//login
    private static final int RC_SIGN_IN = 1;
    private Location mCurrentlocation;
    String  socialId,name,email;
    @Inject
    LiveLocationDetecter liveLocationDetecter;
    private GoogleSignInClient mGoogleSignInClient;
    LoginActivityVM loginViewModel;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+.+[a-z]+";
    CallbackManager callbackManager;
    @Override
    protected BindingActivity<LoginActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_login, LoginActivityVM.class);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String lang = sharedPref.get("language","en");
        if (lang.equalsIgnoreCase("en")){
            binding.langName.setText("English");
        }else if (lang.equalsIgnoreCase("pt")){
            binding.langName.setText("Português");
        }
    }

    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
    }





    @Override
    protected void subscribeToEvents(final LoginActivityVM vm) {

        printHashKey(this);

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        sharedPref.put(Constants.FCM_TOKEN, task.getResult().getToken());
                    }
                });


        vm.base_back.observe(this, aVoid -> {
            finish(true);
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
                            locCallback.getApiException().startResolutionForResult(LoginActivity.this, LiveLocationDetecter.REQUEST_CHECK_SETTINGS);
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

        vm.base_click.observe(this, view -> {
            if (view == null)
                return;
            switch (view != null ? view.getId() : 0) {

                case R.id.changeLanguage:
                    Intent lngIntent = SelectLanguageActivity.newIntent(LoginActivity.this);
                    startNewActivity(lngIntent);
                    break;
                case R.id.btn_login:

                    if (binding.etEmail.getText().toString().isEmpty()){

                        vm.error.setValue(getResources().getString(R.string.enter_email));
                        return;

                    } if (!binding.etEmail.getText().toString().matches(emailPattern)){

                    vm.error.setValue(getResources().getString(R.string.valid_email));
                    return;

                }else if (binding.etPassword.getText().toString().isEmpty()){

                        vm.error.setValue(getResources().getString(R.string.enter_password));
                    return;

                    }else{
                        Map<String, String> map = new HashMap<>();
                        map.put("email", binding.etEmail.getText().toString());
                        map.put("password", binding.etPassword.getText().toString());
                        map.put("device_type", "1");
                        map.put("device_token", sharedPref.get(Constants.FCM_TOKEN, "xyz"));
                        if (mCurrentlocation != null) {
                            map.put("latitude", String.valueOf(mCurrentlocation.getLatitude()));
                            map.put("longitude", String.valueOf(mCurrentlocation.getLongitude()));
                        } else {
                            map.put("latitude", "30.7046");
                            map.put("longitude", "76.7179");
                        }

                        vm.signIn(map);
                    }


                    break;
                case R.id.tv_signup:
                    Intent intent = SignupActivity.newIntent(LoginActivity.this);
                    startNewActivity(intent, false, true);
                    break;
                case R.id.tv_forgot:
                    Intent intent2 = ForgotPasswordActivity.newIntent(LoginActivity.this);
                    startNewActivity(intent2, false, true);
                    break;
                case R.id.cv_gmail:
                    signInGoogle();
                    break;

                case R.id.cv_facebook:
                    facebookLogin();
                    break;
            }
        });

        vm.sendOTPEvent.observe(this, new SingleRequestEvent.RequestObserver<SimpleApiResponse>() {
            @Override
            public void onRequestReceived(@NonNull Resource<SimpleApiResponse> resource) {
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        vm.success.setValue(resource.message);
                        break;
                    case ERROR:
                       vm.error.setValue(resource.message);
                        break;
                    case WARN:
                        vm.warn.setValue(resource.message);
                        break;
                }
            }
        });


        vm.verifyEmailEvent.observe(this, new SingleRequestEvent.RequestObserver<SimpleApiResponse>() {
            @Override
            public void onRequestReceived(@NonNull Resource<SimpleApiResponse> resource) {
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        verificationDialog.dismiss();
                        vm.success.setValue(resource.message);

                        Intent intent = MainActivity.newIntent(LoginActivity.this);
                        intent.putExtra("social", false);
                        startNewActivity(intent,true);
                        finishAffinity();

                        break;
                    case ERROR:
                        if (resource.message.equalsIgnoreCase("bad request")){
                            vm.error.setValue(getResources().getString(R.string.invalid_otp));
                        }else{
                            vm.error.setValue(resource.message);
                        }
                        break;
                    case WARN:
                        vm.warn.setValue(resource.message);
                        break;
                }
            }
        });


        vm.socialBean.observe(this, new SingleRequestEvent.RequestObserver<SignInModel>() {
            @Override
            public void onRequestReceived(@NonNull Resource<SignInModel> resource) {
                switch (resource.status) {
                    case LOADING:
                        binding.setCheck(true);
                        break;
                    case SUCCESS:
                        binding.setCheck(false);
                        if (resource.data.getStatus()==0){
                            vm.sharedPref.put(Constants.SOCIAL_ID,socialId);

                            Intent intent1 = SignupActivity.newIntent(LoginActivity.this);
                            intent1.putExtra(Constants.NAME,name);
                            intent1.putExtra("social",true);
                            intent1.putExtra(Constants.EMAIL,email);
                            startNewActivity(intent1, false, true);

                        }else if (resource.data.getStatus()==1) {
                        vm.sharedPref.put(Constants.SOCIAL_ID,socialId);
                        Intent intent1 = MainActivity.newIntent(LoginActivity.this);
                        intent1.putExtra("comeFrom","fblogin");
                        startNewActivity(intent1, true, true);
                    }
                        break;
                    case ERROR:
                        binding.setCheck(false);
                        if (resource.status.equals("400")) {
                            vm.error.setValue(getResources().getString(R.string.incorrect_email_password));
                        }else{
                            vm.error.setValue(resource.message);
                        }

                        break;
                    case WARN:
                        binding.setCheck(false);
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
                        binding.setCheck(true);
                        break;
                    case SUCCESS:
                        binding.setCheck(false);

                        if (resource.data.getIsEmailVerify() ==0){

                            verifyDialog(resource.data.getEmail());

                        }else {
                            Intent intent1 = MainActivity.newIntent(LoginActivity.this);
                            intent1.putExtra("social", false);
                            startNewActivity(intent1, true, true);

                        }
                        break;
                    case ERROR:
                        binding.setCheck(false);
                        if (resource.message.equalsIgnoreCase("Bad Request")) {
                            vm.error.setValue(getResources().getString(R.string.incorrect_email_password));
                        }else{
                            vm.error.setValue(resource.message);
                        }

                        break;
                    case WARN:
                        binding.setCheck(false);
                        vm.warn.setValue(resource.message);
                        break;
                }
            }
        });


        googleSignIn();

    }

    @Override
    public void onStart() {
        super.onStart();
        getCurrentLocation();
    }

    private void getCurrentLocation() {
        Permissions.check(this,Manifest.permission.ACCESS_FINE_LOCATION, 0, new PermissionHandler() {
            @Override
            public void onGranted() {
                viewModel.liveLocationDetecter.startLocationUpdates();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        liveLocationDetecter.onActivityResult(requestCode, resultCode);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void googleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signInGoogle() {
        mGoogleSignInClient.signOut();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void updateUI(GoogleSignInAccount account) {
       /* viewModel.success.setValue("Success::" + account.getId() + "\n" + account.getDisplayName()
                + "\n" + account.getEmail() + "\n" + account.getPhotoUrl());
*/
        socialId = account.getId();
        Log.d("Social_id ",socialId);
        viewModel.sharedPref.put(Constants.SOCIAL_ID,socialId);
        name = account.getDisplayName();
        email = account.getEmail();
        Map<String, String> map = new HashMap<>();
        map.put(Constants.SOCIAL_ID, account.getId());
        map.put("social_type", "2");
        map.put("device_type", "1");
        map.put("device_token", "xyz");


        viewModel.socialSignIn(map);

    }


    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("KEY", " : " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("KEY", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("KEY", "printHashKey()", e);
        }
    }



    private void facebookLogin(){
        LoginManager.getInstance().logOut();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("user_photos", "email", "public_profile", "user_posts"));
        LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList("publish_actions"));
        binding.loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String accessToken = loginResult.getAccessToken()
                        .getToken();
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                Log.d("facebook : ",
                                        response.toString());
                                try {
                                    socialId = object.getString("id");
                                      name = object.getString("name");
                                     email = object.getString("email");
                                     viewModel.sharedPref.put(Constants.SOCIAL_ID,socialId);
                                    try {
                                        URL profile_pic = new URL(
                                                "http://graph.facebook.com/" + socialId + "/picture?type=large");
                                        Log.d("facebook : profile : ",
                                                profile_pic + "");

                                        Map<String, String> map = new HashMap<>();
                                        map.put("social_id", socialId);
                                        map.put("social_type", "1");
                                        map.put("device_type", "1");
                                        map.put("device_token", "xyz");

                                        viewModel.socialSignIn(map);


                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields",
                        "id,name,email,gender, birthday");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();

            }

            @Override
            public void onCancel() {

                viewModel.error.setValue("onCancel");
                Log.d("facebook: ","onCancel");

            }

            @Override
            public void onError(FacebookException error) {
                viewModel.error.setValue("onError");
                Log.d("facebook: ","onError "+error);
            }
        });
    }


    private BaseCustomDialog<OtpBinding> verificationDialog;
    private void verifyDialog(String email) {
        verificationDialog = new BaseCustomDialog<>(LoginActivity.this, R.layout.otp, new BaseCustomDialog.Listener() {
            @Override
            public void onViewClick(View view) {
                if (view != null && view.getId() != 0) {
                    switch (view.getId()) {

                        case R.id.iv_cancel:
                            verificationDialog.dismiss();
                            break;
                        case R.id.btn_submit:
                            if (!verificationDialog.getBinding().etOtp.getText().toString().isEmpty()){
                                viewModel.verifyEmail(email,verificationDialog.getBinding().etOtp.getText().toString().trim());
                            }else{
                                viewModel.error.setValue(getResources().getString(R.string.enter_otp));
                            }
                            break;

                        case R.id.resend_otp:
                            if (!email.isEmpty()){
                                viewModel.sendOTP(email);
                            }else{
                                viewModel.error.setValue("Something went wrong");
                            }
                            break;

                    }
                }
            }
        });
        verificationDialog.getBinding().tvTwo.setText(getResources().getString(R.string.enter_the_otp_you_recive_at)+"\n"+email);
        verificationDialog.setCancelable(true);
        verificationDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        verificationDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        verificationDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        verificationDialog.show();

    }


}
