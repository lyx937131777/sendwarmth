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
import android.widget.Toast;

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
import com.example.sendwarmth.util.MapUtil;
import com.example.sendwarmth.util.TimeUtil;

import org.litepal.LitePal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OrderingActivity extends AppCompatActivity implements DateAndTimePickerDialog.DateAndTimePickerDialogInterface
{
    private static final int COMMAND_START = 0;
    private static final int COMMAND_END = 1;
    private ServiceSubject serviceSubject;
    private Spinner tipSpinner;
    private List<String> tipList = new ArrayList<>();
    private ArrayAdapter<String> tipArrayAdapter;
    private Spinner workerSpiner;
    private List<Worker> workerList = new ArrayList<>();
    private ArrayAdapter<Worker> workerArraryAdapter;
    private String workerId, worker;
    private Spinner urgentSpinner;
    private List<String> urgentList = new ArrayList<>();
    private ArrayAdapter<String> urgentArrayAdapter;
    private String orderType = "book_order";
    private Spinner timeUnitSpinner;
    private List<String> timeUnitList = new ArrayList<>();
    private ArrayAdapter<String> timeUnitArrayAdapter;
    private int timeUnit = 1;

    private int tip;
    private double price;
    private TextView priceText;

    private CardView attendantCard, timeUnitCard,endTimeCard;
    private Button locationButton;
    private TextView addressText;
    private TextView defaultAddress;
    private TextView startTimeText,endTimeText;
    private double longitude;
    private double latitude;
    private String address;
    private EditText houseNumText, telText,messageText;
    private Button orderButton;

    private DateAndTimePickerDialog dateAndTimePickerDialog;
    private long startTime = 0,endTime = 0;
    private int command = 0;

//    public LocationClient mLocationClient;

    private SharedPreferences pref;
    private String credential;
    private Customer customer;

    private OrderingPresenter orderingPresenter;

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
        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(this)).build();
        orderingPresenter = myComponent.orderingPresenter();

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
        attendantCard = findViewById(R.id.attendant_card);
        timeUnitCard = findViewById(R.id.time_unit_card);
        endTimeCard = findViewById(R.id.end_time_card);
        addressText = findViewById(R.id.address);
        houseNumText = findViewById(R.id.house_num);
        defaultAddress = findViewById(R.id.default_address);
        telText = findViewById(R.id.tel);
        startTimeText = findViewById(R.id.start_time);
        endTimeText = findViewById(R.id.end_time);
        messageText = findViewById(R.id.message);
        orderButton = findViewById(R.id.order);
        orderButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(orderingPresenter.checkOrder(telText.getText().toString(),startTime,endTime,houseNumText.getText().toString(),address,longitude,latitude)){
                    new AlertDialog.Builder(OrderingActivity.this)
                            .setTitle("提示")
                            .setMessage("是否确认下单？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                        orderingPresenter.postOrder(worker, longitude, latitude, telText.getText().toString(), address, houseNumText.getText().toString(), orderType,
                                                serviceSubject.getServiceClassId(),serviceSubject.getInternetId(),startTime,endTime,timeUnit,messageText.getText().toString(),tip);
                                }
                            })
                            .setNegativeButton("取消",null)
                            .show();
                }

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
        urgentArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,urgentList);
        urgentArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        urgentSpinner.setAdapter(urgentArrayAdapter);

        initTimeUnit();
        timeUnitArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,timeUnitList);
        timeUnitArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeUnitSpinner.setAdapter(timeUnitArrayAdapter);

        dateAndTimePickerDialog = new DateAndTimePickerDialog(this);
        startTime = 0;
        endTime = 0;
        startTimeText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                command = COMMAND_START;
                dateAndTimePickerDialog.showDateAndTimePickerDialog(2,startTime);
            }
        });
