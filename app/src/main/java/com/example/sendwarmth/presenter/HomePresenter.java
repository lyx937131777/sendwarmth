package com.example.sendwarmth.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.sendwarmth.HealthBroadcastActivity;
import com.example.sendwarmth.InterestringActivityActivity;
import com.example.sendwarmth.MainActivity;
import com.example.sendwarmth.ServiceWorkSearchActivity;
import com.example.sendwarmth.adapter.CarouselBannerAdapter;
import com.example.sendwarmth.adapter.InterestingActivityAdapter;
import com.example.sendwarmth.adapter.RecommendServiceSubjectAdapter;
import com.example.sendwarmth.adapter.ServiceClassAdapter;
import com.example.sendwarmth.db.Carousel;
import com.example.sendwarmth.db.HealthBroadcast;
import com.example.sendwarmth.db.InterestingActivity;
import com.example.sendwarmth.db.ServiceClass;
import com.example.sendwarmth.db.ServiceSubject;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.Utility;
import com.sun.banner.BannerAdapter;
import com.sun.banner.BannerView;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomePresenter
{
    private Context context;
    private SharedPreferences pref;
    private List<InterestingActivity> joinedList = new ArrayList<>();

    public HomePresenter(Context context, SharedPreferences pref){
        this.context = context;
        this.pref = pref;
    }

    public void updateServiceClass(final ServiceClassAdapter serviceClassAdapter){
        final String address = HttpUtil.LocalAddress + "/api/serviceclass/list";
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
                if(Utility.checkResponse(responseData,context,address)){
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

    public void updateRecommendServiceSubject(final RecommendServiceSubjectAdapter recommendServiceSubjectAdapter){
        final String address = HttpUtil.LocalAddress + "/api/servicesubject/recommendList";
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
                if(Utility.checkResponse(responseData,context,address)){
                    List<ServiceSubject> serviceSubjectList = Utility.handleServiceSubjectList(responseData);
                    recommendServiceSubjectAdapter.setmList(serviceSubjectList);
                    ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recommendServiceSubjectAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    public void updateCarousel(final BannerView bannerView){
        final String address = HttpUtil.LocalAddress + "/api/carousel/list";
        String credential = pref.getString("credential",null);
        HttpUtil.getHttp(address, credential, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "网络连接错误", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                LogUtil.e("HomePresenter",responseData);
                if(Utility.checkResponse(responseData,context,address)){
                    final List<Carousel> carouselList = Utility.handleCarouselList(responseData);
                    updateJoinedInterestingActivity(bannerView,carouselList);
                }
            }
        });
    }

    public void updateJoinedInterestingActivity(final BannerView bannerView, final List<Carousel> carouselList){
        final String address = HttpUtil.LocalAddress + "/api/activity/my";
        String credential = pref.getString("credential",null);
        String type = "join";

        HttpUtil.getJoinedActivity(address, credential, type, new Callback()
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
                if(Utility.checkResponse(responseData,context,address)){
                    joinedList = Utility.handleInterestingActivityList(responseData);
                    ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final CarouselBannerAdapter carouselBannerAdapter = new CarouselBannerAdapter(carouselList,context, HomePresenter.this);
                            bannerView.setAdapter(carouselBannerAdapter);
                            bannerView.startRoll();
                        }
                    });
                }
            }
        });
    }

    public void startInterestingActivityActivity(String id){
        final String address = HttpUtil.LocalAddress + "/api/activity/" + id;
        String credential = pref.getString("credential",null);
        HttpUtil.getHttp(address, credential, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "网络连接错误", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                LogUtil.e("HomePresenter",responseData);
                if(Utility.checkResponse(responseData,context,address)){
                    InterestingActivity interestingActivity = Utility.handleInterestingActivity(responseData);
                    Intent intent = new Intent(context, InterestringActivityActivity.class);
                    intent.putExtra("interestingActivity",interestingActivity);
                    intent.putExtra("joined", joinedList.contains(interestingActivity));
                    context.startActivity(intent);
                }
            }
        });
    }

    public void startHealthBroadcastActivity(String id){
        final String address = HttpUtil.LocalAddress + "/api/topic/" + id;
        String credential = pref.getString("credential",null);
        HttpUtil.getHttp(address, credential, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "网络连接错误", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                LogUtil.e("HomePresenter",responseData);
                if(Utility.checkResponse(responseData,context,address)){
                    HealthBroadcast healthBroadcast = Utility.handleHealthBroadcast(responseData);
                    Intent intent = new Intent(context, HealthBroadcastActivity.class);
                    intent.putExtra("healthBroadcast",healthBroadcast);
                    context.startActivity(intent);
                }
            }
        });
    }

    public void search(String keyword){
        final String address = HttpUtil.LocalAddress + "/api/servicesubject/list?searchCondition=" + keyword;
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
                if(Utility.checkResponse(responseData,context,address)){
                    List<ServiceSubject> serviceSubjectList = Utility.handleServiceSubjectList(responseData);

                }
            }
        });
    }
}
