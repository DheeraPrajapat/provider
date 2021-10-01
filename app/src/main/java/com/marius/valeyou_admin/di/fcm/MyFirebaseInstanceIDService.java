package com.marius.valeyou_admin.di.fcm;
import com.google.firebase.messaging.FirebaseMessagingService;
//Class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseIIDService";
    public static String refreshedToken;

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project
    }
}

