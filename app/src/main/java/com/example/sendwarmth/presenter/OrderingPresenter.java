package com.example.sendwarmth.presenter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.sendwarmth.NewInterestingActivityActivity;
import com.example.sendwarmth.OrderingActivity;
import com.example.sendwarmth.ProductOrderingActivity;
import com.example.sendwarmth.db.Worker;
import com.example.sendwarmth.util.CheckUtil;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.Utility;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OrderingPresenter
{
    private Context context;
    private SharedPreferences pref;
    private CheckUtil checkUtil;
    private ProgressDialog progressDialog;

    public OrderingPresenter(Context context, SharedPreferences pref, CheckUtil checkUtil){
        this.context = context;
        this.pref = pref;
        this.checkUtil = checkUtil;
    }

    public void updateWorker(double longitude, double latitude, final ArrayAdapter<Worker> workerArrayAdapter, final List<Worker> workerList){
        final String address = HttpUtil.LocalAddress + "/api/order/storeWorker";
        final String credential = pref.getString("credential","");
        LogUtil.e("OrderingPresenter","longitude: " + longitude + "   latitude: " + latitude);
        HttpUtil.getStoreWorker(address, credential, longitude, latitude, new Callback()
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
                LogUtil.e("OrderingPresenter",responseData);
                if(Utility.checkResponse(responseData,context,address)){
                    workerList.clear();
                    Worker noWorker = new Worker();
                    noWorker.setWorkerName("不指定");
                    noWorker.setInternetId("0");
                    workerList.add(noWorker);
                    List<Worker> tempWorkerList = Utility.handleWorkerList(responseData);
                    if (tempWorkerList != null){
                        workerList.addAll(tempWorkerList);
                    }
                    ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            workerArrayAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    public boolean checkOrder(String tel, long startTime, long endTime, String houseNum, String address, double longitude, double latitude){
        return checkUtil.checkOrder(tel,startTime,endTime,houseNum,address,longitude,latitude);
    }

    public void postOrder(String appointedPerson, double longitude, double latitude, String tel, String customerAddress, String houseNum, String orderType,
                          String serviceClassId, String serviceSubjectId, long startTime, long endTime, String message, double tip){
        progressDialog = ProgressDialog.show(context,"","生成订单中...");
        final String address = HttpUtil.LocalAddress + "/api/order";
        String credential = pref.getString("credential","");
        HttpUtil.postOrderRequest(address, credential, appointedPerson, longitude, latitude, tel, customerAddress, houseNum, orderType,
                serviceClassId, serviceSubjectId, startTime, endTime, message, tip, new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                ((AppCompatActivity)context).runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(context, "网络连接错误", Toast.LENGTH_LONG).show();
                    }
                });
                progressDialog.dismiss();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                final String responseData = response.body().string();
                LogUtil.e("OrderingPresenter",responseData);
                if(Utility.checkResponse(responseData,context,address)){
                    progressDialog.dismiss();
                    ((AppCompatActivity)context).runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            new AlertDialog.Builder(context)
                                    .setTitle("提示")
                                    .setMessage("订单生成成功")
                                    .setCancelable(false)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ((OrderingActivity)context).finish();
                                        }
                                    })
                                    .show();
                        }
                    });
                }
            }
        });
    }
}
