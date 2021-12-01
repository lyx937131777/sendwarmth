package com.example.sendwarmth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.db.Customer;
import com.example.sendwarmth.db.ServiceSubject;
import com.example.sendwarmth.db.Worker;
import com.example.sendwarmth.presenter.OrderingPresenter;
import com.example.sendwarmth.util.DateAndTimePickerDialog;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.MapUtil;
import com.example.sendwarmth.util.TimeUtil;

import org.litepal.LitePal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ServiceSubjectActivity extends AppCompatActivity
{
    private ServiceSubject serviceSubject;

    private Button orderButton;

    private SharedPreferences pref;
    private String credential;
    private Customer customer;

    private ImageView serviceClassImage, serviceSubjectImage;

//    private TextView priceText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_subject);
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

        Glide.with(this).load(HttpUtil.getResourceURL(serviceSubject.getImage())).into(picture);
        title.setText(serviceSubject.getSubjectName());
        description.setText(serviceSubject.getSubjectDes());
        pricePerUnit.setText(serviceSubject.getSalaryPerHour() +"（加急："+serviceSubject.getHurrySalaryPerHour()+"）元/单价");

//        priceText = findViewById(R.id.price_total);
//        priceText.setText("￥"+serviceSubject.getSalaryPerHour() +"/单价");

        orderButton = findViewById(R.id.order);
        orderButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                pref = PreferenceManager.getDefaultSharedPreferences(ServiceSubjectActivity.this);
                credential = pref.getString("credential","");
                customer = LitePal.where("credential = ?",credential).findFirst(Customer.class);
                if(customer.informationIncomplete()){
                    new AlertDialog.Builder(ServiceSubjectActivity.this)
                            .setTitle("提示")
                            .setMessage("您的必要身份信息不完整（姓名，性别，详细地址，身份证），请补全信息后再下单。")
                            .setPositiveButton("现在去填", new
                                    DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            Intent intent = new Intent(ServiceSubjectActivity.this, MyInformationActivity.class);
                                            intent.putExtra("customer",customer);
                                            startActivity(intent);
                                        }
                                    })
                            .setNegativeButton("稍后再说",null).show();
                }else{
                    Intent intent = new Intent(ServiceSubjectActivity.this, OrderingActivity.class);
                    intent.putExtra("serviceWork", serviceSubject);
                    startActivity(intent);
                }
            }
        });


        serviceSubjectImage = findViewById(R.id.service_subject_image);
        serviceClassImage = findViewById(R.id.service_class_image);
        Glide.with(this).load(HttpUtil.getResourceURL(serviceSubject.getRemarkImg())).into(serviceSubjectImage);
        Glide.with(this).load(HttpUtil.getResourceURL(serviceSubject.getServiceClassInfo().getRemarkImg())).into(serviceClassImage);

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