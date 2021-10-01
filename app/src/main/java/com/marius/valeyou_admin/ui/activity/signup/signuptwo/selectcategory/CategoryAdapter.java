package com.marius.valeyou_admin.ui.activity.signup.signuptwo.selectcategory;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.categoriesBean.CategoryDataBean;
import com.marius.valeyou_admin.databinding.DialogSetPriceBinding;
import com.marius.valeyou_admin.databinding.HolderCategoryListBinding;
import com.marius.valeyou_admin.databinding.HolderSubcategoryListBinding;
import com.marius.valeyou_admin.di.base.adapter.SimpleRecyclerViewAdapter;
import com.marius.valeyou_admin.di.base.dialog.BaseCustomDialog;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ProductTitleHolder> {

    private List<CategoryDataBean> categoryDataBeanList;
    private ProductCallback mcallback;
    private Context baseContext;
    String priceMode = "";

    public CategoryAdapter(Context baseContext, ProductCallback callback) {
        this.baseContext = baseContext;
        this.mcallback = callback;
    }

    public interface ProductCallback {
        void onItemClick(View v, List<CategoryDataBean> m, int position);
    }
    public void notifyCategory(int pos){
        notifyItemChanged(pos);
    }

    @NonNull
    @Override
    public ProductTitleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HolderCategoryListBinding holderProductTitleBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.holder_category_list, parent, false);
        holderProductTitleBinding.setVariable(BR.callback, mcallback);
        return new ProductTitleHolder(holderProductTitleBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductTitleHolder holder, int position) {
        CategoryDataBean categoryDataBean = categoryDataBeanList.get(position);
        holder.holderProductTitleBinding.setCategory(categoryDataBean);

        if (categoryDataBeanList.get(position).getType() == 1){

            priceMode = "Set price per hour";

        }else if (categoryDataBeanList.get(position).getType() == 0){
            priceMode = "Set fixed price";
        }

        holder.holderProductTitleBinding.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mcallback.onItemClick(view,categoryDataBeanList,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (categoryDataBeanList != null) {
            return categoryDataBeanList.size();
        } else {
            return 0;
        }
    }

    public List<CategoryDataBean> getList(){
        return categoryDataBeanList;
    }

    public void setProductList(List<CategoryDataBean> categoryDataBeanList) {
        this.categoryDataBeanList = categoryDataBeanList;
        notifyDataSetChanged();
    }

    public class ProductTitleHolder extends RecyclerView.ViewHolder {
        private HolderCategoryListBinding holderProductTitleBinding;
        private SimpleRecyclerViewAdapter<CategoryDataBean.SubCategoriesBean, HolderSubcategoryListBinding> adapter;

        public ProductTitleHolder(@NonNull HolderCategoryListBinding itemView) {
            super(itemView.getRoot());
            this.holderProductTitleBinding = itemView;

        }
    }

    private BaseCustomDialog<DialogSetPriceBinding> dialogSuccess;

    private void openDialog(CategoryDataBean.SubCategoriesBean bean,int position) {
        dialogSuccess = new BaseCustomDialog<>(baseContext, R.layout.dialog_set_price, new BaseCustomDialog.Listener() {
            @Override
            public void onViewClick(View view) {
                if (view != null && view.getId() != 0) {
                    switch (view.getId()) {
                        case R.id.btn_submit:
                            if (!TextUtils.isEmpty(dialogSuccess.getBinding()
                                    .etPrice.getText().toString().trim())) {
                                List<CategoryDataBean.SubCategoriesBean> subCatList
                                        = categoryDataBeanList.get(position).getSubCategories();
                                for (int k = 0; k < subCatList.size(); k++) {
                                    if (subCatList.get(k).getId() == bean.getId()) {
                                        subCatList.get(k).setCheck(true);
                                        subCatList.get(k).setPrice(dialogSuccess.getBinding().etPrice.getText().toString());
                                    }
                                }
                                categoryDataBeanList.get(position).setSelected(true);
                                categoryDataBeanList.get(position).setSubCategories(subCatList);
                                notifyItemChanged(position);
                                dialogSuccess.dismiss();

                            }
                            break;
                        case R.id.iv_cancel:
                            dialogSuccess.dismiss();
                            break;
                    }
                }
            }
        });
        dialogSuccess.getBinding().priceMode.setText(priceMode);
        dialogSuccess.getWindow().setBackgroundDrawableResource(R.color.transparance_whhite);
        dialogSuccess.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialogSuccess.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogSuccess.show();
    }

}
