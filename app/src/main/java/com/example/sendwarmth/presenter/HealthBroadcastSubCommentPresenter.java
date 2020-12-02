package com.example.sendwarmth.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sendwarmth.HealthBroadcastActivity;
import com.example.sendwarmth.R;
import com.example.sendwarmth.adapter.HealthBroadcastSubCommentAdapter;
import com.example.sendwarmth.db.Comment;
import com.example.sendwarmth.db.Customer;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.Utility;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HealthBroadcastSubCommentPresenter
{
    private Context context;
    private SharedPreferences pref;
    private ProgressDialog progressDialog;

    public HealthBroadcastSubCommentPresenter(Context context, SharedPreferences pref){
        this.context = context;
        this.pref = pref;
    }

    public void putHealthBroadcastSubComment(final HealthBroadcastSubCommentAdapter healthBroadcastSubCommentAdapter, final String content,final String commentId){
        progressDialog = ProgressDialog.show(context,"","上传中...");
        String address = HttpUtil.LocalAddress + "/api/topic/subComment";
        final String credential = pref.getString("credential","");

        HttpUtil.putCommentRequest(address, credential, content, commentId,new Callback()
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
                LogUtil.e("HealthBroadcastSubCommentPresenter","response"+responseData);
                if(Utility.checkString(responseData,"code").equals("000")){
                    //更新二级评论
                    List<Comment> subCommentList = healthBroadcastSubCommentAdapter.getmList();
                    Comment newComment = Utility.handleHealthBroadcastSubComment(responseData);
                    final String userId = pref.getString("userId","");
                    Customer customer = LitePal.where("userId = ?",userId).findFirst(Customer.class);
                    newComment.setCustomerInfo(customer);
                    subCommentList.add(newComment);
                    healthBroadcastSubCommentAdapter.setmList(subCommentList);

                    //将回复数据删除
                    EditText commentContent = ((AppCompatActivity)context).findViewById(R.id.comment_content);
                    commentContent.setHint("");
                    SharedPreferences.Editor editor = pref.edit();
                    editor.remove("commentId");
                    editor.apply();
                    ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                            healthBroadcastSubCommentAdapter.notifyDataSetChanged();
                            Toast.makeText(context, "发布成功", Toast.LENGTH_LONG).show();
                            EditText comment_content = ((HealthBroadcastActivity)context).findViewById(R.id.comment_content);
                            comment_content.setText("");
                            comment_content.clearFocus();
                        }
                    });
                }
                progressDialog.dismiss();
            }
        });
    }
}
