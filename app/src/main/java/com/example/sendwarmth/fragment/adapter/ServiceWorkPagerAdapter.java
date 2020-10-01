package com.example.sendwarmth.fragment.adapter;

import android.content.Context;

import com.example.sendwarmth.db.ServiceClass;
import com.example.sendwarmth.fragment.OrderFragment;
import com.example.sendwarmth.fragment.ServiceSubjectFragment;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ServiceWorkPagerAdapter extends FragmentStatePagerAdapter
{
    private final Context mContext;
    private List<ServiceClass> serviceClassList;

    public ServiceWorkPagerAdapter(Context context, FragmentManager fm, List<ServiceClass> serviceClassList)
    {
        super(fm);
        mContext = context;
        this.serviceClassList = serviceClassList;
    }

    @Override
    public Fragment getItem(int position)
    {
        // getItem is called to instantiate the fragment for the given page.
        // Return a EventRecordFragment (defined as a static inner class below).
        ServiceClass serviceClass = serviceClassList.get(position);
        return ServiceSubjectFragment.newInstance(serviceClass.getName());
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        return serviceClassList.get(position).getName();
    }

    @Override
    public int getCount()
    {
        return serviceClassList.size();
    }
}
