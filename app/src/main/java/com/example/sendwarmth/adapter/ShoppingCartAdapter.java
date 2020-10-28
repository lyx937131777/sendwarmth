package com.example.sendwarmth.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sendwarmth.R;
import com.example.sendwarmth.db.Product;
import com.example.sendwarmth.presenter.ShoppingMallPresenter;
import com.example.sendwarmth.util.HttpUtil;

import org.litepal.LitePal;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder>
{
    private Context mContext;
    private List<Product> mList;

    private ShoppingMallPresenter shoppingMallPresenter;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View view;
        ImageView picture;
        TextView title;
        TextView price;
        Button addButton;
        Button minusButton;
        TextView count;

        public  ViewHolder(View view)
        {
            super(view);
            this.view = view;
            picture = view.findViewById(R.id.picture);
            title = view.findViewById(R.id.title);
            price = view.findViewById(R.id.price);
            addButton = view.findViewById(R.id.add_button);
            count = view.findViewById(R.id.count);
            minusButton = view.findViewById(R.id.minus_button);
        }
    }

    public ShoppingCartAdapter(List<Product> productList, ShoppingMallPresenter shoppingMallPresenter){
        mList = productList;
        this.shoppingMallPresenter = shoppingMallPresenter;
    }

    @Override
    public ShoppingCartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(mContext == null)
        {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_shopping_cart, parent,false);
        final ShoppingCartAdapter.ViewHolder holder = new ShoppingCartAdapter.ViewHolder(view);
        holder.addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int position = holder.getAdapterPosition();
                Product product = mList.get(position);
                product.add(1);
                Product localProduct = LitePal.where("internetId = ?",product.getInternetId()).findFirst(Product.class);
                if(localProduct == null){
                    product.save();
                }else{
                    localProduct.setSelectedCount(product.getSelectedCount());
                    localProduct.save();
                }
                shoppingMallPresenter.refresh();
            }
        });
        holder.minusButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int position = holder.getAdapterPosition();
                Product product = mList.get(position);
                product.minus(1);
                Product localProduct = LitePal.where("internetId = ?",product.getInternetId()).findFirst(Product.class);
                if(localProduct == null){
                    product.save();
                }else{
                    localProduct.setSelectedCount(product.getSelectedCount());
                    localProduct.save();
                }
                if(product.getSelectedCount() == 0){
                    mList.remove(product);
                }
                shoppingMallPresenter.refresh();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ShoppingCartAdapter.ViewHolder holder, int position)
    {
        Product product = mList.get(position);
        Glide.with(mContext).load(HttpUtil.getResourceURL(product.getProductPic())).into(holder.picture);
        holder.title.setText(product.getProductName());
        holder.price.setText("￥"+ product.getProductPrice());
        if(product.getSelectedCount() > 0){
            holder.minusButton.setVisibility(View.VISIBLE);
            holder.count.setText(""+product.getSelectedCount());
        }else {
            holder.minusButton.setVisibility(View.GONE);
            holder.count.setText("");
        }
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
