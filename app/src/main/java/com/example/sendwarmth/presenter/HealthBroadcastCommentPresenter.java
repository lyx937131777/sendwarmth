package com.example.sendwarmth.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.sendwarmth.HealthBroadcastActivity;
import com.example.sendwarmth.MainActivity;
import com.example.sendwarmth.NewFriendsCircleActivity;
import com.example.sendwarmth.NewInterestingActivityActivity;
import com.example.sendwarmth.adapter.HealthBroadcastAdapter;
import com.example.sendwarmth.adapter.HealthBroadcastCommentAdapter;
import com.example.sendwarmth.db.Comment;
import com.example.sendwarmth.db.HealthBroadcast;
import com.example.sendwarmth.db.InterestingActivity;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.Utility;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HealthBroadcastCommentPresenter
{
    private Context context;
    private SharedPreferences pref;
    private ProgressDialog progressDialog;
    public HealthBroadcastCommentPresenter(Context context, SharedPreferences pref){
        this.context = context;
        this.pref = pref;
    }
    public void updateHealthBroadcastComment(final HealthBroadcastCommentAdapter healthBroadcastCommentAdapter,String topicId){
        String address = HttpUtil.LocalAddress + "/api/topic/"+topicId;
        String credential = pref.getString("credential",null);
        HttpUtil.getHttp(address, credential, new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                e.printStackTrace();
                ((MainActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "网络连接错误", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                final String responsData = response.body().string();
                LogUtil.e("HealthBroadcastCommentPresenter",responsData);
                List<Comment> healthBroadcastCommentList = Utility.handleHealthBroadcastCommentList(responsData);
                healthBroadcastCommentAdapter.setmList(healthBroadcastCommentList);
                ((HealthBroadcastActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        healthBroadcastCommentAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    public void getHealthBroadcastCommentByTopicId(final List<Comment>healthBroadcastCommentList, String topicId){
        String address = HttpUtil.LocalAddress + "/api/topic/"+topicId;
        String credential = pref.getString("credential",null);
        HttpUtil.getHttp(address, credential, new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                e.printStackTrace();
                ((HealthBroadcastActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "网络连接错误", Toast.LENGTH_LONG).show();
                    }
                });
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                final String responsData = response.body().string();
                LogUtil.e("HealthBroadcastCommentPresenter",responsData);
                List<Comment>temCommentList = Utility.handleHealthBroadcastCommentList(responsData);
                healthBroadcastCommentList.clear();
                for (Comment comment:temCommentList
                     ) {
                    healthBroadcastCommentList.add(comment);
                }
            }
        });
    }
    public void postHealthBroadcastComment(final String content,final String topicId){
        progressDialog = ProgressDialog.show(context,"","上传中...");
        String address = HttpUtil.LocalAddress + "/api/topic/comment";
        final String credential = pref.getString("credential","");

        HttpUtil.putHealthBroadcastCommentRequest(address, credential, content, topicId,new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                ((HealthBroadcastActivity)context).runOnUiThread(new Runnable()
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
                //LogUtil.e("HealthBroadcastCommentPresenter","response");
                final String responsData = response.body().string();
                LogUtil.e("HealthBroadcastCommentPresenter","response"+responsData);
//                if(Utility.checkString(responsData,"code").equals("000")){
//                    ((HealthBroadcastActivity)context).runOnUiThread(new Runnable()
//                    {
//                        @Override
//                        public void run()
//                        {
//                            Toast.makeText(context, "发布成功", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                    ((HealthBroadcastActivity) context).finish();
//                }
                progressDialog.dismiss();
            }
        });
    }
}
