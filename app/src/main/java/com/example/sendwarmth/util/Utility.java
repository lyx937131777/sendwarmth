package com.example.sendwarmth.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;

import com.example.sendwarmth.db.Customer;
import com.example.sendwarmth.db.HealthBroadcast;
import com.example.sendwarmth.db.InterestingActivity;
import com.example.sendwarmth.db.Product;
import com.example.sendwarmth.db.ProductClass;
import com.example.sendwarmth.db.ServiceClass;
import com.example.sendwarmth.db.ServiceSubject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Utility
{

    public static final String ERROR_CODE = "-1";
    //返回Json数据的特定string值
    public static String checkString(String response, String string)
    {
        try
        {
            JSONObject dataObject = new JSONObject(response);
            return dataObject.getString(string);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return ERROR_CODE;
    }

    public static String getRole(String response){
        try
        {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray dataArray = jsonObject.getJSONArray("datas");
            JSONObject dataObject = dataArray.getJSONObject(0);
            return dataObject.getString("roleType");
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return ERROR_CODE;
    }

    public static Customer handleCustomer(String response){
        if (!TextUtils.isEmpty(response))
        {
            try
            {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray dataArray = jsonObject.getJSONArray("datas");
                JSONObject dataObject = dataArray.getJSONObject(0);
                JSONObject customerObject = dataObject.getJSONObject("customerInfo");
                String jsonString = customerObject.toString();
                return  new Gson().fromJson(jsonString, Customer.class);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static List<ServiceClass> handleServiceClassList(String response){
        if (!TextUtils.isEmpty(response))
        {
            try
            {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray dataArray = jsonObject.getJSONArray("datas");
                JSONObject dataObject = dataArray.getJSONObject(0);
                JSONArray jsonArray = dataObject.getJSONArray("content");
                String serviceClassJson = jsonArray.toString();
                return new Gson().fromJson(serviceClassJson, new TypeToken<List<ServiceClass>>() {}.getType());
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static List<ServiceSubject> handleServiceSubjectList(String response){
        if (!TextUtils.isEmpty(response))
        {
            try
            {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray dataArray = jsonObject.getJSONArray("datas");
                JSONObject dataObject = dataArray.getJSONObject(0);
                JSONArray jsonArray = dataObject.getJSONArray("content");
                String serviceSubjectJson = jsonArray.toString();
                return new Gson().fromJson(serviceSubjectJson, new TypeToken<List<ServiceSubject>>() {}.getType());
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    //处理活动
    public static List<InterestingActivity> handleInterestingActivityList(String response){
        if (!TextUtils.isEmpty(response))
        {
            try
            {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray dataArray = jsonObject.getJSONArray("datas");
                JSONObject dataObject = dataArray.getJSONObject(0);
                JSONArray jsonArray = dataObject.getJSONArray("content");
                String interestingActivityJson = jsonArray.toString();
                return new Gson().fromJson(interestingActivityJson, new TypeToken<List<InterestingActivity>>() {}.getType());
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    //处理养生播报（topic）
    public static List<HealthBroadcast> handleHealthBroadcastList(String response){
        if (!TextUtils.isEmpty(response))
        {
            try
            {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray dataArray = jsonObject.getJSONArray("datas");
                JSONObject dataObject = dataArray.getJSONObject(0);
                JSONArray jsonArray = dataObject.getJSONArray("content");
                String healthBroadcastJson = jsonArray.toString();
                return new Gson().fromJson(healthBroadcastJson, new TypeToken<List<HealthBroadcast>>() {}.getType());
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    //处理产品类别
    public static List<ProductClass> handleProductClassList(String response){
        if (!TextUtils.isEmpty(response))
        {
            try
            {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray dataArray = jsonObject.getJSONArray("datas");
                JSONObject dataObject = dataArray.getJSONObject(0);
                JSONArray jsonArray = dataObject.getJSONArray("content");
                String productClassJson = jsonArray.toString();
                return new Gson().fromJson(productClassJson, new TypeToken<List<ProductClass>>() {}.getType());
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    //处理产品
    public static List<Product> handleProductList(String response){
        if (!TextUtils.isEmpty(response))
        {
            try
            {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray dataArray = jsonObject.getJSONArray("datas");
                JSONObject dataObject = dataArray.getJSONObject(0);
                JSONArray jsonArray = dataObject.getJSONArray("content");
                String productJson = jsonArray.toString();
                return new Gson().fromJson(productJson, new TypeToken<List<Product>>() {}.getType());
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    //后台给的日期格式 转化为日期Date
    public static Date stringToDate(String s){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
//        df2.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = null;
        try {
            date = df.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    //字符串+格式 转化为日期Date
    public static Date stringToDate(String s, String formatString){
        DateFormat df = new SimpleDateFormat(formatString);
        Date date = null;
        try {
            date = df.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    //后台给的日期格式 转化为目标的日期格式字符串
    public static String timeStampToString(String s, String formatString){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        DateFormat df2 = new SimpleDateFormat(formatString);
//        df2.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = null;
        try {
            date = df.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return df2.format(date);
    }

    //日期转化为目标的日期格式字符串
    public static String dateToString(Date date, String formatString){
        DateFormat df2 = new SimpleDateFormat(formatString);
//        df2.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df2.format(date);
    }

    //时分秒转化成时分的格式
    public static String hmsToHm(String s){
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        DateFormat df2 = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = df.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return df2.format(date);
    }

    /**
     * 将图片转换成Base64编码的字符串
     * @param path
     * @return base64编码的字符串
     */
    public static String imageToBase64(String path){
        if(TextUtils.isEmpty(path)){
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try{
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data,Base64.NO_WRAP);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        LogUtil.e("Push:base64",result.length()+"      "+ result);
        return result;
    }

    //将Bitmap类型的图片转化成file类型，便于上传到服务器
    public static File saveFile(Bitmap bm, String fileName) throws IOException {
        String path = Environment.getExternalStorageDirectory() + "/颂温暖";
        File dirFile = new File(path);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path + "/"+fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 75, bos);
        bos.flush();
        bos.close();
        return myCaptureFile;
    }

    public static String compressImagePathToImagePath(String imagePath){
        long time = System.currentTimeMillis();
        File temp =  null;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        File file = new File(imagePath);
        LogUtil.e("Utility","before: "+file.length()/1024 +"KB");
        try{
            temp = saveFile(bitmap,time+".jpeg");
            LogUtil.e("Utility","abosolutePath: " + temp.getAbsolutePath());
            LogUtil.e("Utility","canonicalPath: " + temp.getCanonicalPath());
            LogUtil.e("Utility","Path: " + temp.getPath());
            LogUtil.e("Utility","parentPath: " + temp.getParent());
            LogUtil.e("Utility","Name: " + temp.getName());
        }catch (IOException e){
            e.printStackTrace();
            LogUtil.e("Utility","文件创建失败");
        }
        LogUtil.e("Utility","after: "+ temp.length()/1024 +"KB");
        return temp.getPath();
    }
}