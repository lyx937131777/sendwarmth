package com.example.sendwarmth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sendwarmth.adapter.ProductItemAdapter;
import com.example.sendwarmth.db.ProductItem;
import com.example.sendwarmth.db.ProductOrder;
import com.example.sendwarmth.util.MapUtil;
import com.example.sendwarmth.util.TimeUtil;

import java.util.List;

public class ProductOrderDetailActivity extends AppCompatActivity
{
    private ProductOrder productOrder;
    private String state;
    private RecyclerView recyclerView;

    private TextView orderNumberText,stateText,nameText, telText, addressText,orderTimeText,businessText,totalPriceText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_order_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        productOrder = (ProductOrder) getIntent().getSerializableExtra("productOrder");
        state = productOrder.getState();

        orderNumberText = findViewById(R.id.order_number);
        stateText = findViewById(R.id.state);
        nameText = findViewById(R.id.name);
        telText = findViewById(R.id.tel);
        addressText = findViewById(R.id.address);
        orderTimeText = findViewById(R.id.order_time);
        businessText = findViewById(R.id.business);

        orderNumberText.setText(productOrder.getOrderNo());
        stateText.setText(MapUtil.getProductOrderState(state));
        nameText.setText(productOrder.getContactPerson());
        telText.setText(productOrder.getContactTel());
        addressText.setText(productOrder.getDeliveryAddress());
        orderTimeText.setText(TimeUtil.timeStampToString(productOrder.getOrderTime(),"yyyy-MM-dd HH:mm"));
        businessText.setText(productOrder.getBusinessInfo().getBusinessName());

        totalPriceText = findViewById(R.id.total_price);
        totalPriceText.setText("总价¥" + productOrder.getOrderPrice());

        button = findViewById(R.id.button);
        if(state.equals("un_paid")){
            button.setText("付款");
        }else if(state.equals("delivered")){
            button.setText("确认收货");
        }else if(state.equals("received")){
            button.setText("评价");
        }else {
            button.setVisibility(View.INVISIBLE);
        }

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<ProductItem> productItemList = productOrder.getProductItemInfos();
        ProductItemAdapter productItemAdapter = new ProductItemAdapter(productItemList);
        recyclerView.setAdapter(productItemAdapter);
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