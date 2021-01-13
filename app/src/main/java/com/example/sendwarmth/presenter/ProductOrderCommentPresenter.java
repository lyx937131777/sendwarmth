package com.example.sendwarmth.presenter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class ProductOrderCommentPresenter
{
    private Context context;
    private SharedPreferences pref;
    private ProgressDialog progressDialog;

    public ProductOrderCommentPresenter(Context context, SharedPreferences pref){
        this.context = context;
        this.pref = pref;
    }

    public void commentProductOrder(String orderId, String comment){
        progressDialog = ProgressDialog.show(context,"","发布中...");
        final String address = HttpUtil.LocalAddress + "/api/orderproduct/evaluate/" + orderId;
        String credential = pref.getString("credential","");
        HttpUtil.commentProductOrderRequest(address, credential, comment, new Callback()
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
                LogUtil.e("ProductOrderCommentPresenter",responseData);
                progressDialog.dismiss();
                if(Utility.checkResponse(responseData,context, address)){
                    ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(context)
                                    .setTitle("提示")
                                    .setMessage("发布成功！")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i)
                                        {
                                            ((AppCompatActivity)context).setResult(Activity.RESULT_OK);
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
