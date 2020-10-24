package com.example.sendwarmth.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sendwarmth.PensionInstitutionActivity;
import com.example.sendwarmth.R;
import com.example.sendwarmth.db.PensionInstitution;
import com.example.sendwarmth.util.HttpUtil;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class PensionInstitutionAdapter extends RecyclerView.Adapter<PensionInstitutionAdapter.ViewHolder>
{
    private Context mContext;
    private List<PensionInstitution> mList;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View view;
        ImageView picture;
        TextView name;
        TextView address;
        TextView tel;
//        TextView contact;

        public  ViewHolder(View view)
        {
            super(view);
            this.view = view;
            picture = view.findViewById(R.id.picture);
            name = view.findViewById(R.id.name);
            tel = view.findViewById(R.id.tel);
            address = view.findViewById(R.id.address);
//            contact = view.findViewById(R.id.contact);
        }
    }

    public PensionInstitutionAdapter(List<PensionInstitution> menuList){
        mList = menuList;
    }

    @Override
    public PensionInstitutionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(mContext == null)
        {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pension_institutions, parent,false);
        final PensionInstitutionAdapter.ViewHolder holder = new PensionInstitutionAdapter.ViewHolder(view);
        holder.view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int position = holder.getAdapterPosition();
                PensionInstitution pensionInstitution = mList.get(position);
                Intent intent = new Intent(mContext, PensionInstitutionActivity.class);
                intent.putExtra("pensionInstitution",pensionInstitution);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(PensionInstitutionAdapter.ViewHolder holder, int position)
    {
        PensionInstitution pensionInstitution = mList.get(position);
        Glide.with(mContext).load(HttpUtil.getPhotoURL(pensionInstitution.getInstitutionPic())).into(holder.picture);
        holder.name.setText(pensionInstitution.getInstitutionName());
        holder.address.setText("地址："+pensionInstitution.getInstitutionLoc());
        holder.tel.setText("联系电话："+pensionInstitution.getInstitutionTel());
//        holder.contact.setText(pensionInstitution.getInstitutionDes());
    }
    @Override
    public int getItemCount()
    {
        return mList.size();
    }

    public List<PensionInstitution> getmList()
    {
        return mList;
    }

    public void setmList(List<PensionInstitution> mList)
    {
        this.mList = mList;
    }
}
