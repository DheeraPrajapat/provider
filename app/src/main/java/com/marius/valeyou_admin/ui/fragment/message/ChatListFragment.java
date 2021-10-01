package com.marius.valeyou_admin.ui.fragment.message;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.NotificationBadgeModel;
import com.marius.valeyou_admin.data.beans.base.MoreBean;
import com.marius.valeyou_admin.data.beans.chat.UsersModel;
import com.marius.valeyou_admin.data.local.SharedPref;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.FragmentChatListBinding;
import com.marius.valeyou_admin.databinding.HolderChatListBinding;
import com.marius.valeyou_admin.di.base.adapter.SimpleRecyclerViewAdapter;
import com.marius.valeyou_admin.di.base.view.AppFragment;
import com.marius.valeyou_admin.di.socket.SocketManager;
import com.marius.valeyou_admin.ui.activity.dashboard.message.chatview.ChatActivity;
import com.marius.valeyou_admin.ui.activity.main.MainActivity;
import com.marius.valeyou_admin.util.AppUtils;
import com.marius.valeyou_admin.util.Constants;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import static com.marius.valeyou_admin.util.Constants.SOCKET_USER_ID;

public class ChatListFragment extends AppFragment<FragmentChatListBinding, ChatListFragmentVM> implements SocketManager.Observer {
    ChatUsersAdapter chatUsersAdapter;
    @Inject
    SharedPref sharedPref;
    public static final String TAG = "ChatListFragment";
    public SocketManager mSocketManager;
    List<UsersModel> usersList = new ArrayList<>();
    public static Fragment newInstance() {
        return new ChatListFragment();
    }
    String stripeId;
    private MainActivity mainActivity;

    private void resumeInit() {
        getChatList();
    }

    @Override
    public void onResume() {
        super.onResume();
        resumeInit();
        viewModel.getCount();

    }

    @Override
    protected BindingFragment<ChatListFragmentVM> getBindingFragment() {
        return new BindingFragment<>(R.layout.fragment_chat_list, ChatListFragmentVM.class);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mSocketManager == null) {
            mSocketManager = new SocketManager(getActivity(),this,sharedPref);
        }
        if (mSocketManager.getmSocket()==null){
            mSocketManager.init();
        }

    }



    @Override
    protected void subscribeToEvents(ChatListFragmentVM vm) {
        mainActivity = (MainActivity) getActivity();
        mainActivity.hideBottomNav(false);

        vm.notificationbadgeEventRequest.observe(this, new SingleRequestEvent.RequestObserver<NotificationBadgeModel>() {
            @Override
            public void onRequestReceived(@NonNull Resource<NotificationBadgeModel> resource) {
                switch (resource.status) {
                    case LOADING:
                         break;
                    case SUCCESS:
                         mainActivity.badgeCount(resource.data.getCount());
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

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("input","==="+charSequence.toString());
                chatUsersAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void setRecyclerView(List<UsersModel> list) {
        Log.e("ASASDASDA", String.valueOf(list.size()));
        Log.e("ASASDASDA1",list.get(0).getChat().getMessage());
        if (list.size()>0) {
            binding.tvNoData.setVisibility(View.GONE);
            binding.rvChatList.setVisibility(View.VISIBLE);
             chatUsersAdapter = new ChatUsersAdapter(getContext(), list, new ChatUsersAdapter.Listner() {
                @Override
                public void onItemClick(HolderChatListBinding v, int position, UsersModel model) {
                    Intent intent = new Intent(getActivity(),ChatActivity.class);
                    intent.putExtra("comeFrom","chat");
                    intent.putExtra("userData",model);
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(getActivity(), v.cvPRoile, "user_profile");
                    startActivity(intent,options.toBundle());
                }
            });
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            binding.rvChatList.setLayoutManager(layoutManager);
            binding.rvChatList.setAdapter(chatUsersAdapter);

        }else{

            binding.tvNoData.setVisibility(View.VISIBLE);
            binding.rvChatList.setVisibility(View.GONE);

        }
    }


    @Override
    public void getChatList(String event, JSONArray args) {
        binding.setCheck(false);
        usersList.clear();
        Type  type = TypeToken.getParameterized(ArrayList.class, UsersModel.class).getType();
        Log.e("ASDASAS",type.getTypeName());
        usersList = new Gson().fromJson(args.toString(), type);
        setRecyclerView(usersList);
    }

    @Override
    public void getMessages(String event, JSONArray args) {

    }

    @Override
    public void sendMessageResponse(String event, JSONObject args) {

    }

    public void getChatList(){
        
        try {
            binding.setCheck(true);
            JSONObject jsonObject =  new JSONObject();
            jsonObject.put(Constants.SOCKET_USER_ID, sharedPref.getUserData().getId());
            jsonObject.put(Constants.TYPE, "1");
            jsonObject.put(Constants.PAGE,"1");
            jsonObject.put(Constants.LIMIT,"10");
            jsonObject.put(Constants.APP_TYPE, 0);
            mSocketManager.getChatList(jsonObject);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSocketManager!=null){
            mSocketManager.disconnect();
        }
    }
}
