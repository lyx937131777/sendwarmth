package com.example.sendwarmth.db;

import com.google.gson.annotations.SerializedName;

public class ProductClass
{
    @SerializedName("id")
    private String internetId;
    @SerializedName("className")
    private String name;
    private String classPic;

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

    public String getClassPic()
    {
        return classPic;
    }

    public void setClassPic(String classPic)
    {
        this.classPic = classPic;
    }
}
