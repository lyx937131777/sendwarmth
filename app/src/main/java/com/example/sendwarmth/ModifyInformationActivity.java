package com.example.sendwarmth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.presenter.ModifyInformationPresenter;
import com.example.sendwarmth.util.MapUtil;

public class ModifyInformationActivity extends AppCompatActivity {

    private EditText valueText;
    private Button locationButton;

    private String attribute;
    private String oldValue;

    private ModifyInformationPresenter modifyInformationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_information);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(this)).build();
        modifyInformationPresenter = myComponent.modifyInformationPresenter();

        attribute = getIntent().getStringExtra("attribute");
        oldValue = getIntent().getStringExtra("value");

        toolbar.setTitle("更改" + MapUtil.getAttribute(attribute));

        valueText = findViewById(R.id.value);
        valueText.setText(oldValue);
        locationButton = findViewById(R.id.location);

        if(attribute.equals("customerAddress")){
            valueText.setEnabled(false);
            locationButton.setVisibility(View.VISIBLE);
            locationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ModifyInformationActivity.this, LocationSelectActivity.class);
                    startActivityForResult(intent,1);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.commit:{
                modifyInformationPresenter.modifyCustomer(attribute,valueText.getText().toString());
                break;
            }
        }
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.commit_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 1:
            {
                if (resultCode == RESULT_OK)
                {
                    String address = data.getStringExtra("address");
                    valueText.setText(address);
                    double longitude = data.getDoubleExtra("longitude",0);
                    double latitude = data.getDoubleExtra("latitude",0);
                    modifyInformationPresenter.setLL(longitude,latitude);
                }
                break;
            }
            default:
                break;
        }
    }
}