package com.example.sendwarmth.dagger2;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import com.example.sendwarmth.presenter.LoginPresenter;
import com.example.sendwarmth.presenter.RegisterPresenter;
import com.example.sendwarmth.util.CheckUtil;

import dagger.Module;
import dagger.Provides;

@Module
public class MyModule
{
    private Context context;

    public MyModule(Context context)
    {
        this.context = context;
    }

    @Provides
    public SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    public CheckUtil provideCheckUtil(Context context)
    {
        return new CheckUtil(context);
    }

    @Provides
    public Context provideContext()
    {
        return context;
    }

    @Provides
    public LoginPresenter provideLoginPresenter(Context context, SharedPreferences pref, CheckUtil checkUtil)
    {
        return new LoginPresenter(context,pref,checkUtil);
    }

    @Provides
    public RegisterPresenter provideRegisterPresenter(Context context, CheckUtil checkUtil)
    {
        return new RegisterPresenter(context,checkUtil);
    }


}
