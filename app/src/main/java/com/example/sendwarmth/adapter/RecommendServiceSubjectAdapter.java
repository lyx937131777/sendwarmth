package com.example.sendwarmth.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sendwarmth.R;
import com.example.sendwarmth.ServiceSubjectActivity;
import com.example.sendwarmth.db.ServiceSubject;
import com.example.sendwarmth.util.HttpUtil;

import java.util.List;

public class RecommendServiceSubjectAdapter extends RecyclerView.Adapter<RecommendServiceSubjectAdapter.ViewHolder>
{
    private Context mContext;
    private List<ServiceSubject> mList;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View view;
        ImageView picture;
        TextView name;

        public  ViewHolder(View view)
        {
            super(view);
            this.view = view;
            picture = view.findViewById(R.id.serviece_image);
            name = view.findViewById(R.id.servicce_name);
        }
    }

    public RecommendServiceSubjectAdapter(List<ServiceSubject> serviceSubjectList){
        mList = serviceSubjectList;
    }

    @Override
    public RecommendServiceSubjectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(mContext == null)
        {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recommend_service_subject, parent,false);
        final RecommendServiceSubjectAdapter.ViewHolder holder = new RecommendServiceSubjectAdapter.ViewHolder(view);
        holder.view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int position = holder.getAdapterPosition();
                ServiceSubject serviceSubject = mList.get(position);
                Intent intent = new Intent(mContext, ServiceSubjectActivity.class);
                intent.putExtra("serviceWork", serviceSubject);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecommendServiceSubjectAdapter.ViewHolder holder, int position)
    {
        ServiceSubject serviceSubject = mList.get(position);
        Glide.with(mContext).load(HttpUtil.getResourceURL(serviceSubject.getImage())).into(holder.picture);
        holder.name.setText(serviceSubject.getSubjectName());
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
