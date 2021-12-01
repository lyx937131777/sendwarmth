package com.example.sendwarmth.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sendwarmth.MainActivity;
import com.example.sendwarmth.db.Account;
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

public class MainPresenter {
    private Context context;
    private SharedPreferences pref;

    public MainPresenter(Context context, SharedPreferences pref) {
        this.context = context;
        this.pref = pref;
    }

    public void getMe(){
        final String tel = pref.getString("userId","");
        final String address = HttpUtil.LocalAddress + "/api/users/me";
        final String credential = pref.getString("credential","");
        HttpUtil.getHttp(address, credential, new Callback()
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
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                final String responseData = response.body().string();
                LogUtil.e("MainPresenter",responseData);
                if(Utility.checkResponse(responseData,context,address)){
                    Account account = Utility.handleAccount(responseData);
                    ((MainActivity) context).setAccount(account);
//                    LitePal.deleteAll(Customer.class,"userId = ?",tel);
                    List<Customer> customers = LitePal.where("userId = ?", tel).find(Customer.class);
                    Customer customer = Utility.handleCustomer(responseData);
                    customer.setUserId(tel);
                    customer.setCredential(credential);
                    customer.save();
                    for(Customer customerToDelete : customers){
                        customerToDelete.delete();
                    }
                    String profile = account.getProFile();
                    if(profile != null){
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("profile",profile);
                        editor.putString("latest", String.valueOf(System.currentTimeMillis()));
                        editor.apply();
                    }
//                    ((MainActivity)context).setCustomer(customer);
//                    ((MainActivity)context).setAccount(account);
                }
            }
        });
    }
}
