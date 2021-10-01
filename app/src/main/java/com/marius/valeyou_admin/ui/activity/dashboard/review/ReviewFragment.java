package com.marius.valeyou_admin.ui.activity.dashboard.review;

import android.view.View;

import com.marius.valeyou_admin.BR;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.base.MoreBean;
import com.marius.valeyou_admin.databinding.FragmentReviewBinding;
import com.marius.valeyou_admin.databinding.HolderReviewsBinding;
import com.marius.valeyou_admin.di.base.adapter.SimpleRecyclerViewAdapter;
import com.marius.valeyou_admin.di.base.view.AppFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

public class ReviewFragment extends AppFragment<FragmentReviewBinding,ReviewFragmentVM> {
    public static final String TAG = "ReviewFragment";

    public static Fragment newInstance() {
        return new ReviewFragment();
    }

    @Override
    protected BindingFragment<ReviewFragmentVM> getBindingFragment() {
        return new BindingFragment<>(R.layout.fragment_review,ReviewFragmentVM.class);
    }

    @Override
    protected void subscribeToEvents(ReviewFragmentVM vm) {
        setRecyclerView();
    }

    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.rvReview.setLayoutManager(layoutManager);
        binding.rvReview.setAdapter(adapter);
        adapter.setList(getList());
    }

    SimpleRecyclerViewAdapter<MoreBean, HolderReviewsBinding> adapter =
            new SimpleRecyclerViewAdapter<>(R.layout.holder_reviews, BR.bean, new SimpleRecyclerViewAdapter.SimpleCallback<MoreBean>() {
                @Override
                public void onItemClick(View v, MoreBean moreBean) {

                }
            });

    private List<MoreBean> getList() {
        List<MoreBean> moreBeans = new ArrayList<>();
        moreBeans.add(new MoreBean(1,"",1));
        moreBeans.add(new MoreBean(1,"",2));
        moreBeans.add(new MoreBean(1,"",3));
        moreBeans.add(new MoreBean(1,"",4));
        moreBeans.add(new MoreBean(1,"",5));
        moreBeans.add(new MoreBean(1,"",6));
        return moreBeans;
    }

}
