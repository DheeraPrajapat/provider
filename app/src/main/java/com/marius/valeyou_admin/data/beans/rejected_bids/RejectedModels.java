package com.marius.valeyou_admin.data.beans.rejected_bids;

import java.util.List;

public class RejectedModels {

    /**
     * id : 369
     * userId : 124
     * providerId : 0
     * description : testing video animations.
     * location : São Paulo, State of São Paulo, Brazil
     * date : 1608018626
     * status : 0
     * jobType : 0
     * type : 2
     * title : video anim Test
     * startTime : 1608018626
     * endTime : 1608040226
     * startPrice : 0
     * endPrice : 355
     * bid_price : 300
     * bid_status : 2
     * user : {"id":124,"firstName":"Sanjeev","lastName":"Gautam","image":"5a32e6e1-96f8-4cf7-a9f2-147dba20be27.jpg"}
     * orderImages : [{"id":81,"orderId":369,"images":"9eb1f7f1-507d-4775-bd20-49c41a089687.jpg"}]
     */

    private int id;
    private int userId;
    private int providerId;
    private String description;
    private String location;
    private String date;
    private int status;
    private int jobType;
    private int type;
    private String title;
    private String startTime;
    private String endTime;
    private String startPrice;
    private String endPrice;
    private int bid_price;
    private int bid_status;
    private UserBean user;
    private List<OrderImagesBean> orderImages;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getJobType() {
        return jobType;
    }

    public void setJobType(int jobType) {
        this.jobType = jobType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(String startPrice) {
        this.startPrice = startPrice;
    }

    public String getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(String endPrice) {
        this.endPrice = endPrice;
    }

    public int getBid_price() {
        return bid_price;
    }

    public void setBid_price(int bid_price) {
        this.bid_price = bid_price;
    }

    public int getBid_status() {
        return bid_status;
    }

    public void setBid_status(int bid_status) {
        this.bid_status = bid_status;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public List<OrderImagesBean> getOrderImages() {
        return orderImages;
    }

    public void setOrderImages(List<OrderImagesBean> orderImages) {
        this.orderImages = orderImages;
    }

    public static class UserBean {
        /**
         * id : 124
         * firstName : Sanjeev
         * lastName : Gautam
         * image : 5a32e6e1-96f8-4cf7-a9f2-147dba20be27.jpg
         */

        private int id;
        private String firstName;
        private String lastName;
        private String image;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

    public static class OrderImagesBean {
        /**
         * id : 81
         * orderId : 369
         * images : 9eb1f7f1-507d-4775-bd20-49c41a089687.jpg
         */

        private int id;
        private int orderId;
        private String images;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }
    }
}
