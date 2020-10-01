package com.example.sendwarmth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sendwarmth.db.ServiceSubject;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.MapUtil;

import java.util.ArrayList;
import java.util.List;

public class OrderingActivity extends AppCompatActivity
{
    private ServiceSubject serviceSubject;
    private Spinner tipSpinner;
    private List<String> tipList = new ArrayList<>();
    private ArrayAdapter<String> tipArrayAdapter;
    private int tip;
    private double hour = 2;
    private double price;
    private TextView priceText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordering);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initServiceWork();

    }

    private void initServiceWork()
    {
        serviceSubject = (ServiceSubject) getIntent().getSerializableExtra("serviceWork");
        View serviceWorkView = findViewById(R.id.service_work);
        ImageView picture = serviceWorkView.findViewById(R.id.picture);
        TextView title = serviceWorkView.findViewById(R.id.title);
        TextView description = serviceWorkView.findViewById(R.id.description);
        TextView pricePerUnit = serviceWorkView.findViewById(R.id.price);

        Glide.with(this).load(HttpUtil.getPhotoURL(serviceSubject.getImage())).into(picture);
        title.setText(serviceSubject.getName());
        description.setText(serviceSubject.getDescription());
        pricePerUnit.setText(serviceSubject.getSalaryPerHour()+"元/时");

        priceText = findViewById(R.id.price_total);
        changePrice();

        initTipList();
        tipArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,tipList);
        tipArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipSpinner.setAdapter(tipArrayAdapter);

    }

    private void initTipList()
    {
        tipSpinner = findViewById(R.id.tip);
        tipList.add("无");
        tipList.add("5元");
        tipList.add("10元");
        tipList.add("20元");
        tipSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                tip = MapUtil.getTip(tipList.get(i));
                changePrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    private void changePrice(){
        double priceBase = serviceSubject.getSalaryPerHour()*hour;
        price = priceBase + tip;
        priceText.setText("￥"+price);
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
}