package com.example.sendwarmth.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;

public class NewHealthBroadcastPresenter
{
    private Context context;
    private SharedPreferences pref;
    private ProgressDialog progressDialog;

    public NewHealthBroadcastPresenter(Context context, SharedPreferences pref){
        this.context = context;
        this.pref = pref;
    }

    public void postHealthBroadcast(String content, String imagePath){

    }
}
