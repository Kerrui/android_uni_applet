package com.applet.pay;

public interface OnBillingListener {

    void onFail(int appCode, int googleCode, boolean isSupportV5, String message);

    void onSuccess(String originalJson, boolean isSupportV5);

    String productType();
}
