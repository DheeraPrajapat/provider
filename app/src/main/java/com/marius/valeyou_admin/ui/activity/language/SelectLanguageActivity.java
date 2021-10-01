package com.marius.valeyou_admin.ui.activity.language;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivitySelectLanguage2Binding;
import com.marius.valeyou_admin.databinding.ActivitySelectLanguageBinding;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivityVM;
import com.marius.valeyou_admin.ui.activity.main.MainActivity;
import com.marius.valeyou_admin.util.LocaleHelper;

import java.util.Locale;

public class SelectLanguageActivity extends AppActivity<ActivitySelectLanguage2Binding,SelectLanguageActivityVM> {

    String langCode="en";

    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, SelectLanguageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected BindingActivity<SelectLanguageActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_select_language2, SelectLanguageActivityVM.class);
    }

    @Override
    protected void subscribeToEvents(SelectLanguageActivityVM vm) {

        String lang = sharedPref.get("language","en");

        if (lang.equalsIgnoreCase("en")){
            binding.tickImgEng.setVisibility(View.VISIBLE);
            binding.tickImgPort.setVisibility(View.GONE);
        }else if (lang.equalsIgnoreCase("pt")){
            binding.tickImgEng.setVisibility(View.GONE);
            binding.tickImgPort.setVisibility(View.VISIBLE);
        }

        vm.base_click.observe(this, new Observer<View>() {
            @Override
            public void onChanged(View view) {
                switch (view.getId()){

                    case R.id.save:


                        setLocale(SelectLanguageActivity.this,langCode);
                        sharedPref.put("language",langCode);

                        if (viewModel.sharedPref.getUserData()!=null){
                            Intent intent = MainActivity.newIntent(SelectLanguageActivity.this);
                            startNewActivity(intent,true,true);
                            finish(true);
                        }else{
                            Intent intent = LoginActivity.newIntent(SelectLanguageActivity.this);
                            startNewActivity(intent,true,true);
                            finish(true);
                        }


                        break;

                    case R.id.ll_portLang:
                        langCode = "pt";
                        binding.tickImgPort.setVisibility(View.VISIBLE);
                        binding.tickImgEng.setVisibility(View.GONE);

                        break;

                    case R.id.ll_engLang:
                        langCode = "en";
                        binding.tickImgEng.setVisibility(View.VISIBLE);
                        binding.tickImgPort.setVisibility(View.GONE);
                        break;


                }
            }
        });

    }


}