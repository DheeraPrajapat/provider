package com.marius.valeyou_admin.ui.fragment.myjob.addwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.AddWorkModel;
import com.marius.valeyou_admin.data.beans.map.MapListModel;
import com.marius.valeyou_admin.databinding.HolderAlreadyPlacedBidsBinding;
import com.marius.valeyou_admin.databinding.HolderWorkListBinding;
import com.marius.valeyou_admin.ui.activity.bids.PlacedBidsAdapter;

import java.util.List;

public class AddWorkAdapter extends RecyclerView.Adapter<AddWorkAdapter.MyViewHolder> {

    Context context;
    List<AddWorkModel> list;
    ItemCallBack callBack;


    public interface ItemCallBack {
        void onItemClick(View v, List<AddWorkModel> list, int pos);
    }

    public AddWorkAdapter(Context context, List<AddWorkModel> list,ItemCallBack callBack){
        this.context = context;
        this.list = list;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public AddWorkAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HolderWorkListBinding holderWorkListBinding =   DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.holder_work_list, parent, false);

        holderWorkListBinding.setVariable(BR.callback,callBack);
        return new AddWorkAdapter.MyViewHolder(holderWorkListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddWorkAdapter.MyViewHolder holder, int position) {

        AddWorkModel model = list.get(position);
        holder.holderWorkListBinding.setBean(model);

        holder.holderWorkListBinding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onItemClick(view,list,position);
            }
        });

    }

    public void updateList(AddWorkModel model){
        list.add(model);
        notifyDataSetChanged();
    }


    public void remove(int pos){
        list.remove(pos);
        notifyDataSetChanged();
    }

    public List<AddWorkModel> getList(){
        return list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        HolderWorkListBinding holderWorkListBinding;
        public MyViewHolder(@NonNull HolderWorkListBinding itemView) {
            super(itemView.getRoot());
            holderWorkListBinding = itemView;

        }
    }
}
