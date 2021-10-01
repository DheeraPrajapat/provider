package com.marius.valeyou_admin.ui.activity.faq;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.base.MoreBean;
import com.marius.valeyou_admin.data.beans.categoriesBean.CategoryDataBean;
import com.marius.valeyou_admin.data.beans.faq.FaqModel;
import com.marius.valeyou_admin.databinding.HolderFaqBinding;
import com.marius.valeyou_admin.databinding.HolderSetCategoryBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class faqAdapter extends RecyclerView.Adapter<faqAdapter.MyViewHolder> {

    Context context;
    List<FaqModel> moreBeans;
    public CheckCallback checkCallback;

    public faqAdapter(Context context, faqAdapter.CheckCallback checkCallback){
        this.context = context;
        this.checkCallback = checkCallback;
    }

    public interface CheckCallback {
        void onItemClick(View v, int position, FaqModel model ,HolderFaqBinding binding);
    }

    public void setCatList(List<FaqModel> moreBeans) {
        this.moreBeans = moreBeans;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HolderFaqBinding holderFaqBinding =  DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.holder_faq, parent, false);
        holderFaqBinding.setVariable(BR.callback, checkCallback);

        return new faqAdapter.MyViewHolder(holderFaqBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        FaqModel moreBean = moreBeans.get(position);
        holder.holderFaqBinding.setBean(moreBean);


    }

    @Override
    public int getItemCount() {
        if (moreBeans.size()>0){
            return moreBeans.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        HolderFaqBinding holderFaqBinding;
        public MyViewHolder(@NonNull HolderFaqBinding itemView) {
            super(itemView.getRoot());
            holderFaqBinding = itemView;
            holderFaqBinding.cvFaq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkCallback.onItemClick(v,getAdapterPosition(),moreBeans.get(getAdapterPosition()),holderFaqBinding);
                }
            });
        }
    }


}
