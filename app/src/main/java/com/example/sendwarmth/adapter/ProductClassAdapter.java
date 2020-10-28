package com.example.sendwarmth.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sendwarmth.R;
import com.example.sendwarmth.db.Product;
import com.example.sendwarmth.db.ProductClass;
import com.example.sendwarmth.presenter.ShoppingMallPresenter;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProductClassAdapter extends RecyclerView.Adapter<ProductClassAdapter.ViewHolder>
{
    private Context mContext;
    private List<ProductClass> mList;
    private List<Product> productList = new ArrayList<>();
    private ShoppingMallPresenter shoppingMallPresenter;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View view;
        TextView productClass;
        RecyclerView recyclerView;

        public  ViewHolder(View view)
        {
            super(view);
            this.view = view;
            productClass = view.findViewById(R.id.product_class);
            recyclerView = view.findViewById(R.id.recycler_product);
        }
    }

    public ProductClassAdapter(List<ProductClass> menuList, ShoppingMallPresenter shoppingMallPresenter){
        mList = menuList;
        this.shoppingMallPresenter = shoppingMallPresenter;
    }

    @Override
    public ProductClassAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(mContext == null)
        {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_product_class, parent,false);
        final ProductClassAdapter.ViewHolder holder = new ProductClassAdapter.ViewHolder(view);
        holder.view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int position = holder.getAdapterPosition();
                ProductClass productClass = mList.get(position);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ProductClassAdapter.ViewHolder holder, int position)
    {
        ProductClass productClass = mList.get(position);
        holder.productClass.setText(productClass.getName());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        List<Product> mProductList = new ArrayList<>();
        for(Product product : productList){
            if(product.getProductClassId().equals(productClass.getInternetId())){
                mProductList.add(product);
            }
        }
        ProductAdapter productAdapter = new ProductAdapter(mProductList,shoppingMallPresenter);
        holder.recyclerView.setAdapter(productAdapter);
    }

    @Override
    public int getItemCount()
    {
        return mList.size();
    }

    public void refresh(){
        for(Product product : productList){
            Product localProduct = LitePal.where("internetId = ?",product.getInternetId()).findFirst(Product.class);
            if(localProduct == null){
                product.setSelectedCount(0);
            }else{
                product.setSelectedCount(localProduct.getSelectedCount());
            }
        }
        notifyDataSetChanged();
    }

    public List<ProductClass> getmList()
    {
        return mList;
    }

    public void setmList(List<ProductClass> mList)
    {
        this.mList = mList;
    }

    public List<Product> getProductList()
    {
        return productList;
    }

    public void setProductList(List<Product> productList)
    {
        this.productList = productList;
    }
}
