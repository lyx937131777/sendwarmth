package com.example.sendwarmth.presenter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sendwarmth.MainActivity;
import com.example.sendwarmth.R;
import com.example.sendwarmth.adapter.ProductClassAdapter;
import com.example.sendwarmth.adapter.ShoppingCartAdapter;
import com.example.sendwarmth.adapter.TabAdapter;
import com.example.sendwarmth.db.InterestingActivity;
import com.example.sendwarmth.db.Product;
import com.example.sendwarmth.db.ProductClass;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.Utility;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ShoppingMallPresenter
{
    private Context context;
    private SharedPreferences pref;

    private List<Product> shoppingCartList;
    private ShoppingCartAdapter shoppingCartAdapter;
    private ProductClassAdapter productClassAdapter;
    private int totalCount;
    private double totalPrice;
    private TextView totalCountText;
    private TextView totalPriceText;
    private Button goToOrder;

    private Dialog dialog;

    public ShoppingMallPresenter(Context context, SharedPreferences pref){
        this.context = context;
        this.pref = pref;
    }

    public void updateProductClass(final ProductClassAdapter productClassAdapter, final TabAdapter tabAdapter, final List<ProductClass> productClassList){
        String address = HttpUtil.LocalAddress + "/api/productclass/list";
        String credential = pref.getString("credential",null);
        HttpUtil.getHttp(address, credential, new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                e.printStackTrace();
                ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "网络连接错误", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                final String responseData = response.body().string();
                LogUtil.e("ShoppingMallPresenter",responseData);
                if(Utility.checkResponse(responseData,context)){
                    List<ProductClass> tempList = Utility.handleProductClassList(responseData);
                    productClassList.clear();
                    if(tempList != null){
                        productClassList.addAll(tempList);
                    }
                    productClassAdapter.setmList(productClassList);
                    tabAdapter.setmList(productClassList);
                    ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            productClassAdapter.notifyDataSetChanged();
                            tabAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    public void updateProduct(final ProductClassAdapter productClassAdapter){
        String address = HttpUtil.LocalAddress + "/api/product/getClass";
        String credential = pref.getString("credential",null);
        HttpUtil.getHttp(address, credential, new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                e.printStackTrace();
                ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "网络连接错误", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                final String responseData = response.body().string();
                LogUtil.e("ProductClassPresenter",responseData);
                List<Product> productList = Utility.handleProductList(responseData);
                for(Product product : productList){
                    Product localProduct = LitePal.where("internetId = ?",product.getInternetId()).findFirst(Product.class);
                    if(localProduct == null){
                        product.setSelectedCount(0);
                    }else{
                        product.setSelectedCount(localProduct.getSelectedCount());
                    }
                }
                productClassAdapter.setProductList(productList);
                ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        productClassAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    public void refresh(){
        shoppingCartList = LitePal.where("selectedCount > ?","0").find(Product.class);
        shoppingCartAdapter.setmList(shoppingCartList);
        shoppingCartAdapter.notifyDataSetChanged();
        productClassAdapter.refresh();
        totalCount = 0;
        totalPrice = 0;
        for(Product product : shoppingCartList){
            totalCount += product.getSelectedCount();
            totalPrice += product.getProductPrice() * product.getSelectedCount();
        }
        totalCountText.setText("" + totalCount);
        totalPriceText.setText("￥" + totalPrice);
        if(totalCount == 0){
            dialog.cancel();
            goToOrder.setEnabled(false);
            goToOrder.setBackgroundResource(R.color.gainsboro);
        }else {
            goToOrder.setEnabled(true);
            goToOrder.setBackgroundResource(R.color.colorAccent);
        }
    }

    public ShoppingCartAdapter getShoppingCartAdapter()
    {
        return shoppingCartAdapter;
    }

    public void setShoppingCartAdapter(ShoppingCartAdapter shoppingCartAdapter)
    {
        this.shoppingCartAdapter = shoppingCartAdapter;
    }

    public int getTotalCount()
    {
        return totalCount;
    }

    public void setTotalCount(int totalCount)
    {
        this.totalCount = totalCount;
    }

    public double getTotalPrice()
    {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice)
    {
        this.totalPrice = totalPrice;
    }

    public TextView getTotalCountText()
    {
        return totalCountText;
    }

    public void setTotalCountText(TextView totalCountText)
    {
        this.totalCountText = totalCountText;
    }

    public TextView getTotalPriceText()
    {
        return totalPriceText;
    }

    public void setTotalPriceText(TextView totalPriceText)
    {
        this.totalPriceText = totalPriceText;
    }

    public ProductClassAdapter getProductClassAdapter()
    {
        return productClassAdapter;
    }

    public void setProductClassAdapter(ProductClassAdapter productClassAdapter)
    {
        this.productClassAdapter = productClassAdapter;
    }

    public List<Product> getShoppingCartList()
    {
        return shoppingCartList;
    }

    public void setShoppingCartList(List<Product> shoppingCartList)
    {
        this.shoppingCartList = shoppingCartList;
    }

    public Dialog getDialog()
    {
        return dialog;
    }

    public void setDialog(Dialog dialog)
    {
        this.dialog = dialog;
    }

    public Button getGoToOrder()
    {
        return goToOrder;
    }

    public void setGoToOrder(Button goToOrder)
    {
        this.goToOrder = goToOrder;
    }
}
