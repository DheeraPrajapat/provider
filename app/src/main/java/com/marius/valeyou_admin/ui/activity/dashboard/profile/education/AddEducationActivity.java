package com.marius.valeyou_admin.ui.activity.dashboard.profile.education;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.profilebean.ProfileModel;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityAddEducationBinding;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.di.base.view.BaseActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.ProfileActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.certificate.AddCertificateActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.certificate.AddCertificateActivityVM;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.util.Constants;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class AddEducationActivity extends AppActivity<ActivityAddEducationBinding,AddEducationActivityVM> {

    String auth;
    String start;
    String end;
    int education_id;
    String comeFrom;
    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, AddEducationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected BindingActivity<AddEducationActivityVM> getBindingActivity() {
        return new BaseActivity.BindingActivity<>(R.layout.activity_add_education, AddEducationActivityVM.class);
    }

    @Override
    protected void subscribeToEvents(AddEducationActivityVM vm) {
        binding.header.tvTitle.setText(getResources().getString(R.string.education));
        auth = vm.sharedPref.getUserData().getAuthKey();


        Intent intent = getIntent();
        if (intent!=null){
            comeFrom = intent.getStringExtra("comeFrom");
            if (comeFrom.equalsIgnoreCase("edit")){

                ProfileModel.EducationBean educationBean = intent.getParcelableExtra("educationBean");
                binding.etSchool.setText(educationBean.getSchool());
                binding.etDegree.setText(educationBean.getDegree());
                if (!educationBean.getGrade().isEmpty()){
                    binding.etGrade.setText(educationBean.getGrade());
                }
                education_id = educationBean.getId();


            }
        }

        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 1900; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, years);
        binding.spStart.setAdapter(adapter);
        binding.spStart.setSelection(years.size()-1);
        binding.spEnd.setAdapter(adapter);
        binding.spEnd.setSelection(years.size()-1);

        binding.spStart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
               start = selection;
            }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selection = (String) parent.getItemAtPosition(position);
                end = selection;

            }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                finish(true);
            }
        });


        vm.addEducationEvent.observe(this, new SingleRequestEvent.RequestObserver<SimpleApiResponse>() {
            @Override
            public void onRequestReceived(@NonNull Resource<SimpleApiResponse> resource) {
                switch (resource.status) {
                    case LOADING:
                        showProgressDialog(R.string.wait);
                        break;
                    case SUCCESS:
                        dismissProgressDialog();
                        Intent intent = ProfileActivity.newIntent(AddEducationActivity.this);
                        startNewActivity(intent);
                        finish();
                        break;
                    case ERROR:
                        dismissProgressDialog();
                        vm.error.setValue(resource.message);

                        if (resource.message.equalsIgnoreCase("unauthorized")){

                            Intent intent1 = LoginActivity.newIntent(AddEducationActivity.this);
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
                switch (view.getId()){

                    case R.id.cv_cancel:

                        finish(true);

                        break;

                    case R.id.cv_save:

                        String schoolStr = binding.etSchool.getText().toString();
                        String degreeStr = binding.etDegree.getText().toString();
                        String startDateStr = start;
                        String endDateStr = end;
                        String gradeStr = binding.etGrade.getText().toString();

                        HashMap<String,String> map = new HashMap<>();
                        if (schoolStr.isEmpty()){
                            vm.error.setValue(getResources().getString(R.string.please_enter_your_school_name));
                            return;
                        }
                        if (degreeStr.isEmpty()){
                            vm.error.setValue(getResources().getString(R.string.enter_your_degree_name));
                            return;
                        }

                        if (startDateStr.equals("")){
                            vm.error.setValue(getResources().getString(R.string.enter_your_degree_start_date));
                            return;
                        }

                        if (endDateStr.equals("")){
                            vm.error.setValue(getResources().getString(R.string.enter_your_degree_end_date));
                            return;
                        }


                        map.put(Constants.SCHOOL,schoolStr);

                        map.put(Constants.DEGREE,degreeStr);
                        map.put(Constants.START_DATE,startDateStr);
                        map.put(Constants.END_DATE,endDateStr);

                        if (!gradeStr.equals("")){
                            map.put(Constants.GRADE,gradeStr);
                        }


                        if (comeFrom.equalsIgnoreCase("edit")){
                            map.put(Constants.TYPE,"1");
                            vm.EducationApi(auth,map,education_id);

                        }else {
                            map.put(Constants.TYPE,"0");
                            vm.addEducationApi(auth, map);

                        }

                        break;
                }
            }
        });


    }


}
