package com.marius.valeyou_admin.ui.activity.dashboard.profile.portfolio.category;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.gson.Gson;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.RequestBean;
import com.marius.valeyou_admin.data.beans.RequestSubCategory;
import com.marius.valeyou_admin.data.beans.categoriesBean.CategoryDataBean;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityPortfolioCategoryBinding;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.myservices.MyServicesActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.myservices.MyServicesActivityVM;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;

import java.util.ArrayList;
import java.util.List;

public class PortfolioCategoryActivity extends AppActivity<ActivityPortfolioCategoryBinding, PortfolioCategoryActivityVM> {

    private List<RequestBean> selected = new ArrayList<>();
    List<CategoryDataBean> categoryList;
    CategoryAdapter categoryAdapter;


    @Override
    protected BindingActivity<PortfolioCategoryActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_portfolio_category, PortfolioCategoryActivityVM.class);

    }

    @Override
    protected void subscribeToEvents(PortfolioCategoryActivityVM vm) {
        binding.header.tvTitle.setText("Select Category");
        binding.setCat(false);
        vm.categoriesData(0, String.valueOf(sharedPref.getUserData().getId()));
        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                finish(true);
            }
        });

        vm.base_click.observe(this, new Observer<View>() {
            @Override
            public void onChanged(View view) {
                switch (view.getId()){
                    case R.id.cv_remote_job:
                        binding.setCat(false);
                        vm.categoriesData(0, String.valueOf(sharedPref.getUserData().getId()));
                        break;
                    case R.id.cv_local_job:
                        vm.categoriesData(1, String.valueOf(sharedPref.getUserData().getId()));
                        binding.setCat(true);
                        break;
                }
            }
        });


        vm.categoriesbeandata.observe(this, new SingleRequestEvent.RequestObserver<List<CategoryDataBean>>() {
            @Override
            public void onRequestReceived(@NonNull Resource<List<CategoryDataBean>> resource) {
                switch (resource.status) {
                    case LOADING:
                        showProgressDialog(R.string.plz_wait);
                        break;
                    case SUCCESS:
                        dismissProgressDialog();
                        categoryList = resource.data;
                        categoryAdapter.setProductList(categoryList);
                        break;
                    case WARN:
                        dismissProgressDialog();
                        vm.warn.setValue(resource.message);
                        break;
                    case ERROR:
                        dismissProgressDialog();
                        vm.error.setValue(resource.message);
                        break;
                }
            }
        });

        setRecyclerView();

    }

    private void setRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.rvCategory.setLayoutManager(layoutManager);
        categoryAdapter = new CategoryAdapter(this, new CategoryAdapter.ProductCallback() {
            @Override
            public void onItemClick(View v, List<CategoryDataBean> m, int position) {
                if (m.get(position).getType() == 0) {

                    if (m.get(position).getSubCategories().size() > 0) {

                        showDialog(PortfolioCategoryActivity.this, m, position);

                    } else {
                        RequestBean requestBean = new RequestBean();
                        List<RequestSubCategory> subList = new ArrayList<>();
                        requestBean.setCategory_id(String.valueOf(m.get(position).getId()));
                        requestBean.setSubcategory(subList);
                        selected.add(requestBean);
                        String json = new Gson().toJson(selected);


                        Intent intent = new Intent();
                        intent.putExtra("json",json);
                        intent.putExtra("subCat","");
                        intent.putExtra("name",m.get(position).getName());
                        setResult(101,intent);
                        finish();

                    }

                } else {

                    RequestBean requestBean = new RequestBean();
                    List<RequestSubCategory> subList = new ArrayList<>();
                    requestBean.setCategory_id(String.valueOf(m.get(position).getId()));
                    requestBean.setSubcategory(subList);
                    selected.add(requestBean);
                    String json = new Gson().toJson(selected);

                    Intent intent = new Intent();
                    intent.putExtra("json",json);
                    intent.putExtra("subCat","");
                    intent.putExtra("name",m.get(position).getName());
                    setResult(101,intent);
                    finish();


                }
            }
        });

        binding.rvCategory.setAdapter(categoryAdapter);
        if (categoryList != null)
            categoryAdapter.setProductList(categoryList);
    }


    public void showDialog(Activity activity, List<CategoryDataBean> categoryListBeans, int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.sub_cat_custom_layout);

        RecyclerView recycler_view = dialog.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        SubCatAdapter subCatAdapter = new SubCatAdapter(activity, categoryListBeans.get(position).getSubCategories());
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
                String subCat = "";
                RequestBean requestBean = new RequestBean();
                List<RequestSubCategory> subcategory = new ArrayList<>();
                requestBean.setCategory_id(String.valueOf(categoryListBeans.get(position).getId()));
                for (int i = 0; i < categoryListBeans.get(position).getSubCategories().size(); i++) {
                    if (categoryListBeans.get(position).getSubCategories().get(i).isCheck()) {
                        subcategory.add(new RequestSubCategory(String.valueOf(categoryListBeans.get(position).getSubCategories().get(i).getId()), categoryListBeans.get(position).getSubCategories().get(i).getPrice()));

                        if (subCat.equalsIgnoreCase("")){
                            subCat = categoryListBeans.get(position).getSubCategories().get(i).getName();

                        }else{

                            subCat = subCat+","+categoryListBeans.get(position).getSubCategories().get(i).getName();

                        }

                    }
                    requestBean.setSubcategory(subcategory);
                }

                selected.add(requestBean);

                if (subcategory.size() > 0) {
                    String json = new Gson().toJson(selected);
                    Intent intent = new Intent();
                    intent.putExtra("json",json);
                    intent.putExtra("subCat",subCat);
                    intent.putExtra("name",categoryListBeans.get(position).getName());
                    setResult(101,intent);
                    finish();
                    dialog.cancel();

                } else {

                    viewModel.info.setValue("Please select atleast one sub category.");

                }


            }
        });

        dialog.show();
    }


}