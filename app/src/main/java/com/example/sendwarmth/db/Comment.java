package com.example.sendwarmth.db;

import com.google.gson.annotations.SerializedName;

public class Comment
{
    @SerializedName("id")
    private String internetId;
    private String content;
    private String blogId;
    private String topicId;
    private String customerId;
    private Customer customerInfo;
    private String timestamp;

    public String getInternetId()
    {
        return internetId;
    }

    public void setInternetId(String internetId)
    {
        this.internetId = internetId;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getBlogId()
    {
        return blogId;
    }

    public void setBlogId(String blogId)
    {
        this.blogId = blogId;
    }

    public String getTopicId()
    {
        return topicId;
    }

    public void setTopicId(String topicId)
    {
        this.topicId = topicId;
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

    public String getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }
}
