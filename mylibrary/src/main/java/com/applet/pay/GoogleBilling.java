package com.applet.pay;

import android.app.Activity;
import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryPurchasesParams;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.applet.feature.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GoogleBilling implements PurchasesUpdatedListener {

    private static final int FAIL_CODE_CANCEL = 0;
    private static final int FAIL_CODE_DISCONNECT = -11;
    private static final int FAIL_CODE_CONNECT = -12;
    private static final int FAIL_CODE_SKU_TYPE = -13;
    private static final int FAIL_CODE_SKU = -14;
    private static final int FAIL_CODE_SKU_CHECK = -15;
    private static final int FAIL_CODE_CALL_PAY = -16;
    private static final int FAIL_CODE_CONSUME_QUERY = -17;
    private static final int FAIL_CODE_CONSUME_NO = -18;
    private static final int FAIL_CODE_CONSUME_STATUS = -19;
    private static final int FAIL_CODE_CONSUME = -20;
    private static final int FAIL_CODE_ACKNOWLEDGE_QUERY = -21;
    private static final int FAIL_CODE_ACKNOWLEDGE_NO = -22;
    private static final int FAIL_CODE_ACKNOWLEDGE_STATUS = -23;
    private static final int FAIL_CODE_ACKNOWLEDGE = -24;
    private static final int FAIL_CODE_PURCHASES = -25;
    private static final int FAIL_CODE_GOOGLE_DEF = -999;

    private static final GoogleBilling mGoogleBilling = new GoogleBilling();

    private GoogleBilling() {
    }

    public static GoogleBilling getInstance() {
        return mGoogleBilling;
    }

    private BillingClient mBillingClient;
    private OnBillingListener mOnBillingListener;
    private boolean isSupportV5 = true;

    public void initBilling(Context context) {
        isSupportV5 = isSupportPayV5();
        if (mBillingClient == null) {
            mBillingClient = BillingClient.newBuilder(context)
                    .setListener(GoogleBilling.this)
                    .enablePendingPurchases()
                    .build();
        }
    }

    public void setBillingListener(OnBillingListener billingListener) {
        this.mOnBillingListener = billingListener;
    }

    public void pay(Context context, JSONObject params) {
        initBilling(context);
        String sku = params.getString("sku");
        String tranNo = params.getString("tran_no");

        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
                onFail(FAIL_CODE_DISCONNECT, FAIL_CODE_GOOGLE_DEF, "billing service disconnected");
            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK) {
                    onFail(FAIL_CODE_CONNECT, billingResult.getResponseCode(), "billing setup finish response no ok");
                    return;
                }
                if (isSupportPayV5()) {
                    queryProductDetail(((Activity) context), sku, tranNo);
                } else {
                    querySkuDetail(((Activity) context), sku, tranNo);
                }
            }
        });
    }

    public void check(Context context) {
        initBilling(context);
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK) {
                    return;
                }

                acknowledgePurchase();
                consumePurchase();
            }
        });
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
            LogUtil.t("onPurchasesUpdated: '----->");
            if (mOnBillingListener.productType().equals(BillingClient.ProductType.SUBS)) {
                acknowledgePurchase();
            } else if (mOnBillingListener.productType().equals(BillingClient.ProductType.INAPP)) {
                consumePurchase();
            } else {
                onFail(FAIL_CODE_SKU_TYPE, FAIL_CODE_GOOGLE_DEF, "product type error");
            }
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            onFail(FAIL_CODE_CANCEL, FAIL_CODE_GOOGLE_DEF, "user canceled pay");
        } else {
            onFail(FAIL_CODE_PURCHASES, billingResult.getResponseCode(), "Google pay deduction failed");
        }
    }

    private void queryProductDetail(Activity activity, String sku, String tranNo) {
        List<QueryProductDetailsParams.Product> productList = new ArrayList<>();
        productList.add(QueryProductDetailsParams.Product.newBuilder()
                .setProductId(sku)
                .setProductType(mOnBillingListener.productType())
                .build());
        QueryProductDetailsParams queryProductDetailsParams =
                QueryProductDetailsParams.newBuilder()
                        .setProductList(productList)
                        .build();
        mBillingClient.queryProductDetailsAsync(queryProductDetailsParams, new ProductDetailsResponseListener() {
            @Override
            public void onProductDetailsResponse(@NonNull BillingResult billingResult, @NonNull List<ProductDetails> productDetailsList) {
                if (productDetailsList == null || productDetailsList.size() <= 0) {
                    onFail(FAIL_CODE_SKU, billingResult.getResponseCode(), "sku get fail size 0");
                    return;
                }
                ProductDetails targetProductDetails = null;
                for (ProductDetails productDetails : productDetailsList) {
                    if (sku.equals(productDetails.getProductId())) {
                        targetProductDetails = productDetails;
                    }
                }
                if (targetProductDetails == null) {
                    onFail(FAIL_CODE_SKU_CHECK, billingResult.getResponseCode(), "sku get fail can not find sku");
                } else {
                    BillingFlowParams.ProductDetailsParams productDetailsParams;
                    String offerToken = null;
                    if (targetProductDetails.getProductType().equals(BillingClient.ProductType.SUBS)) {
                        if (targetProductDetails.getSubscriptionOfferDetails() != null && targetProductDetails.getSubscriptionOfferDetails().size() > 0) {
                            offerToken = targetProductDetails
                                    .getSubscriptionOfferDetails()
                                    .get(0)
                                    .getOfferToken();
                        }
                    }
                    if (offerToken != null) {
                        productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(targetProductDetails)
                                .setOfferToken(offerToken)
                                .build();
                    } else {
                        productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(targetProductDetails)
                                .build();
                    }

                    List<BillingFlowParams.ProductDetailsParams> productDetailsParamsList = new ArrayList<>();
                    productDetailsParamsList.add(productDetailsParams);
                    BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                            .setProductDetailsParamsList(productDetailsParamsList)
                            .setObfuscatedAccountId(tranNo)
                            .build();
                    int responseCode = mBillingClient.launchBillingFlow(activity, billingFlowParams).getResponseCode();
                    if (responseCode != BillingClient.BillingResponseCode.OK) {
                        onFail(FAIL_CODE_CALL_PAY, responseCode, "evoked payment fail");
                    }
                }
            }
        });
    }

    private void querySkuDetail(Activity activity, String sku, String tranNo) {
        List<String> skuList = new ArrayList<>();
        skuList.add(sku);
        SkuDetailsParams params = SkuDetailsParams.newBuilder()
                .setSkusList(skuList)
                .setType(mOnBillingListener.productType())
                .build();
        mBillingClient.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> skuDetailsList) {
                LogUtil.t("onSkuDetailsResponse: ------> get goods list success " + sku);
                if (skuDetailsList == null || skuDetailsList.size() <= 0) {
                    onFail(FAIL_CODE_SKU, billingResult.getResponseCode(), "sku get fail size 0 v4");
                    return;
                }
                SkuDetails targetSku = null;
                for (SkuDetails skuDetails : skuDetailsList) {
                    if (sku.equals(skuDetails.getSku())) {
                        targetSku = skuDetails;
                    }
                }
                if (targetSku == null) {
                    onFail(FAIL_CODE_SKU_CHECK, billingResult.getResponseCode(), "sku get fail can not find sku v4");
                } else {
                    BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                            .setSkuDetails(targetSku)
                            .setObfuscatedAccountId(tranNo)
                            .build();
                    int responseCode = mBillingClient.launchBillingFlow(activity, billingFlowParams).getResponseCode();
                    if (responseCode != BillingClient.BillingResponseCode.OK) {
                        onFail(FAIL_CODE_CALL_PAY, responseCode, "evoked payment fail v4");
                    }
                }
            }
        });
    }

    private final PurchasesResponseListener mInAppPurchasesResponseListener = new PurchasesResponseListener() {
        @Override
        public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> list) {
            LogUtil.t("consumePurchase: billingResult '----> " + billingResult.getResponseCode());

            if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK) {
                onFail(FAIL_CODE_CONSUME_QUERY, billingResult.getResponseCode(), "inApp consume goods query fail");
                return;
            }

            if (list.size() <= 0) {
                onFail(FAIL_CODE_CONSUME_NO, FAIL_CODE_GOOGLE_DEF, "inApp consume goods no size 0");
                return;
            }

            for (Purchase purchase : list) {
                LogUtil.t("consumePurchase: '------> " + purchase.getOriginalJson());
                if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged()) {
                    ConsumeParams consumeParams = ConsumeParams.newBuilder()
                            .setPurchaseToken(purchase.getPurchaseToken())
                            .build();
                    mBillingClient.consumeAsync(consumeParams, new ConsumeResponseListener() {
                        @Override
                        public void onConsumeResponse(@NonNull BillingResult billingResult, @NonNull String s) {
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                LogUtil.t("onConsumeResponse: -----> sku consume success " + purchase);
                                onSuccess(purchase.getOriginalJson());
                            } else {
                                onFail(FAIL_CODE_CONSUME, billingResult.getResponseCode(), "inApp consume fail");
                            }
                        }
                    });
                } else {
                    onFail(FAIL_CODE_CONSUME_STATUS, FAIL_CODE_GOOGLE_DEF, "inApp consume status not PURCHASED");
                }
            }
        }
    };

    private void consumePurchase() {
        if (isSupportPayV5()) {
            mBillingClient.queryPurchasesAsync(
                    QueryPurchasesParams.newBuilder()
                            .setProductType(BillingClient.ProductType.INAPP)
                            .build(),
                    mInAppPurchasesResponseListener);
        } else {
            mBillingClient.queryPurchasesAsync(BillingClient.SkuType.INAPP, mInAppPurchasesResponseListener);
        }
    }

    private final PurchasesResponseListener mSubsPurchasesResponseListener = new PurchasesResponseListener() {
        @Override
        public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> list) {
            LogUtil.t("acknowledgePurchase: billingResult '----> " + billingResult.getResponseCode());

            if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK) {
                onFail(FAIL_CODE_ACKNOWLEDGE_QUERY, billingResult.getResponseCode(), "subs consume goods query fail");
                return;
            }

            if (list.size() <= 0) {
                onFail(FAIL_CODE_ACKNOWLEDGE_NO, FAIL_CODE_GOOGLE_DEF, "subs consume goods no size 0");
                return;
            }

            for (Purchase purchase : list) {
                LogUtil.t("acknowledgePurchase: '------> " + purchase.getOriginalJson());

                if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged()) {
                    AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                            .setPurchaseToken(purchase.getPurchaseToken())
                            .build();
                    mBillingClient.acknowledgePurchase(acknowledgePurchaseParams, new AcknowledgePurchaseResponseListener() {
                        @Override
                        public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                onSuccess(purchase.getOriginalJson());
                            } else {
                                onFail(FAIL_CODE_ACKNOWLEDGE, billingResult.getResponseCode(), "subs consume fail");
                            }
                        }
                    });
                } else {
                    onFail(FAIL_CODE_ACKNOWLEDGE_STATUS, FAIL_CODE_GOOGLE_DEF, "subs consume status not PURCHASED");
                }
            }
        }
    };

    private void acknowledgePurchase() {
        if (isSupportPayV5()) {
            mBillingClient.queryPurchasesAsync(
                    QueryPurchasesParams.newBuilder()
                            .setProductType(BillingClient.ProductType.SUBS)
                            .build(),
                    mSubsPurchasesResponseListener);
        } else {
            mBillingClient.queryPurchasesAsync(BillingClient.SkuType.SUBS, mSubsPurchasesResponseListener);
        }
    }

    private boolean isSupportPayV5() {
        if (mBillingClient == null) return false;
        BillingResult featureSupported = mBillingClient.isFeatureSupported(BillingClient.FeatureType.PRODUCT_DETAILS);
        if (featureSupported == null) return false;
        return featureSupported.getResponseCode() == BillingClient.BillingResponseCode.OK;
    }

    private void onFail(int appCode, int googleCode, String message) {
        if (mOnBillingListener != null) {
            mOnBillingListener.onFail(appCode, googleCode, isSupportV5, message);
        }
    }

    private void onSuccess(String originalJson) {
        if (mOnBillingListener != null) {
            mOnBillingListener.onSuccess(originalJson, isSupportV5);
        }
    }
}
