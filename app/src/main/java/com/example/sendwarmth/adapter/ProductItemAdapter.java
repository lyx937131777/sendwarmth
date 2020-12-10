package com.example.sendwarmth.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sendwarmth.R;
import com.example.sendwarmth.db.Product;
import com.example.sendwarmth.db.ProductItem;
import com.example.sendwarmth.db.ProductOrder;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.MapUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemAdapter.ViewHolder>
{
    private Context mContext;
    private List<ProductItem> mList;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View view;
        ImageView picture;
        TextView title;
        TextView description;
        TextView price;
        TextView count;

        public  ViewHolder(View view)
        {
            super(view);
            this.view = view;
            picture = view.findViewById(R.id.picture);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            price = view.findViewById(R.id.price);
            count = view.findViewById(R.id.count);
        }
    }

    public ProductItemAdapter(List<ProductItem> productItemList){
        mList = productItemList;
    }

    @Override
    public ProductItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(mContext == null)
        {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_product_item, parent,false);
        final ProductItemAdapter.ViewHolder holder = new ProductItemAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ProductItemAdapter.ViewHolder holder, int position)
    {
        ProductItem productItem = mList.get(position);
        Product product = productItem.getProductInfo();
        Glide.with(mContext).load(HttpUtil.getResourceURL(product.getProductPic())).into(holder.picture);
        holder.title.setText(product.getProductName());
        holder.description.setText(product.getProductDes());
        holder.price.setText("￥"+ productItem.getPrice());
        holder.count.setText("x" + productItem.getProductNum());
    }
    @Override
    public int getItemCount()
    {
        return mList.size();
    }

    public List<ProductItem> getmList()
    {
        return mList;
    }

    public void setmList(List<ProductItem> mList)
    {
        this.mList = mList;
    }
}
