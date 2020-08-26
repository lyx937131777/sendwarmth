package com.example.sendwarmth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Layout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sendwarmth.db.ServiceWork;
import com.example.sendwarmth.util.MapUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderingActivity extends AppCompatActivity
{
    private ServiceWork serviceWork;
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
        serviceWork = (ServiceWork) getIntent().getSerializableExtra("serviceWork");
        View serviceWorkView = findViewById(R.id.service_work);
        ImageView picture = serviceWorkView.findViewById(R.id.picture);
        TextView title = serviceWorkView.findViewById(R.id.title);
        TextView description = serviceWorkView.findViewById(R.id.description);
        TextView pricePerUnit = serviceWorkView.findViewById(R.id.price);

        Glide.with(this).load(serviceWork.getPicture()).into(picture);
        title.setText(serviceWork.getName());
        description.setText(serviceWork.getDescription());
        pricePerUnit.setText(serviceWork.getPrice()+"元/"+serviceWork.getUnit());

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
        double priceBase;
        switch (serviceWork.getUnit()){
            case "次":
                priceBase = serviceWork.getPrice();
                break;
            case "小时":
                priceBase = serviceWork.getPrice()*hour;
                break;
            default:
                priceBase = 0;
        }
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