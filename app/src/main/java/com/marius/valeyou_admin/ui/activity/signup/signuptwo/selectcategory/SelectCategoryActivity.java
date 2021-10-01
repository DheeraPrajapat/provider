package com.marius.valeyou_admin.ui.activity.signup.signuptwo.selectcategory;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.CatBean;
import com.marius.valeyou_admin.data.beans.RequestBean;
import com.marius.valeyou_admin.data.beans.RequestSubCategory;
import com.marius.valeyou_admin.data.beans.SubCatBean;
import com.marius.valeyou_admin.data.beans.base.ApiResponse;
import com.marius.valeyou_admin.data.beans.base.MoreBean;
import com.marius.valeyou_admin.data.beans.categoriesBean.CategoryDataBean;
import com.marius.valeyou_admin.data.beans.singninbean.SignInModel;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivitySelectCategoryBinding;
import com.marius.valeyou_admin.databinding.DialogSetPriceBinding;
import com.marius.valeyou_admin.di.base.dialog.BaseCustomDialog;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.indentity.IdentityActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.myservices.MyServicesActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.activity.main.MainActivity;
import com.marius.valeyou_admin.ui.activity.signup.uploaddocument.UploadDocumentActivity;
import com.marius.valeyou_admin.ui.fragment.newhome.SubCatAdapter;
import com.marius.valeyou_admin.util.Constants;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import okhttp3.Request;
import okhttp3.RequestBody;

public class SelectCategoryActivity extends AppActivity<ActivitySelectCategoryBinding, SelectCategoryActivityVM> {

    private CategoryAdapter categoryAdapter;
    HashMap<String, String> map;

    List<CategoryDataBean> categoryList;
    List<CatBean> catList;
    String auth_key;
    private List<RequestBean> selected = new ArrayList<>();
    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, SelectCategoryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected BindingActivity<SelectCategoryActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_select_category, SelectCategoryActivityVM.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        Intent in = getIntent();
        if (in!=null){
            map = (HashMap<String, String>) in.getSerializableExtra("signupMap");
            auth_key = in.getStringExtra("auth_key");
        }

