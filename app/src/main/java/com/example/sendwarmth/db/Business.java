package com.example.sendwarmth.db;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Business implements Serializable
{
    @SerializedName("id")
    private String internetId;
    private String businessAddress;
    private String businessName;
    private String businessLicense; //经营执照，图片
    private String contact;
    private String contactTel;
    private String idCardBack;    //身份证正反面 图片
    private String idCardFront;
    private String legalPersonIdCard;
    private String legalPersonName;
    private String legalPersonTel;
    private String productClassId;
    private ProductClass productClassInfo;

    public String getInternetId()
    {
        return internetId;
    }

    public void setInternetId(String internetId)
    {
        this.internetId = internetId;
    }

    public String getBusinessAddress()
    {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress)
    {
        this.businessAddress = businessAddress;
    }

    public String getBusinessName()
    {
        return businessName;
    }

    public void setBusinessName(String businessName)
    {
        this.businessName = businessName;
    }

    public String getBusinessLicense()
    {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense)
    {
        this.businessLicense = businessLicense;
    }

    public String getContact()
    {
        return contact;
    }

    public void setContact(String contact)
    {
        this.contact = contact;
    }

    public String getContactTel()
    {
        return contactTel;
    }

    public void setContactTel(String contactTel)
    {
        this.contactTel = contactTel;
    }

    public String getIdCardBack()
    {
        return idCardBack;
    }

    public void setIdCardBack(String idCardBack)
    {
        this.idCardBack = idCardBack;
    }

    public String getIdCardFront()
    {
        return idCardFront;
    }

    public void setIdCardFront(String idCardFront)
    {
        this.idCardFront = idCardFront;
    }

    public String getLegalPersonIdCard()
    {
        return legalPersonIdCard;
    }

    public void setLegalPersonIdCard(String legalPersonIdCard)
    {
        this.legalPersonIdCard = legalPersonIdCard;
    }

    public String getLegalPersonName()
    {
        return legalPersonName;
    }

    public void setLegalPersonName(String legalPersonName)
    {
        this.legalPersonName = legalPersonName;
    }

    public String getLegalPersonTel()
    {
        return legalPersonTel;
    }

    public void setLegalPersonTel(String legalPersonTel)
    {
        this.legalPersonTel = legalPersonTel;
    }

    public String getProductClassId()
    {
        return productClassId;
    }

    public void setProductClassId(String productClassId)
    {
        this.productClassId = productClassId;
    }

    public ProductClass getProductClassInfo()
    {
        return productClassInfo;
    }

    public void setProductClassInfo(ProductClass productClassInfo)
    {
        this.productClassInfo = productClassInfo;
    }
}
