package com.example.sendwarmth.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.Utility;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ProductOrderDetailPresenter
{
    private Context context;
    private SharedPreferences pref;
    private ProgressDialog progressDialog;

    public ProductOrderDetailPresenter(Context context, SharedPreferences pref){
        this.context = context;
        this.pref = pref;
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
                LogUtil.e("ProductOrderDetailPresenter",responseData);
                progressDialog.dismiss();
                if(Utility.checkResponse(responseData,context, address)){
                    ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(context)
                                    .setTitle("提示")
                                    .setMessage("支付成功！")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i)
                                        {
                                            ((AppCompatActivity)context).finish();
                                        }
                                    })
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
                LogUtil.e("ProductOrderDetailPresenter",responseData);
                progressDialog.dismiss();
                if(Utility.checkResponse(responseData,context, address)){
                    ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(context)
                                    .setTitle("提示")
                                    .setMessage("收货成功！")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i)
                                        {
                                            ((AppCompatActivity)context).finish();
                                        }
                                    })
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
                LogUtil.e("ProductOrderDetailPresenter",responseData);
                progressDialog.dismiss();
                if(Utility.checkResponse(responseData,context, address)){
                    ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(context)
                                    .setTitle("提示")
                                    .setMessage("申请成功，请等待审核！")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i)
                                        {
                                            ((AppCompatActivity)context).finish();
                                        }
                                    })
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
                LogUtil.e("ProductOrderDetailPresenter",responseData);
                progressDialog.dismiss();
                if(Utility.checkResponse(responseData,context, address)){
                    ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(context)
                                    .setTitle("提示")
                                    .setMessage("已取消订单！")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i)
                                        {
                                            ((AppCompatActivity)context).finish();
                                        }
                                    })
                                    .show();
                        }
                    });
                }
            }
        });
    }
}
