package com.example.sendwarmth.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.sendwarmth.MainActivity;
import com.example.sendwarmth.adapter.ServiceClassAdapter;
import com.example.sendwarmth.db.ServiceClass;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.Utility;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomePresenter
{
    private Context context;
    private SharedPreferences pref;

    public HomePresenter(Context context, SharedPreferences pref){
        this.context = context;
        this.pref = pref;
    }

    public void updateServiceClass(final ServiceClassAdapter serviceClassAdapter){
        String address = HttpUtil.LocalAddress + "/api/serviceclass/list";
        String credential = pref.getString("credential",null);
        HttpUtil.getHttp(address, credential, new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                e.printStackTrace();
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
                LogUtil.e("HomePresenter",responseData);
                if(Utility.checkResponse(responseData,context)){
                    List<ServiceClass> internetList = Utility.handleServiceClassList(responseData);
                    List<ServiceClass> localList = LitePal.findAll(ServiceClass.class);
                    for(ServiceClass localServiceClass : localList){
                        String internetId = localServiceClass.getInternetId();
                        boolean flag = true;
                        for(ServiceClass internetServiceClass : internetList){
                            if(internetServiceClass.getInternetId().equals(internetId)){
                                flag = false;
                                break;
                            }
                        }
                        if(flag){
                            localServiceClass.delete();
                        }
                    }
                    LogUtil.e("HomePresenter","internetList number: " + internetList.size());
                    for(ServiceClass serviceClass : internetList){
                        ServiceClass temp = LitePal.where("internetId = ?",serviceClass.getInternetId()).findFirst(ServiceClass.class);
                        if(temp == null){
                            serviceClass.save();
                            LogUtil.e("HomePresenter","save: " + serviceClass.getName());
                        }else{
                            temp.setImage(serviceClass.getImage());
                            temp.setName(serviceClass.getName());
                            temp.setOrderWorkType(serviceClass.getOrderWorkType());
                            temp.setDes(serviceClass.getDes());
                            temp.save();
                        }
                    }
                    List<ServiceClass> serviceClassList = LitePal.order("clickCount desc").limit(7).find(ServiceClass.class);
                    LogUtil.e("HomePresenter","databaseList number: " + serviceClassList.size());
                    ServiceClass serviceClass = new ServiceClass();
                    serviceClass.setName("全部服务");
                    serviceClassList.add(serviceClass);
                    serviceClassAdapter.setmList(serviceClassList);
                    ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            serviceClassAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }
}
