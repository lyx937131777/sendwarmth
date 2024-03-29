package com.example.sendwarmth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sendwarmth.adapter.ProductItemAdapter;
import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.db.ProductItem;
import com.example.sendwarmth.db.ProductOrder;
import com.example.sendwarmth.presenter.ProductOrderDetailPresenter;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.MapUtil;
import com.example.sendwarmth.util.TimeUtil;
import com.example.sendwarmth.wxapi.WXPayEntryActivity;

import java.util.List;

public class ProductOrderDetailActivity extends AppCompatActivity
{
    private ProductOrder productOrder;
    private String state;
    private RecyclerView recyclerView;

    private TextView orderNumberText,stateText,nameText, telText, addressText,orderTimeText,businessText,totalPriceText;
    private Button cancelButton,button;

    private CardView logisticsCard;
    private TextView logisticsNumText, logisticsCompanyText;

    private Context context;
    private ProductOrderDetailPresenter productOrderDetailPresenter;

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

        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(this)).build();
        productOrderDetailPresenter = myComponent.productOrderDetailPresenter();
        context = this;
        initDialog();

        productOrder = (ProductOrder) getIntent().getSerializableExtra("productOrder");
        state = productOrder.getState();

        orderNumberText = findViewById(R.id.order_number);
        stateText = findViewById(R.id.state);
        logisticsCard = findViewById(R.id.logistics_card);
        logisticsNumText = findViewById(R.id.logistics_num);
        logisticsCompanyText = findViewById(R.id.logistics_compnay);
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

        if(state.equals("delivered") || state.equals("received") || state.equals("evaluated")){
            logisticsNumText.setText(productOrder.getLogisticsNo());
            logisticsCompanyText.setText(productOrder.getLogisticsCom());
        }else {
            logisticsCard.setVisibility(View.GONE);
        }

        cancelButton = findViewById(R.id.cancel);
        if(!state.equals("un_paid")){
            cancelButton.setVisibility(View.GONE);
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String orderId = productOrder.getInternetId();
                new AlertDialog.Builder(context)
                        .setTitle("提示")
                        .setMessage("确认取消订单么？")
                        .setPositiveButton("是", new
                                DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        productOrderDetailPresenter.cancelProductOrder(orderId);
                                    }
                                })
                        .setNegativeButton("否",null).show();
            }
        });

        button = findViewById(R.id.button);
        if(state.equals("un_paid")){
            button.setText("付款");
        }else if(state.equals("paid")){
            button.setText("申请退款");
        } else if(state.equals("delivered")){
            button.setText("确认收货");
        }else if(state.equals("received")){
            button.setText("评价");
        }else {
            button.setVisibility(View.INVISIBLE);
        }

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final String orderId = productOrder.getInternetId();
                if(state.equals("un_paid")){
                    selectPaymentMethodDialog.show();
//                    new AlertDialog.Builder(context)
//                            .setTitle("提示")
//                            .setMessage("确认支付么？")
//                            .setPositiveButton("确定", new
//                                    DialogInterface.OnClickListener()
//                                    {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which)
//                                        {
//                                            productOrderDetailPresenter.payProductOrder(orderId);
//                                        }
//                                    })
//                            .setNegativeButton("取消",null).show();
                }else if(state.equals("paid")){
                    new AlertDialog.Builder(context)
                            .setTitle("提示")
                            .setMessage("确认申请退款么？")
                            .setPositiveButton("确定", new
                                    DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            productOrderDetailPresenter.refundProductOrder(orderId);
                                        }
                                    })
                            .setNegativeButton("取消",null).show();
                }else if(state.equals("delivered")){
                    new AlertDialog.Builder(context)
                            .setTitle("提示")
                            .setMessage("确认收货么？")
                            .setPositiveButton("确定", new
                                    DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            productOrderDetailPresenter.receiveProductOrder(orderId);
                                        }
                                    })
                            .setNegativeButton("取消",null).show();
                }else if(state.equals("received")){
                    Intent intent = new Intent(context, ProductOrderCommentActivity.class);
                    intent.putExtra("productOrder",productOrder);
                    startActivityForResult(intent,1);
                }
            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    finish();
                }
                break;
        }
    }

    //TODO 支付选择窗口
    private Dialog selectPaymentMethodDialog;
    private Button pay;
    private RadioGroup radioGroup;
    private RadioButton alipay,wechat;
    private int paymentMethod;

    private void initDialog()
    {
        selectPaymentMethodDialog = new Dialog(context);
        View view = View.inflate(context, R.layout.dialog_select_payment_method, null);
        selectPaymentMethodDialog.setContentView(view);
        selectPaymentMethodDialog.setCanceledOnTouchOutside(true);
        //view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(this).getScreenHeight() *
        // 0.23f));
        Window dialogWindow = selectPaymentMethodDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //lp.width = (int) (ScreenSizeUtils.getInstance(this).getScreenWidth() * 0.9f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(lp);
        pay = view.findViewById(R.id.pay);
        radioGroup = view.findViewById(R.id.select_payment_method);
        alipay = view.findViewById(R.id.alipay);
        wechat = view.findViewById(R.id.weChat);
        paymentMethod = alipay.getId();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                paymentMethod = checkedId;
            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(paymentMethod == alipay.getId()){
                    productOrderDetailPresenter.payProductOrder(productOrder.getInternetId());
                }else {
                    productOrderDetailPresenter.payProductOrderWX(productOrder.getInternetId());
                }
            }
        });
    }

    public void dismissDialog(){
        if(selectPaymentMethodDialog != null){
            selectPaymentMethodDialog.dismiss();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.e("ProductOrderDetailActivity","onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.e("ProductOrderDetailActivity","onStop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.e("ProductOrderDetailActivity","onStart");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        LogUtil.e("ProductOrderDetailActivity","onResume");
        if(WXPayEntryActivity.isSuccess()){
            dismissDialog();
            finish();
        }else {
            LogUtil.e("ProductOrderDetailActivity","WXnotSuccess");
        }
    }

}