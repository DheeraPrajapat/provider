package com.marius.valeyou_admin.ui.activity.signup.signuptwo.selectcategory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.categoriesBean.CategoryDataBean;
import com.marius.valeyou_admin.ui.fragment.newhome.SubCatAdapter;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder>{

    Context context;
    List<CategoryDataBean.SubCategoriesBean> list;

    public SubCategoryAdapter(Context context, List<CategoryDataBean.SubCategoriesBean> list){
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public SubCategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_new_sub_categories_layout, parent, false);

        return new SubCategoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryAdapter.MyViewHolder holder, int position) {
        holder.title.setText(list.get(position).getName());
        if (list.get(position).isLocalSelection()){
            holder.checkBox.setChecked(true);
        }else{
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    list.get(position).setLocalSelection(true);
                }else{
                    list.get(position).setLocalSelection(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView title;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            title = itemView.findViewById(R.id.title);

        }
    }
}
