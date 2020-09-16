package com.example.sendwarmth.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import com.example.sendwarmth.MyInformationActivity;
import com.example.sendwarmth.OrderActivity;
import com.example.sendwarmth.R;
import com.example.sendwarmth.adapter.MenuAdapter;
import com.example.sendwarmth.db.Customer;
import com.example.sendwarmth.db.Menu;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.Utility;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PersonalCenterFragment extends Fragment implements View.OnClickListener
{
    private RecyclerView myOrderMenuRecycler;
    private Menu[] myOrderMenus = {new Menu("toBePaid",R.drawable.to_be_paid,"待付款"),
            new Menu("toBeReceived",R.drawable.to_be_received,"待收货"),
            new Menu("toBeEvaluated",R.drawable.to_be_evaluated,"待评价"),
            new Menu("closed",R.drawable.completed,"已结束")};
    private List<Menu> myOrderMenuList = new ArrayList<>();
    private MenuAdapter myOrderMenuAdapter;

    private RecyclerView mMenuRecycler;
    private Menu[] mMenus = {new Menu("myInformation",R.drawable.my_information,"我的信息"),
            new Menu("myCommunity",R.drawable.my_community,"我的社区"),
            new Menu("myActivity",R.drawable.my_activity,"我的活动"),
            new Menu("myHealth",R.drawable.my_health,"我的健康"),
            new Menu("myAddress",R.drawable.my_address,"收货地址"),
            new Menu("feedback",R.drawable.feedback,"意见反馈"),
            new Menu("customService",R.drawable.custom_service2,"定制服务"),
            new Menu("hotline",R.drawable.hotline,"热线电话")};
    private List<Menu> mMenuList = new ArrayList<>();
    private MenuAdapter mMenuAdapter;

    private View allOrder;

    private Customer customer;
    private SharedPreferences pref;
    private String credential;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_personal_center, container, false);

        initMenus();
        myOrderMenuRecycler = root.findViewById(R.id.recycler_menu_my_order);
        GridLayoutManager layoutManager1 = new GridLayoutManager(getContext(), 4);
        myOrderMenuRecycler.setLayoutManager(layoutManager1);
        myOrderMenuAdapter = new MenuAdapter(myOrderMenuList);
        myOrderMenuRecycler.setAdapter(myOrderMenuAdapter);

        mMenuRecycler = root.findViewById(R.id.recycler_menu_me);
        GridLayoutManager layoutManager2 = new GridLayoutManager(getContext(), 4);
        mMenuRecycler.setLayoutManager(layoutManager2);
        mMenuAdapter = new MenuAdapter(mMenuList);
        mMenuRecycler.setAdapter(mMenuAdapter);

        CircleImageView profile = root.findViewById(R.id.profile);
        final TextView userName = root.findViewById(R.id.user_name);
        final TextView level = root.findViewById(R.id.level);

        profile.setOnClickListener(this);
        userName.setOnClickListener(this);
        level.setOnClickListener(this);

        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        credential = pref.getString("credential","");
        String address = HttpUtil.LocalAddress + "/api/users/me";
        HttpUtil.getHttp(address, credential, new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                final String responsData = response.body().string();
                customer = Utility.handleCustomer(responsData);
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        userName.setText(customer.getUserName());
                        level.setText("Lv "+customer.getMemberLevel());
                    }
                });
            }
        });


        allOrder = root.findViewById(R.id.all_orders);
        allOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getContext(), OrderActivity.class);
                intent.putExtra("index",0);
                startActivity(intent);
            }
        });

        return root;
    }

    private void initMenus()
    {
        myOrderMenuList.clear();
        for(int i = 0; i < myOrderMenus.length; i++) {
            myOrderMenus[i].setType("order");
            myOrderMenuList.add(myOrderMenus[i]);
        }

        mMenuList.clear();
        for(int i = 0; i < mMenus.length; i++){
            mMenus[i].setType("information");
            mMenuList.add(mMenus[i]);
        }

    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId()){
            case R.id.profile:
            case R.id.nickname:
            case R.id.level: {
                Intent intent = new Intent(getContext(), MyInformationActivity.class);
                intent.putExtra("customer",customer);
                startActivity(intent);
                break;
            }
        }
    }
}