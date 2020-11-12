package com.example.sendwarmth.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;

import com.example.sendwarmth.db.Comment;
import com.example.sendwarmth.db.Customer;
import com.example.sendwarmth.db.FriendsCircle;
import com.example.sendwarmth.db.HealthBroadcast;
import com.example.sendwarmth.db.InterestingActivity;
import com.example.sendwarmth.db.PensionInstitution;
import com.example.sendwarmth.db.Product;
import com.example.sendwarmth.db.ProductClass;
import com.example.sendwarmth.db.ServiceClass;
import com.example.sendwarmth.db.ServiceSubject;
import com.example.sendwarmth.db.Worker;
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
        try {
            JSONObject dataObject = new JSONObject(response);
            return dataObject.getString(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ERROR_CODE;
    }

    public static String getRole(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray dataArray = jsonObject.getJSONArray("datas");
            JSONObject dataObject = dataArray.getJSONObject(0);
            return dataObject.getString("roleType");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ERROR_CODE;
    }

    public static Customer handleCustomer(String response){
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray dataArray = jsonObject.getJSONArray("datas");
                JSONObject dataObject = dataArray.getJSONObject(0);
                JSONObject customerObject = dataObject.getJSONObject("customerInfo");
                String jsonString = customerObject.toString();
                return  new Gson().fromJson(jsonString, Customer.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static List<ServiceClass> handleServiceClassList(String response){
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray dataArray = jsonObject.getJSONArray("datas");
                JSONObject dataObject = dataArray.getJSONObject(0);
                JSONArray jsonArray = dataObject.getJSONArray("content");
                String serviceClassJson = jsonArray.toString();
                return new Gson().fromJson(serviceClassJson, new TypeToken<List<ServiceClass>>() {}.getType());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static List<ServiceSubject> handleServiceSubjectList(String response){
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray dataArray = jsonObject.getJSONArray("datas");
                JSONObject dataObject = dataArray.getJSONObject(0);
                JSONArray jsonArray = dataObject.getJSONArray("content");
                String serviceSubjectJson = jsonArray.toString();
                return new Gson().fromJson(serviceSubjectJson, new TypeToken<List<ServiceSubject>>() {}.getType());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //处理活动
    public static List<InterestingActivity> handleInterestingActivityList(String response){
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray dataArray = jsonObject.getJSONArray("datas");
                JSONObject dataObject = dataArray.getJSONObject(0);
                JSONArray jsonArray = dataObject.getJSONArray("content");
                String interestingActivityJson = jsonArray.toString();
                return new Gson().fromJson(interestingActivityJson, new TypeToken<List<InterestingActivity>>() {}.getType());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //处理养生播报（topic）
    public static List<HealthBroadcast> handleHealthBroadcastList(String response){
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray dataArray = jsonObject.getJSONArray("datas");
                JSONObject dataObject = dataArray.getJSONObject(0);
                JSONArray jsonArray = dataObject.getJSONArray("content");
                String healthBroadcastJson = jsonArray.toString();
                return new Gson().fromJson(healthBroadcastJson, new TypeToken<List<HealthBroadcast>>() {}.getType());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    //处理养生播报评论
    public static List<Comment> handleHealthBroadcastCommentList(String response){
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray dataArray = jsonObject.getJSONArray("datas");
                JSONObject dataObject = dataArray.getJSONObject(0);
                JSONArray jsonArray = dataObject.getJSONArray("commentInfos");
                LogUtil.e("Utility","comment:"+jsonArray.toString());
                String healthBroadcastCommentJson = jsonArray.toString();
                return new Gson().fromJson(healthBroadcastCommentJson, new TypeToken<List<Comment>>() {}.getType());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    //处理朋友圈
    public static List<FriendsCircle> handleFriendsCircleList(String response){
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray dataArray = jsonObject.getJSONArray("datas");
                JSONObject dataObject = dataArray.getJSONObject(0);
                JSONArray jsonArray = dataObject.getJSONArray("content");
                String friendsCircleJson = jsonArray.toString();
                return new Gson().fromJson(friendsCircleJson, new TypeToken<List<FriendsCircle>>() {}.getType());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //处理养老机构
    public static List<PensionInstitution> handlePensionInstitutionList(String response){
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray dataArray = jsonObject.getJSONArray("datas");
                JSONObject dataObject = dataArray.getJSONObject(0);
                JSONArray jsonArray = dataObject.getJSONArray("content");
                String pensionInstitutionJson = jsonArray.toString();
                return new Gson().fromJson(pensionInstitutionJson, new TypeToken<List<PensionInstitution>>() {}.getType());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //处理产品类别
    public static List<ProductClass> handleProductClassList(String response){
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray dataArray = jsonObject.getJSONArray("datas");
                JSONObject dataObject = dataArray.getJSONObject(0);
                JSONArray jsonArray = dataObject.getJSONArray("content");
                String productClassJson = jsonArray.toString();
                return new Gson().fromJson(productClassJson, new TypeToken<List<ProductClass>>() {}.getType());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //处理产品
    public static List<Product> handleProductList(String response){
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray dataArray = jsonObject.getJSONArray("datas");
                JSONObject dataObject = dataArray.getJSONObject(0);
                JSONArray jsonArray = dataObject.getJSONArray("content");
                String productJson = jsonArray.toString();
                return new Gson().fromJson(productJson, new TypeToken<List<Product>>() {}.getType());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //处理员工
    public static List<Worker> handleWorkerList(String response){
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray dataArray = jsonObject.getJSONArray("datas");
                String workerJson = dataArray.toString();
                return new Gson().fromJson(workerJson, new TypeToken<List<Worker>>() {}.getType());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //获得购物车Id
    public static String handleShoppingCartId(String response){
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray dataArray = jsonObject.getJSONArray("datas");
                JSONObject dataObject = dataArray.getJSONObject(0);
                return dataObject.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ERROR_CODE;
    }

}