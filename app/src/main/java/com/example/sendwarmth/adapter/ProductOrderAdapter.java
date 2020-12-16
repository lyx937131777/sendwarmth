package com.example.sendwarmth.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sendwarmth.ProductOrderDetailActivity;
import com.example.sendwarmth.R;
import com.example.sendwarmth.db.ProductItem;
import com.example.sendwarmth.db.ProductOrder;
import com.example.sendwarmth.util.MapUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProductOrderAdapter extends RecyclerView.Adapter<ProductOrderAdapter.ViewHolder>
{
    private Context mContext;
    private List<ProductOrder> mList;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View view;
        ImageView state;
        TextView stateText;
        TextView business;
        RecyclerView recyclerView;
        TextView totalPrice;
        Button button;

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
        }
    }

    public ProductOrderAdapter(List<ProductOrder> productOrderList)
    {
        mList = productOrderList;
    }

    @NonNull
    @Override
    public ProductOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if(mContext == null)
        {
            mContext = parent.getContext();
        }
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

        if(state.equals("un_paid")){
            holder.button.setVisibility(View.VISIBLE);
            holder.button.setText("付款");
        }else if(state.equals("delivered")){
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
}
