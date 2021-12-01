package com.example.sendwarmth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.sendwarmth.fragment.ProductOrderFragment;
import com.example.sendwarmth.fragment.adapter.OrderPagerAdapter;
import com.example.sendwarmth.fragment.adapter.ProductOrderPagerAdapter;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.wxapi.WXPayEntryActivity;
import com.google.android.material.tabs.TabLayout;

public class ProductOrderActivity extends AppCompatActivity
{
    private ViewPager viewPager;
    private ProductOrderPagerAdapter productOrderPagerAdapter;

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

        viewPager = findViewById(R.id.view_pager);
        productOrderPagerAdapter = new ProductOrderPagerAdapter(this,getSupportFragmentManager());
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

    public void refresh(){
        LogUtil.e("ProductOrderActivity","refresh!");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        LogUtil.e("ProductOrderActivity","onResume");
        if(WXPayEntryActivity.isSuccess()){
            ProductOrderFragment productOrderFragment = (ProductOrderFragment) productOrderPagerAdapter.getFragment(viewPager.getCurrentItem());
            productOrderFragment.refresh();
        }else {
            LogUtil.e("ProductOrderActivity","WXnotSuccess");
        }
    }
}