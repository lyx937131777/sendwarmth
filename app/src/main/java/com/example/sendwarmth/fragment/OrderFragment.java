package com.example.sendwarmth.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sendwarmth.R;
import com.example.sendwarmth.adapter.OrderAdapter;
import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.db.Order;
import com.example.sendwarmth.presenter.OrderPresenter;

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
    private List<Order> orderList = new ArrayList<>();
    private OrderAdapter orderAdapter;

    private OrderPresenter orderPresenter;

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
        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(getContext())).build();
        orderPresenter = myComponent.orderPresenter();

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

    @Override
    public void onStart()
    {
        super.onStart();
        orderPresenter.updateOrderList(orderAdapter,getTypes());
    }

    private String getType(){
        switch (index){
            case 0:
                return "all";
            case 1:
                return "toBeAccept";
            case 2:
                return "Accepted";
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
                return new String[]{"not_accepted","not_start","on_going","un_evaluated","canceled","completed"};
            case 1:
                return new String[]{"not_accepted"};
            case 2:
                return new String[]{"not_start","on_going"};
            case 3:
                return new String[]{"un_evaluated"};
            case 4:
                return new String[]{"canceled","completed"};
            default:
                return new String[]{};
        }
    }

}
