package com.example.sendwarmth.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.sendwarmth.adapter.FriendsCircleAdapter;

public class FriendsCirclePresenter
{
    private Context context;
    private SharedPreferences pref;

    public FriendsCirclePresenter(Context context, SharedPreferences pref){
        this.context = context;
        this.pref = pref;
    }

    public void updateFriendsCircle(final FriendsCircleAdapter friendsCircleAdapter){

    }
}