        viewModel.categoriesData(0);
        binding.setCat(false);


    }

    @Override
    protected void subscribeToEvents(SelectCategoryActivityVM vm) {
        binding.header.tvTitle.setText(R.string.slect_resource);
        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                finish(true);
            }
        });

        vm.profilebeandata.observe(this, new Observer<Resource<ApiResponse<SignInModel>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<SignInModel>> apiResponseResource) {
                switch (apiResponseResource.status) {
                    case LOADING:
                        showProgressDialog(R.string.wait);
                        break;
                    case SUCCESS:
                        dismissProgressDialog();

                            Intent intent = IdentityActivity.newIntent(SelectCategoryActivity.this);
                            intent.putExtra("auth_key",auth_key);
                            intent.putExtra("comeFrom","cat");
                            startNewActivity(intent,true);

                        break;
                    case ERROR:
                        dismissProgressDialog();
                        vm.error.setValue(apiResponseResource.message);
                        break;
                    case WARN:
                        dismissProgressDialog();
                        vm.warn.setValue(apiResponseResource.message);
                        break;
                }
            }
        });


        vm.base_click.observe(this, new Observer<View>() {
            @Override
            public void onChanged(View view) {
                switch (view != null ? view.getId() : 0) {
                    case R.id.cv_remote_job:
                        binding.setCat(false);
                        vm.categoriesData(0);
                        break;
                    case R.id.cv_local_job:
                        vm.categoriesData(1);
                        binding.setCat(true);
                        break;

                    case R.id.btn_next:
                        List<CategoryDataBean> list = categoryList;
                        String sub_category = "";
                        for (int k = 0; k < list.size(); k++) {
                            if (list.get(k).isSelected()) {
                                RequestBean requestBean = new RequestBean();
                                List<RequestSubCategory> subcategory = new ArrayList<>();
                                requestBean.setCategory_id(String.valueOf(list.get(k).getId()));
                                for (int r = 0; r < list.get(k).getSubCategories().size(); r++) {
                                    if (list.get(k).getSubCategories().get(r).isCheck()) {
                                        subcategory.add(new RequestSubCategory(String.valueOf(list.get(k).getSubCategories().get(r).getId()), list.get(k).getSubCategories().get(r).getPrice()));
                                        if (sub_category != null && sub_category.equalsIgnoreCase("")) {
                                            sub_category = list.get(k).getSubCategories().get(r).getName();
                                        } else {
                                            sub_category = sub_category.trim().substring(0, sub_category.trim().length() - 1) + ", " + list.get(k).getSubCategories().get(r).getName() + ", ";
                                        }
                                    }
                                }
                                requestBean.setSubcategory(subcategory);
                                selected.add(requestBean);
                            }
                        }
                        String json = new Gson().toJson(selected);
                        if (!json.equalsIgnoreCase("[]")) {
                            String cat_value = "";
                            if (sub_category != null && !sub_category.equalsIgnoreCase("")) {
                                if (sub_category.contains(",")) {
                                    cat_value = sub_category.trim().substring(0, sub_category.trim().length() - 1);
                                } else {
                                    cat_value = sub_category.trim();
                                }
                            }

                            Log.d("jsonData"," == "+json);
                            map.put(Constants.SELECTED_DATA, json);
                            viewModel.editProfile(auth_key,map);

                        } else {
                            Toast.makeText(SelectCategoryActivity.this, "Please select at least one category", Toast.LENGTH_SHORT).show();
                        }

                        break;



                }
            }
        });

        vm.categoriesbeandata.observe(this, new SingleRequestEvent.RequestObserver<List<CategoryDataBean>>() {
            @Override
            public void onRequestReceived(@NonNull Resource<List<CategoryDataBean>> resource) {
                switch (resource.status){
                    case LOADING:
                        binding.setCheck(true);
                        break;
                    case SUCCESS:
                        binding.setCheck(false);
                        categoryList = resource.data;
                        setRecyclerView(resource.data);
                        break;
                    case ERROR:
                        binding.setCheck(false);
                        vm.error.setValue(resource.message);
                        break;
                    case WARN:
                        binding.setCheck(false);
                        vm.warn.setValue(resource.message);
                        break;

                }
            }
        });
    }

    private void setRecyclerView(List<CategoryDataBean> dataList) {
        GridLayoutManager layoutManager = new GridLayoutManager(SelectCategoryActivity.this,2);
        binding.rvCategory.setLayoutManager(layoutManager);
        categoryAdapter = new CategoryAdapter(this, new CategoryAdapter.ProductCallback() {
            @Override
            public void onItemClick(View v, List<CategoryDataBean> m, int position) {
                if (m.get(position).getType()==0) {

                    if (m.get(position).getSubCategories().size()>0) {

                        showDialog(SelectCategoryActivity.this, m, position);

                    }else{

                        if (dataList.get(position).isSelected()){
                            dataList.get(position).setSelected(false);
                        }else {

                            dataList.get(position).setSelected(true);

                    }

                        categoryAdapter.notifyCategory(position);


                    }

                }else{

                    if (dataList.get(position).isSelected()){
                        dataList.get(position).setSelected(false);
                    }else {
                        dataList.get(position).setSelected(true);
                    }

                    categoryAdapter.notifyCategory(position);


                }


            }
        });

        binding.rvCategory.setAdapter(categoryAdapter);
        categoryAdapter.setProductList(dataList);

    }


    public void showDialog(Activity activity, List<CategoryDataBean> categoryDataBeans,int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.sub_cat_custom_layout);

        RecyclerView recycler_view = dialog.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        SubCategoryAdapter subCatAdapter = new SubCategoryAdapter(activity, categoryDataBeans.get(position).getSubCategories());
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setAdapter(subCatAdapter);
        Button cancel = dialog.findViewById(R.id.cancel);
        Button done = dialog.findViewById(R.id.done);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String price = "101";
                List<CategoryDataBean.SubCategoriesBean> subCategories = categoryDataBeans.get(position).getSubCategories();
                for (int i = 0; i < subCategories.size(); i++) {
                    if (subCategories.get(i).getId() == categoryDataBeans.get(position).getId()) {
                        subCategories.get(i).setLocalSelection(true);
                        subCategories.get(i).setPrice(price);
                    }
                }
                categoryDataBeans.get(position).setSelected(true);
                categoryDataBeans.get(position).setSubCategories(subCategories);

                RequestBean requestBean = new RequestBean();
                List<RequestSubCategory> subcategory = new ArrayList<>();
                requestBean.setCategory_id(String.valueOf(categoryDataBeans.get(position).getId()));
                for (int i = 0; i < categoryDataBeans.get(position).getSubCategories().size();i++){
                    if (categoryDataBeans.get(position).getSubCategories().get(i).isLocalSelection()){
                        subcategory.add(new RequestSubCategory(String.valueOf(categoryDataBeans.get(position).getSubCategories().get(i).getId()),categoryDataBeans.get(position).getSubCategories().get(i).getPrice()));
                        categoryDataBeans.get(position).setSelected(true);
                    }

                }

                if (subcategory.size()>0) {
                    categoryAdapter.notifyCategory(position);
                    dialog.cancel();
                }else{
                    viewModel.info.setValue(getResources().getString(R.string.plsease_select_atleast_one_category));
                }

            }
        });

        dialog.show();
    }

}
