package com.example.sendwarmth.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.sendwarmth.adapter.OrderAdapter;
import com.example.sendwarmth.db.Customer;
import com.example.sendwarmth.db.Order;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.Utility;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OrderPresenter
{
    private Context context;
    private SharedPreferences pref;

    public OrderPresenter(Context context, SharedPreferences pref){
        this.context = context;
        this.pref = pref;
    }

    public void updateOrderList(final OrderAdapter orderAdapter, final String[] types){
        String credential = pref.getString("credential","");
        Customer customer = LitePal.where("credential = ?",credential).findFirst(Customer.class);
        final String address = HttpUtil.LocalAddress + "/api/order/customer/list?customerId=" + customer.getInternetId();
        HttpUtil.getHttp(address, credential, new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "网络连接错误", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                final String responseData = response.body().string();
                LogUtil.e("OrderPresenter",responseData);
                if(Utility.checkResponse(responseData,context, address)){
                    List<Order> orderList = Utility.handleOrderList(responseData);
                    List<Order> typeOrderList = new ArrayList<>();
                    List<String> typeList = new ArrayList<>();
                    for(int i = 0; i < types.length; i++){
                        typeList.add(types[i]);
                    }
                    for(Order order : orderList){
                        if(typeList.contains(order.getState())){
                            typeOrderList.add(order);
                        }
                    }
                    orderAdapter.setmList(typeOrderList);
                    ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            orderAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }
}
