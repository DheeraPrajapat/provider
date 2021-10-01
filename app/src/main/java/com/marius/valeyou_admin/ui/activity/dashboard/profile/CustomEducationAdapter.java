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
import com.marius.valeyou_admin.data.beans.profilebean.ProfileModel;
import com.marius.valeyou_admin.databinding.HolderEducationItemBinding;
import com.marius.valeyou_admin.databinding.HolderPortfolioItemsBinding;

import java.util.List;

public class CustomEducationAdapter extends RecyclerView.Adapter<CustomEducationAdapter.MyViewHolder>  {

    Context context;
    boolean isAll;
    ClickListner mcallback;
    List<ProfileModel.EducationBean> list;

    public CustomEducationAdapter(Context context, boolean isAll, List<ProfileModel.EducationBean> list, ClickListner mcallback){
        this.context = context;
        this.mcallback = mcallback;
        this.list = list;
        this.isAll = isAll;
    }

    public interface ClickListner {
        void onEducationItemClick(View v, List<ProfileModel.EducationBean> list, int position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HolderEducationItemBinding holderEducationItemBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.holder_education_item, parent, false);
        holderEducationItemBinding.setVariable(BR.callback, mcallback);
        return new CustomEducationAdapter.MyViewHolder(holderEducationItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProfileModel.EducationBean educationBean = list.get(position);
        holder.holderEducationItemBinding.setBean(educationBean);

        holder.holderEducationItemBinding.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mcallback.onEducationItemClick(view,list,position);
            }
        });

        holder.holderEducationItemBinding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mcallback.onEducationItemClick(view,list,position);
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
        HolderEducationItemBinding holderEducationItemBinding;
        public MyViewHolder(@NonNull HolderEducationItemBinding itemView) {
            super(itemView.getRoot());
            holderEducationItemBinding = itemView;
        }
    }
}
