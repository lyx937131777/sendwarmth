package com.example.sendwarmth.db;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductItem implements Serializable
{
    @SerializedName("id")
    private String internetId;
    private String orderId;
    private String productId;
    private int productNum;
    private double price;
    private Product productInfo;

    public String getInternetId()
    {
        return internetId;
    }

    public void setInternetId(String internetId)
    {
        this.internetId = internetId;
    }

    public String getOrderId()
    {
        return orderId;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    public String getProductId()
    {
        return productId;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    public int getProductNum()
    {
        return productNum;
    }

    public void setProductNum(int productNum)
    {
        this.productNum = productNum;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public Product getProductInfo()
    {
        return productInfo;
    }

    public void setProductInfo(Product productInfo)
    {
        this.productInfo = productInfo;
    }
}
