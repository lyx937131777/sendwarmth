package com.example.sendwarmth.db;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PensionInstitution implements Serializable
{
    @SerializedName("id")
    private String internetId;
    private String institutionName;
    private String institutionTel;
    private String institutionPic;
    private String institutionLoc;
    private String institutionDes;

    public String getInternetId()
    {
        return internetId;
    }

    public void setInternetId(String internetId)
    {
        this.internetId = internetId;
    }

    public String getInstitutionName()
    {
        return institutionName;
    }

    public void setInstitutionName(String institutionName)
    {
        this.institutionName = institutionName;
    }

    public String getInstitutionTel()
    {
        return institutionTel;
    }

    public void setInstitutionTel(String institutionTel)
    {
        this.institutionTel = institutionTel;
    }

    public String getInstitutionPic()
    {
        return institutionPic;
    }

    public void setInstitutionPic(String institutionPic)
    {
        this.institutionPic = institutionPic;
    }

    public String getInstitutionLoc()
    {
        return institutionLoc;
    }

    public void setInstitutionLoc(String institutionLoc)
    {
        this.institutionLoc = institutionLoc;
    }

    public String getInstitutionDes()
    {
        return institutionDes;
    }

    public void setInstitutionDes(String institutionDes)
    {
        this.institutionDes = institutionDes;
    }
}
