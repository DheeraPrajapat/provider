package com.marius.valeyou_admin.ui.fragment.myjob;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.facebook.share.Share;
import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.NotificationBadgeModel;
import com.marius.valeyou_admin.data.beans.base.MoreBean;
import com.marius.valeyou_admin.data.beans.categoriesBean.CategoryDataBean;
import com.marius.valeyou_admin.data.beans.jobs.MyJobsModel;
import com.marius.valeyou_admin.data.beans.map.MapListModel;
import com.marius.valeyou_admin.data.beans.rejected_bids.RejectedModels;
import com.marius.valeyou_admin.data.local.SharedPref;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.FragmentMyJobBinding;
import com.marius.valeyou_admin.databinding.HolderFilterCategoryBinding;
import com.marius.valeyou_admin.databinding.HolderRejectedBidBinding;
import com.marius.valeyou_admin.databinding.HolderSetCategoryBinding;
import com.marius.valeyou_admin.di.base.adapter.PagerAdapter;
import com.marius.valeyou_admin.di.base.adapter.SimpleRecyclerViewAdapter;
import com.marius.valeyou_admin.di.base.view.AppFragment;
import com.marius.valeyou_admin.ui.activity.RatingActivity;
import com.marius.valeyou_admin.ui.activity.bids.PlacedBidsActivity;
import com.marius.valeyou_admin.ui.activity.bids.PlacedBidsAdapter;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.activity.main.MainActivity;
import com.marius.valeyou_admin.ui.activity.notification.NotificationActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.startjob.StartJobTimerActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.UpComingJobFragment;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.UpComingJobsAdapter;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.closedetails.JobDetailsActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.complete.CompleteJobActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.opendetails.CurrentJobDetailsActivity;
import com.marius.valeyou_admin.ui.fragment.myjob.upcoming.opendetails.closejobs.CloseJobsFragment;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import javax.inject.Inject;

public class MyJobFragment extends AppFragment<FragmentMyJobBinding,MyJobFragmentVM> implements PlacedBidsAdapter.ProductCallback {

    public static final String TAG = "MyJobFragment";
    PopupWindow popupWindow;
    String authKey;
    List<MapListModel> projectsList;
    List<RejectedModels> rejectedBidsList = new ArrayList<>();
    List<MapListModel> finalList = new ArrayList<>();
    MainActivity mainActivity;
    @Inject
    SharedPref sharedPref;

    public static Fragment newInstance() {
        return new MyJobFragment();
    }

