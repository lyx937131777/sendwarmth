package com.example.sendwarmth.util;

import com.example.sendwarmth.R;

import java.util.HashMap;
import java.util.Map;

public class MapUtil
{
    private static Map<String,Integer> tipMap = new HashMap<>();
    private static Map<String,Integer> stateMap =new HashMap<>();
    private static Map<String,String> orderStateMap = new HashMap<>();

    static{
        tipMap.put("无",0);
        tipMap.put("5元",5);
        tipMap.put("10元",10);
        tipMap.put("20元",20);

        //toBePaid -> 付钱 -> waiting -> 接单 -> unstart -> 取消 -> canceled
        //                                              -> 开始 -> running -> 结束 -> toBeEvaluated -> 评价 -> completed
        stateMap.put("running", R.drawable.state_yellow);
        stateMap.put("unstart", R.drawable.state_blue);
        stateMap.put("canceled",R.drawable.state_red);
        stateMap.put("completed",R.drawable.state_green);
        stateMap.put("toBePaid",R.drawable.state_yellow);
        stateMap.put("waiting",R.drawable.state_green);
        stateMap.put("toBeEvaluated",R.drawable.state_blue);

        orderStateMap.put("running","进行中");
        orderStateMap.put("unstart","未开始");
        orderStateMap.put("canceled","已取消");
        orderStateMap.put("completed","已完成");
        orderStateMap.put("toBePaid","待付款");
        orderStateMap.put("waiting","等待中");
        orderStateMap.put("toBeEvaluated","待评价");
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
}
