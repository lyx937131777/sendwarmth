package com.example.sendwarmth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.presenter.RegisterPresenter;
import com.example.sendwarmth.util.MapUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RegisterActivity extends AppCompatActivity implements OnClickListener
{
    private EditText telText, passwordText, confirmPasswordText;
    private Button telClearButton,passwordClearButton,confirmPasswordClearButton;
    private Button registerButton;
    private String tel, password, confirmPassword;
//    private double longitude,latitude;

//    private Spinner roleSpinner;
//    private List<String> roleList = new ArrayList<>();
//    private ArrayAdapter<String> roleArrayAdapter;
//    private String role;

    private RegisterPresenter registerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(this)).build();
        registerPresenter = myComponent.registerPresenter();


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
                tel = telText.getText().toString();
                if (tel.equals("")) {
                    // 用户名为空,设置按钮不可见
                    telClearButton.setVisibility(View.INVISIBLE);
                } else {
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
                password = passwordText.getText().toString();
                if (password.equals("")) {
                    passwordClearButton.setVisibility(View.INVISIBLE);
                } else {
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
        confirmPasswordText = findViewById(R.id.confirm_password);
        confirmPasswordText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                confirmPassword = confirmPasswordText.getText().toString();
                if (confirmPassword.equals("")) {
                    confirmPasswordClearButton.setVisibility(View.INVISIBLE);
                } else {
                    confirmPasswordClearButton.setVisibility(View.VISIBLE);
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

//        initroleList();
//        roleArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,roleList);
//        roleArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        roleSpinner.setAdapter(roleArrayAdapter);

        telClearButton =  findViewById(R.id.tel_clear);
        telClearButton.setOnClickListener(this);
        passwordClearButton = findViewById(R.id.password_clear);
        passwordClearButton.setOnClickListener(this);
        confirmPasswordClearButton = findViewById(R.id.confirm_password_clear);
        confirmPasswordClearButton.setOnClickListener(this);
        registerButton = findViewById(R.id.register);
        registerButton.setOnClickListener(this);
    }

//    private void initroleList()
//    {
//        roleSpinner = findViewById(R.id.role);
////        roleText = findViewById(R.id.role_text);
//        roleList.add("角色");
//        roleList.add("普通用户");
//        roleList.add("专家");
//        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//        {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
//            {
//                role = MapUtil.getRole(roleList.get(i));
////                roleText.setText(roleList.get(i));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView)
//            {
//
//            }
//        });
//    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            // 清除
            case R.id.tel_clear:
                telText.setText("");
                break;
            case R.id.password_clear:
                passwordText.setText("");
                break;
            case R.id.confirm_password_clear:
                confirmPasswordText.setText("");
                break;

            case R.id.register:
                tel = telText.getText().toString();
                password = passwordText.getText().toString();
                confirmPassword = confirmPasswordText.getText().toString();
                registerPresenter.register(tel,password,confirmPassword);
                break;

            default:
                break;
        }


    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode)
//        {
//            case 1:
//            {
//                if (resultCode == RESULT_OK)
//                {
//                    address = data.getStringExtra("address");
//                    addressText.setText(address);
//                    longitude = data.getDoubleExtra("longitude",0);
//                    latitude = data.getDoubleExtra("latitude",0);
//                }
//                break;
//            }
//            default:
//                break;
//        }
//    }
}

