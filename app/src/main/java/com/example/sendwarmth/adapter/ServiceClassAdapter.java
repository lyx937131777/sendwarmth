package com.example.sendwarmth.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sendwarmth.MyInformationActivity;
import com.example.sendwarmth.OrderActivity;
import com.example.sendwarmth.R;
import com.example.sendwarmth.ServiceWorkActivity;
import com.example.sendwarmth.db.Customer;
import com.example.sendwarmth.db.Menu;
import com.example.sendwarmth.db.ServiceClass;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;

import org.litepal.LitePal;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ServiceClassAdapter extends RecyclerView.Adapter<ServiceClassAdapter.ViewHolder>
{
    private Context mContext;
    private List<ServiceClass> mList;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View view;
        ImageView serviceClassImage;
        TextView serviceClassName;

        public  ViewHolder(View view)
        {
            super(view);
            this.view = view;
            serviceClassImage = view.findViewById(R.id.service_class_image);
            serviceClassName = view.findViewById(R.id.service_class_name);
        }
    }

    public ServiceClassAdapter(List<ServiceClass> serviceClassList){
        mList = serviceClassList;
    }

    @Override
    public ServiceClassAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(mContext == null)
        {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_service_class, parent,false);
        final ServiceClassAdapter.ViewHolder holder = new ServiceClassAdapter.ViewHolder(view);
        holder.view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int position = holder.getAdapterPosition();
                ServiceClass serviceClass = mList.get(position);
                serviceClass.setClickCount(serviceClass.getClickCount()+1);
                if(!serviceClass.getName().equals("全部服务")){
                    serviceClass.save();
                }
                Intent intent = new Intent(mContext, ServiceWorkActivity.class);
                intent.putExtra("serviceClassName",serviceClass.getName());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ServiceClassAdapter.ViewHolder holder, int position)
    {
        ServiceClass serviceClass = mList.get(position);
        if(serviceClass.getName().equals("全部服务")){
            Glide.with(mContext).load(R.drawable.all_service).into(holder.serviceClassImage);
        }else{
            Glide.with(mContext).load(HttpUtil.getPhotoURL(serviceClass.getImage())).into(holder.serviceClassImage);
        }
        holder.serviceClassName.setText(serviceClass.getName());
    }

    @Override
    public int getItemCount()
    {
        return mList.size();
    }

    public List<ServiceClass> getmList()
    {
        return mList;
    }

    public void setmList(List<ServiceClass> mList)
    {
        this.mList = mList;
    }
}
