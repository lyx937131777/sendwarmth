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

    public boolean checkLogin(String tel, String password)
    {
        if (tel.length() != 11) {
            Toast.makeText(context, "手机号码格式不正确", Toast.LENGTH_LONG).show();
            return false;
        }
        if (password.length() < 6) {
            Toast.makeText(context, "密码位数不正确", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public boolean checkRegister(String tel, String password, String confirmPassword, String userName, String role, String name, String address, String personalDescription)
    {
        //确认密码不正确、邮箱格式不正确、昵称已被占用
        if (tel.length() != 11) {
            Toast.makeText(context, "手机号码格式不正确", Toast.LENGTH_LONG).show();
            return false;
        }
        if (password.length() < 6) {
            Toast.makeText(context, "请输入至少6位的密码", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(context, "两次密码输入不一致", Toast.LENGTH_LONG).show();
            return false;
        }
        if (userName.length() < 1) {
            Toast.makeText(context, "请填写用户名", Toast.LENGTH_LONG).show();
            return false;
        }
        if(role.equals("notSelected")){
            Toast.makeText(context, "请选择角色", Toast.LENGTH_LONG).show();
            return false;
        }
        if(name.length() < 1){
            Toast.makeText(context, "请填写姓名", Toast.LENGTH_LONG).show();
            return false;
        }
        if(address.length() < 1){
            Toast.makeText(context, "请填写地址", Toast.LENGTH_LONG).show();
            return false;
        }
        if(personalDescription.length() < 1){
            Toast.makeText(context, "请填写个人描述", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
