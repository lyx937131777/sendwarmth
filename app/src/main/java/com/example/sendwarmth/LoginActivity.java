package com.example.sendwarmth;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;


import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.presenter.LoginPresenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LoginActivity extends AppCompatActivity implements OnClickListener
{
    private EditText telText, passwordText;
    private Button telClearButton;
    private Button passwordClearButton;
    private Button passwordEyeButton;
    private Button loginButton;
    private Button goRegisterButton, setNewPasswordButton;
    private boolean isOpen = false;
    private String tel, passowrd;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private Dialog agreementDialog;

    private LoginPresenter loginPresenter;

    private CheckBox agreedCheckBox;
    private boolean agreed = false;
    private TextView userAgreementText, privacyPolicyText;

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
        editor = pref.edit();
        tel = pref.getString("userId",null);
        passowrd = pref.getString("password",null);
        if(tel != null & passowrd != null)
        {
            Intent intent_login = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent_login);
            finish();
        }

        agreedCheckBox = findViewById(R.id.agreed_check);
        agreedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                agreed = isChecked;
            }
        });
        userAgreementText = findViewById(R.id.user_agreement);
        privacyPolicyText = findViewById(R.id.privacy_policy);
        userAgreementText.setText(Html.fromHtml("<a href=\"http://app.swn-sh.com/yhxy.html\">用户协议</a>"));
        userAgreementText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUserAgreement();
            }
        });
        privacyPolicyText.setText(Html.fromHtml("<a href=\"http://app.swn-sh.com/yszc.html\">隐私政策</a>"));
        privacyPolicyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPrivacyPolicy();
            }
        });

        initAgreementDialog();
        Boolean firstLogin = pref.getBoolean("firstLogin",true);
        if(firstLogin)
            agreementDialog.show();
    }

    private void initAgreementDialog()
    {
        agreementDialog = new Dialog(this, R.style.Theme_AppCompat_Dialog_Alert);
        View view = View.inflate(this, R.layout.dialog_agreement, null);
        agreementDialog.setContentView(view);
        agreementDialog.setCanceledOnTouchOutside(false);
        //view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(this).getScreenHeight() *
        // 0.23f));
        Window dialogWindow = agreementDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //lp.width = (int) (ScreenSizeUtils.getInstance(this).getScreenWidth() * 0.9f);
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);

        SpannableString contentString = new SpannableString("请你务必审慎阅读，充分理解“用户协议”和“隐私政策”各条款。\n你可以阅读《用户协议》和《隐私政策》了解详细信息。如你同意，请点击“同意”开始接受我们的服务。");
        ClickableSpan userAgreementSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                goToUserAgreement();
            }
        };
        ClickableSpan privacyPolicySpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                goToPrivacyPolicy();
            }
        };
        contentString.setSpan(userAgreementSpan,36,42, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        contentString.setSpan(privacyPolicySpan,43,49,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        TextView contnet = view.findViewById(R.id.content);
        contnet.setText(contentString);
        contnet.setMovementMethod(LinkMovementMethod.getInstance());

        Button agreeButton = view.findViewById(R.id.agree);
        Button unagreeBotton = view.findViewById(R.id.unagree);
        agreeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                agreedCheckBox.setChecked(true);
                editor.putBoolean("firstLogin",false);
                editor.apply();
                agreementDialog.cancel();
            }
        });

        unagreeBotton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void goToUserAgreement(){
        Intent intent = new Intent(LoginActivity.this, WebActivity.class);
        intent.putExtra("link","http://app.swn-sh.com/yhxy.html");
        intent.putExtra("title","用户协议");
        startActivity(intent);
    }

    private void goToPrivacyPolicy(){
        Intent intent = new Intent(LoginActivity.this, WebActivity.class);
        intent.putExtra("link","http://app.swn-sh.com/yszc.html");
        intent.putExtra("title","隐私政策");
        startActivity(intent);
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
        telText = findViewById(R.id.tel);
        // 监听文本框内容变化
        telText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // 获得文本框中的用户
                tel = telText.getText().toString().trim();
                if (tel.equals(""))
                {
                    // 用户名为空,设置按钮不可见
                    telClearButton.setVisibility(View.INVISIBLE);
                } else
                {
                    // 用户名不为空，设置按钮可见
                    telClearButton.setVisibility(View.VISIBLE);
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

        passwordText = findViewById(R.id.password);
        passwordText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // 获得文本框中的用户
                passowrd = passwordText.getText().toString().trim();
                if (passowrd.equals(""))
                {
                    // 用户名为空,设置按钮不可见
                    passwordClearButton.setVisibility(View.INVISIBLE);
                } else
                {
                    // 用户名不为空，设置按钮可见
                    passwordClearButton.setVisibility(View.VISIBLE);
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
        telClearButton = findViewById(R.id.tel_clear);
        telClearButton.setOnClickListener(this);
        passwordClearButton = findViewById(R.id.password_clear);
        passwordClearButton.setOnClickListener(this);
        passwordEyeButton = findViewById(R.id.password_eye);
        passwordEyeButton.setOnClickListener(this);
        loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(this);
        goRegisterButton = findViewById(R.id.go_register);
        goRegisterButton.setOnClickListener(this);
        setNewPasswordButton = findViewById(R.id.set_new_password);
        setNewPasswordButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.tel_clear:
                telText.setText("");
                break;
            case R.id.password_clear:
                passwordText.setText("");
                break;

            // 密码可见与不可见的切换
            case R.id.password_eye:
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

            case R.id.login:
                tel = telText.getText().toString();
                passowrd = passwordText.getText().toString();
                loginPresenter.login(tel, passowrd,agreed);
                break;
            // 注册按钮
            case R.id.go_register:
                Intent reg = new Intent();
                reg.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(reg);
                break;
            // 找回密码
            case R.id.set_new_password:{
                Intent intent = new Intent(this,SetNewPasswordActivity.class);
                startActivity(intent);
            }
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
            passwordEyeButton.setBackgroundResource(R.drawable.pwd_open);
            // 设置EditText的密码可见
            passwordText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else
        {
            // 密码不可见
            passwordEyeButton.setBackgroundResource(R.drawable.pwd_closed);
            // 设置EditText的密码隐藏
            passwordText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }
}