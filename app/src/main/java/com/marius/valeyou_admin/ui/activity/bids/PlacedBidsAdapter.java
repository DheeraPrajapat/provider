package com.marius.valeyou_admin.ui.activity.bids;

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
import com.marius.valeyou_admin.data.beans.map.MapListModel;
import com.marius.valeyou_admin.databinding.HolderAlreadyPlacedBidsBinding;
import com.marius.valeyou_admin.databinding.HolderNewCategoryLayoutBinding;
import com.marius.valeyou_admin.ui.fragment.newhome.HomePageCategoryAdapter;
import com.marius.valeyou_admin.util.databinding.ImageViewBindingUtils;

import java.util.List;

public class PlacedBidsAdapter  extends RecyclerView.Adapter<PlacedBidsAdapter.MyViewHolder> {

    private List<MapListModel> moreBeans;
    private PlacedBidsAdapter.ProductCallback mcallback;
    private Context baseContext;

    public PlacedBidsAdapter(Context baseContext, PlacedBidsAdapter.ProductCallback callback, List<MapListModel> moreBeans) {
        this.baseContext = baseContext;
        this.mcallback = callback;
        this.moreBeans = moreBeans;
    }

    public interface ProductCallback {
        void onItemClick(View v, List<MapListModel> moreBeans, int pos);
    }

    @NonNull
    @Override
    public PlacedBidsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HolderAlreadyPlacedBidsBinding holderAlreadyPlacedBidsBinding =   DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.holder_already_placed_bids, parent, false);

        holderAlreadyPlacedBidsBinding.setVariable(BR.callback, mcallback);
        return new PlacedBidsAdapter.MyViewHolder(holderAlreadyPlacedBidsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlacedBidsAdapter.MyViewHolder holder, int position) {


        ImageViewBindingUtils.setJobImage(holder.holderAlreadyPlacedBidsBinding.image,moreBeans.get(position).getOrderImages());
        holder.holderAlreadyPlacedBidsBinding.title.setText(moreBeans.get(position).getTitle());
        holder.holderAlreadyPlacedBidsBinding.tvDescription.setText(moreBeans.get(position).getDescription());
        holder.holderAlreadyPlacedBidsBinding.distance.setText(moreBeans.get(position).getDistance()+" Miles away");
        holder.holderAlreadyPlacedBidsBinding.bidTag.setText("Already Placed");



        holder.holderAlreadyPlacedBidsBinding.llListItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mcallback.onItemClick(view,moreBeans,position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return moreBeans.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        HolderAlreadyPlacedBidsBinding holderAlreadyPlacedBidsBinding;
        public MyViewHolder(@NonNull HolderAlreadyPlacedBidsBinding itemView) {
            super(itemView.getRoot());
            holderAlreadyPlacedBidsBinding = itemView;
        }
    }
}
