package com.marius.valeyou_admin.ui.activity.dashboard.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.notification.NotificationModel;
import com.marius.valeyou_admin.databinding.HolderCategoryProfileBinding;
import com.marius.valeyou_admin.databinding.HolderNotificationItemBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfileCategoryAdapter extends RecyclerView.Adapter<ProfileCategoryAdapter.MyViewHolder> {

    Context context;
    String list[];
    public Listner listner;
    int qnt;

    public interface Listner {
        void onItemClick(View v);
    }

    public ProfileCategoryAdapter(Context context, String[] list, Listner listner){
        this.context =context;
        this.list = list;
        this.listner = listner;
        qnt = list.length;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HolderCategoryProfileBinding HolderCategoryProfileBinding =  DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.holder_category_profile, parent, false);
        HolderCategoryProfileBinding.setVariable(BR.callback, listner);

        return new ProfileCategoryAdapter.MyViewHolder(HolderCategoryProfileBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        if (qnt >0) {
//            if (pos <= qnt) {
//                if (list[pos].isEmpty()) {
//                    list = tiraItem(list, 0);
//                } else if (pos <= qnt) {
//                    if (!list[pos - 1].isEmpty()) {
//                        holder.HolderCategoryProfileBinding.txtCategory1.setVisibility(View.VISIBLE);
//                        holder.HolderCategoryProfileBinding.txtCategory1.setText(list[pos - 1]);
//                        list = tiraItem(list, pos - 1);
//                        if (pos <= qnt) {
//                            holder.HolderCategoryProfileBinding.txtCategory2.setVisibility(View.VISIBLE);
//                            holder.HolderCategoryProfileBinding.txtCategory2.setText(list[pos - 1]);
//                            list = tiraItem(list, pos - 1);
//                            int td = holder.HolderCategoryProfileBinding.txtCategory2.getText().length() + holder.HolderCategoryProfileBinding.txtCategory1.getText().length();
//                            if (td <= 29 && pos- 1 < qnt) {
//                                if (!list[pos- 1].isEmpty()) {
//                                    holder.HolderCategoryProfileBinding.txtCategory3.setVisibility(View.VISIBLE);
//                                    holder.HolderCategoryProfileBinding.txtCategory3.setText(list[pos- 1]);
//                                    list = tiraItem(list, pos- 1);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        else{
//
//        }
        if (qnt > 0) {
            if (list[0].isEmpty()) {
                tiraItem(0);
            }
        }
        if (qnt>0){
            holder.HolderCategoryProfileBinding.txtCategory1.setVisibility(View.VISIBLE);
            holder.HolderCategoryProfileBinding.txtCategory1.setText(list[0]);
            tiraItem(0);
            if (qnt>0){
                holder.HolderCategoryProfileBinding.txtCategory2.setVisibility(View.VISIBLE);
                holder.HolderCategoryProfileBinding.txtCategory2.setText(list[0]);
                tiraItem(0);
                if (qnt>0){
                    int td = holder.HolderCategoryProfileBinding.txtCategory1.getText().length() +holder.HolderCategoryProfileBinding.txtCategory2.getText().length();
                    if (td+list[0].length()<50) {
                        holder.HolderCategoryProfileBinding.txtCategory3.setVisibility(View.VISIBLE);
                        holder.HolderCategoryProfileBinding.txtCategory3.setText(list[0]);
                        tiraItem(0);
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public void tiraItem(int pos){
        String[] aux = new String[list.length];
        for (int i=0;i<pos; i++){
            aux[i] = list[i];
        }
        for (int i=pos+1;i<qnt; i++){
            aux[i-1] = list[i];
        }
        qnt--;
        list = aux;
        return;
    }


    public void notifyList(int pos){
        notifyItemChanged(pos);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        HolderCategoryProfileBinding HolderCategoryProfileBinding;
        public MyViewHolder(@NonNull HolderCategoryProfileBinding itemView) {
            super(itemView.getRoot());
            HolderCategoryProfileBinding = itemView;

            HolderCategoryProfileBinding.txtCategory1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listner.onItemClick(view);
                }
            });

            HolderCategoryProfileBinding.txtCategory2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listner.onItemClick(view);
                }
            });

            HolderCategoryProfileBinding.txtCategory3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listner.onItemClick(view);
                }
            });

        }
    }
}
