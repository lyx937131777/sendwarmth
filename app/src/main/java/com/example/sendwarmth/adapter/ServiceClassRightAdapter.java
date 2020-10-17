package com.example.sendwarmth.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sendwarmth.R;
import com.example.sendwarmth.db.ServiceClass;
import com.example.sendwarmth.db.ServiceSubject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ServiceClassRightAdapter extends RecyclerView.Adapter<ServiceClassRightAdapter.ViewHolder>
{
    private Context mContext;
    private List<ServiceClass> mList;
    private List<ServiceSubject> serviceSubjectList = new ArrayList<>();
//    private ServiceClassRightPresenter serviceClassRightPresenter;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View view;
        TextView serviceClass;
        RecyclerView recyclerView;

        public ViewHolder(View view)
        {
            super(view);
            this.view = view;
            serviceClass = view.findViewById(R.id.serivice_class);
            recyclerView = view.findViewById(R.id.recycler_service_subject);
        }
    }

    public ServiceClassRightAdapter(List<ServiceClass> menuList){
        mList = menuList;
    }

    @Override
    public ServiceClassRightAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(mContext == null)
        {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_service_class_right, parent,false);
        final ServiceClassRightAdapter.ViewHolder holder = new ServiceClassRightAdapter.ViewHolder(view);
//        holder.view.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                int position = holder.getAdapterPosition();
//                ServiceClass serviceClass = mList.get(position);
//            }
//        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ServiceClassRightAdapter.ViewHolder holder, int position)
    {
        ServiceClass serviceClass = mList.get(position);
        holder.serviceClass.setText(serviceClass.getName());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        List<ServiceSubject> mServiceSubjectList = new ArrayList<>();
        for(ServiceSubject serviceSubject : serviceSubjectList){
            if(serviceSubject.getServiceClassId().equals(serviceClass.getInternetId())){
                mServiceSubjectList.add(serviceSubject);
            }
        }
        ServiceSubjectAdapter serviceSubjectAdapter = new ServiceSubjectAdapter(mServiceSubjectList);
        holder.recyclerView.setAdapter(serviceSubjectAdapter);
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

    public List<ServiceSubject> getServiceSubjectList()
    {
        return serviceSubjectList;
    }

    public void setServiceSubjectList(List<ServiceSubject> serviceSubjectList)
    {
        this.serviceSubjectList = serviceSubjectList;
    }
}
