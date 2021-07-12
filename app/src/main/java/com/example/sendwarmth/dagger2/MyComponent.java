package com.example.sendwarmth.dagger2;

import com.example.sendwarmth.presenter.ChangePasswordPresenter;
import com.example.sendwarmth.presenter.FriendsCirclePresenter;
import com.example.sendwarmth.presenter.HealthBroadcastCommentPresenter;
import com.example.sendwarmth.presenter.HealthBroadcastPresenter;
import com.example.sendwarmth.presenter.HealthBroadcastSubCommentPresenter;
import com.example.sendwarmth.presenter.HomePresenter;
import com.example.sendwarmth.presenter.InterestingActivityPresenter;
import com.example.sendwarmth.presenter.LoginPresenter;
import com.example.sendwarmth.presenter.ModifyInformationPresenter;
import com.example.sendwarmth.presenter.MyInformationPresenter;
import com.example.sendwarmth.presenter.NewFriendsCirclePresenter;
import com.example.sendwarmth.presenter.NewHealthBroadcastPresenter;
import com.example.sendwarmth.presenter.NewInterestingActivityPresenter;
import com.example.sendwarmth.presenter.OrderDetailPresenter;
import com.example.sendwarmth.presenter.OrderPresenter;
import com.example.sendwarmth.presenter.OrderingPresenter;
import com.example.sendwarmth.presenter.PensionInstitutionPresenter;
import com.example.sendwarmth.presenter.PersonalCenterPresenter;
import com.example.sendwarmth.presenter.ProductOrderCommentPresenter;
import com.example.sendwarmth.presenter.ProductOrderDetailPresenter;
import com.example.sendwarmth.presenter.ProductOrderPresenter;
import com.example.sendwarmth.presenter.ProductOrderingPresenter;
import com.example.sendwarmth.presenter.RegisterPresenter;
import com.example.sendwarmth.presenter.ServiceWorkPresenter;
import com.example.sendwarmth.presenter.SetNewPasswordPresenter;
import com.example.sendwarmth.presenter.SettingPresenter;
import com.example.sendwarmth.presenter.ShoppingMallPresenter;

import dagger.Component;

@Component(modules = {MyModule.class})
public interface MyComponent
{
    LoginPresenter loginPresenter();

    RegisterPresenter registerPresenter();

    HomePresenter homePresenter();

    InterestingActivityPresenter interestingActivityPresenter();

    NewInterestingActivityPresenter newInterestingActivityPresenter();

    ShoppingMallPresenter shoppingMallPresenter();

    ServiceWorkPresenter serviceWorkPresenter();

    HealthBroadcastPresenter healthBroadcastPresenter();

    PensionInstitutionPresenter pensionInstitutionPresenter();

    FriendsCirclePresenter friendsCirclePresenter();

    NewHealthBroadcastPresenter newHealthBroadcastPresenter();

    NewFriendsCirclePresenter newFriendsCirclePresenter();

    ProductOrderingPresenter productOrderingPresenter();

    OrderingPresenter orderingPresenter();

    HealthBroadcastCommentPresenter healthBroadcastCommentPresenter();

    SettingPresenter settingPresenter();

    OrderPresenter orderPresenter();

    OrderDetailPresenter orderDetailPresenter();

    HealthBroadcastSubCommentPresenter healthBroadcastSubCommentPresenter();

    ProductOrderPresenter productOrderPresenter();

    ChangePasswordPresenter changePasswordPresenter();

    SetNewPasswordPresenter setNewPasswordPresenter();

    ProductOrderDetailPresenter productOrderDetailPresenter();

    ProductOrderCommentPresenter productOrderCommentPresenter();

    PersonalCenterPresenter personalCenterPresenter();

    MyInformationPresenter myInformationPresenter();

    ModifyInformationPresenter modifyInformationPresenter();
}
