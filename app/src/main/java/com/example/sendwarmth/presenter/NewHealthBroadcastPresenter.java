package com.example.sendwarmth.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.sendwarmth.NewFriendsCircleActivity;
import com.example.sendwarmth.NewHealthBroadcastActivity;
import com.example.sendwarmth.NewInterestingActivityActivity;
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

public class NewHealthBroadcastPresenter
{
    private Context context;
    private SharedPreferences pref;
    private ProgressDialog progressDialog;

    public NewHealthBroadcastPresenter(Context context, SharedPreferences pref){
        this.context = context;
        this.pref = pref;
    }

    public void postHealthBroadcast(final String title, String imagePath, final String expireTime, final String description){
        progressDialog = ProgressDialog.show(context,"","上传中...");
        String address = HttpUtil.LocalAddress + "/api/file";
        final String credential = pref.getString("credential","");
        final String userId = pref.getString("userId","");
        String fileName = FileUtil.compressImagePathToImagePath(imagePath);
        HttpUtil.fileRequest(address, new File(fileName), new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                ((AppCompatActivity)context).runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(context, "网络连接错误", Toast.LENGTH_LONG).show();
                    }
                });
                progressDialog.dismiss();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                final String responseData = response.body().string();
                LogUtil.e("NewInterestingActivityPresenter",responseData);
                String image = Utility.checkString(responseData,"msg");
                final String address = HttpUtil.LocalAddress + "/api/topic";
                HttpUtil.postHealthBroadcastRequest(address, userId, credential, title, image, expireTime, description, new Callback()
                {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e)
                    {
                        ((AppCompatActivity)context).runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Toast.makeText(context, "网络连接错误", Toast.LENGTH_LONG).show();
                            }
                        });
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
                    {
                        final String responseData = response.body().string();
                        LogUtil.e("NewHealthBroadcastPresenter",responseData);
                        if(Utility.checkResponse(responseData,context,address)){
                            ((AppCompatActivity)context).runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Toast.makeText(context, "发布成功", Toast.LENGTH_LONG).show();
                                }
                            });
                            ((NewHealthBroadcastActivity) context).finish();
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }
}
