package com.example.sendwarmth.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sendwarmth.HealthBroadcastActivity;
import com.example.sendwarmth.InterestringActivityActivity;
import com.example.sendwarmth.R;
import com.example.sendwarmth.db.Goods;
import com.example.sendwarmth.db.HealthBroadcast;
import com.example.sendwarmth.util.LogUtil;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class HealthBroadcastAdapter extends RecyclerView.Adapter<HealthBroadcastAdapter.ViewHolder>
{
    private Context mContext;
    private List<HealthBroadcast> mList;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View view;
        ImageView picture;
        TextView title;
        TextView description;
        TextView time;

        public  ViewHolder(View view)
        {
            super(view);
            this.view = view;
            picture = view.findViewById(R.id.picture);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            time = view.findViewById(R.id.time);
        }
    }

    public HealthBroadcastAdapter(List<HealthBroadcast> menuList){
        mList = menuList;
    }

    @Override
    public HealthBroadcastAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(mContext == null)
        {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_health_broadcast, parent,false);
        final HealthBroadcastAdapter.ViewHolder holder = new HealthBroadcastAdapter.ViewHolder(view);
        holder.view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int position = holder.getAdapterPosition();
                HealthBroadcast healthBroadcast = mList.get(position);
                Intent intent = new Intent(mContext, HealthBroadcastActivity.class);
                intent.putExtra("healthBroadcast",healthBroadcast);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(HealthBroadcastAdapter.ViewHolder holder, int position)
    {
        HealthBroadcast healthBroadcast = mList.get(position);
        Glide.with(mContext).load(healthBroadcast.getPicture()).into(holder.picture);
        holder.title.setText(healthBroadcast.getTitle());
        holder.description.setText(healthBroadcast.getDescription());
        holder.time.setText(healthBroadcast.getTime());
    }
    @Override
    public int getItemCount()
    {
        return mList.size();
    }
}
