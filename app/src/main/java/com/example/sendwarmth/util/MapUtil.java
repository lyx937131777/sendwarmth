package com.example.sendwarmth.util;

import com.example.sendwarmth.R;

import java.util.HashMap;
import java.util.Map;

public class MapUtil
{
    private static Map<String,Integer> tipMap = new HashMap<>();
    private static Map<String,Integer> stateMap =new HashMap<>();
    private static Map<String,Integer> productStateMap = new HashMap<>();
    private static Map<String,String> orderStateMap = new HashMap<>();
    private static Map<String,String> productOrderStateMap = new HashMap<>();
    private static Map<String,String> roleMap = new HashMap<>();
    private static Map<String,String> orderTypeMap = new HashMap<>();
    private static Map<String,String> topicTypeMap = new HashMap<>();
    private static Map<String,String> attributeMap = new HashMap<>();
    private static Map<String,String> genderMap = new HashMap<>();
    private static Map<String,String> relationshipMap = new HashMap<>();

    static{
        tipMap.put("无",0);
        tipMap.put("5元",5);
        tipMap.put("10元",10);
        tipMap.put("20元",20);

        stateMap.put("on_going", R.drawable.state_blue);
        stateMap.put("not_start", R.drawable.state_yellow);
        stateMap.put("canceled",R.drawable.state_red);
        stateMap.put("completed",R.drawable.state_green);
        stateMap.put("un_evaluated",R.drawable.state_yellow);
        stateMap.put("not_accepted",R.drawable.state_red);

        orderStateMap.put("on_going","进行中");
        orderStateMap.put("not_start","未开始");
        orderStateMap.put("canceled","已取消");
        orderStateMap.put("completed","已完成");
        orderStateMap.put("un_evaluated","待评价");
        orderStateMap.put("not_accepted","待接单");

        productStateMap.put("un_paid",R.drawable.state_red);
        productStateMap.put("paid",R.drawable.state_green);
        productStateMap.put("delivered",R.drawable.state_yellow);
        productStateMap.put("received",R.drawable.state_blue);
        productStateMap.put("cancel",R.drawable.state_red);
        productStateMap.put("evaluated",R.drawable.state_green);
        productStateMap.put("refund_request",R.drawable.state_blue);
        productStateMap.put("refunded",R.drawable.state_yellow);

        productOrderStateMap.put("un_paid","待付款");
        productOrderStateMap.put("paid","待发货");
        productOrderStateMap.put("delivered","待收货");
        productOrderStateMap.put("received","待评价");
        productOrderStateMap.put("cancel","已取消");
        productOrderStateMap.put("evaluated","已结束");
        productOrderStateMap.put("refund_request","退款中");
        productOrderStateMap.put("refunded","已退款");


        roleMap.put("角色","notSelected");
        roleMap.put("普通用户","customer");
        roleMap.put("专家","expert");

        orderTypeMap.put("预约订单","book_order");
        orderTypeMap.put("加急订单","expedited_order");
        orderTypeMap.put("book_order","预约订单");
        orderTypeMap.put("expedited_order","加急订单");

        topicTypeMap.put("新闻","news");
        topicTypeMap.put("养生","health_care");
        topicTypeMap.put("定期话题","regular_topic");

        attributeMap.put("customerName","姓名");
        attributeMap.put("customerAddress","地址");
        attributeMap.put("houseNum","门牌号");
        attributeMap.put("personalDescription","个人描述");
        attributeMap.put("idNumber","身份证号");
        attributeMap.put("gender","性别");
        attributeMap.put("contactName","紧急联系人");
        attributeMap.put("contactPhone","紧急联系人电话");
        attributeMap.put("relationship","紧急联系人关系");
        attributeMap.put("commonDiseases","常见病");

        genderMap.put("male","男");
        genderMap.put("female","女");

        relationshipMap.put("children","子女");
        relationshipMap.put("relatives","亲属");
        relationshipMap.put("friends","朋友");
    }

    public static int getTip(String s){
        return tipMap.get(s);
    }

    public static int getState(String s){
        return stateMap.get(s);
    }

    public static int getProductState(String s){
        return productStateMap.get(s);
    }
    public static String getOrderState(String s){
        return orderStateMap.get(s);
    }

    public static String getProductOrderState(String s){
        return productOrderStateMap.get(s);
    }
    public static String getRole(String s){
        return roleMap.get(s);
    }

    public static String getOrderType(String s){
        return orderTypeMap.get(s);
    }

    public static String getTopicType(String s){
        return topicTypeMap.get(s);
    }

    public static String getAttribute(String s){
        return attributeMap.get(s);
    }

    public static String getGender(String s){
        return genderMap.get(s);
    }

    public static String getRelationship(String s){
        return relationshipMap.get(s);
    }
}
