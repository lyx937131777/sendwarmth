package com.example.sendwarmth.presenter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.example.sendwarmth.ProductOrderActivity;
import com.example.sendwarmth.R;
import com.example.sendwarmth.adapter.OrderAdapter;
import com.example.sendwarmth.adapter.ProductOrderAdapter;
import com.example.sendwarmth.alipay.AuthResult;
import com.example.sendwarmth.alipay.PayDemoActivity;
import com.example.sendwarmth.alipay.PayResult;
import com.example.sendwarmth.db.Customer;
import com.example.sendwarmth.db.Order;
import com.example.sendwarmth.db.ProductOrder;
import com.example.sendwarmth.fragment.ProductOrderFragment;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.Utility;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ProductOrderPresenter
{
    private Context context;
    private SharedPreferences pref;
    private ProgressDialog progressDialog;
    private ProductOrderFragment productOrderFragment;

    public ProductOrderPresenter(Context context, SharedPreferences pref){
        this.context = context;
        this.pref = pref;
    }

    public ProductOrderFragment getProductOrderFragment()
    {
        return productOrderFragment;
    }

    public void setProductOrderFragment(ProductOrderFragment productOrderFragment)
    {
        this.productOrderFragment = productOrderFragment;
    }

    public void updateOrderList(final ProductOrderAdapter productOrderAdapter, final String[] types){
        String credential = pref.getString("credential","");
        Customer customer = LitePal.where("credential = ?",credential).findFirst(Customer.class);
        final String address = HttpUtil.LocalAddress + "/api/orderproduct/list?customerId=" + customer.getInternetId();
        HttpUtil.getHttp(address, credential, new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "网络连接错误", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                final String responseData = response.body().string();
                LogUtil.e("ProductOrderPresenter",responseData);
                if(Utility.checkResponse(responseData,context, address)){
                    List<ProductOrder> productOrderList = Utility.handleProductOrderList(responseData);
                    List<ProductOrder> typeOrderList = new ArrayList<>();
                    List<String> typeList = new ArrayList<>();
                    for(int i = 0; i < types.length; i++){
                        typeList.add(types[i]);
                    }
                    for(ProductOrder productOrder : productOrderList){
                        if(typeList.contains(productOrder.getState())){
                            typeOrderList.add(productOrder);
                        }
                    }
                    productOrderAdapter.setmList(typeOrderList);
                    ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            productOrderAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    public void payProductOrder(String orderId){
        progressDialog = ProgressDialog.show(context,"","支付中...");
        final String address = HttpUtil.LocalAddress + "/api/orderproduct/pay";
        String credential = pref.getString("credential","");
        HttpUtil.payProductOrderRequest(address, credential, orderId, new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "网络连接错误", Toast.LENGTH_LONG).show();
                    }
                });
                progressDialog.dismiss();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                final String responseData = response.body().string();
                LogUtil.e("ProductOrderPresenter",responseData);
                progressDialog.dismiss();
//                payV2(responseData);
                if(Utility.checkResponse(responseData,context, address)){
                    ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String orderInfo = Utility.checkString(responseData,"msg");
                            payV2(orderInfo);
//                            if(productOrderFragment != null){
//                                productOrderFragment.refresh();
//                            }
//                            new AlertDialog.Builder(context)
//                                    .setTitle("提示")
//                                    .setMessage("支付成功！")
//                                    .setPositiveButton("确定",null)
//                                    .show();
                        }
                    });
                }
            }
        });
    }

    public void payProductOrderWX(String orderId){
        progressDialog = ProgressDialog.show(context,"","支付中...");
        final String address = HttpUtil.LocalAddress + "/api/orderproduct/wxpay";
        String credential = pref.getString("credential","");
        HttpUtil.payProductOrderRequest(address, credential, orderId, new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "网络连接错误", Toast.LENGTH_LONG).show();
                    }
                });
                progressDialog.dismiss();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                final String responseData = response.body().string();
                LogUtil.e("ProductOrderPresenterWX",responseData);
                progressDialog.dismiss();

                PayReq req = new PayReq();
                try {
                    JSONObject json = new JSONObject(responseData);
                    req.appId          = json.getString("appId");
                    req.partnerId      = json.getString("partnerid");
                    req.prepayId       = json.getString("prepayid");
                    req.nonceStr       = json.getString("nonceStr");
                    req.timeStamp      = json.getString("timeStamp");
                    req.packageValue   = "Sign=WXPay";
                    req.sign           = json.getString("sign");
                    IWXAPI api = WXAPIFactory.createWXAPI(context, null);
                    api.registerApp(req.appId);
                    LogUtil.e("ProductOrderPresenter","appId: " + req.appId + " partnerId: "+ req.partnerId);
                    boolean flag = api.sendReq(req); //这里就发起调用微信支付了
                    LogUtil.e("ProductOrderPresenter","sendReq finished!" + flag + " wxInstalled: " + api.isWXAppInstalled());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void receiveProductOrder(String orderId){
        progressDialog = ProgressDialog.show(context,"","确认中...");
        final String address = HttpUtil.LocalAddress + "/api/orderproduct/receive/" + orderId;
        String credential = pref.getString("credential","");
        HttpUtil.receiveProductOrderRequest(address, credential, new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "网络连接错误", Toast.LENGTH_LONG).show();
                    }
                });
                progressDialog.dismiss();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                final String responseData = response.body().string();
                LogUtil.e("ProductOrderPresenter",responseData);
                progressDialog.dismiss();
                if(Utility.checkResponse(responseData,context, address)){
                    ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(productOrderFragment != null){
                                productOrderFragment.refresh();
                            }
                            new AlertDialog.Builder(context)
                                    .setTitle("提示")
                                    .setMessage("收货成功！")
                                    .setPositiveButton("确定",null)
                                    .show();
                        }
                    });
                }
            }
        });
    }

    public void refundProductOrder(String orderId){
        progressDialog = ProgressDialog.show(context,"","确认中...");
        final String address = HttpUtil.LocalAddress + "/api/orderproduct/refundRequest";
        String credential = pref.getString("credential","");
        HttpUtil.refundProductOrderRequest(address, credential, orderId, new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "网络连接错误", Toast.LENGTH_LONG).show();
                    }
                });
                progressDialog.dismiss();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                final String responseData = response.body().string();
                LogUtil.e("ProductOrderPresenter",responseData);
                progressDialog.dismiss();
                if(Utility.checkResponse(responseData,context, address)){
                    ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(productOrderFragment != null){
                                productOrderFragment.refresh();
                            }
                            new AlertDialog.Builder(context)
                                    .setTitle("提示")
                                    .setMessage("申请成功，请等待审核！")
                                    .setPositiveButton("确定", null)
                                    .show();
                        }
                    });
                }
            }
        });
    }

    public void cancelProductOrder(String orderId){
        progressDialog = ProgressDialog.show(context,"","确认中...");
        final String address = HttpUtil.LocalAddress + "/api/orderproduct/cancel";
        String credential = pref.getString("credential","");
        HttpUtil.cancelProductOrderRequest(address, credential, orderId, new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "网络连接错误", Toast.LENGTH_LONG).show();
                    }
                });
                progressDialog.dismiss();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                final String responseData = response.body().string();
                LogUtil.e("ProductOrderPresenter",responseData);
                progressDialog.dismiss();
                if(Utility.checkResponse(responseData,context, address)){
                    ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(productOrderFragment != null){
                                productOrderFragment.refresh();
                            }
                            new AlertDialog.Builder(context)
                                    .setTitle("提示")
                                    .setMessage("已取消订单！")
                                    .setPositiveButton("确定", null)
                                    .show();
                        }
                    });
                }
            }
        });
    }

    //TODO 支付
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(context, "支付成功",Toast.LENGTH_LONG).show();
                        if(productOrderFragment != null){
                            productOrderFragment.refresh();
                        }
