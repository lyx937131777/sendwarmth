package com.example.sendwarmth.presenter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.sendwarmth.FriendsCircleActivity;
import com.example.sendwarmth.NewFriendsCircleActivity;
import com.example.sendwarmth.ProductOrderingActivity;
import com.example.sendwarmth.db.Product;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.Utility;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ProductOrderingPresenter
{
    private Context context;
    private SharedPreferences pref;
    private List<Product> productList;
    private List<String> shoppingCartIds = new ArrayList<>();
    private ProgressDialog progressDialog;
    private String contactPerson;
    private String contactTel;
    private String deliveryAddress;

    public ProductOrderingPresenter(Context context, SharedPreferences pref){
        this.context = context;
        this.pref = pref;
    }

    public void postShoppingCart(String contactPerson, String contactTel, String deliveryAddress){
        progressDialog = ProgressDialog.show(context,"","生成订单中...");
        this.contactPerson = contactPerson;
        this.contactTel = contactTel;
        this.deliveryAddress = deliveryAddress;
        String address = HttpUtil.LocalAddress + "/api/shoppingcart";
        final String credential = pref.getString("credential","");
        productList = LitePal.where("selectedCount > ?","0").find(Product.class);
        shoppingCartIds.clear();
        for(Product product : productList){
            HttpUtil.postShoppingCartRequest(address, credential, product.getInternetId(),String.valueOf(product.getSelectedCount()), new Callback()
            {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e)
                {
                    ((ProductOrderingActivity)context).runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(context, "网络连接错误", Toast.LENGTH_LONG).show();
                        }
                    });
                    progressDialog.dismiss();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
                {
                    final String responsData = response.body().string();
                    LogUtil.e("ProductOrderingPresenter",responsData);
                    if(Utility.checkString(responsData,"code") != null && Utility.checkString(responsData,"code").equals("000")){
                        String shoppingCartId = Utility.handleShoppingCartId(responsData);
                        if(shoppingCartId.equals(Utility.ERROR_CODE)){
                            ((ProductOrderingActivity)context).runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Toast.makeText(context, "数据解析发生错误", Toast.LENGTH_LONG).show();
                                }
                            });
                            progressDialog.dismiss();
                        }else {
                            shoppingCartIds.add(shoppingCartId);
                        }
                        if(shoppingCartIds.size() == productList.size()){
                            postProductOrder();
                        }
                    }else{
                        ((ProductOrderingActivity)context).runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Toast.makeText(context, "数据传输发生错误", Toast.LENGTH_LONG).show();
                            }
                        });
                        progressDialog.dismiss();
                    }
                }
            });
        }

    }

    public void postProductOrder(){
        String address = HttpUtil.LocalAddress + "/api/orderproduct/create";
        final String credential = pref.getString("credential","");
        HttpUtil.postProductOrderRequest(address, credential, contactPerson, contactTel, deliveryAddress, shoppingCartIds, new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                ((ProductOrderingActivity)context).runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(context, "网络连接错误", Toast.LENGTH_LONG).show();
                    }
                });
                progressDialog.dismiss();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                final String responsData = response.body().string();
                LogUtil.e("ProductOrderingPresenter",responsData);
                if(Utility.checkString(responsData,"code") != null && Utility.checkString(responsData,"code").equals("000")){
                    LitePal.deleteAll(Product.class);
                    progressDialog.dismiss();
                    ((ProductOrderingActivity)context).runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            new AlertDialog.Builder(context)
                                    .setTitle("提示")
                                    .setMessage("订单生成成功，前往支付")
                                    .setCancelable(false)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    ((ProductOrderingActivity)context).finish();
                                                }
                                            })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    ((ProductOrderingActivity)context).finish();
                                                }
                                            })
                                    .show();
                        }
                    });
                }else{
                    ((ProductOrderingActivity)context).runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(context, "数据传输发生错误", Toast.LENGTH_LONG).show();
                        }
                    });
                    progressDialog.dismiss();
                }
            }
        });
    }

}
