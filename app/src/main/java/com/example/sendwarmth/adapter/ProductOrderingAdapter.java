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
import com.example.sendwarmth.util.HttpUtil;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ProductOrderingAdapter extends RecyclerView.Adapter<ProductOrderingAdapter.ViewHolder>
{
    private Context mContext;
    private List<Product> mList;

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

    public ProductOrderingAdapter(List<Product> productList){
        mList = productList;
    }

    @Override
    public ProductOrderingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(mContext == null)
        {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_product_ordering, parent,false);
        final ProductOrderingAdapter.ViewHolder holder = new ProductOrderingAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ProductOrderingAdapter.ViewHolder holder, int position)
    {
        Product product = mList.get(position);
        Glide.with(mContext).load(HttpUtil.getResourceURL(product.getProductPic())).into(holder.picture);
        holder.title.setText(product.getProductName());
        holder.description.setText(product.getProductDes());
        holder.price.setText("￥"+ product.getProductPrice());
        holder.count.setText("x" + product.getSelectedCount());
    }
    @Override
    public int getItemCount()
    {
        return mList.size();
    }

    public List<Product> getmList()
    {
        return mList;
    }

    public void setmList(List<Product> mList)
    {
        this.mList = mList;
    }
}
