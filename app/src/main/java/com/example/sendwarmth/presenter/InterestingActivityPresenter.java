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
        String address = HttpUtil.LocalAddress + "/api/activity/list";
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
                LogUtil.e("InterestingActivityPresenter",responsData);
                List<InterestingActivity> interestingActivityList = Utility.handleInterestingActivityList(responsData);
                interestingActivityAdapter.setmList(interestingActivityList);
                ((MainActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        interestingActivityAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}