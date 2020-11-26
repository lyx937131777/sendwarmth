package com.example.sendwarmth.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.example.sendwarmth.HealthBroadcastActivity;
import com.example.sendwarmth.MainActivity;
import com.example.sendwarmth.NewFriendsCircleActivity;
import com.example.sendwarmth.NewInterestingActivityActivity;
import com.example.sendwarmth.R;
import com.example.sendwarmth.adapter.HealthBroadcastAdapter;
import com.example.sendwarmth.adapter.HealthBroadcastCommentAdapter;
import com.example.sendwarmth.db.Comment;
import com.example.sendwarmth.db.HealthBroadcast;
import com.example.sendwarmth.db.InterestingActivity;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.Utility;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

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
        final String address = HttpUtil.LocalAddress + "/api/topic/"+topicId;
        String credential = pref.getString("credential",null);
        HttpUtil.getHttp(address, credential, new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                e.printStackTrace();
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
                LogUtil.e("HealthBroadcastCommentPresenter",responseData);
                if(Utility.checkResponse(responseData,context,address)){
                    final List<Comment> healthBroadcastCommentList = Utility.handleHealthBroadcastCommentList(responseData);
                    if(healthBroadcastCommentList!=null){
                        LogUtil.e("HealthBroadcastCommentPresenter","The number of comments is " + healthBroadcastCommentList.size());
                        healthBroadcastCommentAdapter.setmList(healthBroadcastCommentList);
                        ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                healthBroadcastCommentAdapter.notifyDataSetChanged();
                                if(healthBroadcastCommentList.size()>0){
                                    ((HealthBroadcastActivity) context).findViewById(R.id.no_comment).setVisibility(View.GONE);
                                }
                                //NestedScrollView nestedScrollView = ((HealthBroadcastActivity) context).findViewById(R.id.nested_scroll_view);
                                //LogUtil.e("HealthBroadcastCommentPresenter","nestedScrollView's height is " + nestedScrollView.getHeight());
                            }
                        });
                    }
                }
            }
        });
    }

    public void putHealthBroadcastComment(final HealthBroadcastCommentAdapter healthBroadcastCommentAdapter, final String content,final String topicId){
        progressDialog = ProgressDialog.show(context,"","上传中...");
        String address = HttpUtil.LocalAddress + "/api/topic/comment";
        final String credential = pref.getString("credential","");

        HttpUtil.putHealthBroadcastCommentRequest(address, credential, content, topicId,new Callback()
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
                //LogUtil.e("HealthBroadcastCommentPresenter","response");
                final String responseData = response.body().string();
                LogUtil.e("HealthBroadcastCommentPresenter","response"+responseData);
                if(Utility.checkString(responseData,"code").equals("000")){
                    updateHealthBroadcastComment(healthBroadcastCommentAdapter,topicId);
                    ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                            Toast.makeText(context, "发布成功", Toast.LENGTH_LONG).show();
                        }
                    });
                    EditText comment_content = ((HealthBroadcastActivity)context).findViewById(R.id.comment_content);
                    comment_content.setText("");
                }
                progressDialog.dismiss();
            }
        });
    }
}
