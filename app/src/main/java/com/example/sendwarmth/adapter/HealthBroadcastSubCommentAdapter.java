package com.example.sendwarmth.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sendwarmth.R;
import com.example.sendwarmth.db.Comment;
import com.example.sendwarmth.util.LogUtil;


import java.util.List;

public class HealthBroadcastSubCommentAdapter extends RecyclerView.Adapter<HealthBroadcastSubCommentAdapter.ViewHolder> {
    private Context mContext;
    private List<Comment> mList;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View view;
        TextView authorName;
        TextView content;
        public  ViewHolder(View view)
        {
            super(view);
            this.view = view;
            authorName = view.findViewById(R.id.author_name);
            content = view.findViewById(R.id.content);
        }
    }

    public HealthBroadcastSubCommentAdapter(List<Comment> commentList){
        mList = commentList;
    }

    @Override
    public HealthBroadcastSubCommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(mContext == null)
        {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_health_broadcast_sub_comment, parent,false);
        final HealthBroadcastSubCommentAdapter.ViewHolder holder = new HealthBroadcastSubCommentAdapter.ViewHolder(view);
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
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Comment healthBroadcastComment = mList.get(position);
        holder.authorName.setText(healthBroadcastComment.getCustomerInfo().getCustomerNameWithRole());
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