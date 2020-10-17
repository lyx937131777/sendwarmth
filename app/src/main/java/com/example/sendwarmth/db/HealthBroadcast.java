package com.example.sendwarmth.db;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HealthBroadcast implements Serializable
{
    @SerializedName("id")
    private String internetId;
    private String timestamp;
    private String topicName;
    private String topicClassType;
    private String des;
    private String customerId;

    private String topicPic;

    private String topicStatus;

    public String getInternetId()
    {
        return internetId;
    }

    public void setInternetId(String internetId)
    {
        this.internetId = internetId;
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getTopicName()
    {
        return topicName;
    }

    public void setTopicName(String topicName)
    {
        this.topicName = topicName;
    }

    public String getTopicClassType()
    {
        return topicClassType;
    }

    public void setTopicClassType(String topicClassType)
    {
        this.topicClassType = topicClassType;
    }

    public String getDes()
    {
        return des;
    }

    public void setDes(String des)
    {
        this.des = des;
    }

    public String getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }

    public String getTopicPic()
    {
        return topicPic;
    }

    public void setTopicPic(String topicPic)
    {
        this.topicPic = topicPic;
    }

    public String getTopicStatus()
    {
        return topicStatus;
    }

    public void setTopicStatus(String topicStatus)
    {
        this.topicStatus = topicStatus;
    }
}
