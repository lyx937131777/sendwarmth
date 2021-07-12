package com.example.sendwarmth.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sendwarmth.ModifyInformationActivity;
import com.example.sendwarmth.MyInformationActivity;
import com.example.sendwarmth.db.Account;
import com.example.sendwarmth.db.Customer;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.Utility;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ModifyInformationPresenter {

    private Context context;
    private SharedPreferences pref;
    private ProgressDialog progressDialog;
    private double longitude,latitude;

    public ModifyInformationPresenter(Context context, SharedPreferences pref){
        this.context = context;
        this.pref = pref;
    }

    public void modifyCustomer(String attribute, String value){
        progressDialog = ProgressDialog.show(context, "", "保存中...");
        final String address = HttpUtil.LocalAddress + "/api/customer";
        final String credential = pref.getString("credential","");
        Customer customer = LitePal.where("credential = ?", credential).findFirst(Customer.class);
        switch (attribute){
            case "userName":
                customer.setUserName(value);
                break;
            case "customerName":
                customer.setCustomerName(value);
                break;
            case "houseNum":
                customer.setHouseNum(value);
                break;
            case "personalDescription":
                customer.setPersonalDescription(value);
                break;
            case "customerAddress":
                customer.setCustomerAddress(value);
                customer.setLongitude(longitude);
                customer.setLatitude(latitude);
                break;
        }
        HttpUtil.modifyCustomerRequest(address,credential, customer, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "网络连接错误", Toast.LENGTH_LONG).show();
                    }
                });
                progressDialog.dismiss();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                LogUtil.e("ModifyInformationPresenter",responseData);
                if(Utility.checkResponse(responseData,context,address)){
                    customer.save();
                    ((ModifyInformationActivity)context).finish();
                    ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "保存成功", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                progressDialog.dismiss();
            }
        });
    }

    public void setLL(double longitude, double latitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
