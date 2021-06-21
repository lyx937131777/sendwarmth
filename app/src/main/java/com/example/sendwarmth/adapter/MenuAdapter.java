package com.example.sendwarmth.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sendwarmth.MyActivityActivity;
import com.example.sendwarmth.MyInformationActivity;
import com.example.sendwarmth.OrderActivity;
import com.example.sendwarmth.ProductOrderActivity;
import com.example.sendwarmth.R;
import com.example.sendwarmth.SettingActivity;
import com.example.sendwarmth.db.Customer;
import com.example.sendwarmth.db.Menu;
import com.example.sendwarmth.util.LogUtil;

import org.litepal.LitePal;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>
{
    private Context mContext;
    private List<Menu> mList;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View view;
        ImageView menuImage;
        TextView menuName;

        public  ViewHolder(View view)
        {
            super(view);
            this.view = view;
            menuImage = view.findViewById(R.id.menu_image);
            menuName = view.findViewById(R.id.menu_name);
        }
    }

    public MenuAdapter(List<Menu> menuList){
        mList = menuList;
    }

    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(mContext == null)
        {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_menu, parent,false);
        final MenuAdapter.ViewHolder holder = new MenuAdapter.ViewHolder(view);
        holder.view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int position = holder.getAdapterPosition();
                Menu menu = mList.get(position);
                switch (menu.getType()){
                    case "serviceOrder":{
                        Intent intent = new Intent(mContext, OrderActivity.class);
                        int index = 0;
                        switch (menu.getName()){
                            case "toBeAccept":
                                index = 1;
                                break;
                            case "accepted":
                                index = 2;
                                break;
                            case "toBeEvaluated":
                                index = 3;
                                break;
                            case "closed":
                                index = 4;
                                break;
                            default:
                                index = 0;
                                break;
                        }
                        intent.putExtra("index",index);
                        mContext.startActivity(intent);
                        break;
                    }
                    case "productOrder":{
                        Intent intent = new Intent(mContext, ProductOrderActivity.class);
                        int index = 0;
                        switch (menu.getName()){
                            case "toBePaid":
                                index = 1;
                                break;
                            case "toBeReceived":
                                index = 2;
                                break;
                            case "toBeEvaluated":
                                index = 3;
                                break;
                            case "closed":
                                index = 4;
                                break;
                            default:
                                index = 0;
                                break;
                        }
                        intent.putExtra("index",index);
                        mContext.startActivity(intent);
                        break;
                    }
                    case "information":{
                        switch (menu.getName()){
                            case "myInformation":{
                                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
                                String credential = pref.getString("credential","");
                                Customer customer = LitePal.where("credential = ?",credential).findFirst(Customer.class);
                                Intent intent = new Intent(mContext, MyInformationActivity.class);
                                intent.putExtra("customer",customer);
                                mContext.startActivity(intent);
                                break;
                            }
                            case "myActivity":{
                                Intent intent = new Intent(mContext, MyActivityActivity.class);
                                mContext.startActivity(intent);
                                break;
                            }
                            default:{
                                new AlertDialog.Builder(mContext)
                                        .setTitle("提示")
                                        .setMessage("该模块暂未开放，请期待后续版本!")
                                        .setPositiveButton("确定", null)
                                        .show();
                            }
                        }
                       break;
                    }
                }
                LogUtil.e("MenuAdapter", menu.getName() + "  " + menu.getMenuName());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MenuAdapter.ViewHolder holder, int position)
    {
        Menu menu = mList.get(position);
        Glide.with(mContext).load(menu.getImageId()).into(holder.menuImage);
        holder.menuName.setText(menu.getMenuName());
    }
    @Override
    public int getItemCount()
    {
        return mList.size();
    }
}
