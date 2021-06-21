package com.example.sendwarmth.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.sendwarmth.ProductOrderActivity;
import com.example.sendwarmth.adapter.OrderAdapter;
import com.example.sendwarmth.adapter.ProductOrderAdapter;
import com.example.sendwarmth.db.Customer;
import com.example.sendwarmth.db.Order;
import com.example.sendwarmth.db.ProductOrder;
import com.example.sendwarmth.fragment.ProductOrderFragment;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.Utility;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
                if(Utility.checkResponse(responseData,context, address)){
                    ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(productOrderFragment != null){
                                productOrderFragment.refresh();
                            }
                            new AlertDialog.Builder(context)
                                    .setTitle("提示")
                                    .setMessage("支付成功！")
                                    .setPositiveButton("确定",null)
                                    .show();
                        }
                    });
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
}
