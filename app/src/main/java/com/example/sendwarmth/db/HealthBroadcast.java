package com.example.sendwarmth.db;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class HealthBroadcast implements Serializable
{
    @SerializedName("id")
    private String internetId;
    private String timestamp;
    private String topicName;
    private String des;
    private String remarkImg;
    private String topicPic;
    private String topicClassType;
    private String topicStatus;
    private String creatorId;
    private Account creatorInfo;
    private int validTime;
    private List<Comment> commentInfos;

    public List<Comment> getCommentInfos() {
        return commentInfos;
    }

    public void setCommentInfos(List<Comment> commentInfos) {
        this.commentInfos = commentInfos;
    }

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

    public String getCreatorId()
    {
        return creatorId;
    }

    public void setCreatorId(String creatorId)
    {
        this.creatorId = creatorId;
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

    public Account getCreatorInfo()
    {
        return creatorInfo;
    }

    public void setCreatorInfo(Account creatorInfo)
    {
        this.creatorInfo = creatorInfo;
    }

    public int getValidTime()
    {
        return validTime;
    }

    public void setValidTime(int validTime)
    {
        this.validTime = validTime;
    }

    public String getRemarkImg() {
        return remarkImg;
    }

    public void setRemarkImg(String remarkImg) {
        this.remarkImg = remarkImg;
    }
}
