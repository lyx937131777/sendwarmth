package com.example.sendwarmth;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.sendwarmth.ui.ShoppingMallFragment;
import com.example.sendwarmth.ui.CommunityFragment;
import com.example.sendwarmth.ui.HomeFragment;
import com.example.sendwarmth.ui.PersonalCenterFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    private BottomNavigationView navView;

    public static MainActivity instance = null;

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
        listFragment.add(new HomeFragment());
        listFragment.add(new CommunityFragment());
        listFragment.add(new ShoppingMallFragment());
        listFragment.add(new PersonalCenterFragment());
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
                navView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });

//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);
    }

}