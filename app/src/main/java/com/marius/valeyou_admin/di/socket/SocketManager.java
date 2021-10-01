package com.marius.valeyou_admin.di.socket;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.marius.valeyou_admin.data.local.SharedPref;
import com.marius.valeyou_admin.util.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import javax.inject.Inject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketManager {

    // sockets events
    public static String CONNECT_USER = "connect_user";
    public static String CONNECT_USER_LISTNER = "user_online";
    public static String GET_CHAT_LIST = "get_chat_list";
    public static String CHAT_LIST_LISTNER = "get_list";
    public static String MESSEGES_LIST= "get_chat";
    public static String MESSAGES_LIST_LISTNER = "my_chat";
    public static String MESSAGE_LISTNER = "body";
    public static String SEND_MESSAGE = "send_message";
    private Socket mSocket;
    public Observer observer;
    Activity mActivity;
    SharedPref sharedPref;

    public SocketManager(Activity mActviity,Observer observer, SharedPref sharedPref){
        this.mActivity=mActviity;
        this.observer = observer;
        this.sharedPref = sharedPref;

    }

    private Emitter.Listener onConnected = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i("Socket : ", "CONNECTED");


                if (isConnected()) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", sharedPref.getUserData().getId());
                        jsonObject.put(Constants.USER_TYPE, "1");
                        mSocket.on(CONNECT_USER_LISTNER, onConnectionListner);
                        mSocket.emit(CONNECT_USER, jsonObject);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


            } else {

                initializeSocket();

            }
        }
    };

    private boolean isConnected(){
        return mSocket!=null&&mSocket.connected();
    }


    private Emitter.Listener onConnectionListner = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("Response","onConnectionListner : "+"i am online now");
        }
    };

    private Emitter.Listener getChatList = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
               mActivity.runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       try {
                           Log.d("Response","Users List");
                             JSONArray data = (JSONArray) args[0];
                           Log.d("Response", String.valueOf(args[0]));

                           observer.getChatList(CHAT_LIST_LISTNER, data);

                       } catch (Exception e) {
                           e.printStackTrace();
                       }

                   }
               });

        }
    };

    private Emitter.Listener getMessegesListListner = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
           mActivity.runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   try {
                       Log.d("Response","Messages List");
                       JSONArray data = (JSONArray) args[0];
                       observer.getMessages(MESSAGES_LIST_LISTNER, data);

                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }
           });

        }


    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d("Response","New Message");
                        JSONObject data = (JSONObject) args[0];
                        observer.sendMessageResponse(MESSAGE_LISTNER, data);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

        }
    };


    public void sendMessage(JSONObject jsonObject) {
        if (jsonObject != null) {
            if (!mSocket.connected()) {
                mSocket.connect();
                mSocket.off(MESSAGE_LISTNER, onNewMessage);
                mSocket.on(MESSAGE_LISTNER, onNewMessage);
                mSocket.emit(SEND_MESSAGE, jsonObject);
            } else {
                mSocket.off(MESSAGE_LISTNER, onNewMessage);
                mSocket.on(MESSAGE_LISTNER, onNewMessage);
               mSocket.emit(SEND_MESSAGE, jsonObject);

            }
        }

    }

        public void getChatList(JSONObject jsonObject){
        try {
            if (jsonObject != null) {
                if (!mSocket.connected()) {
                    mSocket.connect();
                    mSocket.off(CHAT_LIST_LISTNER, getChatList);
                    mSocket.on(CHAT_LIST_LISTNER, getChatList);
                    mSocket.emit(GET_CHAT_LIST, jsonObject);
                } else {
                    mSocket.off(CHAT_LIST_LISTNER, getChatList);
                    mSocket.on(CHAT_LIST_LISTNER, getChatList);
                    mSocket.emit(GET_CHAT_LIST, jsonObject);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getMessageList(JSONObject jsonObject){
        try {

            if (jsonObject!=null){
                if (!mSocket.connected()) {
                    mSocket.connect();
                    mSocket.off(MESSAGES_LIST_LISTNER, getMessegesListListner);
                    mSocket.on(MESSAGES_LIST_LISTNER, getMessegesListListner);
                    mSocket.emit(MESSEGES_LIST, jsonObject);
                } else {
                    mSocket.off(MESSAGES_LIST_LISTNER, getMessegesListListner);
                    mSocket.on(MESSAGES_LIST_LISTNER, getMessegesListListner);
                    mSocket.emit(MESSEGES_LIST, jsonObject);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void init() {
        initializeSocket();
    }


    private void initializeSocket() {
        if (mSocket == null) {
            mSocket = getSocket();
        }
        mSocket.on(Socket.EVENT_CONNECT, onConnected);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on(MESSAGES_LIST_LISTNER, getMessegesListListner);
        mSocket.on(CHAT_LIST_LISTNER, getChatList);
        mSocket.on(MESSAGE_LISTNER, onNewMessage);
        mSocket.connect();

    }
    private Socket getSocket() {
        try {
            mSocket = IO.socket(Constants.SOCKET_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return mSocket;
    }

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e("Response", "onConnectError");
        }
    };
    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e("Response", "onDisconnect");
        }
    };



    public Socket getmSocket(){
        return mSocket;
    }


    public interface Observer {
        void getChatList(String event, JSONArray args);
        void getMessages(String event, JSONArray args);
        void sendMessageResponse(String event, JSONObject args);
    }

    public void disconnect(){
        if (mSocket!=null) {
            mSocket.off(Socket.EVENT_CONNECT, onConnected);
            mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
            mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket.off(MESSAGES_LIST_LISTNER, getMessegesListListner);
            mSocket.off(CHAT_LIST_LISTNER, getChatList);
            mSocket.off(MESSAGE_LISTNER, onNewMessage);
            mSocket.off();
            mSocket.disconnect();
        }
    }

}
