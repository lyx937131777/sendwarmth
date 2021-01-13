package com.example.sendwarmth.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

    public boolean checkRegister(String tel, String password, String confirmPassword, String userName, String name, String address,
                                 double longitude, double latitude, String houseNum, String personalDescription)
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
//        if(role.equals("notSelected")){
//            Toast.makeText(context, "请选择角色", Toast.LENGTH_LONG).show();
//            return false;
//        }
        if(name.length() < 1){
            Toast.makeText(context, "请填写姓名", Toast.LENGTH_LONG).show();
            return false;
        }
        if(address.length() < 1){
            Toast.makeText(context, "请选择地址", Toast.LENGTH_LONG).show();
            return false;
        }
        if(longitude == 0 || latitude == 0){
            Toast.makeText(context, "经纬度信息获取失败", Toast.LENGTH_LONG).show();
            return false;
        }
        if(houseNum.length() < 1){
            Toast.makeText(context, "请填写门牌号", Toast.LENGTH_LONG).show();
            return false;
        }
//        if(personalDescription.length() < 1){
//            Toast.makeText(context, "请填写个人描述", Toast.LENGTH_LONG).show();
//            return false;
//        }
        return true;
    }

    public boolean checkOrder(String tel, long startTime, long endTime, String houseNum, String address, double longitude, double latitude){
        if(address.length() < 1){
            Toast.makeText(context, "请选择地址", Toast.LENGTH_LONG).show();
            return false;
        }
        if(houseNum.length() < 1){
            Toast.makeText(context, "请填写门牌号", Toast.LENGTH_LONG).show();
            return false;
        }
        if(longitude == 0 || latitude == 0){
            Toast.makeText(context, "经纬度信息获取失败", Toast.LENGTH_LONG).show();
            return false;
        }
        if(tel.length() != 11){
            Toast.makeText(context, "手机号码格式不正确", Toast.LENGTH_LONG).show();
            return false;
        }
        if(startTime == 0){
            Toast.makeText(context, "请选择上门时间", Toast.LENGTH_LONG).show();
            return false;
        }
        if(endTime == 0){
            Toast.makeText(context, "请选择结束时间", Toast.LENGTH_LONG).show();
            return false;
        }
        if(endTime < startTime){
            Toast.makeText(context, "结束时间需晚于上门时间", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public boolean checkChangePassword(String oldPassword, String newPassword, String confirmNewPassword){
        if (oldPassword.length() < 1) {
            Toast.makeText(context, "请填写原密码", Toast.LENGTH_LONG).show();
            return false;
        }
        if (newPassword.length() < 6) {
            Toast.makeText(context, "请输入至少6位的新密码", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!newPassword.equals(confirmNewPassword)) {
            Toast.makeText(context, "确认新密码与新密码不一致", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public boolean checkSendVerificationCode(String tel){
        if (tel.length() != 11) {
            Toast.makeText(context, "手机号码格式不正确", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public boolean checkSetNewPassword(String tel, String newPassword, String confirmNewPassword, String verificationCode){
        if (tel.length() != 11) {
            Toast.makeText(context, "手机号码格式不正确", Toast.LENGTH_LONG).show();
            return false;
        }
        if (newPassword.length() < 6) {
            Toast.makeText(context, "请输入至少6位的新密码", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!newPassword.equals(confirmNewPassword)) {
            Toast.makeText(context, "确认新密码与新密码不一致", Toast.LENGTH_LONG).show();
            return false;
        }
        if (verificationCode.length() < 1) {
            Toast.makeText(context, "请填写验证码", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public boolean checkInterestingActivity(String imagePath, String title, String lowBudget, String highBudget, String maxNum, String location, String contactName, String contactTel, String description){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if(imagePath == null){
            builder .setTitle("提示")
                    .setMessage("请选择图片后再发布！")
                    .setPositiveButton("确定", null)
                    .show();
            return false;
        }
        if(title.length() < 1){
            builder .setTitle("提示")
                    .setMessage("请输入主题！")
                    .setPositiveButton("确定", null)
                    .show();
            return false;
        }
        if(title.length() > 30){
            builder .setTitle("提示")
                    .setMessage("标题请限制在30字以内！")
                    .setPositiveButton("确定", null)
                    .show();
            return false;
        }
        if(lowBudget.length() < 1 || highBudget.length() < 1){
            builder .setTitle("提示")
                    .setMessage("请给出预算限制！")
                    .setPositiveButton("确定", null)
                    .show();
            return false;
        }
        if(Integer.valueOf(lowBudget) > Integer.valueOf(highBudget)){
            builder .setTitle("提示")
                    .setMessage("最低预算不得超过最高预算！")
                    .setPositiveButton("确定", null)
                    .show();
            return false;
        }
        if(maxNum.length() < 1){
            builder .setTitle("提示")
                    .setMessage("请输入人数限制！")
                    .setPositiveButton("确定", null)
                    .show();
            return false;
        }
        if(location.length() < 1){
            builder .setTitle("提示")
                    .setMessage("请输入活动地址！")
                    .setPositiveButton("确定", null)
                    .show();
            return false;
        }
        if(contactName.length() < 1){
            builder .setTitle("提示")
                    .setMessage("请输入主办人姓名！")
                    .setPositiveButton("确定", null)
                    .show();
            return false;
        }
        if(contactTel.length() != 11){
            builder .setTitle("提示")
                    .setMessage("电话号码应为11位")
                    .setPositiveButton("确定", null)
                    .show();
            return false;
        }
        if(description.length() < 1){
            builder .setTitle("提示")
                    .setMessage("请输入内容！")
                    .setPositiveButton("确定", null)
                    .show();
            return false;
        }
        if(description.length() > 1500){
            builder.setTitle("提示")
                    .setMessage("内容请限制在1500字以内！")
                    .setPositiveButton("确定", null)
                    .show();
            return false;
        }
        return true;
    }

    public boolean checkFriendCircle(String content, String imagePath){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if(imagePath == null){
            builder .setTitle("提示")
                    .setMessage("请选择图片后再发表！")
                    .setPositiveButton("确定", null)
                    .show();
            return false;
        }
        if(content.length() < 1){
            builder .setTitle("提示")
                    .setMessage("请输入朋友圈内容！")
                    .setPositiveButton("确定",null)
                    .show();
            return false;
        }
        if(content.length() > 1500){
            builder .setTitle("提示")
                    .setMessage("请限制在1500字以内！")
                    .setPositiveButton("确定", null)
                    .show();
            return false;
        }
        return true;
    }

    public boolean checkPostShoppingCart(String contactPerson, String contactTel, String deliveryAddress){
        if(contactPerson.length() < 1){
            Toast.makeText(context, "请填写收件人", Toast.LENGTH_LONG).show();
            return false;
        }
        if(contactTel.length() != 11){
            if (contactTel.length() != 11) {
                Toast.makeText(context, "手机号码格式不正确", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if(deliveryAddress.length() < 1){
            Toast.makeText(context, "请填写收货地址", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
