package com.example.sendwarmth.presenter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.sendwarmth.NewInterestingActivityActivity;
import com.example.sendwarmth.util.CheckUtil;
import com.example.sendwarmth.util.FileUtil;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.Utility;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NewInterestingActivityPresenter
{
    private Context context;
    private SharedPreferences pref;
    private CheckUtil checkUtil;
    private ProgressDialog progressDialog;

    public NewInterestingActivityPresenter(Context context, SharedPreferences pref, CheckUtil checkUtil){
        this.context = context;
        this.pref = pref;
        this.checkUtil = checkUtil;
    }

    public void postInterestingActivity(final String title, final String imagePath, final String lowBudget, final String upBudget, final String maxNum, final String location, final String contactName, final String contactTel, final String description){
        if(!checkUtil.checkInterestingActivity(imagePath, title, lowBudget, upBudget, maxNum, location,contactName, contactTel, description))
            return;
        new AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage("确定发表该趣味活动么？")
                .setPositiveButton("确定", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog = ProgressDialog.show(context, "", "上传中...");
                                String address = HttpUtil.LocalAddress + "/api/file";
                                final String credential = pref.getString("credential", "");
                                final String userId = pref.getString("userId", "");
                                String fileName = FileUtil.compressImagePathToImagePath(imagePath);
                                HttpUtil.fileRequest(address, new File(fileName), new Callback() {
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
                                        LogUtil.e("NewInterestingActivityPresenter", responseData);
                                        String image = Utility.checkString(responseData, "msg");
                                        final String address = HttpUtil.LocalAddress + "/api/activity";
                                        HttpUtil.postInterestingActivityRequest(address, userId, credential, title, image, lowBudget, upBudget, maxNum, location, contactName, contactTel, description, new Callback() {
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
                                                LogUtil.e("NewInterestingActivityPresenter", responseData);
                                                if (Utility.checkResponse(responseData, context, address)) {
                                                    ((AppCompatActivity) context).runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(context, "发布成功", Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                                    ((NewInterestingActivityActivity) context).finish();
                                                }
                                                progressDialog.dismiss();
                                            }
                                        });
                                    }
                                });
                            }
                        }).setNegativeButton("取消",null).show();

    }
}
