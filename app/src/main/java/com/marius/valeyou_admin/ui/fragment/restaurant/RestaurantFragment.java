package com.marius.valeyou_admin.ui.fragment.restaurant;

import androidx.fragment.app.Fragment;

import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.databinding.FragmentRestaurantBinding;
import com.marius.valeyou_admin.di.base.view.AppFragment;
import com.marius.valeyou_admin.ui.activity.main.MainActivity;

public class RestaurantFragment extends AppFragment<FragmentRestaurantBinding, RestaurantFragmentVM> {
    public static final String TAG = "RestaurantFragment";
    private MainActivity mainActivity;

    public static Fragment newInstance() {
        return new RestaurantFragment();
    }

    @Override
    protected BindingFragment<RestaurantFragmentVM> getBindingFragment() {
        return new BindingFragment<>(R.layout.fragment_restaurant, RestaurantFragmentVM.class);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void subscribeToEvents(final RestaurantFragmentVM vm) {
        mainActivity = (MainActivity) getActivity();
        mainActivity.hideBottomNav(false);
        // Crashlytics.getInstance().crash(); // Force a crash
        vm.base_click.observe(this, view -> {
            switch (view != null ? view.getId() : 0) {

            }
        });
    }
}
