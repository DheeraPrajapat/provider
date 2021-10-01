package com.marius.valeyou_admin.ui.fragment.products;

import androidx.databinding.ObservableField;

import com.marius.valeyou_admin.data.local.SharedPref;
import com.marius.valeyou_admin.data.remote.helper.NetworkErrorHandler;
import com.marius.valeyou_admin.di.base.viewmodel.BaseFragmentViewModel;
import com.marius.valeyou_admin.util.event.SingleActionEvent;

import javax.inject.Inject;

public class ProductFragmentVM extends BaseFragmentViewModel {

    private final SharedPref sharedPref;
    private final NetworkErrorHandler networkErrorHandler;
    final SingleActionEvent<Integer> passIntent = new SingleActionEvent<>();
    public final ObservableField<String> field_Player1 = new ObservableField<>();

    @Inject
    public ProductFragmentVM(SharedPref sharedPref, NetworkErrorHandler networkErrorHandler) {
        this.sharedPref = sharedPref;
        this.networkErrorHandler = networkErrorHandler;
    }
}
