package com.example.sendwarmth.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.example.sendwarmth.ProductOrderCommentActivity;
import com.example.sendwarmth.ProductOrderDetailActivity;
import com.example.sendwarmth.R;
import com.example.sendwarmth.alipay.PayDemoActivity;
import com.example.sendwarmth.alipay.util.OrderInfoUtil2_0;
import com.example.sendwarmth.db.Product;
import com.example.sendwarmth.db.ProductItem;
import com.example.sendwarmth.db.ProductOrder;
import com.example.sendwarmth.presenter.ProductOrderPresenter;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.MapUtil;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.litepal.LitePal;

public class ProductOrderAdapter extends RecyclerView.Adapter<ProductOrderAdapter.ViewHolder>
{
    private Context mContext;
    private List<ProductOrder> mList;
    private ProductOrderPresenter productOrderPresenter;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View view;
        ImageView state;
        TextView stateText;
        TextView business;
        RecyclerView recyclerView;
        TextView totalPrice;
        Button button;
        Button cancelButton;

        public ViewHolder(View view)
        {
            super(view);
            this.view = view;
            state = view.findViewById(R.id.state);
            stateText = view.findViewById(R.id.state_text);
            business = view.findViewById(R.id.business);
            recyclerView = view.findViewById(R.id.recycler);
            totalPrice = view.findViewById(R.id.total_price);
            button = view.findViewById(R.id.button);
            cancelButton = view.findViewById(R.id.cancel);
        }
    }

    public ProductOrderAdapter(List<ProductOrder> productOrderList, ProductOrderPresenter productOrderPresenter)
    {
        mList = productOrderList;
        this.productOrderPresenter = productOrderPresenter;
    }

    @NonNull
    @Override
    public ProductOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if(mContext == null)
        {
            mContext = parent.getContext();
        }
        initDialog();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_product_order,parent,false);
        final ProductOrderAdapter.ViewHolder holder = new ProductOrderAdapter.ViewHolder(view);
        holder.view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int position = holder.getAdapterPosition();
                ProductOrder productOrder = mList.get(position);
                Intent intent = new Intent(mContext, ProductOrderDetailActivity.class);
                intent.putExtra("productOrder", productOrder);
                mContext.startActivity(intent);
            }
        });
        holder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                ProductOrder productOrder = mList.get(position);
                final String orderId = productOrder.getInternetId();
                new AlertDialog.Builder(mContext)
                        .setTitle("提示")
                        .setMessage("确认取消订单么？")
                        .setPositiveButton("确定", new
                                DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        productOrderPresenter.cancelProductOrder(orderId);
                                    }
                                })
                        .setNegativeButton("取消", null).show();
            }
        });
        holder.button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                ProductOrder productOrder = mList.get(position);
                String state = productOrder.getState();
                orderId = productOrder.getInternetId();
                if (state.equals("un_paid")) {
                    selectPaymentMethodDialog.show();
//                    new AlertDialog.Builder(mContext)
//                            .setTitle("提示")
//                            .setMessage("确认支付么？")
//                            .setPositiveButton("确定", new
//                                    DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            productOrderPresenter.payProductOrder(orderId);
//                                        }
//                                    })
//                            .setNegativeButton("取消", null).show();
                }else if(state.equals("paid")){
                    new AlertDialog.Builder(mContext)
                            .setTitle("提示")
                            .setMessage("确认申请退款么？")
                            .setPositiveButton("确定", new
                                    DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            productOrderPresenter.refundProductOrder(orderId);
                                        }
                                    })
                            .setNegativeButton("取消",null).show();
                }else if(state.equals("delivered")){
                    new AlertDialog.Builder(mContext)
                            .setTitle("提示")
                            .setMessage("确认收货么？")
                            .setPositiveButton("确定", new
                                    DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            productOrderPresenter.receiveProductOrder(orderId);
                                        }
                                    })
                            .setNegativeButton("取消",null).show();
                }else if(state.equals("received")){
                    Intent intent = new Intent(mContext, ProductOrderCommentActivity.class);
                    intent.putExtra("productOrder", productOrder);
                    mContext.startActivity(intent);
                }

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductOrderAdapter.ViewHolder holder, int position)
    {
        ProductOrder productOrder = mList.get(position);
        holder.business.setText(productOrder.getBusinessInfo().getBusinessName());
        String state = productOrder.getState();
        Glide.with(mContext).load(MapUtil.getProductState(state)).into(holder.state);
        holder.stateText.setText(MapUtil.getProductOrderState(state));
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        List<ProductItem> productItemList = productOrder.getProductItemInfos();
        ProductItemAdapter productItemAdapter = new ProductItemAdapter(productItemList,productOrder);
        holder.recyclerView.setAdapter(productItemAdapter);
        holder.totalPrice.setText("总价¥" + productOrder.getOrderPrice());

        if(!state.equals("un_paid")){
            holder.cancelButton.setVisibility(View.GONE);
        }

        if(state.equals("un_paid")){
            holder.button.setVisibility(View.VISIBLE);
            holder.button.setText("付款");
        }else if(state.equals("paid")){
            holder.button.setVisibility(View.VISIBLE);
            holder.button.setText("申请退款");
        } else if(state.equals("delivered")){
            holder.button.setVisibility(View.VISIBLE);
            holder.button.setText("确认收货");
        }else if(state.equals("received")){
            holder.button.setVisibility(View.VISIBLE);
            holder.button.setText("评价");
        }else {
            holder.button.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount()
    {
        return mList.size();
    }

    public List<ProductOrder> getmList()
    {
        return mList;
    }

    public void setmList(List<ProductOrder> mList)
    {
        this.mList = mList;
    }


    //TODO 支付选择窗口
    private Dialog selectPaymentMethodDialog;
    private Button pay;
    private RadioGroup radioGroup;
    private RadioButton alipay,wechat;
    private int paymentMethod;
    private String orderId;

    private void initDialog()
    {
        selectPaymentMethodDialog = new Dialog(mContext);
        View view = View.inflate(mContext, R.layout.dialog_select_payment_method, null);
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
                    productOrderPresenter.payProductOrder(orderId);
                }else {
                    productOrderPresenter.payProductOrderWX(orderId);
//                    Toast.makeText(mContext, "暂时不支持微信支付，请等待后续版本。",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void dismissDialog(){
        if(selectPaymentMethodDialog != null){
            selectPaymentMethodDialog.dismiss();
        }
    }
}
