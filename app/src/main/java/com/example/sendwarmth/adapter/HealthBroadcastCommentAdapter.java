package com.example.sendwarmth.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sendwarmth.R;
import com.example.sendwarmth.db.Comment;
import com.example.sendwarmth.util.HttpUtil;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HealthBroadcastCommentAdapter extends RecyclerView.Adapter<HealthBroadcastCommentAdapter.ViewHolder> {
    private Context mContext;
    private List<Comment> mList;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View view;
        CircleImageView authorProflie;
        TextView authorName;
        TextView content;

        public  ViewHolder(View view)
        {
            super(view);
            this.view = view;
            authorProflie = view.findViewById(R.id.author_profile);
            authorName = view.findViewById(R.id.author_name);
            content = view.findViewById(R.id.content);
        }
    }

    public HealthBroadcastCommentAdapter(List<Comment> commentList){
        mList = commentList;
    }

    @Override
    public HealthBroadcastCommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(mContext == null)
        {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_health_broadcast_comment, parent,false);
        final HealthBroadcastCommentAdapter.ViewHolder holder = new HealthBroadcastCommentAdapter.ViewHolder(view);
//        holder.view.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                int position = holder.getAdapterPosition();
//                Comment healthBroadcastComment = mList.get(position);
//                Intent intent = new Intent(mContext, HealthBroadcastActivity.class);
//                intent.putExtra("healthBroadcast",healthBroadcast);
//                mContext.startActivity(intent);
//            }
//        });
        return holder;
    }

    @Override
    public void onBindViewHolder(HealthBroadcastCommentAdapter.ViewHolder holder, int position)
    {
        Comment healthBroadcastComment = mList.get(position);
        Glide.with(mContext).load(R.drawable.profile_uri).into(holder.authorProflie);
        holder.authorName.setText(healthBroadcastComment.getCustomerInfo().getName());
//        holder.description.setText(healthBroadcast.getDes());
//        holder.time.setText(Utility.timeStampToString(healthBroadcast.getTimestamp(),"yyyy-MM-dd HH:mm"));
        holder.content.setText(healthBroadcastComment.getContent());
    }
    @Override
    public int getItemCount()
    {
        return mList.size();
    }

    public List<Comment> getmList()
    {
        return mList;
    }

    public void setmList(List<Comment> mList)
    {
        this.mList = mList;
    }
}