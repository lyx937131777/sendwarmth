package com.example.sendwarmth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sendwarmth.db.Customer;

public class MyInformationActivity extends AppCompatActivity
{
    private CircleImageView profile;
    private TextView userNameText, nameText, cusAddressText, houseNumText, telText, personalDescriptionText;


    private Customer customer;
    private SharedPreferences pref;
    private String credential;

    private Button logoutButton,changePasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initUser();

        changePasswordButton = findViewById(R.id.change_password);
        changePasswordButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MyInformationActivity.this,ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        logoutButton = findViewById(R.id.logout);
        logoutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyInformationActivity.this).edit();
                editor.remove("userId");
                editor.remove("password");
                editor.apply();
                Intent intent_logout = new Intent(MyInformationActivity.this, LoginActivity.class);
                startActivity(intent_logout);
                MainActivity.instance.finish();
                finish();
            }
        });
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

    private void initUser()
    {
        customer = (Customer) getIntent().getSerializableExtra("customer");

        profile = findViewById(R.id.profile);
        userNameText = findViewById(R.id.user_name);
        nameText = findViewById(R.id.name);
        cusAddressText = findViewById(R.id.address);
        houseNumText = findViewById(R.id.house_num);
        telText = findViewById(R.id.tel);
        personalDescriptionText = findViewById(R.id.personal_description);

        Glide.with(this).load(R.drawable.profile_uri).into(profile);
        userNameText.setText(customer.getUserName());
        nameText.setText(customer.getCustomerName());
        cusAddressText.setText(customer.getCustomerAddress());
        houseNumText.setText(customer.getHouseNum());
        telText.setText(customer.getCustomerTel());
        personalDescriptionText.setText(customer.getPersonalDescription());

    }
}