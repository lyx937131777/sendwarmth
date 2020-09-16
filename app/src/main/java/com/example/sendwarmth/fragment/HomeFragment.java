package com.example.sendwarmth.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sendwarmth.MainActivity;
import com.example.sendwarmth.R;
import com.example.sendwarmth.adapter.MenuAdapter;
import com.example.sendwarmth.db.Menu;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment
{
    private Toolbar toolbar;

    private EditText searchText;

    private RecyclerView menuRecyler;
    private Menu[]menus = {new Menu("lifeCare",R.drawable.life_care,"生活护理"),
            new Menu("medicalService",R.drawable.medical_service,"医疗服务"),
            new Menu("propertyMaintenance",R.drawable.property_maintenance,"物业维修"),
            new Menu("housekeeping",R.drawable.housekeeping,"家政保洁"),
            new Menu("accompanyService",R.drawable.accompany_service,"陪同服务"),
            new Menu("purchasingAgent",R.drawable.purchasing_agent,"跑腿代购"),
            new Menu("customService",R.drawable.custom_service,"定制服务"),
            new Menu("allService",R.drawable.all_service,"全部服务")};
    private List<Menu> menuList = new ArrayList<>();
    private MenuAdapter menuAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        toolbar = root.findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        searchText = root.findViewById(R.id.search_edit);

        initMenus();
        menuRecyler = root.findViewById(R.id.recycler_menu);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        menuRecyler.setLayoutManager(layoutManager);
        menuAdapter = new MenuAdapter(menuList);
        menuRecyler.setAdapter(menuAdapter);

        return root;
    }


    private void initMenus()
    {
        menuList.clear();
        for(int i = 0; i < menus.length; i++) {
            menus[i].setType("ServiceWork");
            menuList.add(menus[i]);
        }

    }

    public Toolbar getToolbar(){
        return toolbar;
    }
}