package com.example.sendwarmth.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sendwarmth.R;
import com.example.sendwarmth.adapter.OrderAdapter;
import com.example.sendwarmth.db.Order;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderFragment extends Fragment
{
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int index;

    private RecyclerView recyclerView;
//    private Order[] orders = {
//            new Order("013","张小花","李思思","2020-08-07 14:00~15:00","toBePaid","","修理钟表",40),
//            new Order("012","张三","张小琴","2020-08-07 10:00~14:00","moving","","理发",50),
//            new Order("011","张三","张小琴","2020-08-07 10:00~14:00","arrived","","理发",50),
//            new Order("010","周五","李思思","2020-08-07 12:00~14:00","unstart","","护理",50),
//            new Order("009","李四","张小琴","2020-08-07 13:00~15:00","unstart","","修锁",20),
//            new Order("008","小红","未接单","2020-08-07 12:00~14:00","waiting","","打扫卫生",50),
//            new Order("007","小明","未接单","2020-08-07 11:00~12:00","waiting","","按摩",30),
//            new Order("006","小王","李思思","2020-08-06 15:00~16:00","toBeEvaluated","","陪聊",50),
//            new Order("005","小白","李思思","2020-08-06 13:00~15:00","toBeEvaluated","","修空调",60),
//            new Order("004","小李","李思思","2020-08-07 12:00~14:00","canceled","","修锁",20),
//            new Order("003","小许","未接单","2020-08-06 15:00~16:00","canceled","","裁缝",30),
//            new Order("002","小徐","李思思","2020-08-06 14:00~15:00","completed","","打扫卫生",50),
//            new Order("001","小黑","张小琴","2020-08-06 11:00~13:00","completed","","理发",60)};
    private List<Order> orderList = new ArrayList<>();
    private OrderAdapter orderAdapter;


    public static OrderFragment newInstance(int index)
    {
        OrderFragment orderFragment = new OrderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER,index);
        orderFragment.setArguments(bundle);
        return orderFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        index = 0;
        if (getArguments() != null)
        {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_order, container, false);

        initOrders();
        recyclerView = root.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderAdapter = new OrderAdapter(orderList);
        recyclerView.setAdapter(orderAdapter);
        return root;
    }

    private void initOrders()
    {
        String[] types = getTypes();
        List<String> typeList = new ArrayList<>();
        for(int i = 0; i < types.length; i++){
            typeList.add(types[i]);
        }
//        for(int i = 0; i < orders.length; i++){
//            if(typeList.contains(orders[i].getState())){
//                orderList.add(orders[i]);
//            }
//        }
    }

    private String getType(){
        switch (index){
            case 0:
                return "all";
            case 1:
                return "toBePaid";
            case 2:
                return "toBeReceived";
            case 3:
                return "toBeEvaluated";
            case 4:
                return "closed";
            default:
                return "unknown";
        }
    }

    private String[] getTypes(){
        switch (index){
            case 0:
                return new String[]{"toBePaid","waiting","unstart","moving","arrived","toBeEvaluated","canceled","completed"};
            case 1:
                return new String[]{"toBePaid"};
            case 2:
                return new String[]{"waiting","unstart","moving","arrived"};
            case 3:
                return new String[]{"toBeEvaluated"};
            case 4:
                return new String[]{"canceled","completed"};
            default:
                return new String[]{};
        }
    }

}
