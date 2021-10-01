package com.marius.valeyou_admin.data.beans.suggesstions;

public class SuggesstionModel {

    /**
     * id : 197
     * userId : 97
     * providerId : 0
     * description : Need a guy for seo job
     * title : SEO job
     * latitude : 30.7386
     * longitude : 76.6692
     * location : Model Town, Kharar, PB, India
     * state : PB
     * status : 0
     * date : 1596306180.0
     * search_type : 0
     */



    private int id;
    private int userId;
    private int providerId;
    private String description;
    private String title;
    private double latitude;
    private double longitude;
    private String location;
    private String state;
    private int status;
    private String date;
    private int search_type;
    /**
     * name : Digital Marketing
     */

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSearch_type() {
        return search_type;
    }

    public void setSearch_type(int search_type) {
        this.search_type = search_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
