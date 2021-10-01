package com.marius.valeyou_admin.data.beans.favourites;

import java.util.List;

public class MyFavouritesModel {


    /**
     * id : 2
     * jobId : 182
     * providerId : 127
     * order : {"id":182,"userId":70,"title":"New Job SPA","description":"Hello lorem ippsum lorrem ippsum lorrem ippsum lorrem ippsum lorrem ippsum lorrem ippsum lorrem....","location":"abc","jobType":1,"latitude":30.6103,"longitude":76.8418,"distance":101.51,"orderImages":[{"id":66,"images":"766f3680-3c06-4af1-b030-be134565cf6b.jpg"}]}
     */

    private int id;
    private int jobId;
    private int providerId;
    private OrderBean order;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }

    public static class OrderBean {
        /**
         * id : 182
         * userId : 70
         * title : New Job SPA
         * description : Hello lorem ippsum lorrem ippsum lorrem ippsum lorrem ippsum lorrem ippsum lorrem ippsum lorrem....
         * location : abc
         * jobType : 1
         * latitude : 30.6103
         * longitude : 76.8418
         * distance : 101.51
         * orderImages : [{"id":66,"images":"766f3680-3c06-4af1-b030-be134565cf6b.jpg"}]
         */

        private int id;
        private int userId;
        private String title;
        private String description;
        private String location;
        private int jobType;
        private double latitude;
        private double longitude;
        private double distance;
        private int status;
        private List<OrderImagesBean> orderImages;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
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
             * id : 66
             * images : 766f3680-3c06-4af1-b030-be134565cf6b.jpg
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
}
