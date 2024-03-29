package com.example.sendwarmth.db;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class Customer extends LitePalSupport implements Serializable
{
    @SerializedName("id")
    private String internetId;
    private String accountId;

    private String userId;
    private String credential;

    private String customerName;
    private String customerAddress;
    private String customerTel;
//    private String userName;
    private String personalDescription;

    private double accountBalance;
    private int activity;

    private double longitude;
    private double latitude;
    private String houseNum;

    private String gender;
    private String idNumber;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String relationship;
    //TODO 常见病
    @SerializedName("image")
    private String commonDiseases;

    private int memberLevel;
    private double memberPoints;

    private String roleType;

    private String tempAddress;

    public String getTempAddress() {
        return tempAddress;
    }

    public void setTempAddress(String tempAddress) {
        this.tempAddress = tempAddress;
    }

    public String getCustomerNameWithRole(){
        if(customerName == null){
            return "暂未填写";
        }
        if(roleType.equals("customer")){
            return customerName;
        }
        if(roleType.equals("expert")){
            return customerName + "（专家）";
        }
        return customerName + "（未知角色）";
    }

    public boolean informationIncomplete(){
        return customerName == null || gender == null || customerAddress == null || idNumber == null;
    }

    public String getCommonDiseases() {
        return commonDiseases;
    }

    public void setCommonDiseases(String commonDiseases) {
        this.commonDiseases = commonDiseases;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getRoleType()
    {
        return roleType;
    }

    public void setRoleType(String roleType)
    {
        this.roleType = roleType;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

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

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    public String getCustomerAddress()
    {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress)
    {
        this.customerAddress = customerAddress;
    }

    public String getCustomerTel()
    {
        return customerTel;
    }

    public void setCustomerTel(String customerTel)
    {
        this.customerTel = customerTel;
    }

//    public String getUserName()
//    {
//        return userName;
//    }
//
//    public void setUserName(String userName)
//    {
//        this.userName = userName;
//    }

    public String getPersonalDescription()
    {
        return personalDescription;
    }

    public void setPersonalDescription(String personalDescription)
    {
        this.personalDescription = personalDescription;
    }

    public double getAccountBalance()
    {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance)
    {
        this.accountBalance = accountBalance;
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

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public String getHouseNum()
    {
        return houseNum;
    }

    public void setHouseNum(String houseNum)
    {
        this.houseNum = houseNum;
    }


}
