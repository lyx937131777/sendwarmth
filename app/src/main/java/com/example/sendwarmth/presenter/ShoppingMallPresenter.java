package com.example.sendwarmth.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.sendwarmth.MainActivity;
import com.example.sendwarmth.adapter.ProductClassAdapter;
import com.example.sendwarmth.adapter.TabAdapter;
import com.example.sendwarmth.db.InterestingActivity;
import com.example.sendwarmth.db.Product;
import com.example.sendwarmth.db.ProductClass;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.Utility;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ShoppingMallPresenter
{
    private Context context;
    private SharedPreferences pref;

    public ShoppingMallPresenter(Context context, SharedPreferences pref){
        this.context = context;
        this.pref = pref;
    }

    public void updateProductClass(final ProductClassAdapter productClassAdapter, final TabAdapter tabAdapter, final List<ProductClass> productClassList){
        String address = HttpUtil.LocalAddress + "/api/productclass/list";
        String credential = pref.getString("credential",null);
        HttpUtil.getHttp(address, credential, new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                e.printStackTrace();
                ((MainActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "网络连接错误", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                final String responsData = response.body().string();
                LogUtil.e("ShoppingMallPresenter",responsData);
                List<ProductClass> tempList = Utility.handleProductClassList(responsData);
                if(tempList != null){
                    productClassList.addAll(tempList);
                }
                productClassAdapter.setmList(productClassList);
                tabAdapter.setmList(productClassList);
                ((MainActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        productClassAdapter.notifyDataSetChanged();
                        tabAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    public void updateProduct(final ProductClassAdapter productClassAdapter){
        String address = HttpUtil.LocalAddress + "/api/product/getClass";
        String credential = pref.getString("credential",null);
        HttpUtil.getHttp(address, credential, new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                e.printStackTrace();
                ((MainActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "网络连接错误", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                final String responsData = response.body().string();
                LogUtil.e("ProductClassPresenter",responsData);
                List<Product> productList = Utility.handleProductList(responsData);
                productClassAdapter.setProductList(productList);
                ((MainActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        productClassAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}
