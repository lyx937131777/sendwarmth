package com.example.sendwarmth.fragment.adapter;

import android.content.Context;

import com.example.sendwarmth.R;
import com.example.sendwarmth.fragment.OrderFragment;
import com.example.sendwarmth.fragment.ProductOrderFragment;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ProductOrderPagerAdapter extends FragmentStatePagerAdapter
{
    @StringRes
    private static final int[] TAB_TITLES = new int[] {R.string.title_all_orders, R.string.title_to_be_paid_orders,R.string.title_to_be_received_orders,R.string.title_to_be_evaluated_orders,R.string.title_closed_orders};
    private final Context mContext;

    public ProductOrderPagerAdapter(Context context, FragmentManager fm)
    {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position)
    {
        // getItem is called to instantiate the fragment for the given page.
        // Return a EventRecordFragment (defined as a static inner class below).
        return ProductOrderFragment.newInstance(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount()
    {
        return 5;
    }
}
