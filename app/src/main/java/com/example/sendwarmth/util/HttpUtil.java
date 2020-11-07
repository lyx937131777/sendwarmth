package com.example.sendwarmth.util;

import com.example.sendwarmth.db.Customer;
import com.google.gson.Gson;

import org.litepal.LitePal;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil
{
    //正式
    public static final String LocalAddress = "http://47.101.68.214:8999";

    public static String getResourceURL(String url){
        return LocalAddress + "/resources/" + url;
    }

    //get 登录界面
    public static void getHttp(String address,String credential, Callback callback)
    {
//        OkHttpClient client = buildBasicAuthClient(userID,"123456");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).addHeader("Authorization",credential).build();
//        LogUtil.e("Login","111111111   " + request.header("Authorization"));
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

    public static void registerRequest(String address, String tel, String password, String userName, String name, String cusAddress, String personalDescription, Callback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        HashMap<String, String> cusMap = new HashMap<>();
        cusMap.put("customerAddress",cusAddress);
        cusMap.put("customerName",name);
        cusMap.put("customerTel",tel);
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

    //上传照片
    public static void fileRequest(String address, File file, okhttp3.Callback callback)
    {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        MediaType fileType = MediaType.parse("image/jpeg");//数据类型为File格式，
        RequestBody fileBody = RequestBody.create(file, fileType);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();
        Request request = new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    //发布新活动
    public static void postInterestingActivityRequest(String address, String userId, String credential, String title, String image, String lowBudget, String upBudget,
                                                     String maxNum, String location, String contactName, String contactTel, String description, Callback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Customer customer = LitePal.where("userId = ?",userId).findFirst(Customer.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("activityName",title);
        map.put("promoterLoginName",userId);
        map.put("promoterId",customer.getInternetId());
        map.put("activityPic",image);
        map.put("activityBud",upBudget);
        map.put("maxNum",maxNum);
        map.put("activityLoc",location);
        map.put("contactTel",contactTel);
        map.put("activityDes",description);
        String jsonStr = new Gson().toJson(map);
        LogUtil.e("HttpUtil","postInterestingActivityRequest: "+jsonStr);
        RequestBody requestBody = RequestBody.create(jsonStr, JSON);
        Request request = new Request.Builder().url(address).post(requestBody).addHeader("Authorization",credential).build();
        client.newCall(request).enqueue(callback);
    }

    //发布评论
    public static void putHealthBroadcastCommentRequest(String address,String credential, String content, String topicId,Callback callback){
        OkHttpClient client = new OkHttpClient();
        Customer customer = LitePal.where("credential = ?",credential).findFirst(Customer.class);
        String customerId = customer.getInternetId();
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("comment", content)
                .addFormDataPart("customerId",customerId)
                .addFormDataPart("id",topicId);
        RequestBody requestBody = builder.build();
        LogUtil.e("HttpUtil","postHealthBroadcastComment:"+address+"/"+topicId+" "+customerId+" "+topicId+" "+content);
        Request request = new Request.Builder().url(address+"/"+topicId).addHeader("Authorization",credential).put(requestBody).build();
        client.newCall(request).enqueue(callback);
    }
    //发布朋友圈
    public static void postFriendsCircleRequest(String address, String credential, String content, String image, Callback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Customer customer = LitePal.where("credential = ?",credential).findFirst(Customer.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("content",content);
        map.put("customerId",customer.getInternetId());
        map.put("picture",image);
        String jsonStr = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(jsonStr, JSON);
        Request request = new Request.Builder().url(address).addHeader("Authorization",credential).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    //删除朋友圈
    public static void deleteFriendsCircleRequest(String address, String credential, String friendsCircleId, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address+"/"+friendsCircleId).addHeader("Authorization",credential).delete().build();
        client.newCall(request).enqueue(callback);
    }

    //新建购物车
    public static void postShoppingCartRequest(String address, String credential, String productId, String productNum, Callback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Customer customer = LitePal.where("credential = ?",credential).findFirst(Customer.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("customerId",customer.getInternetId());
        map.put("productId",productId);
        map.put("productNum",productNum);
        String jsonStr = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(jsonStr, JSON);
        Request request = new Request.Builder().url(address).addHeader("Authorization",credential).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    //新建产品订单
    public static void postProductOrderRequest(String address, String credential, String contactPerson,
                                               String contactTel, String deliveryAddress, List<String> shoppingCartIds, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Customer customer = LitePal.where("credential = ?",credential).findFirst(Customer.class);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("contactPerson", contactPerson)
                .addFormDataPart("contactTel",contactTel)
                .addFormDataPart("deliveryAddress",deliveryAddress)
                .addFormDataPart("customerId",customer.getInternetId());
//                .add("shoppingChartIds","25")
//                .add("shoppingChartIds","26")
//                .build();
        for(String shoppingCartId : shoppingCartIds){
            builder = builder.addFormDataPart("shoppingCartIds",shoppingCartId);
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(address).post(requestBody).addHeader("Authorization",credential).build();
        client.newCall(request).enqueue(callback);
    }
}