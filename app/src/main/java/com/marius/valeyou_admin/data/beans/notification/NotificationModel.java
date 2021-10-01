package com.marius.valeyou_admin.data.beans.notification;

public class NotificationModel {

    /**
     * id : 288
     * userId : 88
     * orderId : 186
     * type : 1
     * user2Id : 127
     * isSeen : 0
     * message : Sanjeev Sharma Accept your Bid
     * createdAt : 1596189471
     * userImage : 90c38d2c-7b66-4dd1-ba44-b81a7c5a7aa8.jpg
     */

    private int id;
    private int userId;
    private int orderId;
    private int type;
    private int user2Id;
    private int isSeen;
    private String message;
    private int createdAt;
    private String userImage;
    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(int user2Id) {
        this.user2Id = user2Id;
    }

    public int getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(int isSeen) {
        this.isSeen = isSeen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(int createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }


    public class Order{

        /**
         * id : 272
         * status : 3
         * jobType : 0
         */

        private int id;
        private int status;
        private int jobType;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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
    }
}
