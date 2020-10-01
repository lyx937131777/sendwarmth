package com.example.sendwarmth.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.sendwarmth.MainActivity;
import com.example.sendwarmth.ServiceWorkActivity;
import com.example.sendwarmth.adapter.ServiceSubjectAdapter;
import com.example.sendwarmth.db.ServiceClass;
import com.example.sendwarmth.db.ServiceSubject;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.Utility;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ServiceSubjectPresenter
{
    private Context context;
    private SharedPreferences pref;

    public ServiceSubjectPresenter(Context context, SharedPreferences pref){
        this.context = context;
        this.pref = pref;
    }

    public void updateServiceSubject(final ServiceSubjectAdapter serviceSubjectAdapter, final String serviceClassName){
        String address = HttpUtil.LocalAddress + "/api/servicesubject/list";
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
                LogUtil.e("ServiceSubjectPresenter",responsData);
                List<ServiceSubject> internetList = Utility.handleServiceSubjectList(responsData);
                if(!serviceClassName.equals("全部服务")){
                    ServiceClass serviceClass = LitePal.where("name = ?",serviceClassName).findFirst(ServiceClass.class);
                    String serviceClassId = serviceClass.getInternetId();
                    List<ServiceSubject> temp = new ArrayList<>();
                    for(ServiceSubject serviceSubject : internetList){
                        if(serviceSubject.getServiceClassId().equals(serviceClassId)){
                            temp.add(serviceSubject);
                        }
                    }
                    serviceSubjectAdapter.setmList(temp);
                }else{
                    serviceSubjectAdapter.setmList(internetList);
                }

                ((ServiceWorkActivity)context).runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        serviceSubjectAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}
