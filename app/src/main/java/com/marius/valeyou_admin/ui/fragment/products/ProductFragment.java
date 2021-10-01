package com.marius.valeyou_admin.ui.fragment.products;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.base.MoreBean;
import com.marius.valeyou_admin.databinding.FragmentProductBinding;
import com.marius.valeyou_admin.databinding.HolderCategoryItemsBinding;
import com.marius.valeyou_admin.di.base.adapter.SimpleRecyclerViewAdapter;
import com.marius.valeyou_admin.di.base.view.AppFragment;
import com.marius.valeyou_admin.ui.activity.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends AppFragment<FragmentProductBinding, ProductFragmentVM> {
    public static final String TAG = "ProductFragment";
    private MainActivity mainActivity;

    public static Fragment newInstance() {
        return new ProductFragment();
    }

    @Override
    protected BindingFragment<ProductFragmentVM> getBindingFragment() {
        return new BindingFragment<>(R.layout.fragment_product, ProductFragmentVM.class);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void subscribeToEvents(final ProductFragmentVM vm) {
        mainActivity = (MainActivity) getActivity();
        mainActivity.hideBottomNav(false);
        mainActivity.setHeader("");
        mainActivity.hideBackButton();
        vm.base_click.observe(this, view -> {
            switch (view != null ? view.getId() : 0) {

            }
        });

        setRecyclerView();

    }

    private void setRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        binding.rvCategory.setLayoutManager(layoutManager);
        binding.rvCategory.setAdapter(adapter);
        adapter.setList(getListData());
    }

    private List<MoreBean> getListData() {
        List<MoreBean> beanList = new ArrayList<>();
        beanList.add(new MoreBean(1,"AUTO/BOAT",R.drawable.ic_auto_boat_icon));
        beanList.add(new MoreBean(1,"HANDYMAN",R.drawable.ic_handyman_icon));
        beanList.add(new MoreBean(1,"JUNK REMOVAL",R.drawable.ic_junk_removal_icon));
        beanList.add(new MoreBean(1,"PET CARE",R.drawable.ic_per_care_icon));
        beanList.add(new MoreBean(1,"PAINTING",R.drawable.ic_painting_icon));
        beanList.add(new MoreBean(1,"TV MOUNT &\nELECTRONICS",R.drawable.ic_electronics_icon));
        return beanList;
    }

    SimpleRecyclerViewAdapter<MoreBean, HolderCategoryItemsBinding> adapter = new SimpleRecyclerViewAdapter(R.layout.holder_category_items, BR.bean,
            new SimpleRecyclerViewAdapter.SimpleCallback<MoreBean>() {
                @Override
                public void onItemClick(View v, MoreBean o) {

                }
            });

}
