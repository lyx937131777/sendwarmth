package com.example.sendwarmth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.db.ServiceSubject;
import com.example.sendwarmth.db.Worker;
import com.example.sendwarmth.presenter.OrderingPresenter;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.MapUtil;

import java.util.ArrayList;
import java.util.List;

public class OrderingActivity extends AppCompatActivity
{
    private ServiceSubject serviceSubject;
    private Spinner tipSpinner;
    private List<String> tipList = new ArrayList<>();
    private ArrayAdapter<String> tipArrayAdapter;
    private Spinner workerSpiner;
    private List<Worker> workerList = new ArrayList<>();
    private ArrayAdapter<Worker> workerArraryAdapter;
    private String workerId;
    private Spinner urgentSpiner;
    private List<String> urgentList = new ArrayList<>();
    private ArrayAdapter<String> urgentArraryAdapter;
    private String orderType;

    private int tip;
    private double price;
    private double hour = 1;
    private TextView priceText;


    private TextView addressText;
    private double longitude;
    private double latitude;
    private EditText telText;
    private EditText timeText;
    private EditText messageText;
    private Button orderButton;

    public LocationClient mLocationClient;

    private OrderingPresenter orderingPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new OrderingActivity.MyLocationListener());
        setContentView(R.layout.activity_ordering);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(this)).build();
        orderingPresenter = myComponent.orderingPresenter();

        initServiceWork();

        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this, permissions, 1);
        } else {
            requestLocation();
        }

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
        title.setText(serviceSubject.getName());
        description.setText(serviceSubject.getDescription());
        pricePerUnit.setText(serviceSubject.getSalaryPerHour()+"元/时");
        addressText = findViewById(R.id.address);
        telText = findViewById(R.id.tel);
        timeText = findViewById(R.id.time);
        messageText = findViewById(R.id.message);
        orderButton = findViewById(R.id.order);
        orderButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new AlertDialog.Builder(OrderingActivity.this)
                        .setTitle("提示")
                        .setMessage("是否确认下单？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
//                                        orderingPresenter.postOrder(longitude,latitude,);
                                    }
                                })
                        .setNegativeButton("取消",null)
                        .show();
            }
        });

        priceText = findViewById(R.id.price_total);
        changePrice();

        initTipList();
        tipArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,tipList);
        tipArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipSpinner.setAdapter(tipArrayAdapter);

        initWorkerList();
        workerArraryAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,workerList);
        workerArraryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workerSpiner.setAdapter(workerArraryAdapter);

        initUrgent();
        urgentArraryAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,urgentList);
        urgentArraryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        urgentSpiner.setAdapter(urgentArraryAdapter);
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

    private void initWorkerList(){
        workerSpiner = findViewById(R.id.worker);
        Worker noWorker = new Worker();
        noWorker.setWorkerName("不指定");
        noWorker.setInternetId("0");
        workerList.add(noWorker);
        workerSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                workerId = workerList.get(i).getInternetId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    private void initUrgent(){
        urgentSpiner = findViewById(R.id.urgent);
        urgentList.add("预约订单");
        urgentList.add("加急订单");
        urgentSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                orderType = MapUtil.getOrderType(urgentList.get(i));
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
        priceText.setText("约：￥"+price);
    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");//百度API保护隐私 默认获取火星坐标 加上这一行可直接获得真实坐标
//        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }


    private class MyLocationListener extends BDAbstractLocationListener
    {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location.getLocType() == BDLocation.TypeGpsLocation
                    || location.getLocType() == BDLocation.TypeNetWorkLocation) {
//                navigateTo(location);
                addressText.setText(location.getCity() + " " + location.getDistrict() + " "+ location.getStreet());
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                LogUtil.e("OrderingActivity","longitude: " + longitude + "   latitude: " + latitude);
                orderingPresenter.updateWorker(longitude,latitude,workerArraryAdapter,workerList);
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用相关功能", Toast.LENGTH_SHORT).show();
                            ActivityCompat.requestPermissions(this, permissions, 1);
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }
}