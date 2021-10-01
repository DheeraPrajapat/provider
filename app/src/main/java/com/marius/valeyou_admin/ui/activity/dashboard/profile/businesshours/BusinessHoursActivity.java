package com.marius.valeyou_admin.ui.activity.dashboard.profile.businesshours;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.base.MoreBean;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.business.BusinessHoursModel;
import com.marius.valeyou_admin.data.beans.categoriesBean.CategoryDataBean;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityBusinessHoursBinding;
import com.marius.valeyou_admin.databinding.HolderAddBusinessHoursBinding;
import com.marius.valeyou_admin.databinding.HolderBusinessHourBinding;
import com.marius.valeyou_admin.di.base.adapter.SimpleRecyclerViewAdapter;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.ProfileActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.ProfileActivityVM;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.certificate.AddCertificateActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.portfolio.AddPortfolioActivity;
import com.marius.valeyou_admin.ui.activity.signup.signuptwo.selectcategory.CategoryAdapter;
import com.marius.valeyou_admin.ui.activity.signup.signuptwo.selectcategory.SelectCategoryActivity;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

public class BusinessHoursActivity extends AppActivity<ActivityBusinessHoursBinding,BusinessHoursActivityVM> {
    private int mYear, mMonth, mDay, mHour, mMinute;

    BusinessHoursAdapter mAdapter;

    BusinessHoursModel model = null;

    List<BusinessHoursModel> hoursList = new ArrayList<>();

    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, BusinessHoursActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }


    @Override
    protected BindingActivity<BusinessHoursActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_business_hours, BusinessHoursActivityVM.class);
    }


    @Override
    protected void subscribeToEvents(BusinessHoursActivityVM vm) {
        binding.header.tvTitle.setText(getResources().getString(R.string.businesshours));

        setRecycleBusiness(getBusinessHours());

        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                finish(true);
            }
        });

        vm.addBussinessHours.observe(this, new SingleRequestEvent.RequestObserver<SimpleApiResponse>() {
            @Override
            public void onRequestReceived(@NonNull Resource<SimpleApiResponse> resource) {
                switch (resource.status) {
                    case LOADING:
                        showProgressDialog(R.string.wait);
                        break;
                    case SUCCESS:
                        dismissProgressDialog();
                        Intent intent = ProfileActivity.newIntent(BusinessHoursActivity.this);
                        startNewActivity(intent);
                        finish();
                        break;
                    case ERROR:
                        dismissProgressDialog();
                        vm.error.setValue(resource.message);
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
                switch (view.getId()){
                    case R.id.cv_save:
                        List<BusinessHoursModel> list = mAdapter.getList();
                        List<BusinessHoursModel> finalLsit  =new ArrayList<>();
                        for (int i = 0; i < list.size();i++){
                            if (list.get(i).isOff()){

                            }else{
                                BusinessHoursModel model = new BusinessHoursModel();
                                model.setOff(list.get(i).isOff());
                                model.setDay(list.get(i).getDay());
                                model.setFromTime(list.get(i).getFromTime());
                                model.setToTime(list.get(i).getToTime());
                                finalLsit.add(model);
                            }
                        }
                        Gson gson = new Gson();
                        String json = gson.toJson(finalLsit);
                        vm.addBusienssHoursApi(vm.sharedPref.getUserData().getAuthKey(),
                                json,"1");

                        break;
                }
            }
        });
    }


    private void setRecycleBusiness(List<BusinessHoursModel> dataList) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(BusinessHoursActivity.this);
        binding.rvBusinessHours.setLayoutManager(layoutManager);
        mAdapter = new BusinessHoursAdapter(this, new BusinessHoursAdapter.ProductCallback() {
            @Override
            public void onItemClick(View v, List<BusinessHoursModel> list,int pos,HolderAddBusinessHoursBinding binding) {

                switch (v.getId()){


                    case R.id.ll_time_item:


                        break;

                    case R.id.cv_fromTimeView:
                            fromTimePickerVeiw(list.get(pos),pos);

                        break;

                    case R.id.cv_toTimeView:
                        toTimePickerVeiw(list.get(pos),pos);
                        break;


                }


            }

            @Override
            public void onItemChecked(View v, List<BusinessHoursModel> list, int pos, boolean isCheck, HolderAddBusinessHoursBinding binding) {
             if (isCheck){
                 list.get(pos).setOff(true);
                 binding.llEditTimeView.setVisibility(View.GONE);
                 binding.llTimeItem.setClickable(false);
                 binding.llTimeItem.setBackgroundColor(getResources().getColor(R.color.grey_light));
             }else{
                 list.get(pos).setOff(false);
                 binding.llEditTimeView.setVisibility(View.VISIBLE);
                 binding.llTimeItem.setClickable(true);
                 binding.llTimeItem.setBackgroundColor(getResources().getColor(R.color.white));
             }
            }
        });
        binding.rvBusinessHours.setAdapter(mAdapter);
        mAdapter.setProductList(dataList);
    }


    private void toTimePickerVeiw(BusinessHoursModel m,int pos){
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        m.setToTime(hourOfDay +":"+ minute);
                        m.setOff(m.isOff());
                        mAdapter.notifyItemChanged(pos);


                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void fromTimePickerVeiw(BusinessHoursModel m,int pos){
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        m.setFromTime(hourOfDay +":"+ minute);
                        m.setOff(m.isOff());
                        mAdapter.notifyItemChanged(pos);


                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }



    private List<BusinessHoursModel> getBusinessHours() {
        List<BusinessHoursModel> beanList = new ArrayList<>();
        beanList.add(new BusinessHoursModel("Monday","8:00","18:00"));
        beanList.add(new BusinessHoursModel("Tuesday","8:00","18:00"));
        beanList.add(new BusinessHoursModel("Wednesday","8:00","18:00"));
        beanList.add(new BusinessHoursModel("Thrusday","8:00","18:00"));
        beanList.add(new BusinessHoursModel("Friday","8:00","18:00"));
        beanList.add(new BusinessHoursModel("Saturday","8:00","18:00"));
        beanList.add(new BusinessHoursModel("Sunday","8:00","18:00"));

        return beanList;
    }

}
