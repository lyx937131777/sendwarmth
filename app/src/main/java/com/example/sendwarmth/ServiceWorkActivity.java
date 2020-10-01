package com.example.sendwarmth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.sendwarmth.adapter.ServiceSubjectAdapter;
import com.example.sendwarmth.db.ServiceClass;
import com.example.sendwarmth.db.ServiceSubject;
import com.example.sendwarmth.fragment.adapter.OrderPagerAdapter;
import com.example.sendwarmth.fragment.adapter.ServiceWorkPagerAdapter;
import com.example.sendwarmth.util.LogUtil;
import com.google.android.material.tabs.TabLayout;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class ServiceWorkActivity extends AppCompatActivity
{
    private ServiceSubject[] serviceSubjects = {new ServiceSubject("业务名称1","propertyMaintenance","相关描述xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",50,"次",R.drawable.temp),
            new ServiceSubject("业务名称2","propertyMaintenance","相关描述xxxxxxxxxxxx",10,"小时",R.drawable.temp),
            new ServiceSubject("业务名称3","propertyMaintenance","相关描述xxxxxxxxxxxxxxxxxxxxx",50,"次",R.drawable.temp)};
    private List<ServiceSubject> serviceSubjectList = new ArrayList<>();
    private ServiceSubjectAdapter serviceSubjectAdapter;
    private RecyclerView serviceSubjectRecycler;

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

        ViewPager viewPager = findViewById(R.id.view_pager);
        List<ServiceClass> serviceClassList = LitePal.order("clickCount desc").find(ServiceClass.class);
        ServiceClass serviceClass = new ServiceClass();
        serviceClass.setName("全部服务");
        serviceClassList.add(0,serviceClass);
        ServiceWorkPagerAdapter serviceWorkPagerAdapter = new ServiceWorkPagerAdapter(this,getSupportFragmentManager(),serviceClassList);
        viewPager.setAdapter(serviceWorkPagerAdapter);
        String serviceClassName = getIntent().getStringExtra("serviceClassName");
        int index = 0;
        for(;index < serviceClassList.size(); index++){
            if(serviceClassList.get(index).getName().equals(serviceClassName)){
                break;
            }
        }
        LogUtil.e("ServiceWorkActivity","index: " + index);
        viewPager.setCurrentItem(index);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


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