package com.example.sendwarmth.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sendwarmth.OrderingActivity;
import com.example.sendwarmth.R;
import com.example.sendwarmth.db.ServiceSubject;
import com.example.sendwarmth.util.HttpUtil;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public
class ServiceSubjectAdapter extends RecyclerView.Adapter<ServiceSubjectAdapter.ViewHolder>
{
    private Context mContext;
    private List<ServiceSubject> mList;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View view;
        ImageView picture;
        TextView title;
        TextView description;
        TextView price;

        public  ViewHolder(View view)
        {
            super(view);
            this.view = view;
            picture = view.findViewById(R.id.picture);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            price = view.findViewById(R.id.price);
        }
    }

    public ServiceSubjectAdapter(List<ServiceSubject> serviceSubjectList){
        mList = serviceSubjectList;
    }

    @Override
    public ServiceSubjectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(mContext == null)
        {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_service_work, parent,false);
        final ServiceSubjectAdapter.ViewHolder holder = new ServiceSubjectAdapter.ViewHolder(view);
        holder.view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int position = holder.getAdapterPosition();
                ServiceSubject serviceSubject = mList.get(position);
                Intent intent = new Intent(mContext, OrderingActivity.class);
                intent.putExtra("serviceWork", serviceSubject);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ServiceSubjectAdapter.ViewHolder holder, int position)
    {
        ServiceSubject serviceSubject = mList.get(position);
        Glide.with(mContext).load(HttpUtil.getResourceURL(serviceSubject.getImage())).into(holder.picture);
        holder.title.setText(serviceSubject.getSubjectName());
        holder.description.setText(serviceSubject.getSubjectDes());
        holder.price.setText(serviceSubject.getSalaryPerHour()+"元/时");
    }
    @Override
    public int getItemCount()
    {
        return mList.size();
    }

    public List<ServiceSubject> getmList()
    {
        return mList;
    }

    public void setmList(List<ServiceSubject> mList)
    {
        this.mList = mList;
    }
}
