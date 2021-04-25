package com.example.sendwarmth.db;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class Product extends LitePalSupport implements Serializable
{
    @SerializedName("id")
    private String internetId;
    private String productId;
    private String productName;
    private String productDes;
    private String productPic;
    private String remarkImg;
    private double productPrice;
    private String productClassId;
    private ProductClass productClassInfo;

    private String businessId;
    private Business businessInfo;
    private String productBrand;
    private String producerName;
    private String productStatus;
    private int saleNum;

    private int selectedCount;


    public String getProductClassName(){
        if(productClassInfo != null){
            return productClassInfo.getName();
        }
        return "未知类别";
    }

    public String getBusinessName(){
        if(businessInfo != null){
            return businessInfo.getBusinessName();
        }
        return "未知商家";
    }

    public String getRemarkImg() {
        return remarkImg;
    }

    public void setRemarkImg(String remarkImg) {
        this.remarkImg = remarkImg;
    }

    public String getInternetId()
    {
        return internetId;
    }

    public void setInternetId(String internetId)
    {
        this.internetId = internetId;
    }

    public String getProductId()
    {
        return productId;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getProductPic()
    {
        return productPic;
    }

    public String getProductDes()
    {
        return productDes;
    }

    public void setProductDes(String productDes)
    {
        this.productDes = productDes;
    }

    public void setProductPic(String productPic)
    {
        this.productPic = productPic;
    }

    public double getProductPrice()
    {
        return productPrice;
    }

    public void setProductPrice(double productPrice)
    {
        this.productPrice = productPrice;
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

    public String getBusinessId()
    {
        return businessId;
    }

    public void setBusinessId(String businessId)
    {
        this.businessId = businessId;
    }

    public Business getBusinessInfo()
    {
        return businessInfo;
    }

    public void setBusinessInfo(Business businessInfo)
    {
        this.businessInfo = businessInfo;
    }

    public String getProductBrand()
    {
        return productBrand;
    }

    public void setProductBrand(String productBrand)
    {
        this.productBrand = productBrand;
    }

    public String getProducerName()
    {
        return producerName;
    }

    public void setProducerName(String producerName)
    {
        this.producerName = producerName;
    }

    public String getProductStatus()
    {
        return productStatus;
    }

    public void setProductStatus(String productStatus)
    {
        this.productStatus = productStatus;
    }

    public int getSaleNum()
    {
        return saleNum;
    }

    public void setSaleNum(int saleNum)
    {
        this.saleNum = saleNum;
    }

    public int getSelectedCount()
    {
        return selectedCount;
    }

    public void setSelectedCount(int selectedCount)
    {
        this.selectedCount = selectedCount;
    }

    public void add(int num){
        selectedCount += num;
    }

    public void minus(int num){
        selectedCount -= num;
        if(selectedCount < 0){
            selectedCount = 0;
        }
    }
}