//                        new AlertDialog.Builder(context)
//                                .setTitle("提示")
//                                .setMessage("支付成功！")
//                                .setPositiveButton("确定",null)
//                                .show();
//                        showAlert(PayDemoActivity.this, getString(R.string.pay_success) + payResult);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(context, "支付失败",Toast.LENGTH_LONG).show();
//                        showAlert(PayDemoActivity.this, getString(R.string.pay_failed) + payResult);
                    }
                    LogUtil.e("ProductOrderPresenter","payResult: "+payResult.toString());
                    break;
                }
//                case SDK_AUTH_FLAG: {
//                    @SuppressWarnings("unchecked")
//                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
//                    String resultStatus = authResult.getResultStatus();
//
//                    // 判断resultStatus 为“9000”且result_code
//                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
//                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
//                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
//                        // 传入，则支付账户为该授权账户
//                        showAlert(PayDemoActivity.this, getString(R.string.auth_success) + authResult);
//                    } else {
//                        // 其他状态值则为授权失败
//                        showAlert(PayDemoActivity.this, getString(R.string.auth_failed) + authResult);
//                    }
//                    break;
//                }
                default:
                    break;
            }
        };
    };

    /**
     * 支付宝支付业务示例
     */
    public void payV2(String orderInfo) {

        final Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask((AppCompatActivity)context);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                LogUtil.e("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    //微信支付
}
