package com.marius.valeyou_admin.ui.activity.dashboard.profile.providerlanguages;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.google.gson.JsonElement;
import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.LanguagesBean;
import com.marius.valeyou_admin.data.beans.base.MoreBean;
import com.marius.valeyou_admin.data.beans.profilebean.LanguagesModel;
import com.marius.valeyou_admin.data.beans.profilebean.ProfileModel;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivitySelectLanguageBinding;
import com.marius.valeyou_admin.databinding.HolderSelectProfBinding;
import com.marius.valeyou_admin.databinding.HoldetSelectLanguageBinding;
import com.marius.valeyou_admin.di.base.adapter.SimpleRecyclerViewAdapter;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.di.base.view.BaseActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.ProfileActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.portfolio.AddPortfolioActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.activity.main.MainActivity;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

public class SelectLanguageActivity extends AppActivity<ActivitySelectLanguageBinding, SelectLanguageActivityVM> {

    String comeFrom;
    int langId;

    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, SelectLanguageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }


    @Override
    protected BindingActivity<SelectLanguageActivityVM> getBindingActivity() {
        return new BaseActivity.BindingActivity<>(R.layout.activity_select_language, SelectLanguageActivityVM.class);

    }

    @Override
    protected void subscribeToEvents(SelectLanguageActivityVM vm) {
        binding.header.tvTitle.setText(getResources().getString(R.string.languages));
        Intent intent = getIntent();

        vm.getLanguages();

        if (intent!=null){

            comeFrom = intent.getStringExtra("comeFrom");

        if (comeFrom.equalsIgnoreCase("edit")){

            binding.titleTxt.setText(getResources().getString(R.string.edit_language));
            ProfileModel.ProviderLanguagesBean languageData = intent.getParcelableExtra("languageData");
            langId = languageData.getId();
            binding.txtSelectLanguages.setText(languageData.getName());
            binding.txtProficiency.setText(languageData.getType());

        }else{

            binding.titleTxt.setText(getResources().getString(R.string.add_language));

        }



        }


        seytRecyclerViewForProficiency();

        vm.languagesBean.observe(this, new SingleRequestEvent.RequestObserver<List<LanguagesBean>>() {
            @Override
            public void onRequestReceived(@NonNull Resource<List<LanguagesBean>> resource) {
                switch (resource.status) {
                    case LOADING:
                    showProgressDialog(R.string.plz_wait);
                        break;

                    case SUCCESS:
                        dismissProgressDialog();
                       // vm.success.setValue(resource.message);
                        seytRecyclerViewForLanguage(resource.data);

                        break;

                        case ERROR:
                            dismissProgressDialog();
                        vm.error.setValue(resource.message);
                        if (resource.message.equalsIgnoreCase("unauthorized")){

                            Intent intent1 = LoginActivity.newIntent(SelectLanguageActivity.this);
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


        vm.addLanguageBean.observe(this, new SingleRequestEvent.RequestObserver<JsonElement>() {
            @Override
            public void onRequestReceived(@NonNull Resource<JsonElement> resource) {
                switch (resource.status) {
                    case LOADING:
                        showProgressDialog(R.string.plz_wait);
                        break;
                    case SUCCESS:
                        dismissProgressDialog();
                        Intent intent = ProfileActivity.newIntent(SelectLanguageActivity.this);
                        startNewActivity(intent);
                        finish();
                        break;
                    case ERROR:
                        dismissProgressDialog();

                        if (resource.message.equalsIgnoreCase("language edit successfully")){
                            Intent intent1 = ProfileActivity.newIntent(SelectLanguageActivity.this);
                            startNewActivity(intent1);
                            vm.success.setValue(resource.message);
                        }else{
                            vm.error.setValue(resource.message);
                        }

                        break;
                    case WARN:
                        dismissProgressDialog();
                        vm.warn.setValue(resource.message);
                        break;
                }
            }
        });

        binding.header.tvTitle.setTextColor(getResources().getColor(R.color.white));
        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                finish(true);
            }
        });

        binding.rvProficiency.setVisibility(View.GONE);
        binding.rvLanguage.setVisibility(View.GONE);
        vm.base_click.observe(this, new Observer<View>() {
            @Override
            public void onChanged(View view) {
                switch (view != null ? view.getId() : 0) {

                    case R.id.txt_select_languages:
                        if (binding.rvLanguage.getVisibility()!=View.VISIBLE){
                            binding.rvLanguage.setVisibility(View.VISIBLE);
                        }else{
                            binding.rvLanguage.setVisibility(View.GONE);
                        }
                        break;


                    case R.id.txt_proficiency:
                        if (binding.rvProficiency.getVisibility()!=View.VISIBLE){
                            binding.rvProficiency.setVisibility(View.VISIBLE);
                        }else{
                            binding.rvProficiency.setVisibility(View.GONE);
                        }
                        break;

                    case R.id.cv_save:
                        String authKey = vm.sharedPref.getUserData().getAuthKey();
                        String name = binding.txtSelectLanguages.getText().toString();
                        String type = binding.txtProficiency.getText().toString();
                        if (name.equalsIgnoreCase(getResources().getString(R.string.search_for_language))){
                            vm.info.setValue(getResources().getString(R.string.please_select_langauge));
                        }else if (type.equalsIgnoreCase(getResources().getString(R.string.select))){
                            vm.info.setValue(getResources().getString(R.string.select_type));
                        }else {

                            if (comeFrom.equalsIgnoreCase("add")){

                                vm.addLanguage(authKey,name,type);

                            }else {

                                vm.editLanguage(authKey,langId,name,type);

                            }

                        }
                        break;


                }
            }
        });


    }

    private List<MoreBean> getProficiency() {
        List<MoreBean> beanList = new ArrayList<>();
        beanList.add(new MoreBean(1,"Basic",1));
        beanList.add(new MoreBean(1,"Conversational",1));
        beanList.add(new MoreBean(1,"Fluent",1));


        return beanList;
    }

    private void seytRecyclerViewForLanguage(List<LanguagesBean> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvLanguage.setLayoutManager(layoutManager);
        binding.rvLanguage.setAdapter(adapter);
        adapter.setList(list);
    }

    private void seytRecyclerViewForProficiency() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvProficiency.setLayoutManager(layoutManager);
        binding.rvProficiency.setAdapter(padapter);
        padapter.setList(getProficiency());
    }

    SimpleRecyclerViewAdapter<LanguagesBean, HoldetSelectLanguageBinding> adapter
            = new SimpleRecyclerViewAdapter(R.layout.holdet_select_language, BR.bean, new SimpleRecyclerViewAdapter.SimpleCallback<LanguagesBean>() {
        @Override
        public void onItemClick(View v, LanguagesBean o) {

            binding.txtSelectLanguages.setText(o.getName());
            binding.rvLanguage.setVisibility(View.GONE);

        }
    });

    SimpleRecyclerViewAdapter<MoreBean, HoldetSelectLanguageBinding> padapter
            = new SimpleRecyclerViewAdapter(R.layout.holder_language_prof, BR.bean, new SimpleRecyclerViewAdapter.SimpleCallback<MoreBean>() {
        @Override
        public void onItemClick(View v, MoreBean o) {
            binding.txtProficiency.setText(o.getName());
            binding.rvProficiency.setVisibility(View.GONE);
        }
    });



}
