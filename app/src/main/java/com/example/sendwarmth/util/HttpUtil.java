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

    //get 登录界面 获取列表
    public static void getHttp(String address,String credential, Callback callback)
    {
//        OkHttpClient client = buildBasicAuthClient(userID,"123456");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).addHeader("Authorization",credential).build();
//        LogUtil.e("Login","111111111   " + request.header("Authorization"));
        client.newCall(request).enqueue(callback);
    }

    //登录
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

    //注册
    public static void registerRequest(String address, String tel, String password, String userName, String name, String cusAddress,
                                       double longitude, double latitude, String houseNum, String personalDescription, Callback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        HashMap<String, String> cusMap = new HashMap<>();
        cusMap.put("customerAddress",cusAddress);
        cusMap.put("customerName",name);
        cusMap.put("customerTel",tel);
        cusMap.put("userName",userName);
        cusMap.put("longitude",String.valueOf(longitude));
        cusMap.put("latitude",String.valueOf(latitude));
        cusMap.put("houseNum",houseNum);
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

    //修改密码
    public static void changePasswordRequest(String address, String credential,String oldPassword, String newPassword, Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("oldPassword", oldPassword)
                .add("newPassword", newPassword)
                .build();
        Request request = new Request.Builder().url(address).put(requestBody).addHeader("Authorization",credential).build();
        client.newCall(request).enqueue(callback);
    }

    //发送验证码
    public static void sendVerificationCodeRequest(String address,String tel, Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("phoneNum", tel)
                .build();
        Request request = new Request.Builder().url(address).put(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    //通过验证码设置新密码
    public static void setNewPasswordRequest(String address,String tel, String verificationCode, String newPassword, Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("phoneNum", tel)
                .add("telCode",verificationCode)
                .add("newPassword",newPassword)
                .build();
        Request request = new Request.Builder().url(address).put(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    //上传照片
    public static void fileRequest(String address, File file, okhttp3.Callback callback) {
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

    //更换头像
    public static void resetProfileRequest(String address, String accountId, File file, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        MediaType fileType = MediaType.parse("image/jpeg");//数据类型为File格式，
        RequestBody fileBody = RequestBody.create(file, fileType);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();
        Request request = new Request.Builder().url(address + "?accountId=" + accountId).put(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    //修改个人信息
    public static void modifyCustomerRequest(String address,String credential, Customer customer, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String jsonStr = new Gson().toJson(customer);
        RequestBody requestBody = RequestBody.create(jsonStr, JSON);
        Request request = new Request.Builder().url(address).put(requestBody).addHeader("Authorization",credential).build();
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
        map.put("host", contactName);
        map.put("promoterId",customer.getAccountId());
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
    //参加活动
    public static void putInterestingActivityParticipantRequest(String address,String credential,String activityId,Callback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Customer customer = LitePal.where("credential = ?",credential).findFirst(Customer.class);
        String customerId = customer.getInternetId();
        HashMap<String, String> map = new HashMap<>();
        //map.put("comment",content);
        //map.put("customerId",customerId);
        String jsonStr = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(jsonStr, JSON);

//        MultipartBody.Builder builder = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("comment", content)
//                .addFormDataPart("customerId",customerId);
//        RequestBody requestBody = builder.build();
        LogUtil.e("HttpUtil","putParticipant:"+address+"/"+activityId+"?customerId="+customerId);
        Request request = new Request.Builder().url(address+"/"+activityId+"?customerId="+customerId).addHeader("Authorization",credential).put(requestBody).build();
        client.newCall(request).enqueue(callback);
    }
    //获取我参加的活动
    public static void getJoinedActivity(String address, String credential, String type, Callback callback)
    {
        OkHttpClient client = new OkHttpClient();
        Customer customer = LitePal.where("credential = ?",credential).findFirst(Customer.class);
        String customerId = customer.getAccountId();
        Request request = new Request.Builder().url(address+"?customerId=" + customerId +"&type=" + type).addHeader("Authorization",credential).build();
        client.newCall(request).enqueue(callback);
    }
    //发布新健康播报
    public static void postHealthBroadcastRequest(String address, String userId, String credential, String title, String image, String expireTime, String type, String description, Callback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        HashMap<String, String> map = new HashMap<>();
        Customer customer = LitePal.where("userId = ?",userId).findFirst(Customer.class);
        map.put("creatorId",customer.getAccountId());
        map.put("des",description);
        map.put("expireTime",expireTime);
        map.put("topicClassType",type);
        map.put("topicName",title);
        map.put("topicPic",image);
        String jsonStr = new Gson().toJson(map);
        LogUtil.e("HttpUtil","postHealthBroadcastRequest: "+address + jsonStr);
        RequestBody requestBody = RequestBody.create(jsonStr, JSON);
        Request request = new Request.Builder().url(address).post(requestBody).addHeader("Authorization",credential).build();
        client.newCall(request).enqueue(callback);
    }

    //发布评论
    public static void putCommentRequest(String address, String credential, String content, String targetId, Callback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Customer customer = LitePal.where("credential = ?",credential).findFirst(Customer.class);
        String customerId = customer.getInternetId();
        HashMap<String, String> map = new HashMap<>();
        //map.put("comment",content);
        //map.put("customerId",customerId);
        String jsonStr = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(jsonStr, JSON);

//        MultipartBody.Builder builder = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("comment", content)
//                .addFormDataPart("customerId",customerId);
//        RequestBody requestBody = builder.build();
        LogUtil.e("HttpUtil","putComment:"+address+"/"+targetId+"?comment="+content+"&customerId="+customerId);
        Request request = new Request.Builder().url(address+"/"+targetId+"?comment="+content+"&customerId="+customerId).addHeader("Authorization",credential).put(requestBody).build();
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

    //新建商品订单
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

    //根据经纬度获取员工
    public static void getStoreWorker(String address,String credential, double longitude, double latitude,  Callback callback)
    {
//        OkHttpClient client = buildBasicAuthClient(userID,"123456");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address+"?longitude="+longitude+"&latitude="+latitude).addHeader("Authorization",credential).build();
//        LogUtil.e("Login","111111111   " + request.header("Authorization"));
        client.newCall(request).enqueue(callback);
    }

    //新建服务订单
    public static void postOrderRequest(String address, String credential, String appointedPerson,double longitude, double latitude,String deliveryPhone, String deliveryDetail, String houseNum,
                                        String orderType, String serviceClassId, String serviceSubjectId, long startTime, long endTime,int timeUnit, String message, double tip, Callback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Customer customer = LitePal.where("credential = ?",credential).findFirst(Customer.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("customerId",customer.getInternetId());
        map.put("appointedPerson", appointedPerson);
        map.put("longitude", ""+longitude);
        map.put("latitude", ""+latitude);
        map.put("deliveryPhone",deliveryPhone);
        map.put("deliveryDetail",deliveryDetail);
        map.put("houseNum",houseNum);
        map.put("expectStartTime", ""+startTime);
        map.put("expectEndTime", ""+endTime);
        map.put("timeUnit",""+timeUnit);
        map.put("message",message);
        map.put("orderType",orderType);
        map.put("tip",""+tip);
        map.put("serviceClassId",serviceClassId);
        map.put("serviceSubjectId",serviceSubjectId);
        String jsonStr = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(jsonStr, JSON);
        Request request = new Request.Builder().url(address).addHeader("Authorization",credential).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    //取消订单
    public static void cancelOrderRequest(String address, String credential, String orderId,Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("orderId", orderId)
                .build();
        Request request = new Request.Builder().url(address).put(requestBody).addHeader("Authorization",credential).build();
        client.newCall(request).enqueue(callback);
    }

    //评价订单
    public static void commentOrderRequest(String address, String credential, String orderId, String comment, int score,Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("orderId", orderId)
                .add("customerDes",comment)
                .add("score",String.valueOf(score))
                .build();
        Request request = new Request.Builder().url(address).put(requestBody).addHeader("Authorization",credential).build();
        client.newCall(request).enqueue(callback);
    }

    //支付商品订单
    public static void payProductOrderRequest(String address, String credential, String orderId,Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("orderId", orderId)
                .build();
        Request request = new Request.Builder().url(address).put(requestBody).addHeader("Authorization",credential).build();
        client.newCall(request).enqueue(callback);
    }

    //商品取消订单
    public static void cancelProductOrderRequest(String address, String credential,String orderId, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Customer customer = LitePal.where("credential = ?",credential).findFirst(Customer.class);
        RequestBody requestBody = new FormBody.Builder()
                .add("customerId", customer.getInternetId())
                .add("orderId",orderId)
                .build();
        Request request = new Request.Builder().url(address).put(requestBody).addHeader("Authorization",credential).build();
        client.newCall(request).enqueue(callback);
    }

    //商品申请退款
    public static void refundProductOrderRequest(String address, String credential,String orderId, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Customer customer = LitePal.where("credential = ?",credential).findFirst(Customer.class);
        RequestBody requestBody = new FormBody.Builder()
                .add("customerId", customer.getInternetId())
                .add("orderId",orderId)
                .build();
        Request request = new Request.Builder().url(address).put(requestBody).addHeader("Authorization",credential).build();
        client.newCall(request).enqueue(callback);
    }

    //商品订单收货
    public static void receiveProductOrderRequest(String address, String credential,Callback callback){
        OkHttpClient client = new OkHttpClient();
        Customer customer = LitePal.where("credential = ?",credential).findFirst(Customer.class);
        RequestBody requestBody = new FormBody.Builder()
                .add("customerId", customer.getInternetId())
                .build();
        Request request = new Request.Builder().url(address).put(requestBody).addHeader("Authorization",credential).build();
        client.newCall(request).enqueue(callback);
    }

    //商品订单评价
    public static void commentProductOrderRequest(String address, String credential, String comment, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Customer customer = LitePal.where("credential = ?",credential).findFirst(Customer.class);
        RequestBody requestBody = new FormBody.Builder()
                .add("customerId", customer.getInternetId())
                .add("comment",comment)
                .build();
        Request request = new Request.Builder().url(address).put(requestBody).addHeader("Authorization",credential).build();
        client.newCall(request).enqueue(callback);
    }
}
