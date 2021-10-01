package com.marius.valeyou_admin.ui.activity.dashboard.profile.myservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.CatBean;
import com.marius.valeyou_admin.data.beans.RequestBean;
import com.marius.valeyou_admin.data.beans.RequestSubCategory;
import com.marius.valeyou_admin.data.beans.SubCatBean;
import com.marius.valeyou_admin.data.beans.base.SimpleApiResponse;
import com.marius.valeyou_admin.data.beans.categoriesBean.CategoryDataBean;
import com.marius.valeyou_admin.data.beans.profilebean.ProfileModel;
import com.marius.valeyou_admin.data.remote.helper.Resource;
import com.marius.valeyou_admin.databinding.ActivityMyServicesBinding;
import com.marius.valeyou_admin.databinding.DialogSetPriceBinding;
import com.marius.valeyou_admin.databinding.HolderMyServicesListBinding;
import com.marius.valeyou_admin.di.base.dialog.BaseCustomDialog;
import com.marius.valeyou_admin.di.base.view.AppActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.ProfileActivity;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.certificate.AddCertificateActivity;
import com.marius.valeyou_admin.ui.activity.login.LoginActivity;
import com.marius.valeyou_admin.ui.activity.main.MainActivity;
import com.marius.valeyou_admin.ui.activity.signup.signuptwo.selectcategory.CategoryAdapter;
import com.marius.valeyou_admin.ui.activity.signup.signuptwo.selectcategory.SelectCategoryActivity;
import com.marius.valeyou_admin.ui.activity.signup.signuptwo.selectcategory.SelectCategoryActivityVM;
import com.marius.valeyou_admin.ui.activity.signup.uploaddocument.UploadDocumentActivity;
import com.marius.valeyou_admin.ui.fragment.newhome.SubCatAdapter;
import com.marius.valeyou_admin.util.event.SingleRequestEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyServicesActivity extends AppActivity<ActivityMyServicesBinding,MyServicesActivityVM> {


    private MyServicesAdapter myServicesAdapter;
    private List<RequestBean> selected = new ArrayList<>();
    List<CategoryDataBean> categoryList;
    int cat_position;


    int typeValue =0;

    List<CatBean> catList;
    List<ProfileModel.ProviderCategoriesBean> categoriesBeansList;

    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, MyServicesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected BindingActivity<MyServicesActivityVM> getBindingActivity() {
        return new BindingActivity<>(R.layout.activity_my_services, MyServicesActivityVM.class);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );


        viewModel.categoriesData(0, String.valueOf(sharedPref.getUserData().getId()));
        binding.setCat(false);

    }

    @Override
    protected void subscribeToEvents(MyServicesActivityVM vm) {
        binding.header.tvTitle.setText(R.string.my_services);



        vm.base_back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                finish(true);
            }
        });

        vm.base_click.observe(this, new Observer<View>() {
            @Override
            public void onChanged(View view) {
                switch (view != null ? view.getId() : 0) {

                    case R.id.cv_remote_job:
                        binding.setCat(false);
                        vm.categoriesData(0, String.valueOf(sharedPref.getUserData().getId()));
                        break;
                    case R.id.cv_local_job:
                        vm.categoriesData(1, String.valueOf(sharedPref.getUserData().getId()));
                        binding.setCat(true);
                        break;

                    case R.id.btn_save:
                        List<CategoryDataBean> list = categoryList;
                        String sub_category = "";
                        for (int k = 0; k < list.size(); k++) {
                            if (list.get(k).getIsCheck()==1) {
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

                            Log.d("jsonData"," == "+json);

                            String authKey = viewModel.sharedPref.getUserData().getAuthKey();
                            viewModel.editSerivces(authKey, json);

                        } else {
                            Toast.makeText(MyServicesActivity.this, "Please select at least one category", Toast.LENGTH_SHORT).show();
                        }



                        break;

                }
            }
        });

        vm.profilebeandata.observe(this, new SingleRequestEvent.RequestObserver<SimpleApiResponse>() {
            @Override
            public void onRequestReceived(@NonNull Resource<SimpleApiResponse> resource) {
                switch (resource.status){
                    case LOADING:
                        binding.setCheck(true);
                        break;
                    case SUCCESS:
                        binding.setCheck(false);
                        Intent intent = ProfileActivity.newIntent(MyServicesActivity.this);
                        startNewActivity(intent);
                        finish();
                        break;
                    case ERROR:
                        binding.setCheck(false);
                        vm.error.setValue(resource.message);
                        if (resource.message.equalsIgnoreCase("unauthorized")){
                            Intent intent1 = LoginActivity.newIntent(MyServicesActivity.this);
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

        vm.updatePriceEvent.observe(this, new SingleRequestEvent.RequestObserver<SimpleApiResponse>() {
            @Override
            public void onRequestReceived(@NonNull Resource<SimpleApiResponse> resource) {
                switch (resource.status){
                    case LOADING:
                       showProgressDialog(R.string.plz_wait);
                        break;
                    case SUCCESS:
                       dismissProgressDialog();
                       myServicesAdapter.notifyCategory(cat_position);
                        break;
                    case ERROR:
                        dismissProgressDialog();
                        vm.error.setValue(resource.message);
                        if (resource.message.equalsIgnoreCase("unauthorized")){
                            Intent intent1 = LoginActivity.newIntent(MyServicesActivity.this);
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
        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        binding.rvCategory.setLayoutManager(layoutManager);
        myServicesAdapter = new MyServicesAdapter(this, new MyServicesAdapter.ProductCallback() {
            @Override
            public void onItemClick(View v, List<CategoryDataBean> categoryDataBeanList, int position) {
                if (categoryDataBeanList.get(position).getType()==0) {

                    if (categoryDataBeanList.get(position).getSubCategories().size()>0) {

                        if (categoryDataBeanList.get(position).getIsCheck()==1){

                            categoryDataBeanList.get(position).setIsCheck(0);
                            myServicesAdapter.notifyCategory(position);

                        }else {

                            showDialog(MyServicesActivity.this, categoryDataBeanList, position);
                        }

                    }else{
                        if (categoryDataBeanList.get(position).getIsCheck() == 1){

                            categoryDataBeanList.get(position).setIsCheck(0);

                        }else {

                            categoryDataBeanList.get(position).setIsCheck(1);

                        }

                        myServicesAdapter.notifyCategory(position);

                    }

                }else{


                    if (categoryDataBeanList.get(position).getIsCheck() == 1){

                        categoryDataBeanList.get(position).setIsCheck(0);

                    }else {

                        categoryDataBeanList.get(position).setIsCheck(1);

                    }

                    myServicesAdapter.notifyCategory(position);


                }
            }

            @Override
            public void addOnPriceClickLisner(View view, List<CategoryDataBean> categoryDataBeansList, int position, HolderMyServicesListBinding binding) {

                openDialog(categoryDataBeansList.get(position),position,binding);
            }
        });
        binding.rvCategory.setAdapter(myServicesAdapter);
        myServicesAdapter.setProductList(dataList);
    }


    public void showDialog(Activity activity, List<CategoryDataBean> categoryDataBeans,int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.sub_cat_custom_layout);

        RecyclerView recycler_view = dialog.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        SubCatAdapter subCatAdapter = new SubCatAdapter(activity, categoryDataBeans.get(position).getSubCategories());
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

                String price = "";
                List<CategoryDataBean.SubCategoriesBean> subCategories = categoryDataBeans.get(position).getSubCategories();
                for (int i = 0; i < subCategories.size(); i++) {
                    if (subCategories.get(i).getId() == categoryDataBeans.get(position).getId()) {
                        subCategories.get(i).setIsCheck(1);
                        subCategories.get(i).setPrice(price);
                    }
                }
                categoryDataBeans.get(position).setIsCheck(1);
                categoryDataBeans.get(position).setSubCategories(subCategories);

                    RequestBean requestBean = new RequestBean();
                    List<RequestSubCategory> subcategory = new ArrayList<>();
                    requestBean.setCategory_id(String.valueOf(categoryDataBeans.get(position).getId()));
                    for (int i = 0; i < categoryDataBeans.get(position).getSubCategories().size();i++){
                        if (categoryDataBeans.get(position).getSubCategories().get(i).getIsCheck()==1){
                            subcategory.add(new RequestSubCategory(String.valueOf(categoryDataBeans.get(position).getSubCategories().get(i).getId()),categoryDataBeans.get(position).getSubCategories().get(i).getPrice()));
                            categoryDataBeans.get(position).setIsCheck(1);
                        }
                    }
                    if (subcategory.size()>0) {
                        myServicesAdapter.notifyCategory(position);
                        dialog.cancel();
                    }else{
                        viewModel.info.setValue("Please select atleast one sub category.");
                    }


            }
        });

        dialog.show();
    }



    private BaseCustomDialog<DialogSetPriceBinding> dialogSuccess;
    private void openDialog(CategoryDataBean bean,int position,HolderMyServicesListBinding binding) {
        dialogSuccess = new BaseCustomDialog<>(MyServicesActivity.this, R.layout.dialog_set_price, new BaseCustomDialog.Listener() {
            @Override
            public void onViewClick(View view) {
                if (view != null && view.getId() != 0) {
                    switch (view.getId()) {
                        case R.id.btn_submit:

                            if (dialogSuccess.getBinding().etPrice.getText().toString().isEmpty()){

                                viewModel.error.setValue("Please enter price");

                            }else{
                                cat_position = position;
                                bean.setPrice(dialogSuccess.getBinding().etPrice.getText().toString().trim());
                                viewModel.updatePrice(String.valueOf(bean.getId()),dialogSuccess.getBinding().etPrice.getText().toString().trim());
                                dialogSuccess.dismiss();
                            }

                            break;
                        case R.id.iv_cancel:
                            dialogSuccess.dismiss();
                            break;
                    }
                }
            }
        });

        dialogSuccess.getWindow().setBackgroundDrawableResource(R.color.transparance_whhite);
        dialogSuccess.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialogSuccess.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogSuccess.show();
    }

}
