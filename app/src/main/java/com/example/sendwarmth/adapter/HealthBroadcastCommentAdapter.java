package com.example.sendwarmth.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sendwarmth.R;

import com.example.sendwarmth.db.Comment;
import com.example.sendwarmth.db.Customer;
import com.example.sendwarmth.util.LogUtil;

import org.litepal.LitePal;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HealthBroadcastCommentAdapter extends RecyclerView.Adapter<HealthBroadcastCommentAdapter.ViewHolder> {
    private Context mContext;
    private List<Comment> mList;
    private List<HealthBroadcastSubCommentAdapter> healthBroadcastSubCommentAdapterList;
    private String mId;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View view;
        CircleImageView authorProfile;
        TextView authorName;
        TextView content;
        RecyclerView recyclerSubComment;
        public  ViewHolder(View view)
        {
            super(view);
            this.view = view;
            authorProfile = view.findViewById(R.id.author_profile);
            authorName = view.findViewById(R.id.author_name);
            content = view.findViewById(R.id.content);
            recyclerSubComment = view.findViewById(R.id.recycler_sub_comment);
        }
    }

    public HealthBroadcastCommentAdapter(List<Comment> commentList, List<HealthBroadcastSubCommentAdapter> adapterList, String topicCreatorId
    ){
        mList = commentList;
        healthBroadcastSubCommentAdapterList = adapterList;
        mId = topicCreatorId;
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

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        String userId = pref.getString("userId",null);
        Customer customer = LitePal.where("userId=?",userId).findFirst(Customer.class);
        if(customer.getAccountId().equals(mId)){
            holder.view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    int position = holder.getAdapterPosition();
                    Comment healthBroadcastComment = mList.get(position);
                    EditText commentContent = ((AppCompatActivity)mContext).findViewById(R.id.comment_content);
                    commentContent.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                    commentContent.setHint("回复@" + healthBroadcastComment.getCustomerInfo().getName() + ":");
                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("commentId", healthBroadcastComment.getInternetId());
                    editor.apply();
                }
            });
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Comment healthBroadcastComment = mList.get(position);
        Glide.with(mContext).load(R.drawable.profile_uri).into(holder.authorProfile);
        holder.authorName.setText(healthBroadcastComment.getCustomerInfo().getUserNameWithRole());
//        holder.description.setText(healthBroadcast.getDes());
//        holder.time.setText(Utility.timeStampToString(healthBroadcast.getTimestamp(),"yyyy-MM-dd HH:mm"));
        holder.content.setText(healthBroadcastComment.getContent());

        holder.recyclerSubComment.setLayoutManager(new LinearLayoutManager(mContext));
        HealthBroadcastSubCommentAdapter healthBroadcastSubCommentAdapter = new HealthBroadcastSubCommentAdapter(healthBroadcastComment.getSubComment());
        holder.recyclerSubComment.setAdapter(healthBroadcastSubCommentAdapter);
        healthBroadcastSubCommentAdapterList.add(healthBroadcastSubCommentAdapter);
        LogUtil.e("HealthBroadcastCommentAdapter", healthBroadcastSubCommentAdapterList.size() + " " + healthBroadcastComment.getInternetId());
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