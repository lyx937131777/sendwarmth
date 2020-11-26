package com.example.sendwarmth.util;

import com.example.sendwarmth.R;

import java.util.HashMap;
import java.util.Map;

public class MapUtil
{
    private static Map<String,Integer> tipMap = new HashMap<>();
    private static Map<String,Integer> stateMap =new HashMap<>();
    private static Map<String,String> orderStateMap = new HashMap<>();
    private static Map<String,String> roleMap = new HashMap<>();
    private static Map<String,String> orderType = new HashMap<>();

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


        roleMap.put("角色","notSelected");
        roleMap.put("普通用户","customer");
        roleMap.put("专家","expert");

        orderType.put("预约订单","book_order");
        orderType.put("加急订单","expedited_order");
        orderType.put("book_order","预约订单");
        orderType.put("expedited_order","加急订单");
    }

    public static int getTip(String s){
        return tipMap.get(s);
    }

    public static int getState(String s){
        return stateMap.get(s);
    }

    public static String getOrderState(String s){
        return orderStateMap.get(s);
    }

    public static String getRole(String s){
        return roleMap.get(s);
    }

    public static String getOrderType(String s){
        return orderType.get(s);
    }
}
