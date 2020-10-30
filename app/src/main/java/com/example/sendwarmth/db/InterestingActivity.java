package com.example.sendwarmth.db;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InterestingActivity implements Serializable
{
    @SerializedName("id")
    private String internetId;
    @SerializedName("activityName")
    private String title;
    @SerializedName("activityDes")
    private String description;
    @SerializedName("activityPic")
    private String image;
    @SerializedName("activityLoc")
    private String location;
    @SerializedName("activityBud")
    private String upBudget;
    @SerializedName("contactTel")
    private String contactTel;
    @SerializedName("maxNum")
    private int maxNum;
    @SerializedName("reportedNum")
    private int reportedNum;
    private String activityStatus;
    private String activityDeclarationStatus;
    private String audiStatus;

    @SerializedName("promoterId")
    private String promoterId;
    @SerializedName("promoterLoginName")
    private String promoterName;
    @SerializedName("promoter")
    private Promoter promoter;

    private String time;

    public String getInternetId()
    {
        return internetId;
    }

    public void setInternetId(String internetId)
    {
        this.internetId = internetId;
    }

    public String getPromoterId()
    {
        return promoterId;
    }

    public void setPromoterId(String promoterId)
    {
        this.promoterId = promoterId;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getUpBudget()
    {
        return upBudget;
    }

    public void setUpBudget(String upBudget)
    {
        this.upBudget = upBudget;
    }

    public String getContactTel()
    {
        return contactTel;
    }

    public void setContactTel(String contactTel)
    {
        this.contactTel = contactTel;
    }

    public int getMaxNum()
    {
        return maxNum;
    }

    public void setMaxNum(int maxNum)
    {
        this.maxNum = maxNum;
    }

    public int getReportedNum()
    {
        return reportedNum;
    }

    public void setReportedNum(int reportedNum)
    {
        this.reportedNum = reportedNum;
    }

    public Promoter getPromoter()
    {
        return promoter;
    }

    public void setPromoter(Promoter promoter)
    {
        this.promoter = promoter;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getPromoterName()
    {
        return promoterName;
    }

    public void setPromoterName(String promoterName)
    {
        this.promoterName = promoterName;
    }
}
