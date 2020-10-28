package com.example.sendwarmth.dagger2;

import com.example.sendwarmth.presenter.FriendsCirclePresenter;
import com.example.sendwarmth.presenter.HealthBroadcastPresenter;
import com.example.sendwarmth.presenter.HomePresenter;
import com.example.sendwarmth.presenter.InterestingActivityPresenter;
import com.example.sendwarmth.presenter.LoginPresenter;
import com.example.sendwarmth.presenter.NewFriendsCirclePresenter;
import com.example.sendwarmth.presenter.NewHealthBroadcastPresenter;
import com.example.sendwarmth.presenter.NewInterestingActivityPresenter;
import com.example.sendwarmth.presenter.PensionInstitutionPresenter;
import com.example.sendwarmth.presenter.ProductOrderingPresenter;
import com.example.sendwarmth.presenter.RegisterPresenter;
import com.example.sendwarmth.presenter.ServiceWorkPresenter;
import com.example.sendwarmth.presenter.ShoppingMallPresenter;

import dagger.Component;

@Component(modules = {MyModule.class})
public interface MyComponent
{
    LoginPresenter loginPresenter();

    RegisterPresenter registerPresenter();

    HomePresenter homePresenter();

    InterestingActivityPresenter interestingActivity();

    NewInterestingActivityPresenter newInterestingActivityPresenter();

    ShoppingMallPresenter shoppingMallPresenter();

    ServiceWorkPresenter serviceWorkPresenter();

    HealthBroadcastPresenter healthBroadcastPresenter();

    PensionInstitutionPresenter pensionInstitutionPresenter();

    FriendsCirclePresenter friendsCirclePresenter();

    NewHealthBroadcastPresenter newHealthBroadcastPresenter();

    NewFriendsCirclePresenter newFriendsCirclePresenter();

    ProductOrderingPresenter productOrderingPresenter();
}
