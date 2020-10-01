package com.example.sendwarmth.presenter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Html;
import android.widget.Toast;

import com.example.sendwarmth.LoginActivity;
import com.example.sendwarmth.MainActivity;
import com.example.sendwarmth.RegisterActivity;
import com.example.sendwarmth.db.Customer;
import com.example.sendwarmth.util.CheckUtil;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.Utility;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.Response;

public class LoginPresenter
{
    private Context context;
    private SharedPreferences pref;
    private CheckUtil checkUtil;
    private ProgressDialog progressDialog;

    public LoginPresenter(Context context, SharedPreferences pref, CheckUtil checkUtil)
    {
        this.context = context;
        this.pref = pref;
        this.checkUtil = checkUtil;
    }

    public void login(final String tel, final String password)
    {

        if (!checkUtil.checkLogin(tel, password))
            return;
        progressDialog = ProgressDialog.show(context,"","登录中...");
        String address = HttpUtil.LocalAddress + "/api/users/login";
        HttpUtil.loginRequest(address, tel, password, new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                e.printStackTrace();
                ((LoginActivity)context).runOnUiThread(new Runnable()
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
                int state = response.code();
                final String responseData = response.body().string();
                LogUtil.e("LoginPresenter", responseData);
                if(state == 200){
                    String address = HttpUtil.LocalAddress + "/api/users/me";
                    final String credential = Credentials.basic(tel, password);
                    HttpUtil.getHttp(address, credential, new Callback()
                    {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e)
                        {
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
                        {
                            final String responsData = response.body().string();
                            LogUtil.e("Login",responsData);
                            if(Utility.getRole(responsData).equals("customer") || Utility.getRole(responsData).equals("expert")){
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("userId", tel);
                                editor.putString("password", password);
                                editor.putString("credential",credential);
                                editor.putString("latest", String.valueOf(System.currentTimeMillis()));
                                editor.apply();
                                LitePal.deleteAll(Customer.class,"userId = ?",tel);
                                Customer customer = Utility.handleCustomer(responsData);
                                customer.setUserId(tel);
                                customer.setCredential(credential);
                                customer.save();
                                Intent intent_login = new Intent(context, MainActivity.class);
                                context.startActivity(intent_login);
                                ((LoginActivity) context).finish();
                            }else{
                                ((LoginActivity) context).runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        new AlertDialog.Builder(context)
                                                .setTitle("提示")
                                                .setMessage("该账号无权登录此APP！")
                                                .setPositiveButton("确定", null)
                                                .show();
                                    }
                                });
                            }
                            progressDialog.dismiss();
                        }
                    });

                }else if(Utility.checkString(responseData,"code").equals("500")){
                    if(Utility.checkString(responseData,"msg").equals("密码错误。")){
                        ((LoginActivity) context).runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                new AlertDialog.Builder(context)
                                        .setTitle("提示")
                                        .setMessage("密码错误，请重新输入！")
                                        .setPositiveButton("确定", null)
                                        .show();
                            }
                        });
                    }else{
                        ((LoginActivity) context).runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                new AlertDialog.Builder(context)
                                        .setTitle("提示")
                                        .setMessage("账号不存在！")
                                        .setPositiveButton("确定", null)
                                        .show();
                            }
                        });
                    }
                    progressDialog.dismiss();
                }
            }
        });

    }
}
