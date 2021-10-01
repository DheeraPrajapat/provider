package com.marius.valeyou_admin.ui.activity.dashboard.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.categoriesBean.CategoryDataBean;
import com.marius.valeyou_admin.data.beans.profilebean.ProfileModel;
import com.marius.valeyou_admin.databinding.HolderMyServicesListBinding;
import com.marius.valeyou_admin.databinding.HolderPortfolioItemsBinding;
import com.marius.valeyou_admin.ui.activity.dashboard.profile.myservices.MyServicesAdapter;

import java.util.List;

public class CustomPortfolioAdapter extends RecyclerView.Adapter<CustomPortfolioAdapter.MyViewHolder>  {

    Context context;
    boolean isAll;
    ClickListner mcallback;
    List<ProfileModel.ProviderPortfoliosBean> list;

    public CustomPortfolioAdapter(Context context,boolean isAll,List<ProfileModel.ProviderPortfoliosBean> list,ClickListner mcallback){
        this.context = context;
        this.mcallback = mcallback;
        this.list = list;
        this.isAll = isAll;
    }

    public interface ClickListner {
        void onItemClick(View v, List<ProfileModel.ProviderPortfoliosBean> list,int position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HolderPortfolioItemsBinding holderPortfolioItemsBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.holder_portfolio_items, parent, false);
        holderPortfolioItemsBinding.setVariable(BR.callback, mcallback);
        return new CustomPortfolioAdapter.MyViewHolder(holderPortfolioItemsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProfileModel.ProviderPortfoliosBean portfoliosBean = list.get(position);
        holder.holderPortfolioItemsBinding.setPortfolio(portfoliosBean);

        holder.holderPortfolioItemsBinding.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mcallback.onItemClick(view,list,position);
            }
        });

        holder.holderPortfolioItemsBinding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mcallback.onItemClick(view,list,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (isAll) {
            return list.size();
        } else {
            if (list.size() < 2) {
                return list.size();
            } else {
                return 2;

            }
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        HolderPortfolioItemsBinding holderPortfolioItemsBinding;
        public MyViewHolder(@NonNull HolderPortfolioItemsBinding itemView) {
            super(itemView.getRoot());
            holderPortfolioItemsBinding = itemView;
        }
    }
}
