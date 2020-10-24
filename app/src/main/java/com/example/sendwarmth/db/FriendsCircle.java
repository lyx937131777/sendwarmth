package com.example.sendwarmth.db;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FriendsCircle implements Serializable
{
    @SerializedName("id")
    private String internetId;
    private String customerId;
    private Customer customerInfo;
    private String content;
    private String timestamp;
    private String picture;

    public String getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getPicture()
    {
        return picture;
    }

    public void setPicture(String picture)
    {
        this.picture = picture;
    }

    public String getInternetId()
    {
        return internetId;
    }

    public void setInternetId(String internetId)
    {
        this.internetId = internetId;
    }

    public String getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }

    public Customer getCustomerInfo()
    {
        return customerInfo;
    }

    public void setCustomerInfo(Customer customerInfo)
    {
        this.customerInfo = customerInfo;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }
}
