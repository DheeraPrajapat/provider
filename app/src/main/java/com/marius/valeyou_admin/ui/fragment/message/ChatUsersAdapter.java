package com.marius.valeyou_admin.ui.fragment.message;

import android.content.Context;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.chat.UsersModel;
import com.marius.valeyou_admin.data.beans.faq.FaqModel;
import com.marius.valeyou_admin.databinding.HolderChatListBinding;
import com.marius.valeyou_admin.databinding.HolderFaqBinding;
import com.marius.valeyou_admin.di.module.GlideApp;
import com.marius.valeyou_admin.ui.activity.faq.faqAdapter;
import com.marius.valeyou_admin.util.databinding.ImageViewBindingUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ChatUsersAdapter extends RecyclerView.Adapter<ChatUsersAdapter.MyViewHolder> implements Filterable {
    Context context;
    List<UsersModel> usersModelList;
    List<UsersModel> finalList;
    public Listner listner;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    finalList = usersModelList;
                }else {
                    List<UsersModel> filteredList = new ArrayList<>();
                    for (UsersModel row : usersModelList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getChat().getUser().getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    finalList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = finalList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                finalList = (ArrayList<UsersModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface Listner {
        void onItemClick(HolderChatListBinding binding, int position, UsersModel model);
    }

    public ChatUsersAdapter(Context context,List<UsersModel> usersModelList,Listner listner){
        this.context =context;
        this.usersModelList = usersModelList;
        this.finalList = usersModelList;
        this.listner = listner;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        HolderChatListBinding holderChatListBinding =  DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.holder_chat_list, parent, false);
        holderChatListBinding.setVariable(BR.callback, listner);

        return new ChatUsersAdapter.MyViewHolder(holderChatListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.holderChatListBinding.tvName.setText(finalList.get(position).getChat().getUser().getName());
        if (finalList.get(position).getChat().getMsgType()==0){
            holder.holderChatListBinding.message.setText(finalList.get(position).getChat().getMessage());

        }else{
            holder.holderChatListBinding.message.setText("image");

        }
        if (usersModelList.get(position).getChat().getUnread_message() > 0){
            holder.holderChatListBinding.tvCount.setText(finalList.get(position).getChat().getUnread_message() + "");
            holder.holderChatListBinding.tvCount.setVisibility(View.VISIBLE);
        }else {
            holder.holderChatListBinding.tvCount.setVisibility(View.GONE);
        }
      ImageViewBindingUtils.setProfileUrl(holder.holderChatListBinding.image,finalList.get(position).getChat().getUser().getImage());

        if (usersModelList.get(position).getChat().getUnread_message() > 0){
            holder.holderChatListBinding.message.setTextColor(context.getResources().getColor(R.color.black));
            holder.holderChatListBinding.message.setTypeface(holder.holderChatListBinding.message.getTypeface(), Typeface.BOLD);
        }

        holder.holderChatListBinding.time.setText(convertTimeStampIntoDateTime(finalList.get(position).getChat().getCreatedAt()));

    }

    @Override
    public int getItemCount() {
        return finalList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        HolderChatListBinding holderChatListBinding;
        public MyViewHolder(@NonNull HolderChatListBinding itemView) {
            super(itemView.getRoot());
            holderChatListBinding = itemView;
            holderChatListBinding.llItems.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.onItemClick(holderChatListBinding,getAdapterPosition(),finalList.get(getAdapterPosition()));
                }
            });
        }
    }

    private String convertTimeStampIntoDateTime(long timestamp){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp*1000L);
        String date = DateFormat.format("MMM dd, yyyy \n hh:mm a", cal).toString();

        return date;
    }
}
