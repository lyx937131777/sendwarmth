package com.example.sendwarmth.dagger2;

import com.example.sendwarmth.presenter.HomePresenter;
import com.example.sendwarmth.presenter.InterestingActivityPresenter;
import com.example.sendwarmth.presenter.LoginPresenter;
import com.example.sendwarmth.presenter.NewInterestingActivityPresenter;
import com.example.sendwarmth.presenter.RegisterPresenter;
import com.example.sendwarmth.presenter.ServiceSubjectPresenter;

import dagger.Component;

@Component(modules = {MyModule.class})
public interface MyComponent
{
    LoginPresenter loginPresenter();

    RegisterPresenter registerPresenter();

    HomePresenter homePresenter();

    ServiceSubjectPresenter serviceSubjectPresenter();

    InterestingActivityPresenter interestingActivity();

    NewInterestingActivityPresenter newInterestingActivityPresenter();
}
