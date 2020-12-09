package com.example.sendwarmth.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sendwarmth.InterestringActivityActivity;
import com.example.sendwarmth.R;
import com.example.sendwarmth.db.InterestingActivity;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class InterestingActivityAdapter extends RecyclerView.Adapter<InterestingActivityAdapter.ViewHolder>
{
    private Context mContext;
    private List<InterestingActivity> mList;
    private List<InterestingActivity> joinedList;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View view;
        ImageView picture;
        TextView title;
        TextView description;
        TextView time;
//        CircleImageView author;
        TextView authorName;

        public  ViewHolder(View view)
        {
            super(view);
            this.view = view;
            picture = view.findViewById(R.id.picture);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            time = view.findViewById(R.id.time);
//            author = view.findViewById(R.id.profile);
            authorName = view.findViewById(R.id.author_name);
        }
    }

    public InterestingActivityAdapter(List<InterestingActivity> menuList, List<InterestingActivity> joinedList){
        mList = menuList;
        this.joinedList = joinedList;
    }

    @Override
    public InterestingActivityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(mContext == null)
        {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_interestring_activity, parent,false);
        final InterestingActivityAdapter.ViewHolder holder = new InterestingActivityAdapter.ViewHolder(view);
        holder.view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int position = holder.getAdapterPosition();
                InterestingActivity interestingActivity = mList.get(position);
                Intent intent = new Intent(mContext, InterestringActivityActivity.class);
                intent.putExtra("interestingActivity",interestingActivity);
                LogUtil.e("InterestingActivityAdapter",String.valueOf(joinedList.contains(interestingActivity)));
                intent.putExtra("joined", joinedList.contains(interestingActivity));
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(InterestingActivityAdapter.ViewHolder holder, int position)
    {
        InterestingActivity interestingActivity = mList.get(position);
        Glide.with(mContext).load(HttpUtil.getResourceURL(interestingActivity.getImage())).into(holder.picture);
//        Glide.with(mContext).load(R.drawable.profile_uri).into(holder.author);
        holder.title.setText(interestingActivity.getTitle());
        holder.description.setText(interestingActivity.getDescription());
        if(interestingActivity.getHost()!=null){
            holder.authorName.setText(interestingActivity.getHost());
        } else if(interestingActivity.getPromoter()!=null){
            holder.authorName.setText(interestingActivity.getPromoter().getName());
        }
        //holder.time.setText(interestingActivity.getContactTel());
    }
    @Override
    public int getItemCount()
    {
        return mList.size();
    }

    public List<InterestingActivity> getmList()
    {
        return mList;
    }

    public void setmList(List<InterestingActivity> mList)
    {
        this.mList = mList;
    }

    public void setJoinedList(List<InterestingActivity> joinedList){
        this.joinedList = joinedList;
    }
}
