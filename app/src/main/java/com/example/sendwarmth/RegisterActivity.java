package com.example.sendwarmth;

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


import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.presenter.RegisterPresenter;
import com.example.sendwarmth.util.MapUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RegisterActivity extends AppCompatActivity implements OnClickListener
{
    private EditText telText, passwordText, confirmPasswordText, userNameText,nameText,addressText,personalDescriptionText;
    private Button telClearButton,passwordClearButton,confirmPasswordClearButton,userNameClearButton,nameClearButton,addressClearButton,personalDescriptionClearButton;
    private Button registerButton;
    private String tel, password, confirmPassword, userName, name, address, personalDescription;

    private Spinner roleSpinner;
    private List<String> roleList = new ArrayList<>();
    private ArrayAdapter<String> roleArrayAdapter;
    private String role;

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
        userNameText = findViewById(R.id.user_name);
        userNameText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                userName = userNameText.getText().toString();
                if (userName.equals("")) {
                    userNameClearButton.setVisibility(View.INVISIBLE);
                } else {
                    userNameClearButton.setVisibility(View.VISIBLE);
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

        nameText = findViewById(R.id.name);
        nameText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                name = nameText.getText().toString();
                if(name.equals("")){
                    nameClearButton.setVisibility(View.INVISIBLE);
                }else {
                    nameClearButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });

        addressText = findViewById(R.id.address);
        addressText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                address = addressText.getText().toString();
                if(address.equals("")){
                    addressClearButton.setVisibility(View.INVISIBLE);
                }else {
                    addressClearButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });

        personalDescriptionText = findViewById(R.id.personal_description);
        personalDescriptionText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                personalDescription = personalDescriptionText.getText().toString();
                if(personalDescription.equals("")){
                    personalDescriptionClearButton.setVisibility(View.INVISIBLE);
                }else {
                    personalDescriptionClearButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });

        initroleList();
        roleArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,roleList);
        roleArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(roleArrayAdapter);

        telClearButton =  findViewById(R.id.tel_clear);
        telClearButton.setOnClickListener(this);
        passwordClearButton = findViewById(R.id.password_clear);
        passwordClearButton.setOnClickListener(this);
        confirmPasswordClearButton = findViewById(R.id.confirm_password_clear);
        confirmPasswordClearButton.setOnClickListener(this);
        userNameClearButton = findViewById(R.id.user_name_clear);
        userNameClearButton.setOnClickListener(this);
        nameClearButton = findViewById(R.id.name_clear);
        nameClearButton.setOnClickListener(this);
        addressClearButton = findViewById(R.id.address_clear);
        addressClearButton.setOnClickListener(this);
        personalDescriptionClearButton = findViewById(R.id.personal_description_clear);
        personalDescriptionClearButton.setOnClickListener(this);
        registerButton = findViewById(R.id.register);
        registerButton.setOnClickListener(this);
    }

    private void initroleList()
    {
        roleSpinner = findViewById(R.id.role);
//        roleText = findViewById(R.id.role_text);
        roleList.add("角色");
        roleList.add("普通用户");
        roleList.add("专家");
        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                role = MapUtil.getRole(roleList.get(i));
//                roleText.setText(roleList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

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
            case R.id.user_name_clear:
                userNameText.setText("");
                break;
            case R.id.name_clear:
                nameText.setText("");
                break;
            case R.id.address_clear:
                addressText.setText("");
                break;
            case R.id.personal_description_clear:
                personalDescriptionText.setText("");
                break;

            // TODO 注册按钮
            case R.id.register:
                tel = telText.getText().toString();
                password = passwordText.getText().toString();
                confirmPassword = confirmPasswordText.getText().toString();
                userName = userNameText.getText().toString();
                name = nameText.getText().toString();
                address = addressText.getText().toString();
                personalDescription = personalDescriptionText.getText().toString();
                registerPresenter.register(tel,password,confirmPassword,userName,role,name,address,personalDescription);
                break;

            default:
                break;
        }


    }
}

