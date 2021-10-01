package com.marius.valeyou_admin.data.remote.helper;

public interface ApiCallback<T> {
    void onLoading();
    void onSuccess(T response, String successMsg);
    void onError(String errMsg);
}
