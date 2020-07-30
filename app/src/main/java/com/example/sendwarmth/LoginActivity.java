package com.example.sendwarmth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.presenter.LoginPresenter;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements OnClickListener
{
    private EditText username, password;
    private Button bt_username_clear;
    private Button bt_pwd_clear;
    private Button bt_pwd_eye;
    private Button bt_login;
    private Button bt_go_register;
    private boolean isOpen = false;
    String username_text, password_text;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private LoginPresenter loginPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        PermissionsUtil.verifyStoragePermissions(this);
        initView();
//        androidx.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null)
//        {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }

        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(this)).build();
        loginPresenter = myComponent.loginPresenter();

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        username_text = pref.getString("userID",null);
        password_text = pref.getString("password",null);
        if(username_text != null & password_text != null)
        {
            Intent intent_login = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent_login);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;

        }
        return true;
    }

    private void initView()
    {
        username = (EditText) findViewById(R.id.username);
        // 监听文本框内容变化
        username.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // 获得文本框中的用户
                username_text = username.getText().toString().trim();
                if ("".equals(username_text))
                {
                    // 用户名为空,设置按钮不可见
                    bt_username_clear.setVisibility(View.INVISIBLE);
                } else
                {
                    // 用户名不为空，设置按钮可见
                    bt_username_clear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });

        password = (EditText) findViewById(R.id.password);
        // 监听文本框内容变化
        password.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // 获得文本框中的用户
                password_text = password.getText().toString().trim();
                if ("".equals(password_text))
                {
                    // 用户名为空,设置按钮不可见
                    bt_pwd_clear.setVisibility(View.INVISIBLE);
                } else
                {
                    // 用户名不为空，设置按钮可见
                    bt_pwd_clear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });

        //初始化button
        bt_username_clear = (Button) findViewById(R.id.bt_username_clear);
        bt_username_clear.setOnClickListener(this);
        bt_pwd_clear = (Button) findViewById(R.id.bt_pwd_clear);
        bt_pwd_clear.setOnClickListener(this);
        bt_pwd_eye = (Button) findViewById(R.id.bt_pwd_eye);
        bt_pwd_eye.setOnClickListener(this);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_login.setOnClickListener(this);
        bt_go_register = (Button) findViewById(R.id.bt_go_register);
        bt_go_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            // 清除登录名
            case R.id.bt_username_clear:
                username.setText("");
                break;

            // 清除密码
            case R.id.bt_pwd_clear:
                password.setText("");
                break;

            // 密码可见与不可见的切换
            case R.id.bt_pwd_eye:
                if (isOpen)
                {
                    isOpen = false;
                } else
                {
                    isOpen = true;
                }
                // 默认isOpen是false,密码不可见
                changePwdOpenOrClose(isOpen);
                break;

            // TODO 登录按钮
            case R.id.bt_login:
                username_text = username.getText().toString();
                password_text = password.getText().toString();
                loginPresenter.login(username_text,password_text);
                break;
            // 注册按钮
            case R.id.bt_go_register:
                Intent reg = new Intent();
                reg.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(reg);
                break;
            default:
                break;
        }
    }

    /**
     * 密码可见与不可见的切换
     *
     * @param flag
     */
    private void changePwdOpenOrClose(boolean flag)
    {
        // 第一次过来是false，密码不可见
        if (flag)
        {
            // 密码可见
            bt_pwd_eye.setBackgroundResource(R.drawable.pwd_open);
            // 设置EditText的密码可见
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else
        {
            // 密码不可见
            bt_pwd_eye.setBackgroundResource(R.drawable.pwd_closed);
            // 设置EditText的密码隐藏
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }
}