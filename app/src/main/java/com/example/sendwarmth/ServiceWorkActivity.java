package com.example.sendwarmth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import q.rorbin.verticaltablayout.VerticalTabLayout;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.sendwarmth.adapter.ServiceClassRightAdapter;
import com.example.sendwarmth.adapter.ServiceClassTabAdapter;
import com.example.sendwarmth.adapter.ServiceSubjectAdapter;
import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.db.ServiceClass;
import com.example.sendwarmth.db.ServiceSubject;
import com.example.sendwarmth.fragment.adapter.OrderPagerAdapter;
import com.example.sendwarmth.presenter.ServiceWorkPresenter;
import com.example.sendwarmth.util.LogUtil;
import com.google.android.material.tabs.TabLayout;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class ServiceWorkActivity extends AppCompatActivity
{
    private List<ServiceClass> serviceClassList = new ArrayList<>();
    private ServiceClassTabAdapter serviceClassTabAdapter;
    private RecyclerView serviceClassTabRecycler;

    private ServiceClassRightAdapter serviceClassRightAdapter;
    private RecyclerView serviceClassRightRecycler;

    private String selectedServiceClassName;

    private ServiceWorkPresenter serviceWorkPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_work);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(this)).build();
        serviceWorkPresenter = myComponent.serviceWorkPresenter();

        initServiceClass();
        serviceClassTabRecycler = findViewById(R.id.recycler_tab);
        serviceClassTabRecycler.setLayoutManager(new LinearLayoutManager(this));
        serviceClassTabAdapter = new ServiceClassTabAdapter(serviceClassList);
        serviceClassTabRecycler.setAdapter(serviceClassTabAdapter);

        serviceClassRightRecycler = findViewById(R.id.recycler_service_class_right);
        serviceClassRightRecycler.setLayoutManager(new LinearLayoutManager(this));
        serviceClassRightAdapter = new ServiceClassRightAdapter(serviceClassList);
        serviceClassRightRecycler.setAdapter(serviceClassRightAdapter);

        serviceClassRightRecycler.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                LogUtil.e("ServiceWorkActivity","x: "  + dx + "  y: " + dy);
                if(dy == 0){
                    return;
                }
                int position = ((LinearLayoutManager)(recyclerView.getLayoutManager())).findFirstVisibleItemPosition();

                selectedServiceClassName = serviceClassList.get(position).getName();
                LogUtil.e("ServiceWorkActivity", position+": "+selectedServiceClassName);
                serviceClassTabAdapter.setSelectedName(selectedServiceClassName);
                serviceClassTabAdapter.notifyDataSetChanged();
            }
        });

        serviceWorkPresenter.updateServiceSubject(serviceClassRightAdapter);
    }

    private void initServiceClass()
    {
        serviceClassList.clear();
        serviceClassList = LitePal.order("clickCount desc").find(ServiceClass.class);
    }

    public void scrollRightRecyclerTo(int position){
        ((LinearLayoutManager)(serviceClassRightRecycler.getLayoutManager())).scrollToPositionWithOffset(position,0);
//        serviceClassRightRecycler.scroll(position);
    }

    public void scrollToInitialState(){
        selectedServiceClassName = getIntent().getStringExtra("serviceClassName");
        if(!selectedServiceClassName.equals("全部服务")){
            serviceClassTabAdapter.setSelectedName(selectedServiceClassName);
            serviceClassTabAdapter.notifyDataSetChanged();
        }
        for(ServiceClass serviceClass : serviceClassList){
            if(serviceClass.getName().equals(selectedServiceClassName)){
                int position = serviceClassList.indexOf(serviceClass);
                LogUtil.e("ServiceWOrkActivity", "初始 "+ position + " : " + selectedServiceClassName);
                scrollRightRecyclerTo(position);
            }
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
//        serviceWorkPresenter.updateServiceSubject(serviceClassRightAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

}