//        endTimeText.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                command = COMMAND_END;
//                dateAndTimePickerDialog.showDateAndTimePickerDialog(2,endTime);
//            }
//        });
        defaultAddress.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        defaultAddress.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                initDefaultAddress();
            }
        });
        locationButton = findViewById(R.id.location);
        locationButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(OrderingActivity.this, LocationSelectActivity.class);
                startActivityForResult(intent,1);
            }
        });

        initDefaultAddress();
        telText.setText(customer.getCustomerTel());

        if(serviceSubject.getServiceClassInfo().getOrderWorkType().equals("all")){
            attendantCard.setVisibility(View.GONE);
        }
        if(serviceSubject.isFixed()){
            timeUnitCard.setVisibility(View.GONE);
            endTimeCard.setVisibility(View.GONE);
        }
    }

    private void initDefaultAddress()
    {
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        credential = pref.getString("credential","");
        customer = LitePal.where("credential = ?",credential).findFirst(Customer.class);
        address = customer.getCustomerAddress();
        addressText.setText(address);
        houseNumText.setText(customer.getHouseNum());
        longitude = customer.getLongitude();
        latitude = customer.getLatitude();
        orderingPresenter.updateWorker(longitude,latitude,workerArraryAdapter,workerList);
    }

    private void initTimeUnit()
    {
        timeUnitSpinner = findViewById(R.id.time_unit);
        timeUnitList.add("   1   ");
        timeUnitList.add("   2   ");
        timeUnitList.add("   3   ");
        timeUnitList.add("   4   ");
        timeUnitList.add("   5   ");
        timeUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                timeUnit = i+1;
                changePrice();
                if(startTime != 0){
                    endTime = startTime + timeUnit * 45 * 60 * 1000;
                    Date date = new Date(endTime);
                    endTimeText.setText(TimeUtil.dateToString(date,"yyyy-MM-dd HH:mm"));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
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
                worker = workerList.get(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    private void initUrgent(){
        urgentSpinner = findViewById(R.id.urgent);
        urgentList.add("预约订单");
        urgentList.add("加急订单");
        urgentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                orderType = MapUtil.getOrderType(urgentList.get(i));
                changePrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    private void changePrice(){
        double priceBase = 0;
        if(orderType.equals("book_order")){
            priceBase = serviceSubject.getSalaryPerHour()* timeUnit;
        }else {
            priceBase = serviceSubject.getHurrySalaryPerHour()* timeUnit;
        }
        price = priceBase + tip;
        BigDecimal b = new BigDecimal(price);
        price = b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
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
                    address = data.getStringExtra("address");
                    addressText.setText(address);
                    longitude = data.getDoubleExtra("longitude",0);
                    latitude = data.getDoubleExtra("latitude",0);
                    orderingPresenter.updateWorker(longitude,latitude,workerArraryAdapter,workerList);
                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void positiveListener()
    {
        Calendar calendar = dateAndTimePickerDialog.getCalendar();
        switch (command){
            case COMMAND_START:
                long selectedTime = calendar.getTimeInMillis();
                long time = Calendar.getInstance().getTimeInMillis();
                if(selectedTime < time - 60 * 1000){
                    Toast.makeText(this,"不可选择已过去的时间点",Toast.LENGTH_LONG).show();
                    break;
                }
                startTimeText.setText(TimeUtil.dateToString(calendar.getTime(),"yyyy-MM-dd HH:mm"));
                startTime = calendar.getTimeInMillis();
                endTime = startTime + timeUnit * 45 * 60 * 1000;
                Date date = new Date(endTime);
                endTimeText.setText(TimeUtil.dateToString(date,"yyyy-MM-dd HH:mm"));
                break;
            case COMMAND_END:
                endTimeText.setText(TimeUtil.dateToString(calendar.getTime(),"yyyy-MM-dd HH:mm"));
                endTime = calendar.getTimeInMillis();
                break;
        }
//        if(endTime > startTime){
//            long duration = endTime - startTime;
//            long minute = duration / 60000;
//            timeUnit = (double)minute / 45;
//            changePrice();
//            LogUtil.e("OrderingActivity","time start: " + startTime + " end: "+ endTime +  " duration: " + duration + " minute: " + minute + " hour: " + timeUnit);
//        }else{
//            timeUnit = 0;
//            changePrice();
//            if(startTime != 0 && endTime != 0){
//                Toast.makeText(this,"提示：结束时间需晚于上门时间！",Toast.LENGTH_LONG).show();
//            }
//        }
    }

    @Override
    public void negativeListener()
    {

    }
}