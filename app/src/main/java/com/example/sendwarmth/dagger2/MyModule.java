package com.example.sendwarmth.dagger2;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import com.example.sendwarmth.db.FriendsCircle;
import com.example.sendwarmth.presenter.FriendsCirclePresenter;
import com.example.sendwarmth.presenter.HealthBroadcastCommentPresenter;
import com.example.sendwarmth.presenter.HealthBroadcastPresenter;
import com.example.sendwarmth.presenter.HomePresenter;
import com.example.sendwarmth.presenter.InterestingActivityPresenter;
import com.example.sendwarmth.presenter.LoginPresenter;
import com.example.sendwarmth.presenter.NewFriendsCirclePresenter;
import com.example.sendwarmth.presenter.NewHealthBroadcastPresenter;
import com.example.sendwarmth.presenter.NewInterestingActivityPresenter;
import com.example.sendwarmth.presenter.OrderingPresenter;
import com.example.sendwarmth.presenter.PensionInstitutionPresenter;
import com.example.sendwarmth.presenter.ProductOrderingPresenter;
import com.example.sendwarmth.presenter.RegisterPresenter;
import com.example.sendwarmth.presenter.ServiceWorkPresenter;
import com.example.sendwarmth.presenter.ShoppingMallPresenter;
import com.example.sendwarmth.util.CheckUtil;

import dagger.Module;
import dagger.Provides;

@Module
public class MyModule
{
    private Context context;

    public MyModule(Context context) {
        this.context = context;
    }

    @Provides
    public SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    public CheckUtil provideCheckUtil(Context context) {
        return new CheckUtil(context);
    }

    @Provides
    public Context provideContext() {
        return context;
    }

    @Provides
    public LoginPresenter provideLoginPresenter(Context context, SharedPreferences pref, CheckUtil checkUtil) {
        return new LoginPresenter(context,pref,checkUtil);
    }

    @Provides
    public RegisterPresenter provideRegisterPresenter(Context context, CheckUtil checkUtil) {
        return new RegisterPresenter(context,checkUtil);
    }

    @Provides
    public HomePresenter provideHomePresenter(Context context, SharedPreferences pref){
        return new HomePresenter(context,pref);
    }

    @Provides
    public InterestingActivityPresenter provideInterestingActivityPresenter(Context context, SharedPreferences pref){
        return new InterestingActivityPresenter(context,pref);
    }

    @Provides
    public NewInterestingActivityPresenter provideNewInterestingActivityPresenter(Context context, SharedPreferences pref){
        return new NewInterestingActivityPresenter(context, pref);
    }

    @Provides
    public ShoppingMallPresenter provideShoppingMallPresenter(Context context, SharedPreferences pref){
        return new ShoppingMallPresenter(context,pref);
    }


    @Provides
    public ServiceWorkPresenter provideServiceWorkPresenter(Context context, SharedPreferences pref){
        return new ServiceWorkPresenter(context, pref);
    }

    @Provides
    public HealthBroadcastPresenter provideHealthBroadcastPresenter(Context context, SharedPreferences pref){
        return new HealthBroadcastPresenter(context, pref);
    }

    @Provides
    public PensionInstitutionPresenter providePensionInstitutionPresenter(Context context, SharedPreferences pref){
        return new PensionInstitutionPresenter(context, pref);
    }

    @Provides
    public FriendsCirclePresenter provideFriendsCirclePresenter(Context context, SharedPreferences pref){
        return new FriendsCirclePresenter(context, pref);
    }

    @Provides
    public NewFriendsCirclePresenter provideNewFriendsCirclePresenter(Context context, SharedPreferences pref){
        return new NewFriendsCirclePresenter(context,pref);
    }

    @Provides
    public NewHealthBroadcastPresenter provideNewHealthBroadcastPresenter(Context context, SharedPreferences pref){
        return new NewHealthBroadcastPresenter(context, pref);
    }

    @Provides
    public ProductOrderingPresenter provideProductOrderingPresenter(Context context, SharedPreferences pref){
        return new ProductOrderingPresenter(context,pref);
    }

    @Provides
    public OrderingPresenter provideOrderingPresenter(Context context, SharedPreferences pref)
    {
        return new OrderingPresenter(context, pref);
    }

    public HealthBroadcastCommentPresenter healthBroadcastCommentPresenter(Context context, SharedPreferences pref){
        return new HealthBroadcastCommentPresenter(context, pref);
    }
}
