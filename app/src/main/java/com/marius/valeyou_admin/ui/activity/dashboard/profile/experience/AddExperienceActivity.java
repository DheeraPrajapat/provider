package com.marius.valeyou_admin.ui.activity.dashboard.profile.experience;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.profilebean.ProfileModel;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityAddExperienceBinding;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.di.base.view.BaseActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.ProfileActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.education.AddEducationActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.education.AddEducationActivityVM;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.activity.signup.signuptwo.SignupTwoActivity;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class AddExperienceActivity extends AppActivity<ActivityAddExperienceBinding,AddExperienceActivityVM> {
    final Calendar myCalendar = Calendar.getInstance();
    String finalDate;
    int status = 0;
    String auth_key;
    String selection;
    String comeFrom;
    int exp_id;
    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, AddExperienceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected BindingActivity<AddExperienceActivityVM> getBindingActivity() {
        return new BaseActivity.BindingActivity<>(R.layout.activity_add_experience, AddExperienceActivityVM.class);
    }


    @Override
    protected void subscribeToEvents(AddExperienceActivityVM vm) {
        binding.header.tvTitle.setText(getResources().getString(R.string.experience));
        auth_key = vm.sharedPref.getUserData().getAuthKey();

        Intent intent = getIntent();
        if (intent!=null){
            comeFrom = intent.getStringExtra("comeFrom");
            if (comeFrom.equalsIgnoreCase("edit")){

                ProfileModel.ExperienceBean experienceBean = intent.getParcelableExtra("experienceBean");
                binding.etTitle.setText(experienceBean.getTitle());
                if (!experienceBean.getDescription().isEmpty()){
                    binding.etDesc.setText(experienceBean.getDescription());
                }
                binding.etCompanyName.setText(experienceBean.getCompany());
                binding.etLocation.setText(experienceBean.getLocation());
                binding.spStart.setText(experienceBean.getStartDate());
                if (experienceBean.getCurrentlyWorking()==0){
                    binding.llEndDateLayout.setVisibility(View.VISIBLE);
                    binding.spEnd.setText(experienceBean.getEndDate());
                    binding.tvPresent.setVisibility(View.GONE);
                    binding.cbStatus.setChecked(false);

                }else{
                    binding.cbStatus.setChecked(false);
                    binding.llEndDateLayout.setVisibility(View.GONE);
                    binding.tvPresent.setVisibility(View.VISIBLE);
                    binding.cbStatus.setChecked(true);
                }


                exp_id = experienceBean.getId();


            }
        }
        ArrayList<String> typeList = new ArrayList<String>();

        typeList.add("Full Time");
        typeList.add("Part Time");
        typeList.add("Self-Employed");
        typeList.add("Freelance");
        typeList.add("Contract");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, typeList);
        binding.etType.setAdapter(adapter);

        binding.etType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selection = parent.getAdapter().getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.cbStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    binding.tvPresent.setVisibility(View.VISIBLE);
                    binding.llEndDateLayout.setVisibility(View.GONE);
                }else{
                    binding.tvPresent.setVisibility(View.GONE);
                    binding.llEndDateLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                finish(true);
            }
        });

        vm.addExperienceEvent.observe(this, new SingleRequestEvent.RequestObserver<SimpleApiResponse>() {
            @Override
            public void onRequestReceived(@NonNull Resource<SimpleApiResponse> resource) {
                switch (resource.status) {
                    case LOADING:
                        showProgressDialog(R.string.wait);
                        break;
                    case SUCCESS:
                        dismissProgressDialog();
                        Intent intent = ProfileActivity.newIntent(AddExperienceActivity.this);
                        startNewActivity(intent);
                        finish();
                        break;
                    case ERROR:
                        dismissProgressDialog();
                        vm.error.setValue(resource.message);

                        if (resource.message.equalsIgnoreCase("unauthorized")){

                            Intent intent1 = LoginActivity.newIntent(AddExperienceActivity.this);
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
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChanged(View view) {
                switch (view.getId()){
                    case R.id.cv_cancel:
                      finish(true);
                        break;

                    case R.id.sp_start:
                        status = 0;
                        datePicker().show();

                        break;

                    case R.id.sp_end:
                        status = 1;
                        datePicker().show();
                            break;

                    case R.id.cv_save:
                        String endDate;
                        String title = binding.etTitle.getText().toString();
                        String emp_type = selection;
                        String comp_name = binding.etCompanyName.getText().toString();
                        String location = binding.etLocation.getText().toString();
                        String startDate = binding.spStart.getText().toString();
                        String desc = binding.etDesc.getText().toString();

                        if (title.isEmpty()){
                            vm.error.setValue(getResources().getString(R.string.enter_your_role));
                            return;
                        }
                        if (emp_type.isEmpty()){
                            vm.error.setValue(getResources().getString(R.string.select_employee_type));
                            return;
                        }

                        if (comp_name.isEmpty()){
                            vm.error.setValue(getResources().getString(R.string.enter_company_name));
                            return;
                        }

                        if (location.isEmpty()){
                            vm.error.setValue(getResources().getString(R.string.enter_company_lcoation));
                            return;
                        }

                        if (startDate.isEmpty()){
                            vm.error.setValue(getResources().getString(R.string.start_date));
                            return;
                        }


                        HashMap<String,String> map = new HashMap<>();
                        map.put("title",title);
                        map.put("experienceType",emp_type);
                        map.put("company",comp_name);
                        map.put("startDate",startDate);
                        map.put("location",location);

                        if (binding.cbStatus.isChecked()){
                         map.put("currentlyWorking","1");
                         endDate = "present";
                         map.put("endDate",endDate);
                        }else{
                            map.put("currentlyWorking","0");
                            endDate = binding.spEnd.getText().toString();
                            if (endDate.isEmpty()){
                                vm.error.setValue(getResources().getString(R.string.enter_end_date));
                                return;
                            }
                            map.put("endDate",endDate);
                        }

                        if (!desc.isEmpty()){
                            map.put("description",desc);
                        }


                        if (comeFrom.equalsIgnoreCase("add")){

                            map.put("type","0");
                            vm.addExperienceApi(auth_key,map);

                        }else{

                            map.put("type","1");
                            vm.EditExperienceApi(auth_key,map,exp_id);

                        }




                        break;
                }
            }
        });

    }

    private DatePickerDialog datePicker(){
        DatePickerDialog dialog =   new DatePickerDialog(AddExperienceActivity.this,date , myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        return dialog;
    }


    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "MMM, yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            finalDate = sdf.format(myCalendar.getTime());
            if (status==0){

                binding.spStart.setText(finalDate);

            }else if (status == 1){

                binding.spEnd.setText(finalDate);

            }
        }

    };


}
