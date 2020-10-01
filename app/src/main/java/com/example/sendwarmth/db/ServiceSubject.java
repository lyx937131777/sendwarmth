package com.example.sendwarmth.db;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ServiceSubject implements Serializable
{
    @SerializedName("id")
    private String internetId;
    @SerializedName("classId")
    private String serviceClassId;
    @SerializedName("subjectName")
    private String name;
    private String type;
    @SerializedName("subjectDes")
    private String description;
    @SerializedName("salaryPerHour")
    private double salaryPerHour;

    private String image;

    private double hurrySalaryPerHour;
    private String unit;
    private int picture;

    public ServiceSubject(String name, String type, String description, double salaryPerHour, String unit,
                          int picture)
    {
        this.name = name;
        this.type = type;
        this.description = description;
        this.salaryPerHour = salaryPerHour;
        this.unit = unit;
        this.picture = picture;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public double getSalaryPerHour()
    {
        return salaryPerHour;
    }

    public void setSalaryPerHour(double salaryPerHour)
    {
        this.salaryPerHour = salaryPerHour;
    }

    public String getUnit()
    {
        return unit;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public int getPicture()
    {
        return picture;
    }

    public void setPicture(int picture)
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

    public String getServiceClassId()
    {
        return serviceClassId;
    }

    public void setServiceClassId(String serviceClassId)
    {
        this.serviceClassId = serviceClassId;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public double getHurrySalaryPerHour()
    {
        return hurrySalaryPerHour;
    }

    public void setHurrySalaryPerHour(double hurrySalaryPerHour)
    {
        this.hurrySalaryPerHour = hurrySalaryPerHour;
    }
}
