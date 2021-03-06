package com.example.sendwarmth.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sendwarmth.R;
import com.example.sendwarmth.db.ProductClass;
import com.example.sendwarmth.util.LogUtil;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TabAdapter extends RecyclerView.Adapter<TabAdapter.ViewHolder>
{
    private Context mContext;
    private List<ProductClass> mList;
    private String selectedName;
    private RecyclerView productClassRecycler;

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

    public TabAdapter(List<ProductClass> productClassList, RecyclerView recyclerView){
        mList = productClassList;
        if(productClassList != null && productClassList.size() > 0)
            selectedName = productClassList.get(0).getName();
        productClassRecycler = recyclerView;
    }

    @Override
    public TabAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(mContext == null)
        {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_tab, parent,false);
        final TabAdapter.ViewHolder holder = new TabAdapter.ViewHolder(view);
        holder.view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int position = holder.getAdapterPosition();
                ProductClass productClass = mList.get(position);
                selectedName = productClass.getName();
                notifyDataSetChanged();
                LogUtil.e("TabAdapter","selected:" + selectedName);
                ((LinearLayoutManager)(productClassRecycler.getLayoutManager())).scrollToPositionWithOffset(position,0);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(TabAdapter.ViewHolder holder, int position)
    {
        ProductClass productClass = mList.get(position);
        holder.typeName.setText(productClass.getName());
        if(productClass.getName().equals(selectedName)){
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

    public List<ProductClass> getmList()
    {
        return mList;
    }

    public void setmList(List<ProductClass> mList)
    {
        this.mList = mList;
        if(mList != null && mList.size() > 0)
            selectedName = mList.get(0).getName();
    }

    public String getSelectedName()
    {
        return selectedName;
    }

    public void setSelectedName(String selectedName)
    {
        this.selectedName = selectedName;
    }

    public RecyclerView getProductClassRecycler()
    {
        return productClassRecycler;
    }

    public void setProductClassRecycler(RecyclerView productClassRecycler)
    {
        this.productClassRecycler = productClassRecycler;
    }
}
