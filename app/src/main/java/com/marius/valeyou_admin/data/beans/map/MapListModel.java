package com.marius.valeyou_admin.data.beans.map;

import java.util.List;

public class MapListModel {

    /**
     * id : 159
     * userId : 70
     * title : IT
     * description : Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.
     * location : Mohali
     * jobType : 0
     * latitude : 30.2157
     * longitude : 70.2157
     * bid_price : 0
     * distance : 317.27
     * orderImages : [{"id":55,"images":"f42f34a0-1169-48ec-9b52-dbf200b4c3e5.jpeg"}]
     */

    private int id;
    private int userId;
    private String title;
    private String description;
    private String location;
    private int jobType;
    private double latitude;
    private double longitude;
    private int bid_price;
    private double distance;
    private String endPrice;
    private int bid_status;
    private List<OrderImagesBean> orderImages;

    public int getBid_status() {
        return bid_status;
    }

    public void setBid_status(int bid_status) {
        this.bid_status = bid_status;
    }

    public String getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(String endPrice) {
        this.endPrice = endPrice;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getJobType() {
        return jobType;
    }

    public void setJobType(int jobType) {
        this.jobType = jobType;
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

    public int getBid_price() {
        return bid_price;
    }

    public void setBid_price(int bid_price) {
        this.bid_price = bid_price;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public List<OrderImagesBean> getOrderImages() {
        return orderImages;
    }

    public void setOrderImages(List<OrderImagesBean> orderImages) {
        this.orderImages = orderImages;
    }

    public static class OrderImagesBean {
        /**
         * id : 55
         * images : f42f34a0-1169-48ec-9b52-dbf200b4c3e5.jpeg
         */

        private int id;
        private String images;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }
    }
}
