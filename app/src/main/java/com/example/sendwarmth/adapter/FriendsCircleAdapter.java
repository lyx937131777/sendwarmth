package com.example.sendwarmth.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sendwarmth.FriendsCircleActivity;
import com.example.sendwarmth.R;
import com.example.sendwarmth.db.FriendsCircle;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.TimeUtil;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsCircleAdapter extends RecyclerView.Adapter<FriendsCircleAdapter.ViewHolder>
{
    private Context mContext;
    private List<FriendsCircle> mList;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View view;
        ImageView picture;
        TextView authorName;
        TextView content;
        TextView time;
        CircleImageView authorProfile;

        public  ViewHolder(View view)
        {
            super(view);
            this.view = view;
            picture = view.findViewById(R.id.picture);
            authorName = view.findViewById(R.id.author_name);
            content = view.findViewById(R.id.content);
            time = view.findViewById(R.id.time);
            authorProfile = view.findViewById(R.id.author_profile);
        }
    }

    public FriendsCircleAdapter(List<FriendsCircle> menuList){
        mList = menuList;
    }

    @Override
    public FriendsCircleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(mContext == null)
        {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_friends_circle, parent,false);
        final FriendsCircleAdapter.ViewHolder holder = new FriendsCircleAdapter.ViewHolder(view);
        holder.view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int position = holder.getAdapterPosition();
                FriendsCircle friendsCircle = mList.get(position);
                Intent intent = new Intent(mContext, FriendsCircleActivity.class);
                intent.putExtra("friendsCircle",friendsCircle);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(FriendsCircleAdapter.ViewHolder holder, int position)
    {
        FriendsCircle friendsCircle = mList.get(position);
        Glide.with(mContext).load(HttpUtil.getResourceURL(friendsCircle.getPicture())).into(holder.picture);
        Glide.with(mContext).load(R.drawable.profile_uri).into(holder.authorProfile);
        holder.authorName.setText(friendsCircle.getCustomerInfo().getCustomerName());
        holder.content.setText(friendsCircle.getContent());
        holder.time.setText(TimeUtil.timeStampToString(friendsCircle.getTimestamp(),"yyyy-MM-dd HH:mm"));
    }
    @Override
    public int getItemCount()
    {
        return mList.size();
    }

    public List<FriendsCircle> getmList()
    {
        return mList;
    }

    public void setmList(List<FriendsCircle> mList)
    {
        this.mList = mList;
    }
}
