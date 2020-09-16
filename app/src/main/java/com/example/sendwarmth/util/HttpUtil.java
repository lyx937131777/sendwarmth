package com.example.sendwarmth.util;

import com.google.gson.Gson;

import java.util.HashMap;

import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil
{
    //正式
    public static final String LocalAddress = "http://47.101.68.214:8999";

    public static String getPhotoURL(String url){
        return LocalAddress + "/resources/" + url;
    }

    //get 登录界面
    public static void getHttp(String address,String credential, Callback callback)
    {
//        OkHttpClient client = buildBasicAuthClient(userID,"123456");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).addHeader("Authorization",credential).build();
        LogUtil.e("Login","111111111   " + request.header("Authorization"));
        client.newCall(request).enqueue(callback);
    }

    public static void loginRequest(String address, String tel, String password, Callback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        HashMap<String, String> map = new HashMap<>();
        map.put("username",tel);
        map.put("password",password);
        String jsonStr = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(jsonStr, JSON);
        Request request = new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    public static void registerRequest(String address, String tel, String password, String userName, String role, String name, String cusAddress, String personalDescription, Callback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        HashMap<String, String> cusMap = new HashMap<>();
        cusMap.put("customerAddress",cusAddress);
        cusMap.put("customerName",name);
        cusMap.put("customerTel",tel);
        cusMap.put("roleType",role);
        cusMap.put("userName",userName);
        cusMap.put("personalDescription",personalDescription);
        String customerInfo = new Gson().toJson(cusMap);
        HashMap<String, String> map = new HashMap<>();
        map.put("loginName",tel);
        map.put("password",password);
        map.put("roleType","customer");
        String jsonStr = new Gson().toJson(map);
        LogUtil.e("HttpUtil","address: "+ address);
        LogUtil.e("HttpUtil","customerInfo: "+ customerInfo);
        jsonStr = jsonStr.split("\\}")[0] + ",\"customerInfo\":"+customerInfo+"}";
        LogUtil.e("HttpUtil","registerRequest: "+jsonStr);
        RequestBody requestBody = RequestBody.create(jsonStr, JSON);
        Request request = new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }
}
