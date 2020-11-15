package com.example.sendwarmth.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.sendwarmth.FriendsCircleActivity;
import com.example.sendwarmth.MainActivity;
import com.example.sendwarmth.adapter.FriendsCircleAdapter;
import com.example.sendwarmth.db.Customer;
import com.example.sendwarmth.db.FriendsCircle;
import com.example.sendwarmth.db.PensionInstitution;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.Utility;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class FriendsCirclePresenter
{
    private Context context;
    private SharedPreferences pref;

    public FriendsCirclePresenter(Context context, SharedPreferences pref){
        this.context = context;
        this.pref = pref;
    }

    public void updateFriendsCircle(final FriendsCircleAdapter friendsCircleAdapter){
        String address = HttpUtil.LocalAddress + "/api/blog/list";
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
                LogUtil.e("FriendsCirclePresenter",responseData);
                List<FriendsCircle> friendsCircleList = Utility.handleFriendsCircleList(responseData);
                friendsCircleAdapter.setmList(friendsCircleList);
                ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        friendsCircleAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    public boolean createMenu(){
        String userId = pref.getString("userId","");
        Customer customer = LitePal.where("userId = ?",userId).findFirst(Customer.class);
        FriendsCircle friendsCircle = ((FriendsCircleActivity)context).getFriendsCircle();
        if(friendsCircle.getCustomerId().equals(customer.getInternetId())){
            return true;
        }
        return false;
    }

    public void delete(){
        String address = HttpUtil.LocalAddress + "/api/blog";
        String credential = pref.getString("credential",null);
        FriendsCircle friendsCircle = ((FriendsCircleActivity)context).getFriendsCircle();
        HttpUtil.deleteFriendsCircleRequest(address, credential, friendsCircle.getInternetId(), new Callback()
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
                LogUtil.e("FriendsCirclePresenter",responseData);
                if(Utility.checkResponse(responseData,context)){
                    ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(context)
                                    .setTitle("提示")
                                    .setMessage("删除成功")
                                    .setCancelable(false)
                                    .setPositiveButton("确定", new
                                            DialogInterface.OnClickListener()
                                            {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which)
                                                {
                                                    ((FriendsCircleActivity)context).finish();
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
