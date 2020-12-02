package com.example.sendwarmth.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.sendwarmth.MainActivity;
import com.example.sendwarmth.adapter.InterestingActivityAdapter;
import com.example.sendwarmth.db.InterestingActivity;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.Utility;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class InterestingActivityPresenter
{
    private Context context;
    private SharedPreferences pref;

    public InterestingActivityPresenter(Context context, SharedPreferences pref){
        this.context = context;
        this.pref = pref;
    }

    public void updateInterestingActivity(final InterestingActivityAdapter interestingActivityAdapter){
        final String address = HttpUtil.LocalAddress + "/api/activity/list?activityStatus=pass";
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
//                LogUtil.complete_e("InterestingActivityPresenter",responseData);
                LogUtil.e("InterestringActivityPresenter",responseData);
                if(Utility.checkResponse(responseData,context,address)){
                    List<InterestingActivity> interestingActivityList = Utility.handleInterestingActivityList(responseData);
                    interestingActivityAdapter.setmList(interestingActivityList);
                    ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            interestingActivityAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    public void addParticipant(String activityId){
        final String address = HttpUtil.LocalAddress + "/api/activity/add";
        String credential = pref.getString("credential",null);
        HttpUtil.putInterestingActivityParticipantRequest(address, credential, activityId, new Callback(){
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
//                LogUtil.complete_e("InterestingActivityPresenter",responseData);
                LogUtil.e("InterestringActivityPresenter",responseData);
                if(Utility.checkString(responseData,"code").equals("000")) {
                    ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"加入活动成功",Toast.LENGTH_LONG).show();
                        }
                    });
                } else if(Utility.checkString(responseData,"code").equals("500")){
                    ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"您已加入该活动，请勿重复操作",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

        });
    }
}