    @Override
    protected BindingFragment<MyJobFragmentVM> getBindingFragment() {
        return new BindingFragment<>(R.layout.fragment_my_job,MyJobFragmentVM.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        authKey = viewModel.sharedPref.getUserData().getAuthKey();
        binding.setValue(0);
        if (!authKey.isEmpty()&&authKey!=null) {
            viewModel.myJobsApi(authKey, "0");
        }
        viewModel.getCount();

    }

    @Override
    protected void subscribeToEvents(MyJobFragmentVM vm) {
        mainActivity = (MainActivity) getActivity();
        mainActivity.hideBottomNav(false);
        vm.base_click.observe(this, new Observer<View>() {
            @Override
            public void onChanged(View view) {
                switch (view!=null?view.getId():0){
                    case R.id.iv_filter:
                        showSortPopup(getActivity());
                        break;

                    case R.id.notificationIcon:
                        Intent in = NotificationActivity.newIntent(getActivity());
                        startNewActivity(in);
                        break;

                    case R.id.llPlacedBids:
                        binding.setValue(1);
                        finalList.clear();
                        rejectedBidsList.clear();
                        vm.getMapListFilterApi(sharedPref.getUserData().getAuthKey(), "","", "", "", "", "","","","");
                        break;

                    case R.id.llRejectedBids:
                        binding.setValue(2);
                        finalList.clear();
                        rejectedBidsList.clear();
                        vm.getREjectedBids(sharedPref.getUserData().getAuthKey());
                        break;

                    case R.id.ll_one:
                        binding.setValue(0);
                        if (!authKey.isEmpty()&&authKey!=null) {
                            viewModel.myJobsApi(authKey, "0");
                        }
                        break;
                }
            }
        });


        vm.mapListBean.observe(this, new SingleRequestEvent.RequestObserver<List<MapListModel>>() {
            @Override
            public void onRequestReceived(@NonNull Resource<List<MapListModel>> resource) {
                switch (resource.status) {
                    case LOADING:
                        binding.setCheck(true);
                        break;
                    case SUCCESS:
                        binding.setCheck(false);
                        binding.setVariable(BR.bean, resource.data);
                        projectsList = resource.data;

                        for (int i = 0; i <projectsList.size(); i++) {

                            if (projectsList.get(i).getBid_status()==0&&projectsList.get(i).getBid_price()!=0) {

                                MapListModel model = new MapListModel();
                                model.setBid_price(projectsList.get(i).getBid_price());
                                model.setId(projectsList.get(i).getId());
                                model.setDescription(projectsList.get(i).getDescription());
                                model.setJobType(projectsList.get(i).getJobType());
                                model.setDistance(projectsList.get(i).getDistance());
                                model.setTitle(projectsList.get(i).getTitle());
                                model.setOrderImages(projectsList.get(i).getOrderImages());
                                model.setUserId(projectsList.get(i).getUserId());

                                finalList.add(model);


                            }

                        }

                        if (finalList.size()>0){
                            binding.rvBids.setVisibility(View.VISIBLE);
                            binding.tvNoJob.setVisibility(View.GONE);
                            setRecyclerViewList(finalList);
                        }else{
                            binding.rvBids.setVisibility(View.GONE);
                            binding.tvNoJob.setVisibility(View.VISIBLE);
                        }





                        break;
                    case ERROR:
                        binding.setCheck(false);
                        vm.error.setValue(resource.message);

                        if (resource.message.equalsIgnoreCase(getResources().getString(R.string.unauthorised))){
                            sharedPref.deleteAll();
                            Intent intent1 = LoginActivity.newIntent(getActivity());
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

        vm.rejectedBeanEvent.observe(this, new SingleRequestEvent.RequestObserver<List<RejectedModels>>() {
            @Override
            public void onRequestReceived(@NonNull Resource<List<RejectedModels>> resource) {
                switch (resource.status) {
                    case LOADING:
                        binding.setCheck(true);
                        break;
                    case SUCCESS:
                        binding.setCheck(false);
                        binding.setVariable(BR.bean, resource.data);
                        rejectedBidsList = resource.data;

                        if (rejectedBidsList.size()>0) {

                            binding.tvNoJob.setVisibility(View.GONE);
                            binding.rvRejectedBids.setVisibility(View.VISIBLE);
                            adapterRejectedBids.setList(rejectedBidsList);

                        }else{

                            binding.rvRejectedBids.setVisibility(View.GONE);
                            binding.tvNoJob.setVisibility(View.VISIBLE);

                        }

                        break;
                    case ERROR:
                        binding.setCheck(false);
                        vm.error.setValue(resource.message);
                        if (resource.message.equalsIgnoreCase(getResources().getString(R.string.unauthorised))){
                            sharedPref.deleteAll();
                            Intent intent1 = LoginActivity.newIntent(getActivity());
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


        vm.notificationbadgeEventRequest.observe(this, new SingleRequestEvent.RequestObserver<NotificationBadgeModel>() {
            @Override
            public void onRequestReceived(@NonNull Resource<NotificationBadgeModel> resource) {
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        mainActivity.badgeCount(resource.data.getCount());

                        if (resource.data.getNotification()>0){

                            binding.notificationCount.setVisibility(View.VISIBLE);
                            if (resource.data.getNotification()>9){
                                binding.notificationCount.setText("9+");
                            }else{
                                binding.notificationCount.setText(resource.data.getNotification()+"");
                            }

                        }else{
                            binding.notificationCount.setVisibility(View.GONE);
                        }

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




        vm.myJobsBean.observe(this, new SingleRequestEvent.RequestObserver<List<MyJobsModel>>() {
            @Override
            public void onRequestReceived(@NonNull Resource<List<MyJobsModel>> resource) {
                switch (resource.status) {
                    case LOADING:
                        binding.setCheck(true);
                        break;
                    case SUCCESS:
                        binding.setCheck(false);
                        setRecyclerView(resource.data);
                        break;
                    case ERROR:
                        binding.setCheck(false);
                        vm.error.setValue(resource.message);
                        if (resource.message.equalsIgnoreCase(getResources().getString(R.string.unauthorised))){
                            sharedPref.deleteAll();
                            Intent intent1 = LoginActivity.newIntent(getActivity());
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

        setrejectedList();
    }

    private void setRecyclerView(List<MyJobsModel> myJobsModelsList) {
        if (myJobsModelsList.size()>0) {
            binding.rvMyJobs.setVisibility(View.VISIBLE);
            binding.tvNoJob.setVisibility(View.GONE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            binding.rvMyJobs.setLayoutManager(layoutManager);
            binding.rvMyJobs.setAdapter(mAdapter);
            mAdapter.setList(myJobsModelsList);
        }else{
            binding.rvMyJobs.setVisibility(View.GONE);
            binding.tvNoJob.setVisibility(View.VISIBLE);
        }
    }

    UpComingJobsAdapter mAdapter = new UpComingJobsAdapter(getActivity(), new UpComingJobsAdapter.ProductCallback() {
        @Override
        public void onItemClick(View v, MyJobsModel m) {

            switch (v.getId()){
                case R.id.ll_job_item_click:

                        Intent intent = CurrentJobDetailsActivity.newIntent(getActivity());
                        intent.putExtra("id", m.getId());
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(getActivity(), v, "card");
                    startActivity(intent,options.toBundle());


                    break;

                case R.id.viewReason:
                    if (m.getReason()!=null){
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("*** Reason ***");
                        alertDialog.setMessage(m.getReason().getUser_data().getFirstName()+" "+m.getReason().getUser_data().getLastName()+"\n"+m.getReason().getReason());
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }else{
                        viewModel.error.setValue("No Reason Available");
                    }




                    break;
            }

        }
    });

    private void showSortPopup(final Activity mContext) {
        LayoutInflater layoutInflater = (LayoutInflater)mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.window_popup_filter, null);
        popupWindow=new PopupWindow(popupView,
                300,  ViewGroup.LayoutParams.WRAP_CONTENT,
                true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setElevation(10);

        popupWindow.showAtLocation(binding.ivFilter, Gravity.RIGHT|Gravity.TOP, 30, 450);
        popupView.findViewById(R.id.tv_all_jobs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (!authKey.isEmpty()&&authKey!=null) {
                    viewModel.myJobsApi(authKey, "0");
                }
            }
        });


        popupView.findViewById(R.id.tv_ongoing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (!authKey.isEmpty()&&authKey!=null) {
                    binding.tvNoJob.setVisibility(View.GONE);
                    viewModel.myJobsApi(authKey, "7");
                }
            }
        });


        popupView.findViewById(R.id.tv_completed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (!authKey.isEmpty()&&authKey!=null) {
                    binding.tvNoJob.setVisibility(View.GONE);
                    viewModel.myJobsApi(authKey, "9");
                }
            }
        });


        popupView.findViewById(R.id.tv_canceled).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (!authKey.isEmpty()&&authKey!=null) {
                    binding.tvNoJob.setVisibility(View.GONE);
                    viewModel.myJobsApi(authKey, "2");
                }
            }
        });

        popupView.findViewById(R.id.tv_accepted).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (!authKey.isEmpty()&&authKey!=null) {
                    binding.tvNoJob.setVisibility(View.GONE);
                    viewModel.myJobsApi(authKey, "1");
                }
            }
        });

        popupView.findViewById(R.id.tv_cheapest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (!authKey.isEmpty()&&authKey!=null) {
                    binding.tvNoJob.setVisibility(View.GONE);
                    viewModel.myJobsApi(authKey, "19");
                }
            }
        });

        popupView.findViewById(R.id.tv_expensive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (!authKey.isEmpty()&&authKey!=null) {
                    binding.tvNoJob.setVisibility(View.GONE);
                    viewModel.myJobsApi(authKey, "20");
                }
            }
        });

    }


    private void setRecyclerViewList(List<MapListModel> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        PlacedBidsAdapter placedBidsAdapter = new PlacedBidsAdapter(getActivity(), this, list);
        binding.rvBids.setLayoutManager(layoutManager);
        binding.rvBids.setAdapter(placedBidsAdapter);

    }


    private void setrejectedList(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.rvRejectedBids.setLayoutManager(layoutManager);
        binding.rvRejectedBids.setAdapter(adapterRejectedBids);

    }

    SimpleRecyclerViewAdapter<RejectedModels, HolderRejectedBidBinding> adapterRejectedBids =
            new SimpleRecyclerViewAdapter<>(R.layout.holder_rejected_bid, BR.bean, new SimpleRecyclerViewAdapter.SimpleCallback<RejectedModels>() {
                @Override
                public void onItemClick(View v, RejectedModels moreBean) {
                }
            });

    @Override
    public void onItemClick(View v, List<MapListModel> moreBeans, int pos) {
        switch (v.getId()){
            case R.id.ll_list_items:
                Intent intent = JobDetailsActivity.newIntent(getActivity());
                intent.putExtra("id",moreBeans.get(pos).getId());
                startNewActivity(intent);
                break;
        }
    }
}
