package com.marius.valeyou_admin.ui.activity.dashboard.profile.businesshours;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.business.BusinessHoursModel;
import com.marius.valeyou_admin.data.beans.categoriesBean.CategoryDataBean;
import com.marius.valeyou_admin.data.beans.profilebean.ProfileModel;
import com.marius.valeyou_admin.databinding.HolderAddBusinessHoursBinding;
import com.marius.valeyou_admin.databinding.HolderBusinessHourBinding;
import com.marius.valeyou_admin.databinding.HolderCategoryListBinding;
import com.marius.valeyou_admin.ui.activity.signup.signuptwo.selectcategory.CategoryAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class BusinessHoursAdapter extends RecyclerView.Adapter<BusinessHoursAdapter.MyViewHolder> {

    Context context;
    List<BusinessHoursModel> listOfHours;
    ProductCallback mcallback;

    public BusinessHoursAdapter(Context context,ProductCallback mcallback){
        this.context = context;
        this.mcallback = mcallback;
    }

    public interface ProductCallback {
        void onItemClick(View v, List<BusinessHoursModel> list,int pos,HolderAddBusinessHoursBinding binding);
        void onItemChecked(View v, List<BusinessHoursModel> list,int pos,boolean isCheck,HolderAddBusinessHoursBinding binding);
    }

    public void setProductList(List<BusinessHoursModel> listOfHours) {
        this.listOfHours = listOfHours;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HolderAddBusinessHoursBinding holderAddBusinessHoursBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.holder_add_business_hours, parent, false);
        holderAddBusinessHoursBinding.setVariable(BR.callback, mcallback);
        return new BusinessHoursAdapter.MyViewHolder(holderAddBusinessHoursBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BusinessHoursModel businessHoursModel = listOfHours.get(position);
        holder.holderAddBusinessHoursBinding.setBean(businessHoursModel);

         String tempFROMHours = "";
        String tempFROMMinutes= "";
        String fromTime = listOfHours.get(position).getFromTime();
        String[] time = fromTime.split(":");
        int FROMHours = Integer.parseInt(time[0]);
        if (FROMHours<=9){
            tempFROMHours = "0"+FROMHours;
        }else{
            tempFROMHours = ""+FROMHours;
        }

        int FROMMinutes = Integer.parseInt(time[1]);

        if (FROMMinutes<=9){
            tempFROMMinutes = "0"+FROMMinutes;
        }else{
            tempFROMMinutes = ""+FROMMinutes;
        }


        holder.holderAddBusinessHoursBinding.mHoursFromTime.setText(tempFROMHours);
        holder.holderAddBusinessHoursBinding.mMinutesFromTime.setText(tempFROMMinutes);


        String tempTOHours = "";
        String tempTOMinutes = "";

        String toTime = listOfHours.get(position).getToTime();
        String[] totimeArray = toTime.split(":");
        int TOHours = Integer.parseInt(totimeArray[0]);
        if (TOHours<=9){
            tempTOHours = "0"+TOHours;
        }else{
            tempTOHours = ""+TOHours;
        }

        int TOMinutes = Integer.parseInt(totimeArray[1]);

        if (TOMinutes<=9){
            tempTOMinutes = "0"+TOMinutes;
        }else{
            tempTOMinutes = ""+TOMinutes;
        }

        holder.holderAddBusinessHoursBinding.mHoursToTime.setText(tempTOHours);
        holder.holderAddBusinessHoursBinding.mMinutesToTime.setText(tempTOMinutes);

        holder.holderAddBusinessHoursBinding.llTimeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mcallback.onItemClick(view,listOfHours,position,holder.holderAddBusinessHoursBinding);
            }
        });

        holder.holderAddBusinessHoursBinding.cvFromTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mcallback.onItemClick(view,listOfHours,position,holder.holderAddBusinessHoursBinding);
            }
        });

        holder.holderAddBusinessHoursBinding.cvToTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mcallback.onItemClick(view,listOfHours,position,holder.holderAddBusinessHoursBinding);
            }
        });


        holder.holderAddBusinessHoursBinding.checkboxDayOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mcallback.onItemChecked(compoundButton,listOfHours,position,b,  holder.holderAddBusinessHoursBinding);
            }
        });


    }

    @Override
    public int getItemCount() {
        if (listOfHours.size()>0){
            return listOfHours.size();
        }
        return 0;
    }

    public List<BusinessHoursModel> getList(){
        return listOfHours;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

       public HolderAddBusinessHoursBinding holderAddBusinessHoursBinding;
        public MyViewHolder(@NonNull HolderAddBusinessHoursBinding itemView) {
            super(itemView.getRoot());
            this.holderAddBusinessHoursBinding = itemView;
        }
    }
}
