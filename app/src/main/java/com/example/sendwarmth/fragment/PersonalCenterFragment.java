package com.example.sendwarmth.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import com.example.sendwarmth.MyInformationActivity;
import com.example.sendwarmth.OrderActivity;
import com.example.sendwarmth.ProductOrderActivity;
import com.example.sendwarmth.R;
import com.example.sendwarmth.SettingActivity;
import com.example.sendwarmth.adapter.MenuAdapter;
import com.example.sendwarmth.db.Customer;
import com.example.sendwarmth.db.Menu;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class PersonalCenterFragment extends Fragment implements View.OnClickListener
{
    private RecyclerView serviceOrderMenuRecycler;
    private Menu[] serviceOrderMenus = {new Menu("toBeAccept",R.drawable.to_be_paid,"待接单"),
            new Menu("accepted",R.drawable.to_be_received,"已接单"),
            new Menu("toBeEvaluated",R.drawable.to_be_evaluated,"待评价"),
            new Menu("closed",R.drawable.completed,"已结束")};
    private List<Menu> serviceOrderMenuList = new ArrayList<>();
    private MenuAdapter serviceOrderMenuAdapter;

    private RecyclerView productOrderMenuRecycler;
    private Menu[] productOrderMenus = {new Menu("toBePaid",R.drawable.to_be_paid,"待付款"),
            new Menu("toBeReceived",R.drawable.to_be_received,"待收货"),
            new Menu("toBeEvaluated",R.drawable.to_be_evaluated,"待评价"),
            new Menu("closed",R.drawable.completed,"已结束")};
    private List<Menu> productOrderMenuList = new ArrayList<>();
    private MenuAdapter productOrderMenuAdapter;

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

    private View allServiceOrder;
    private View allProductOrder;

    private CircleImageView profile;
    private TextView userName;
    private TextView level;
    private ImageView setting;

    private Customer customer;
    private SharedPreferences pref;
    private String credential;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_personal_center, container, false);

        initMenus();
        serviceOrderMenuRecycler = root.findViewById(R.id.recycler_menu_service_order);
        serviceOrderMenuRecycler.setLayoutManager(new GridLayoutManager(getContext(), 4));
        serviceOrderMenuAdapter = new MenuAdapter(serviceOrderMenuList);
        serviceOrderMenuRecycler.setAdapter(serviceOrderMenuAdapter);

        productOrderMenuRecycler = root.findViewById(R.id.recycler_menu_produc_order);
        productOrderMenuRecycler.setLayoutManager(new GridLayoutManager(getContext(),4));
        productOrderMenuAdapter = new MenuAdapter(productOrderMenuList);
        productOrderMenuRecycler.setAdapter(productOrderMenuAdapter);

        mMenuRecycler = root.findViewById(R.id.recycler_menu_me);
        mMenuRecycler.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mMenuAdapter = new MenuAdapter(mMenuList);
        mMenuRecycler.setAdapter(mMenuAdapter);

        profile = root.findViewById(R.id.profile);
        userName = root.findViewById(R.id.user_name);
        level = root.findViewById(R.id.level);
        setting = root.findViewById(R.id.setting);

        profile.setOnClickListener(this);
        userName.setOnClickListener(this);
        level.setOnClickListener(this);
        setting.setOnClickListener(this);

        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        credential = pref.getString("credential","");
        customer = LitePal.where("credential = ?",credential).findFirst(Customer.class);
        userName.setText(customer.getUserNameWithRole());
        level.setText("Lv "+customer.getMemberLevel());


        allServiceOrder = root.findViewById(R.id.all_service_orders);
        allServiceOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getContext(), OrderActivity.class);
                intent.putExtra("index",0);
                startActivity(intent);
            }
        });

        allProductOrder = root.findViewById(R.id.all_produc_orders);
        allProductOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getContext(), ProductOrderActivity.class);
                intent.putExtra("index",0);
                startActivity(intent);
            }
        });

        return root;
    }

    private void initMenus()
    {
        serviceOrderMenuList.clear();
        for(int i = 0; i < serviceOrderMenus.length; i++) {
            serviceOrderMenus[i].setType("serviceOrder");
            serviceOrderMenuList.add(serviceOrderMenus[i]);
        }

        productOrderMenuList.clear();
        for(int i = 0; i < productOrderMenus.length; i++){
            productOrderMenus[i].setType("productOrder");
            productOrderMenuList.add(productOrderMenus[i]);
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
            case R.id.setting:{
                Intent intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
            }
        }
    }
}