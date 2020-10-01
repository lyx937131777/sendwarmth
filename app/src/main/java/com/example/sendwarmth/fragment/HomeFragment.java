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
import com.example.sendwarmth.adapter.ServiceClassAdapter;
import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.db.ServiceClass;
import com.example.sendwarmth.presenter.HomePresenter;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment
{
    private Toolbar toolbar;

    private EditText searchText;

    private RecyclerView serviceClassRecyler;
//    private Menu[]menus = {new Menu("lifeCare",R.drawable.life_care,"生活护理"),
//            new Menu("medicalService",R.drawable.medical_service,"医疗服务"),
//            new Menu("propertyMaintenance",R.drawable.property_maintenance,"物业维修"),
//            new Menu("housekeeping",R.drawable.housekeeping,"家政保洁"),
//            new Menu("accompanyService",R.drawable.accompany_service,"陪同服务"),
//            new Menu("purchasingAgent",R.drawable.purchasing_agent,"跑腿代购"),
//            new Menu("customService",R.drawable.custom_service,"定制服务"),
//            new Menu("allService",R.drawable.all_service,"全部服务")};
    private List<ServiceClass> serviceClassList = new ArrayList<>();
    private ServiceClassAdapter serviceClassAdapter;

    private HomePresenter homePresenter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        toolbar = root.findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        searchText = root.findViewById(R.id.search_edit);

        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(getContext())).build();
        homePresenter = myComponent.homePresenter();

        initServiceClass();
        serviceClassRecyler = root.findViewById(R.id.recycler_service_class);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        serviceClassRecyler.setLayoutManager(layoutManager);
        serviceClassAdapter = new ServiceClassAdapter(serviceClassList);
        serviceClassRecyler.setAdapter(serviceClassAdapter);

        return root;
    }

    private void initServiceClass()
    {
        serviceClassList = LitePal.order("clickCount desc").limit(7).find(ServiceClass.class);
        ServiceClass serviceClass = new ServiceClass();
        serviceClass.setName("全部服务");
        serviceClassList.add(serviceClass);
//        homePresenter.updateServiceClass(serviceClassAdapter);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        homePresenter.updateServiceClass(serviceClassAdapter);
    }

    public Toolbar getToolbar(){
        return toolbar;
    }
}