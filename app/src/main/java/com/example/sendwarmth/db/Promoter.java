package com.example.sendwarmth.db;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Promoter implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("loginName")
    private String loginName;
    @SerializedName("activated")
    private boolean activated;
    @SerializedName("roleType")
    private String roleType;
    @SerializedName("customerId")
    private int customerId;
    @SerializedName("customerInfo")
    private Customer customer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
