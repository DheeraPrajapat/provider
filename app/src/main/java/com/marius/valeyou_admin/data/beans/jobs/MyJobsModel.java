package com.marius.valeyou_admin.data.beans.jobs;

import java.util.List;

public class MyJobsModel {

    /**
     * id : 158
     * userId : 70
     * providerId : 156
     * title : Business Development
     * description : Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.
     * location : Mohali
     * date : 1594915709
     * status : 1
     * bid_price : 300
     * orderImages : [{"id":54,"orderId":158,"images":"05831980-11f3-41c5-8214-2fd6830fa404.jpeg"}]
     */

    private int id;
    private int userId;
    private int providerId;
    private String title;
    private String description;
    private String location;
    private String date;
    private int status;
    private int bid_price;
    private String city;
    private String state;
    private int isCheck;
    private int endPrice;
    private String price;
    private List<OrderImagesBean> orderImages;
    private ReasonBean reason;

    public ReasonBean getReason() {
        return reason;
    }

    public void setReason(ReasonBean reason) {
        this.reason = reason;
    }

    public int getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(int endPrice) {
        this.endPrice = endPrice;
    }

    public int getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
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

    public int getBid_price() {
        return bid_price;
    }

    public void setBid_price(int bid_price) {
        this.bid_price = bid_price;
    }

    public List<OrderImagesBean> getOrderImages() {
        return orderImages;
    }

    public void setOrderImages(List<OrderImagesBean> orderImages) {
        this.orderImages = orderImages;
    }

    public static class OrderImagesBean {
        /**
         * id : 54
         * orderId : 158
         * images : 05831980-11f3-41c5-8214-2fd6830fa404.jpeg
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

    public static class ReasonBean{

        /**
         * id : 2
         * reason : i am not able to come to do this job bcause of lots of reasn.
         * type : 1
         * order_id : 26
         * user_id : 243
         * user_data : {"id":243,"firstName":"Dummy","lastName":"Provider","image":"c281e331-c4e8-426a-accf-dac7326f8444.jpg"}
         */

        private int id;
        private String reason;
        private int type;
        private int order_id;
        private int user_id;
        /**
         * id : 243
         * firstName : Dummy
         * lastName : Provider
         * image : c281e331-c4e8-426a-accf-dac7326f8444.jpg
         */

        private UserDataBean user_data;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public UserDataBean getUser_data() {
            return user_data;
        }

        public void setUser_data(UserDataBean user_data) {
            this.user_data = user_data;
        }

        public static class UserDataBean {
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
    }
}
