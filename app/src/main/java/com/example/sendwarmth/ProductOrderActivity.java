package com.example.sendwarmth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.sendwarmth.fragment.adapter.OrderPagerAdapter;
import com.example.sendwarmth.fragment.adapter.ProductOrderPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class ProductOrderActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_order);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ViewPager viewPager = findViewById(R.id.view_pager);
        ProductOrderPagerAdapter productOrderPagerAdapter = new ProductOrderPagerAdapter(this,getSupportFragmentManager());
        viewPager.setAdapter(productOrderPagerAdapter);
        viewPager.setCurrentItem(getIntent().getIntExtra("index",0));
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