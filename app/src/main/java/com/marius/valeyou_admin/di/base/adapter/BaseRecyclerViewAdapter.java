package com.marius.valeyou_admin.di.base.adapter;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<M, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected List<M> dataList = new ArrayList<>();
    protected boolean loading = false;

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    @Override
    public int getItemCount() {
        if (loading)
            return 12;
        else
            return dataList.size();
    }

    public void setList(List<M> newDataList) {
       this.dataList.clear();
        if (newDataList != null) {
            dataList.addAll(newDataList);
        }
        notifyDataSetChanged();
    }

    public void addToList(List<M> newDataList) {
        if (newDataList == null) {
            return;
        }
        int positionStart = dataList.size();
        int itemCount = newDataList.size();
        dataList.addAll(newDataList);
        notifyItemRangeInserted(positionStart, itemCount);
    }

    public void add(M bean) {
        if (bean == null) {
            return;
        }
        dataList.add(bean);
        notifyItemInserted(this.dataList.size());
    }


    public void add(M bean,int pos) {
        if (bean == null) {
            return;
        }
        dataList.add(pos,bean);
        notifyItemInserted(pos);
    }

    public void set(M bean,int pos) {
        if (bean == null) {
            return;
        }
        dataList.set(pos,bean);
        notifyItemChanged(pos);
    }

    public void clearList() {
        dataList.clear();
        notifyDataSetChanged();
    }

    public List<M> getDataList() {
        return dataList;
    }

    public void removeItem(int position) {
        dataList.remove(position);
        notifyItemRemoved(position);
    }
}
