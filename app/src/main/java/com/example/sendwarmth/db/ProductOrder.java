package com.example.sendwarmth.db;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProductOrder implements Serializable
{
    @SerializedName("id")
    private String internetId;
    private String orderNo;

    private String customerId;
    private Customer customerInfo;
    private String contactPerson;
    private String contactTel;
    private String deliveryAddress;
    private String houseNum;

    @SerializedName("logisticsStatus")
    private String state;

    private String logisticsCom;
    private String logisticsNo;
    private String businessId;
    private Business businessInfo;
    private String orderTime;
    private String dealTime;
    private String evaluateTime;
    private String customerComment;
    private double orderPrice;
    private List<ProductItem> productItemInfos;


    public Business getBusinessInfo()
    {
        return businessInfo;
    }

    public void setBusinessInfo(Business businessInfo)
    {
        this.businessInfo = businessInfo;
    }

    public String getInternetId()
    {
        return internetId;
    }

    public void setInternetId(String internetId)
    {
        this.internetId = internetId;
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
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

    public String getContactPerson()
    {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson)
    {
        this.contactPerson = contactPerson;
    }

    public String getContactTel()
    {
        return contactTel;
    }

    public void setContactTel(String contactTel)
    {
        this.contactTel = contactTel;
    }

    public String getDeliveryAddress()
    {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress)
    {
        this.deliveryAddress = deliveryAddress;
    }

    public String getHouseNum()
    {
        return houseNum;
    }

    public void setHouseNum(String houseNum)
    {
        this.houseNum = houseNum;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getLogisticsCom()
    {
        return logisticsCom;
    }

    public void setLogisticsCom(String logisticsCom)
    {
        this.logisticsCom = logisticsCom;
    }

    public String getLogisticsNo()
    {
        return logisticsNo;
    }

    public void setLogisticsNo(String logisticsNo)
    {
        this.logisticsNo = logisticsNo;
    }

    public String getBusinessId()
    {
        return businessId;
    }

    public void setBusinessId(String businessId)
    {
        this.businessId = businessId;
    }

    public String getOrderTime()
    {
        return orderTime;
    }

    public void setOrderTime(String orderTime)
    {
        this.orderTime = orderTime;
    }

    public String getDealTime()
    {
        return dealTime;
    }

    public void setDealTime(String dealTime)
    {
        this.dealTime = dealTime;
    }

    public String getEvaluateTime()
    {
        return evaluateTime;
    }

    public void setEvaluateTime(String evaluateTime)
    {
        this.evaluateTime = evaluateTime;
    }

    public String getCustomerComment()
    {
        return customerComment;
    }

    public void setCustomerComment(String customerComment)
    {
        this.customerComment = customerComment;
    }

    public double getOrderPrice()
    {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice)
    {
        this.orderPrice = orderPrice;
    }

    public List<ProductItem> getProductItemInfos()
    {
        return productItemInfos;
    }

    public void setProductItemInfos(List<ProductItem> productItemInfos)
    {
        this.productItemInfos = productItemInfos;
    }
}
