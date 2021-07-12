package com.example.sendwarmth.presenter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sendwarmth.MainActivity;
import com.example.sendwarmth.MyInformationActivity;
import com.example.sendwarmth.NewFriendsCircleActivity;
import com.example.sendwarmth.db.Account;
import com.example.sendwarmth.db.Customer;
import com.example.sendwarmth.util.FileUtil;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.Utility;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyInformationPresenter {
    private Context context;
    private SharedPreferences pref;
    private ProgressDialog progressDialog;

    public MyInformationPresenter(Context context, SharedPreferences pref){
        this.context = context;
        this.pref = pref;
    }

    public void updateAccount(){
        final String address = HttpUtil.LocalAddress + "/api/users/me";
        final String credential = pref.getString("credential","");
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
                LogUtil.e("MyInformationPresenter",responseData);
                if(Utility.checkResponse(responseData,context,address)){
                    Account account = Utility.handleAccount(responseData);
                    ((MyInformationActivity)context).setAccount(account);
                }
            }
        });
    }

    public void resetProfile(final String imagePath){
        new AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage("确定更换头像么？")
                .setPositiveButton("确定", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog = ProgressDialog.show(context, "", "上传中...");
                                String address = HttpUtil.LocalAddress + "/api/users/resetProfile";
                                final String credential = pref.getString("credential", "");
                                Customer customer = LitePal.where("credential = ?",credential).findFirst(Customer.class);
                                String accountId = customer.getAccountId();
                                String fileName = FileUtil.compressImagePathToImagePath(imagePath);
                                HttpUtil.resetProfileRequest(address, accountId, new File(fileName), new Callback() {
                                    @Override
                                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                        ((AppCompatActivity) context).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(context, "网络连接错误", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        progressDialog.dismiss();
                                    }

                                    @Override
                                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                        final String responseData = response.body().string();
                                        if(Utility.checkResponse(responseData,context,address)){
                                            updateAccount();
                                            ((AppCompatActivity) context).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(context, "头像更换成功！", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        }).setNegativeButton("取消",null).show();
    }
}
