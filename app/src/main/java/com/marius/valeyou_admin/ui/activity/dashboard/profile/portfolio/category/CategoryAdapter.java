package com.marius.valeyou_admin.ui.activity.dashboard.profile.portfolio.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.categoriesBean.CategoryDataBean;
import com.marius.valeyou_admin.databinding.HolderPortfolioCategoryListBinding;
import com.marius.valeyou_admin.di.base.adapter.SimpleRecyclerViewAdapter;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ProductTitleHolder> {

    private List<CategoryDataBean> moreBeans;
    private ProductCallback mcallback;
    private Context baseContext;

    public CategoryAdapter(Context baseContext, ProductCallback callback) {
        this.baseContext = baseContext;
        this.mcallback = callback;
    }

    public interface ProductCallback {
        void onItemClick(View v, List<CategoryDataBean> m, int position);
    }

    @NonNull
    @Override
    public ProductTitleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HolderPortfolioCategoryListBinding holderPortfolioCategoryListBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.holder_portfolio_category_list, parent, false);
        holderPortfolioCategoryListBinding.setVariable(BR.callback, mcallback);
        return new ProductTitleHolder(holderPortfolioCategoryListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductTitleHolder holder, int position) {
        CategoryDataBean moreBean = moreBeans.get(position);
        holder.holderPortfolioCategoryListBinding.setBean(moreBean);
        holder.holderPortfolioCategoryListBinding.setPosition(position);
        holder.holderPortfolioCategoryListBinding.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mcallback.onItemClick(view,moreBeans,position);
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

    public List<CategoryDataBean> getListData() {
        return moreBeans;
    }

    public class ProductTitleHolder extends RecyclerView.ViewHolder {
        private HolderPortfolioCategoryListBinding holderPortfolioCategoryListBinding;
        private SimpleRecyclerViewAdapter<CategoryDataBean.SubCategoriesBean, HolderPortfolioCategoryListBinding> adapter;

        public ProductTitleHolder(@NonNull HolderPortfolioCategoryListBinding itemView) {
            super(itemView.getRoot());
            this.holderPortfolioCategoryListBinding = itemView;
        }
    }


}
