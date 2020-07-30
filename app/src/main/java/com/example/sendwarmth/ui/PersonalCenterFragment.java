package com.example.sendwarmth.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import com.example.sendwarmth.MyInformationActivity;
import com.example.sendwarmth.R;
import com.example.sendwarmth.adapter.MenuAdapter;
import com.example.sendwarmth.db.Menu;

import java.util.ArrayList;
import java.util.List;

public class PersonalCenterFragment extends Fragment implements View.OnClickListener
{
    private RecyclerView myOrderMenuRecycler;
    private Menu[] myOrderMenus = {new Menu("toBePaid",R.drawable.to_be_paid,"待付款"),
            new Menu("toBeReceived",R.drawable.to_be_received,"待收货"),
            new Menu("toBeEvaluated",R.drawable.to_be_evaluated,"待评价"),
            new Menu("completed",R.drawable.completed,"已完成")};
    private List<Menu> myOrderMenuList = new ArrayList<>();
    private MenuAdapter myOrderMenuAdapter;

    private RecyclerView mMenuRecycler;
    private Menu[] mMenus = {new Menu("myInformation",R.drawable.my_information,"我的信息"),
            new Menu("myCommunity",R.drawable.my_evaluate,"我的社区"),
            new Menu("myActivity",R.drawable.my_star,"我的活动"),
            new Menu("myHealth",R.drawable.my_message,"我的健康"),
            new Menu("myAddress",R.drawable.system_message,"收货地址"),
            new Menu("feedback",R.drawable.feedback,"意见反馈"),
            new Menu("customService",R.drawable.my_business_card,"定制服务"),
            new Menu("hotline",R.drawable.hotline,"热线电话")};
    private List<Menu> mMenuList = new ArrayList<>();
    private MenuAdapter mMenuAdapter;

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
        TextView nickname = root.findViewById(R.id.nickname);
        TextView level = root.findViewById(R.id.level);
        profile.setOnClickListener(this);
        nickname.setOnClickListener(this);
        level.setOnClickListener(this);

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
                startActivity(intent);
                break;
            }
        }
    }
}