package com.example.sendwarmth.fragment.adapter;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class MyFragAdapter extends FragmentStatePagerAdapter
{
    private Context context;
    private List<Fragment> listFragment;

    public MyFragAdapter(FragmentManager fm, Context context, List<Fragment> listFragment)
    {
        super(fm);
        this.context = context;
        this.listFragment = listFragment;
    }

    @NotNull
    @Override
    public Fragment getItem(int position)
    {
        return listFragment.get(position);
    }

    @Override
    public int getCount()
    {
        return listFragment.size();
    }
}