package com.marius.valeyou_admin.util.view;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.databinding.SheetNoInternetBinding;


public class NoInternetSheet extends BottomSheetDialogFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SheetNoInternetBinding binding = DataBindingUtil.inflate(inflater, R.layout.sheet_no_internet, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}
