package com.example.sendwarmth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.db.Account;
import com.example.sendwarmth.db.Customer;
import com.example.sendwarmth.fragment.ShoppingMallFragment;
import com.example.sendwarmth.fragment.CommunityFragment;
import com.example.sendwarmth.fragment.HomeFragment;
import com.example.sendwarmth.fragment.PersonalCenterFragment;
import com.example.sendwarmth.fragment.adapter.MyFragAdapter;
import com.example.sendwarmth.presenter.SettingPresenter;
import com.example.sendwarmth.util.LogUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
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
    private SharedPreferences pref;

    public static MainActivity instance = null;

    private int viewPagerSelected = 0;

    private SettingPresenter settingPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;

        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(this)).build();
        settingPresenter = myComponent.settingPresenter();

        navView = findViewById(R.id.nav_view);
        viewPager = findViewById(R.id.view_pager);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
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

        PackageManager manager = getPackageManager();
        String version = "未知";
        try {
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        settingPresenter.getLatestVersionMain(version);

        long time = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        LogUtil.e("TimeTest","time(long): " + time);
        LogUtil.e("TimeTest","calendar(default): " + calendar.getTimeInMillis());
        LogUtil.e("TimeTest","calendar: year: " + calendar.get(Calendar.YEAR) + " month: " + (calendar.get(Calendar.MONTH)+1) + " day:" + calendar.get(Calendar.DAY_OF_MONTH));
        LogUtil.e("TimeTest","calendar: hour: " + calendar.get(Calendar.HOUR_OF_DAY) + " minute: " + calendar.get(Calendar.PM) + " second: " +calendar.get(Calendar.AM_PM));
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
                LogUtil.e("MainActivity",pref.getString("role","customer?"));
                if(!pref.getString("role","").equals("expert")){
                    LogUtil.e("MainActivity","Loginer is customer");
                    menu.removeItem(R.id.menu_new_health_broadcast);
                }
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
            case R.id.menu_new_health_broadcast:{
                Intent intent = new Intent(this,NewHealthBroadcastActivity.class);
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

    public void setCustomer(Customer customer){
        personalCenterFragment.setCustomer(customer);
    }

    public void setAccount(Account account){
        personalCenterFragment.setAccount(account);
    }
}