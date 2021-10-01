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
import com.marius.valeyou_admin.databinding.HolderExperienceItemBinding;

import java.util.List;

public class CustomExperienceAdapter extends RecyclerView.Adapter<CustomExperienceAdapter.MyViewHolder>  {

    Context context;
    boolean isAll;
    ClickListner mcallback;
    List<ProfileModel.ExperienceBean> list;

    public CustomExperienceAdapter(Context context, boolean isAll, List<ProfileModel.ExperienceBean> list, ClickListner mcallback){
        this.context = context;
        this.mcallback = mcallback;
        this.list = list;
        this.isAll = isAll;
    }

    public interface ClickListner {
        void onExperienceItemClick(View v, List<ProfileModel.ExperienceBean> list, int position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HolderExperienceItemBinding holderExperienceItemBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.holder_experience_item, parent, false);
        holderExperienceItemBinding.setVariable(BR.callback, mcallback);
        return new CustomExperienceAdapter.MyViewHolder(holderExperienceItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProfileModel.ExperienceBean experienceBean = list.get(position);
        holder.holderExperienceItemBinding.setBean(experienceBean);

        holder.holderExperienceItemBinding.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mcallback.onExperienceItemClick(view,list,position);
            }
        });

        holder.holderExperienceItemBinding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mcallback.onExperienceItemClick(view,list,position);
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
        HolderExperienceItemBinding holderExperienceItemBinding;
        public MyViewHolder(@NonNull HolderExperienceItemBinding itemView) {
            super(itemView.getRoot());
            holderExperienceItemBinding = itemView;
        }
    }
}
