package com.example.sendwarmth.db;

import com.google.gson.annotations.SerializedName;

public class Product
{
    @SerializedName("id")
    private String internetId;
    private String productId;
    private String productName;
    private String productDes;
    private String productPic;
    private double productPrice;
    private String productClassId;
    private ProductClass productClassInfo;

    private String businessId;
    private String businessInfo;
    private String productBrand;
    private String producerName;
    private String productStatus;
    private int saleNum;

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

    public String getBusinessInfo()
    {
        return businessInfo;
    }

    public void setBusinessInfo(String businessInfo)
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
}
