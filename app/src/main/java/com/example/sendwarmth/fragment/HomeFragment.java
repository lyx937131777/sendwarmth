package com.example.sendwarmth.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sendwarmth.MainActivity;
import com.example.sendwarmth.R;
import com.example.sendwarmth.ServiceWorkSearchActivity;
import com.example.sendwarmth.adapter.RecommendServiceSubjectAdapter;
import com.example.sendwarmth.adapter.ServiceClassAdapter;
import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.db.ServiceClass;
import com.example.sendwarmth.db.ServiceSubject;
import com.example.sendwarmth.presenter.HomePresenter;
import com.example.sendwarmth.util.LogUtil;
import com.sun.banner.BannerAdapter;
import com.sun.banner.BannerScroller;
import com.sun.banner.BannerView;
import com.sun.banner.BannerViewPager;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment
{
    private Toolbar toolbar;

    private EditText searchText;

    private BannerView bannerView;

    private RecyclerView serviceClassRecycler;
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

    private RecyclerView recommendServiceSubjectRecycler;
    private List<ServiceSubject> serviceSubjectList = new ArrayList<>();
    private RecommendServiceSubjectAdapter recommendServiceSubjectAdapter;

    private HomePresenter homePresenter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        toolbar = root.findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        searchText = root.findViewById(R.id.search_edit);

        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(getContext())).build();
        homePresenter = myComponent.homePresenter();

        bannerView = root.findViewById(R.id.banner);
        homePresenter.updateCarousel(bannerView);

        initServiceClass();
        serviceClassRecycler = root.findViewById(R.id.recycler_service_class);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        serviceClassRecycler.setLayoutManager(layoutManager);
        serviceClassAdapter = new ServiceClassAdapter(serviceClassList);
        serviceClassRecycler.setAdapter(serviceClassAdapter);

        recommendServiceSubjectRecycler = root.findViewById(R.id.recycler_recommend_service_subject);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recommendServiceSubjectRecycler.setLayoutManager(linearLayoutManager);
        recommendServiceSubjectAdapter = new RecommendServiceSubjectAdapter(serviceSubjectList);
        recommendServiceSubjectRecycler.setAdapter(recommendServiceSubjectAdapter);

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
        homePresenter.updateRecommendServiceSubject(recommendServiceSubjectAdapter);
    }

    public Toolbar getToolbar(){
        return toolbar;
    }

    public void search(){
        String keyword = searchText.getText().toString();
        if(keyword.equals("")){
            new AlertDialog.Builder(getContext())
                    .setTitle("提示")
                    .setMessage("请输入关键词")
                    .setPositiveButton("确定", null)
                    .show();
        }else {
            Intent intent = new Intent(getContext(), ServiceWorkSearchActivity.class);
            intent.putExtra("keyword",keyword);
            startActivity(intent);
        }
    }
}