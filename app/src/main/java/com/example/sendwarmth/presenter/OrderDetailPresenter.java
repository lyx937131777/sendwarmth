package com.example.sendwarmth.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.sendwarmth.OrderDetailActivity;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.Utility;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OrderDetailPresenter
{
    private Context context;
    private SharedPreferences pref;
    private ProgressDialog progressDialog;

    public OrderDetailPresenter(Context context, SharedPreferences pref) {
        this.context = context;
        this.pref = pref;
    }

    public void cancelOrder(String orderId){
        progressDialog = ProgressDialog.show(context,"","操作中...");
        final String address = HttpUtil.LocalAddress + "/api/order/cancel";
        String credential = pref.getString("credential","");
        HttpUtil.cancelOrderRequest(address, credential, orderId, new Callback()
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
                String responsData = response.body().string();
                LogUtil.e("OrderDetailPresenter",responsData);
                progressDialog.dismiss();
                if(Utility.checkResponse(responsData,context,address)){
                    ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "操作成功！", Toast.LENGTH_LONG).show();
                        }
                    });
                    ((OrderDetailActivity)context).finish();
                }
            }
        });
    }

    public void commentOrder(String orderId, String comment, int score){
        progressDialog = ProgressDialog.show(context,"","操作中...");
        final String address = HttpUtil.LocalAddress + "/api/order/oldComment";
        String credential = pref.getString("credential","");
        HttpUtil.commentOrderRequest(address, credential, orderId, comment, score, new Callback()
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
                String responsData = response.body().string();
                LogUtil.e("OrderDetailPresenter",responsData);
                progressDialog.dismiss();
                if(Utility.checkResponse(responsData,context,address)){
                    ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "操作成功！", Toast.LENGTH_LONG).show();
                        }
                    });
                    ((OrderDetailActivity)context).finish();
                }
            }
        });
    }
}
