package com.example.sendwarmth.dagger2;


import com.example.sendwarmth.presenter.LoginPresenter;
import com.example.sendwarmth.presenter.RegisterPresenter;

import dagger.Component;

@Component(modules = {MyModule.class})
public interface    MyComponent
{
    LoginPresenter loginPresenter();

    RegisterPresenter registerPresenter();

}
