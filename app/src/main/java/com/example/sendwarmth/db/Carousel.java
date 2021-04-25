package com.example.sendwarmth.db;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Carousel {

    @SerializedName("id")
    private String internetId;

    private String carouselPic;
    @SerializedName("carouseType")
    private String carouselType;
    private String link;

    private InterestingActivity interestingActivity;
    private List<InterestingActivity> joinedList;

    private HealthBroadcast healthBroadcast;

    public InterestingActivity getInterestingActivity() {
        return interestingActivity;
    }

    public void setInterestingActivity(InterestingActivity interestingActivity) {
        this.interestingActivity = interestingActivity;
    }

    public List<InterestingActivity> getJoinedList() {
        return joinedList;
    }

    public void setJoinedList(List<InterestingActivity> joinedList) {
        this.joinedList = joinedList;
    }

    public HealthBroadcast getHealthBroadcast() {
        return healthBroadcast;
    }

    public void setHealthBroadcast(HealthBroadcast healthBroadcast) {
        this.healthBroadcast = healthBroadcast;
    }

    public String getInternetId() {
        return internetId;
    }

    public void setInternetId(String internetId) {
        this.internetId = internetId;
    }

    public String getCarouselPic() {
        return carouselPic;
    }

    public void setCarouselPic(String carouselPic) {
        this.carouselPic = carouselPic;
    }

    public String getCarouselType() {
        return carouselType;
    }

    public void setCarouselType(String carouselType) {
        this.carouselType = carouselType;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
