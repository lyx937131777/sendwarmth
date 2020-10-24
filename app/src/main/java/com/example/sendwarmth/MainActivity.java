package com.example.sendwarmth;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sendwarmth.fragment.ShoppingMallFragment;
import com.example.sendwarmth.fragment.CommunityFragment;
import com.example.sendwarmth.fragment.HomeFragment;
import com.example.sendwarmth.fragment.PersonalCenterFragment;
import com.example.sendwarmth.fragment.adapter.MyFragAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity
{

    private ViewPager viewPager;
    private List<Fragment> listFragment;
    private HomeFragment homeFragment;
    private CommunityFragment communityFragment;
    private ShoppingMallFragment shoppingMallFragment;
    private PersonalCenterFragment personalCenterFragment;
    private BottomNavigationView navView;

    public static MainActivity instance = null;

    private int viewPagerSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;

        navView = findViewById(R.id.nav_view);
        viewPager = findViewById(R.id.view_pager);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        listFragment = new ArrayList<>();
        homeFragment = new HomeFragment();
        communityFragment = new CommunityFragment();
        shoppingMallFragment = new ShoppingMallFragment();
        personalCenterFragment = new PersonalCenterFragment();
        listFragment.add(homeFragment);
        listFragment.add(communityFragment);
        listFragment.add(shoppingMallFragment);
        listFragment.add(personalCenterFragment);
        MyFragAdapter myAdapter = new MyFragAdapter(getSupportFragmentManager(), this, listFragment);
        viewPager.setAdapter(myAdapter);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_community:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_shopping_mall:
                        viewPager.setCurrentItem(2);
                        return true;
                    case R.id.navigation_personal_center:
                        viewPager.setCurrentItem(3);
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                viewPagerSelected = position;
                switch (viewPagerSelected){
                    case 0:
                        setSupportActionBar(homeFragment.getToolbar());
                        break;
                    case 1:
                        setSupportActionBar(communityFragment.getToolbar());
                        break;
                }
                supportInvalidateOptionsMenu();
                navView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });
//        setSupportActionBar(homeFragment.getToolbar());
//        supportInvalidateOptionsMenu();

//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //判断当前处于哪个fragment
        switch (viewPagerSelected) {
            case 0:
                //第一个fragment的menu
                getMenuInflater().inflate(R.menu.search_menu, menu);
                break;
            case 1:
                //第二个fragment的menu(无)
                getMenuInflater().inflate(R.menu.community_menu, menu);
                break;
            case 2:
                //第三个fragment的menu
                break;
            case 3:

                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_search2:
                break;
            case R.id.menu_new_interesting_activity:{
                Intent intent = new Intent(this,NewInterestingActivityActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.menu_new_friends_circle:{
                Intent intent = new Intent(this, NewFriendsCircleActivity.class);
                startActivity(intent);
            }

        }
        return true;
    }
}