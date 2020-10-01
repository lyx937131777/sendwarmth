package com.example.sendwarmth.db;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class Customer extends LitePalSupport implements Serializable
{
    @SerializedName("id")
    private String internetId;

    private String userId;
    private String credential;

    @SerializedName("customerName")
    private String name;
    @SerializedName("customerAddress")
    private String address;
    @SerializedName("customerTel")
    private String tel;
    private String userName;
    private String personalDescription;

    private double balance;
    private int activity;

    private int memberLevel;
    private double memberPoints;

    public int getMemberLevel()
    {
        return memberLevel;
    }

    public void setMemberLevel(int memberLevel)
    {
        this.memberLevel = memberLevel;
    }

    public double getMemberPoints()
    {
        return memberPoints;
    }

    public void setMemberPoints(double memberPoints)
    {
        this.memberPoints = memberPoints;
    }

    public String getInternetId()
    {
        return internetId;
    }

    public void setInternetId(String internetId)
    {
        this.internetId = internetId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getTel()
    {
        return tel;
    }

    public void setTel(String tel)
    {
        this.tel = tel;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPersonalDescription()
    {
        return personalDescription;
    }

    public void setPersonalDescription(String personalDescription)
    {
        this.personalDescription = personalDescription;
    }

    public double getBalance()
    {
        return balance;
    }

    public void setBalance(double balance)
    {
        this.balance = balance;
    }

    public int getActivity()
    {
        return activity;
    }

    public void setActivity(int activity)
    {
        this.activity = activity;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getCredential()
    {
        return credential;
    }

    public void setCredential(String credential)
    {
        this.credential = credential;
    }
}
