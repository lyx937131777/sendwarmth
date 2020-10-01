package com.example.sendwarmth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
import com.example.sendwarmth.db.User;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.Utility;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class MyInformationActivity extends AppCompatActivity
{
    private CircleImageView profile;
    private TextView userName;
    private TextView name;
    private TextView cusAddress;
    private TextView tel;
    private TextView personalDescription;

    private User user;

    private Customer customer;
    private SharedPreferences pref;
    private String credential;

    private Button logoutButton;

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
        userName = findViewById(R.id.user_name);
        name = findViewById(R.id.name);
        cusAddress = findViewById(R.id.address);
        tel = findViewById(R.id.tel);
        personalDescription = findViewById(R.id.personal_description);

        Glide.with(this).load(R.drawable.profile_uri).into(profile);
        userName.setText(customer.getUserName());
        name.setText(customer.getName());
        cusAddress.setText(customer.getAddress());
        tel.setText(customer.getTel());
        personalDescription.setText(customer.getPersonalDescription());

    }
}