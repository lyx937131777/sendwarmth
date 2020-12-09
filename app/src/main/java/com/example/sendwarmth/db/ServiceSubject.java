package com.example.sendwarmth.db;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ServiceSubject implements Serializable
{
    @SerializedName("id")
    private String internetId;
    @SerializedName("classId")
    private String serviceClassId;
    private ServiceClass serviceClassInfo;
    private String subjectName;
    private String type;
    private String subjectDes;
    private double salaryPerHour;
    private double hurrySalaryPerHour;
    private String image;

    public ServiceClass getServiceClassInfo()
    {
        return serviceClassInfo;
    }

    public void setServiceClassInfo(ServiceClass serviceClassInfo)
    {
        this.serviceClassInfo = serviceClassInfo;
    }

    public String getSubjectName()
    {
        return subjectName;
    }

    public void setSubjectName(String subjectName)
    {
        this.subjectName = subjectName;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getSubjectDes()
    {
        return subjectDes;
    }

    public void setSubjectDes(String subjectDes)
    {
        this.subjectDes = subjectDes;
    }

    public double getSalaryPerHour()
    {
        return salaryPerHour;
    }

    public void setSalaryPerHour(double salaryPerHour)
    {
        this.salaryPerHour = salaryPerHour;
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
