package com.example.sendwarmth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.sendwarmth.db.Order;
import com.example.sendwarmth.util.MapUtil;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity implements View.OnClickListener
{
    private Order order;

    private NestedScrollView nestedScrollView;
    private CardView attendantNameCard;
    private TextView attendantName;
    private CardView attendantTelCard;
    private TextView attendantTel;
    private TextView state;
    private Button button;
    private Button buttonLeft;
    private Button buttonRight;



    public LocationClient mLocationClient;
    private MapView mapView;
    private BaiduMap baiduMap;
    private boolean isFirstLocate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_order_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        order = (Order) getIntent().getSerializableExtra("order");

        nestedScrollView = findViewById(R.id.nested_scroll_view);
        attendantNameCard = findViewById(R.id.attendant_name_card);
        attendantName = findViewById(R.id.attendant_name);
        attendantTelCard = findViewById(R.id.attendant_tel_card);
        attendantTel = findViewById(R.id.attendant_tel);

        state = findViewById(R.id.state);
        button = findViewById(R.id.button);
        buttonLeft = findViewById(R.id.button_left);
        buttonRight = findViewById(R.id.button_right);

        state.setText(MapUtil.getOrderState(order.getState()));
        if(order.getState().equals("toBePaid")){
            button.setVisibility(View.GONE);
        }else if(order.getState().equals("unstart") || order.getState().equals("waiting")){
            buttonLeft.setVisibility(View.GONE);
            buttonRight.setVisibility(View.GONE);
        }else if(order.getState().equals("toBeEvaluated")) {
            button.setText("评价此订单");
            buttonLeft.setVisibility(View.GONE);
            buttonRight.setVisibility(View.GONE);
        }else {
            button.setVisibility(View.GONE);
            buttonLeft.setVisibility(View.GONE);
            buttonRight.setVisibility(View.GONE);
        }

        attendantNameCard.setOnClickListener(this);
        attendantName.setOnClickListener(this);
        attendantTelCard.setOnClickListener(this);
        attendantTel.setOnClickListener(this);

        mapView = (MapView) findViewById(R.id.bmapView);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
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
        baiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener()
        {
            @Override
            public void onTouch(MotionEvent motionEvent)
            {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    nestedScrollView.requestDisallowInterceptTouchEvent(false);
                } else {
                    nestedScrollView.requestDisallowInterceptTouchEvent(true);
                }
            }
        });

        if(!order.getState().equals("running")){
            mapView.setVisibility(View.GONE);
        }
    }

    private void navigateTo(BDLocation location) {
        if (isFirstLocate) {
//            Toast.makeText(this, "nav to " + location.getAddrStr(), Toast.LENGTH_SHORT).show();
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);
    }

    private class MyLocationListener extends BDAbstractLocationListener
    {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location.getLocType() == BDLocation.TypeGpsLocation
                    || location.getLocType() == BDLocation.TypeNetWorkLocation) {
                navigateTo(location);
            }
        }

    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");//百度API保护隐私 默认获取火星坐标 加上这一行可直接获得真实坐标
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用地图功能", Toast.LENGTH_SHORT).show();
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
    public void onClick(View view)
    {
        switch (view.getId()){
            case R.id.attendant_name_card:
            case R.id.attendant_name:{
                Intent intent = new Intent(OrderDetailActivity.this, AttendantInformationActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.attendant_tel_card:
            case R.id.attendant_tel:{
                final String tel = attendantTel.getText().toString();
                new AlertDialog.Builder(OrderDetailActivity.this).setTitle("提示")
                        .setMessage("是否拨打电话"+tel+"？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:"+tel));
                                startActivity(intent);
                            }
                        }).setNegativeButton("取消",null).show();
                break;
            }
        }
    }
}