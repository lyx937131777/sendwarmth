package com.example.sendwarmth.util;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

public class CheckUtil
{
    private Context context;

    public CheckUtil(Context context)
    {
        this.context = context;
    }

    public boolean checkLogin(String username, String password)
    {
        if (username.length() != 11)
        {
            Toast.makeText(context, "手机号码格式不正确", Toast.LENGTH_LONG).show();
            return false;
        }
        if (password.length() < 6)
        {
            Toast.makeText(context, "密码位数不正确", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public boolean checkRegister(String username, String password, String confirmPassword, String nickname)
    {
        //确认密码不正确、邮箱格式不正确、昵称已被占用
        if (username.length() != 11)
        {
            Toast.makeText(context, "手机号码格式不正确", Toast.LENGTH_LONG).show();
            return false;
        }
        if (password.length() < 6)
        {
            Toast.makeText(context, "请输入至少6位的密码", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!password.equals(confirmPassword))
        {
            Toast.makeText(context, "两次密码输入不一致", Toast.LENGTH_LONG).show();
            return false;
        }
        if (nickname.length() < 1)
        {
            Toast.makeText(context, "昵称不得为空", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
