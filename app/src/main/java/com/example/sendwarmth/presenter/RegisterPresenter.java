package com.example.sendwarmth.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.example.sendwarmth.RegisterActivity;
import com.example.sendwarmth.util.CheckUtil;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.Utility;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterPresenter
{
    private Context context;
    private CheckUtil checkUtil;


    public RegisterPresenter(Context context, CheckUtil checkUtil)
    {
        this.context = context;
        this.checkUtil = checkUtil;
    }

    public void register(String tel, String password, String confirm, String userName, String name, String cusAddress,
                         double longitude, double latitude, String houseNum, String personalDescription)
    {
        if (!checkUtil.checkRegister(tel,password,confirm,userName,name,cusAddress,longitude,latitude,houseNum,personalDescription))
            return;
        String address = HttpUtil.LocalAddress + "/api/users/old";
        HttpUtil.registerRequest(address, tel, password, userName, name, cusAddress,longitude,latitude,houseNum,personalDescription, new
                Callback()
                {
                    @Override
                    public void onFailure(Call call, IOException e)
                    {
                        e.printStackTrace();
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
                    public void onResponse(Call call, Response response) throws IOException
                    {
                        final String responseData = response.body().string();
                        LogUtil.e("RegisterPresenter", responseData);
                        if (Utility.checkString(responseData,"code").equals("000"))
                        {
                            ((RegisterActivity) context).runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    new AlertDialog.Builder(context)
                                            .setTitle("提示")
                                            .setMessage("注册成功！")
                                            .setPositiveButton("确定", new
                                                    DialogInterface.OnClickListener()
                                                    {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int
                                                                which)
                                                        {
                                                            ((RegisterActivity) context).finish();
                                                        }
                                                    }).show();
                                }
                            });
                        } else if (Utility.checkString(responseData,"code").equals("500"))
                        {
                            if(Utility.checkString(responseData,"msg").equals("用户名不能重复。")){
                                ((RegisterActivity) context).runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        new AlertDialog.Builder(context)
                                                .setTitle("提示")
                                                .setMessage("该账户已被注册！")
                                                .setPositiveButton("确定", null)
                                                .show();
                                    }
                                });
                            }else{
                                ((RegisterActivity) context).runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        new AlertDialog.Builder(context)
                                                .setTitle("提示")
                                                .setMessage(Utility.checkString(responseData,"msg"))
                                                .setPositiveButton("确定", null)
                                                .show();
                                    }
                                });
                            }
                        } else {
                            ((RegisterActivity) context).runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    new AlertDialog.Builder(context)
                                            .setTitle("提示")
                                            .setMessage("由于未知原因注册失败，请重试！")
                                            .setPositiveButton("确定", null)
                                            .show();
                                }
                            });
                        }
                    }
                });
    }
}
