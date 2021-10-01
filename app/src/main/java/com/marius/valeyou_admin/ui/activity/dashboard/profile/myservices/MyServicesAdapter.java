package com.marius.valeyou_admin.ui.activity.dashboard.profile.myservices;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.categoriesBean.CategoryDataBean;
import com.marius.valeyou_admin.databinding.DialogSetPriceBinding;
import com.marius.valeyou_admin.databinding.HolderCategoryListBinding;
import com.marius.valeyou_admin.databinding.HolderMyServicesListBinding;
import com.marius.valeyou_admin.databinding.HolderSubcategoryListBinding;
import com.marius.valeyou_admin.di.base.adapter.SimpleRecyclerViewAdapter;
import com.marius.valeyou_admin.di.base.dialog.BaseCustomDialog;
import com.marius.valeyou_admin.ui.activity.signup.signuptwo.selectcategory.CategoryAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyServicesAdapter extends RecyclerView.Adapter<MyServicesAdapter.ProductTitleHolder> {

    private List<CategoryDataBean> categoryDataBeanList;
    private MyServicesAdapter.ProductCallback mcallback;
    private Context baseContext;
    String priceMode = "";


    public MyServicesAdapter(Context baseContext, MyServicesAdapter.ProductCallback callback) {
        this.baseContext = baseContext;
        this.mcallback = callback;
    }

    public interface ProductCallback {
        void onItemClick(View v,List<CategoryDataBean> categoryDataBeanList,int position);
        void addOnPriceClickLisner(View view,List<CategoryDataBean> categoryDataBeansList, int position,HolderMyServicesListBinding binding);
    }

    @NonNull
    @Override
    public ProductTitleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HolderMyServicesListBinding holderMyServicesListBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.holder_my_services_list, parent, false);
        holderMyServicesListBinding.setVariable(BR.callback, mcallback);
        return new MyServicesAdapter.ProductTitleHolder(holderMyServicesListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductTitleHolder holder, int position) {
        CategoryDataBean categoryDataBean = categoryDataBeanList.get(position);
        holder.holderProductTitleBinding.setCategory(categoryDataBean);

         holder.holderProductTitleBinding.cvItem.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 mcallback.onItemClick(view,categoryDataBeanList,position);
             }
         });

         holder.holderProductTitleBinding.addUpdateBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 mcallback.addOnPriceClickLisner(view,categoryDataBeanList,position,holder.holderProductTitleBinding);
             }
         });
    }


    public List<CategoryDataBean> getList(){
        return categoryDataBeanList;
    }

    public void notifyCategory(int pos){
        notifyItemChanged(pos);
    }

    @Override
    public int getItemCount() {
        if (categoryDataBeanList != null) {
            return categoryDataBeanList.size();
        } else {
            return 0;
        }
    }

    public void setProductList(List<CategoryDataBean> categoryDataBeanList) {
        this.categoryDataBeanList = categoryDataBeanList;
        notifyDataSetChanged();
    }

    public class ProductTitleHolder extends RecyclerView.ViewHolder {
        private HolderMyServicesListBinding holderProductTitleBinding;
        private SimpleRecyclerViewAdapter<CategoryDataBean.SubCategoriesBean, HolderSubcategoryListBinding> adapter;

        public ProductTitleHolder(@NonNull HolderMyServicesListBinding itemView) {
            super(itemView.getRoot());
            this.holderProductTitleBinding = itemView;
        }
    }




}
