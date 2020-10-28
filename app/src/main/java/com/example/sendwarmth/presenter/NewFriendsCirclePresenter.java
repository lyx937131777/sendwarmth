package com.example.sendwarmth.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.sendwarmth.NewFriendsCircleActivity;
import com.example.sendwarmth.NewInterestingActivityActivity;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.Utility;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NewFriendsCirclePresenter
{
    private Context context;
    private SharedPreferences pref;
    private ProgressDialog progressDialog;

    public NewFriendsCirclePresenter(Context context, SharedPreferences pref){
        this.context = context;
        this.pref = pref;
    }

    public void postFriendsCircle(final String content, String imagePath){
        progressDialog = ProgressDialog.show(context,"","上传中...");
        String address = HttpUtil.LocalAddress + "/api/file";
        final String credential = pref.getString("credential","");
        String fileName = Utility.compressImagePathToImagePath(imagePath);
        HttpUtil.fileRequest(address, new File(fileName), new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                ((NewFriendsCircleActivity)context).runOnUiThread(new Runnable()
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
                final String responsData = response.body().string();
                LogUtil.e("NewFriendsCirclePresenter",responsData);
                String image = Utility.checkString(responsData,"msg");
                String address = HttpUtil.LocalAddress + "/api/blog";
                HttpUtil.postFriendsCircleRequest(address, credential, content, image, new Callback()
                {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e)
                    {
                        ((NewFriendsCircleActivity)context).runOnUiThread(new Runnable()
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
                        final String responsData = response.body().string();
                        LogUtil.e("NewFriendsCirclePresenter",responsData);
                        if(Utility.checkString(responsData,"code").equals("000")){
                            ((NewFriendsCircleActivity)context).runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Toast.makeText(context, "发布成功", Toast.LENGTH_LONG).show();
                                }
                            });
                            ((NewFriendsCircleActivity) context).finish();
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }
}
