package com.marius.valeyou_admin.data.beans.invoice;

public class InvoiceModel {

    /**
     * id : 9
     * orderId : 222
     * providerId : 127
     * invoiceType : 0
     * userId : 88
     * cardId : 12
     * type : 1
     * amount : 539
     * adminFees : 49
     * providerFees : 490
     * transectionId : pi_1HIBy8ADZPLb6pEwqD0zXUj7
     * createdAt : 2020-08-20T11:30:08.000Z
     * updatedAt : 2020-08-20T11:30:08.000Z
     * isRate : 0
     * order : {"id":222,"userId":88,"date":"1597948200","title":"Mobile app","startTime":"Aug 21 2020","endTime":"null","startPrice":"0","endPrice":"490","description":"food app","location":"jattpura","status":4,"time":"1597926357","jobType":0,"type":1,"startjobDate":"1597924384","endjobDate":"1597924415","state":"punjab","city":"kapurthala","zipCode":"144601","appartment":"312","street":"","user":{"id":88,"firstName":"Sanjeev","lastName":"Sharma","image":"90c38d2c-7b66-4dd1-ba44-b81a7c5a7aa8.jpg","phone":"9781630062"}}
     */

    private int id;
    private int orderId;
    private int providerId;
    private int invoiceType;
    private int userId;
    private int cardId;
    private int type;
    private double amount;
    private double adminFees;
    private double providerFees;
    private String transectionId;
    private String createdAt;
    private String updatedAt;
    private int isRate;
    private OrderBean order;

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

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public int getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(int invoiceType) {
        this.invoiceType = invoiceType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAdminFees() {
        return adminFees;
    }

    public void setAdminFees(double adminFees) {
        this.adminFees = adminFees;
    }

    public double getProviderFees() {
        return providerFees;
    }

    public void setProviderFees(double providerFees) {
        this.providerFees = providerFees;
    }

    public String getTransectionId() {
        return transectionId;
    }

    public void setTransectionId(String transectionId) {
        this.transectionId = transectionId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getIsRate() {
        return isRate;
    }

    public void setIsRate(int isRate) {
        this.isRate = isRate;
    }

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }

    public static class OrderBean {
        /**
         * id : 222
         * userId : 88
         * date : 1597948200
         * title : Mobile app
         * startTime : Aug 21 2020
         * endTime : null
         * startPrice : 0
         * endPrice : 490
         * description : food app
         * location : jattpura
         * status : 4
         * time : 1597926357
         * jobType : 0
         * type : 1
         * startjobDate : 1597924384
         * endjobDate : 1597924415
         * state : punjab
         * city : kapurthala
         * zipCode : 144601
         * appartment : 312
         * street :
         * user : {"id":88,"firstName":"Sanjeev","lastName":"Sharma","image":"90c38d2c-7b66-4dd1-ba44-b81a7c5a7aa8.jpg","phone":"9781630062"}
         */

        private int id;
        private int userId;
        private String date;
        private String title;
        private String startTime;
        private String endTime;
        private String startPrice;
        private String endPrice;
        private String description;
        private String location;
        private int status;
        private String time;
        private int jobType;
        private int type;
        private String startjobDate;
        private String endjobDate;
        private String state;
        private String city;
        private String zipCode;
        private String appartment;
        private String street;
        private UserBean user;

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

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
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

        public String getStartjobDate() {
            return startjobDate;
        }

        public void setStartjobDate(String startjobDate) {
            this.startjobDate = startjobDate;
        }

        public String getEndjobDate() {
            return endjobDate;
        }

        public void setEndjobDate(String endjobDate) {
            this.endjobDate = endjobDate;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }

        public String getAppartment() {
            return appartment;
        }

        public void setAppartment(String appartment) {
            this.appartment = appartment;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * id : 88
             * firstName : Sanjeev
             * lastName : Sharma
             * image : 90c38d2c-7b66-4dd1-ba44-b81a7c5a7aa8.jpg
             * phone : 9781630062
             */

            private int id;
            private String firstName;
            private String lastName;
            private String image;
            private String phone;

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

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }
        }
    }
}
