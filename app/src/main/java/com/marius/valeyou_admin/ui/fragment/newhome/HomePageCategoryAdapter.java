package com.marius.valeyou_admin.ui.fragment.newhome;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestBuilder;
import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.categoriesBean.CategoryDataBean;
import com.marius.valeyou_admin.databinding.HolderNewCategoryLayoutBinding;
import com.marius.valeyou_admin.di.module.GlideApp;

import java.util.List;

public class HomePageCategoryAdapter extends RecyclerView.Adapter<HomePageCategoryAdapter.MyViewHolder> {

    private List<CategoryDataBean> moreBeans;
    private HomePageCategoryAdapter.ProductCallback mcallback;
    private Context baseContext;


    public HomePageCategoryAdapter(Context baseContext, HomePageCategoryAdapter.ProductCallback callback, List<CategoryDataBean> moreBeans) {
        this.baseContext = baseContext;
        this.mcallback = callback;
        this.moreBeans = moreBeans;
    }

    public interface ProductCallback {
        void onItemClick(View v, List<CategoryDataBean> moreBeans, int pos);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HolderNewCategoryLayoutBinding holderNewCategoryLayoutBinding =   DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.holder_new_category_layout, parent, false);

        holderNewCategoryLayoutBinding.setVariable(BR.callback, mcallback);
        return new HomePageCategoryAdapter.MyViewHolder(holderNewCategoryLayoutBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CategoryDataBean moreBean = moreBeans.get(position);
        holder.holderNewCategoryLayoutBinding.setBean(moreBean);

        holder.holderNewCategoryLayoutBinding.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mcallback.onItemClick(holder.holderNewCategoryLayoutBinding.mainLayout,moreBeans,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (moreBeans != null) {
            return moreBeans.size();
        } else {
            return 0;
        }
    }

    public void setProductList(List<CategoryDataBean> moreBeans) {
        this.moreBeans = moreBeans;
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        HolderNewCategoryLayoutBinding holderNewCategoryLayoutBinding;
        public MyViewHolder(@NonNull HolderNewCategoryLayoutBinding itemView) {
            super(itemView.getRoot());
            holderNewCategoryLayoutBinding = itemView;

        }
    }
}
