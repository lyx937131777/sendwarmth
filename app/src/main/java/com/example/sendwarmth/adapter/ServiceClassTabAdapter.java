package com.example.sendwarmth.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sendwarmth.R;
import com.example.sendwarmth.ServiceWorkActivity;
import com.example.sendwarmth.db.ServiceClass;
import com.example.sendwarmth.util.LogUtil;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ServiceClassTabAdapter extends RecyclerView.Adapter<ServiceClassTabAdapter.ViewHolder>
{
    private Context mContext;
    private List<ServiceClass> mList;
    private String selectedName;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View view;
        View selectedLine;
        TextView typeName;

        public  ViewHolder(View view)
        {
            super(view);
            this.view = view;
            selectedLine = view.findViewById(R.id.selected_line);
            typeName = view.findViewById(R.id.type_name);
        }
    }

    public ServiceClassTabAdapter(List<ServiceClass> serviceClassList){
        mList = serviceClassList;
        if(serviceClassList.size() > 0){
            selectedName = serviceClassList.get(0).getName();
        }
    }

    @Override
    public ServiceClassTabAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(mContext == null)
        {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_tab, parent,false);
        final ServiceClassTabAdapter.ViewHolder holder = new ServiceClassTabAdapter.ViewHolder(view);
        holder.view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int position = holder.getAdapterPosition();
                ServiceClass serviceClass = mList.get(position);
                serviceClass.setClickCount(serviceClass.getClickCount()+1);
                serviceClass.save();
                selectedName =serviceClass.getName();
                notifyDataSetChanged();
                ((ServiceWorkActivity)mContext).scrollRightRecyclerTo(position);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ServiceClassTabAdapter.ViewHolder holder, int position)
    {
//        LogUtil.e("ServiceClassTabAdapter","adapter选中的是:" + selectedName);
        ServiceClass serviceClass = mList.get(position);
        holder.typeName.setText(serviceClass.getName());
        if(serviceClass.getName().equals(selectedName)){
            holder.selectedLine.setBackgroundResource(R.color.colorPrimary);
            holder.view.setBackgroundResource(R.color.white);
            holder.typeName.getPaint().setFakeBoldText(true);
        }else{
            holder.selectedLine.setBackgroundResource(R.color.gainsboro);
            holder.view.setBackgroundResource(R.color.gainsboro);
            holder.typeName.getPaint().setFakeBoldText(false);
        }

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

    public String getSelectedName()
    {
        return selectedName;
    }

    public void setSelectedName(String selectedName)
    {
        this.selectedName = selectedName;
    }
}
