package com.marius.valeyou_admin.ui.fragment.myjob.upcoming.opendetails.closejobs;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.jobs.MyJobsModel;
import com.marius.valeyou_admin.databinding.HolderCloseJobBinding;
import com.marius.valeyou_admin.databinding.HolderUpcomingJobBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class CloseJobsAdapter extends RecyclerView.Adapter<CloseJobsAdapter.MyViewHolder> {

    private List<MyJobsModel> myJobsModelsLsit;
    private CloseJobsAdapter.ProductCallback mcallback;
    private Context baseContext;

    public CloseJobsAdapter(Context baseContext, CloseJobsAdapter.ProductCallback callback) {
        this.baseContext = baseContext;
        this.mcallback = callback;
    }

    public interface ProductCallback {
        void onItemClick(View v, MyJobsModel m);
    }

    @NonNull
    @Override
    public CloseJobsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HolderCloseJobBinding holderCloseJobBinding =  DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.holder_close_job, parent, false);
        holderCloseJobBinding.setVariable(BR.callback, mcallback);

        return new CloseJobsAdapter.MyViewHolder(holderCloseJobBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CloseJobsAdapter.MyViewHolder holder, int position) {

        MyJobsModel model = myJobsModelsLsit.get(position);
        holder.holderCloseJobBinding.setBean(model);

        String timeMilis = model.getDate();

        if (!timeMilis.isEmpty()){
            String dateTime = convertDate(timeMilis,"dd/MM/yyyy, hh:mm a");
            holder.holderCloseJobBinding.txtTimeDate.setText(dateTime);
        }


        if (model.getStatus() == 1) {
            holder.holderCloseJobBinding.txtStatus.setText("Already Placed");

            holder.holderCloseJobBinding.txtStatus.setBackgroundColor(Color.parseColor("#FDB537"));


        }else if (model.getStatus() == 0){
            holder.holderCloseJobBinding.txtStatus.setText("Place Bid");
            holder.holderCloseJobBinding.txtStatus.setBackgroundColor(Color.parseColor("#04A7EE"));        }

    }

    @Override
    public int getItemCount() {
        if (myJobsModelsLsit != null) {
            return myJobsModelsLsit.size();
        } else {
            return 0;
        }
    }

    public void setList(List<MyJobsModel> myJobsModelsLsit) {
        this.myJobsModelsLsit = myJobsModelsLsit;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private HolderCloseJobBinding holderCloseJobBinding;
        public MyViewHolder(@NonNull HolderCloseJobBinding itemView) {
            super(itemView.getRoot());
            this.holderCloseJobBinding = itemView;
        }
    }

    public static String convertDate(String dateInMilliseconds,String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }
}
