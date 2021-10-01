package com.marius.valeyou_admin.data.beans.jobs;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class JobDetailModel implements Parcelable {

    /**
     * id : 373
     * date : 1608538190
     * title : Event Job
     * startTime : 1608538190
     * endTime : 1608559790
     * startPrice : 0
     * endPrice : 405
     * description : testing...
     * location : São José dos Campos, Sao Jose dos Campos - State of São Paulo, Brazil
     * city : São José dos Campos
     * state : São Paulo
     * status : 6
     * time : 1608538190
     * jobType : 1
     * type : 2
     * startjobDate : 1608538501
     * endjobDate : 1608538644
     * cityId : 35
     * stateId : 3549904
     * zipCode : 1235
     * appartment : 123
     * confirmationCode : 183157
     * street :
     * bid_price : 300
     * bid_status : 1
     * fav : 2
     * isPayment : 2
     * isRate : 1
     * orderCategories : [{"id":178,"categoryId":48,"category":{"id":48,"name":"Eventos","image":"Events.png"},"subCategory":[]}]
     * orderImages : [{"id":84,"orderId":373,"images":"20d5281c-1b2c-46bd-98d2-dc29d41b33ea.jpg","createdAt":"2020-12-21T08:11:05.000Z","updatedAt":"2020-12-21T08:11:05.000Z"}]
     * user : {"id":124,"firstName":"Sanjeev","lastName":"Gautam","image":"5a32e6e1-96f8-4cf7-a9f2-147dba20be27.jpg"}
     * order_activities : []
     * additional_works : []
     * no_access_charges : []
     * provider_rating : {"id":44,"userby":295,"userTo":124,"orderId":373,"ratings":4,"description":"i rated this job with 4 star.","type":2,"createdAt":1608545065,"updatedAt":"2020-12-21T10:04:25.000Z","userBy":295}
     * user_rating : {"id":45,"userby":124,"userTo":295,"orderId":373,"ratings":5,"description":"5 star service","type":1,"createdAt":1608546408,"updatedAt":"2020-12-21T10:26:48.000Z","userBy":124}
     */

    private int id;
    private String date;
    private String title;
    private String startTime;
    private String endTime;
    private String startPrice;
    private String endPrice;
    private String description;
    private String location;
    private String city;
    private String state;
    private int status;
    private String time;
    private int jobType;
    private int type;
    private String startjobDate;
    private String endjobDate;
    private int cityId;
    private int stateId;
    private String zipCode;
    private String appartment;
    private String confirmationCode;
    private String street;
    private int bid_price;
    private int bid_status;
    private int fav;
    private int isPayment;
    private int isRate;
    private int isSchedule;
    private String reScheduleDate;
    private int total_additional_amount;

    public int getTotal_additional_amount() {
        return total_additional_amount;
    }

    public void setTotal_additional_amount(int total_additional_amount) {
        this.total_additional_amount = total_additional_amount;
    }

    /**
     * id : 124
     * firstName : Sanjeev
     * lastName : Gautam
     * image : 5a32e6e1-96f8-4cf7-a9f2-147dba20be27.jpg
     */



    private UserBean user;
    /**
     * id : 44
     * userby : 295
     * userTo : 124
     * orderId : 373
     * ratings : 4
     * description : i rated this job with 4 star.
     * type : 2
     * createdAt : 1608545065
     * updatedAt : 2020-12-21T10:04:25.000Z
     * userBy : 295
     */

    private ProviderRatingBean provider_rating;
    private List<ProviderWorkImagesBean> provider_work_images;

    public List<ProviderWorkImagesBean> getProvider_work_images() {
        return provider_work_images;
    }

    public void setProvider_work_images(List<ProviderWorkImagesBean> provider_work_images) {
        this.provider_work_images = provider_work_images;
    }

    /**
     * id : 45
     * userby : 124
     * userTo : 295
     * orderId : 373
     * ratings : 5
     * description : 5 star service
     * type : 1
     * createdAt : 1608546408
     * updatedAt : 2020-12-21T10:26:48.000Z
     * userBy : 124
     */

    private UserRatingBean user_rating;
    /**
     * id : 178
     * categoryId : 48
     * category : {"id":48,"name":"Eventos","image":"Events.png"}
     * subCategory : []
     */

    private List<OrderCategoriesBean> orderCategories;
    /**
     * id : 84
     * orderId : 373
     * images : 20d5281c-1b2c-46bd-98d2-dc29d41b33ea.jpg
     * createdAt : 2020-12-21T08:11:05.000Z
     * updatedAt : 2020-12-21T08:11:05.000Z
     */

    private List<OrderImagesBean> orderImages;
    private List<OrderActivitesBean> order_activities;
    private List<AdditionalWorksBean> additional_works;
    private List<NoAccessChargesBean> no_access_charges;

    protected JobDetailModel(Parcel in) {
        id = in.readInt();
        date = in.readString();
        title = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        startPrice = in.readString();
        endPrice = in.readString();
        description = in.readString();
        location = in.readString();
        city = in.readString();
        state = in.readString();
        status = in.readInt();
        time = in.readString();
        jobType = in.readInt();
        type = in.readInt();
        startjobDate = in.readString();
        endjobDate = in.readString();
        cityId = in.readInt();
        stateId = in.readInt();
        zipCode = in.readString();
        appartment = in.readString();
        confirmationCode = in.readString();
        street = in.readString();
        bid_price = in.readInt();
        bid_status = in.readInt();
        fav = in.readInt();
        isPayment = in.readInt();
        isRate = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(date);
        dest.writeString(title);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(startPrice);
        dest.writeString(endPrice);
        dest.writeString(description);
        dest.writeString(location);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeInt(status);
        dest.writeString(time);
        dest.writeInt(jobType);
        dest.writeInt(type);
        dest.writeString(startjobDate);
        dest.writeString(endjobDate);
        dest.writeInt(cityId);
        dest.writeInt(stateId);
        dest.writeString(zipCode);
        dest.writeString(appartment);
        dest.writeString(confirmationCode);
        dest.writeString(street);
        dest.writeInt(bid_price);
        dest.writeInt(bid_status);
        dest.writeInt(fav);
        dest.writeInt(isPayment);
        dest.writeInt(isRate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<JobDetailModel> CREATOR = new Creator<JobDetailModel>() {
        @Override
        public JobDetailModel createFromParcel(Parcel in) {
            return new JobDetailModel(in);
        }

        @Override
        public JobDetailModel[] newArray(int size) {
            return new JobDetailModel[size];
        }
    };

    public int getIsSchedule() {
        return isSchedule;
    }

    public void setIsSchedule(int isSchedule) {
        this.isSchedule = isSchedule;
    }

    public String getReScheduleDate() {
        return reScheduleDate;
    }

    public void setReScheduleDate(String reScheduleDate) {
        this.reScheduleDate = reScheduleDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
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

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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

    public int getFav() {
        return fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    public int getIsPayment() {
        return isPayment;
    }

    public void setIsPayment(int isPayment) {
        this.isPayment = isPayment;
    }

    public int getIsRate() {
        return isRate;
    }

    public void setIsRate(int isRate) {
        this.isRate = isRate;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public ProviderRatingBean getProvider_rating() {
        return provider_rating;
    }

    public void setProvider_rating(ProviderRatingBean provider_rating) {
        this.provider_rating = provider_rating;
    }

    public UserRatingBean getUser_rating() {
        return user_rating;
    }

    public void setUser_rating(UserRatingBean user_rating) {
        this.user_rating = user_rating;
    }

    public List<OrderCategoriesBean> getOrderCategories() {
        return orderCategories;
    }

    public void setOrderCategories(List<OrderCategoriesBean> orderCategories) {
        this.orderCategories = orderCategories;
    }

    public List<OrderImagesBean> getOrderImages() {
        return orderImages;
    }

    public void setOrderImages(List<OrderImagesBean> orderImages) {
        this.orderImages = orderImages;
    }

    public List<OrderActivitesBean> getOrder_activities() {
        return order_activities;
    }

    public void setOrder_activities(List<OrderActivitesBean> order_activities) {
        this.order_activities = order_activities;
    }

    public List<AdditionalWorksBean> getAdditional_works() {
        return additional_works;
    }

    public void setAdditional_works(List<AdditionalWorksBean> additional_works) {
        this.additional_works = additional_works;
    }

    public List<NoAccessChargesBean> getNo_access_charges() {
        return no_access_charges;
    }

    public void setNo_access_charges(List<NoAccessChargesBean> no_access_charges) {
        this.no_access_charges = no_access_charges;
    }

    public static class UserBean implements Parcelable{
        private int id;
        private String firstName;
        private String lastName;
        private String image;

        protected UserBean(Parcel in) {
            id = in.readInt();
            firstName = in.readString();
            lastName = in.readString();
            image = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(firstName);
            dest.writeString(lastName);
            dest.writeString(image);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
            @Override
            public UserBean createFromParcel(Parcel in) {
                return new UserBean(in);
            }

            @Override
            public UserBean[] newArray(int size) {
                return new UserBean[size];
            }
        };

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

    public static class ProviderRatingBean implements Parcelable{
        private String id;
        private int userby;
        private int userTo;
        private int orderId;
        private String ratings;
        private String description;
        private int type;
        private int createdAt;
        private String updatedAt;
        private int userBy;

        protected ProviderRatingBean(Parcel in) {
            id = in.readString();
            userby = in.readInt();
            userTo = in.readInt();
            orderId = in.readInt();
            ratings = in.readString();
            description = in.readString();
            type = in.readInt();
            createdAt = in.readInt();
            updatedAt = in.readString();
            userBy = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeInt(userby);
            dest.writeInt(userTo);
            dest.writeInt(orderId);
            dest.writeString(ratings);
            dest.writeString(description);
            dest.writeInt(type);
            dest.writeInt(createdAt);
            dest.writeString(updatedAt);
            dest.writeInt(userBy);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ProviderRatingBean> CREATOR = new Creator<ProviderRatingBean>() {
            @Override
            public ProviderRatingBean createFromParcel(Parcel in) {
                return new ProviderRatingBean(in);
            }

            @Override
            public ProviderRatingBean[] newArray(int size) {
                return new ProviderRatingBean[size];
            }
        };

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getUserby() {
            return userby;
        }

        public void setUserby(int userby) {
            this.userby = userby;
        }

        public int getUserTo() {
            return userTo;
        }

        public void setUserTo(int userTo) {
            this.userTo = userTo;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public String getRatings() {
            return ratings;
        }

        public void setRatings(String ratings) {
            this.ratings = ratings;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(int createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public int getUserBy() {
            return userBy;
        }

        public void setUserBy(int userBy) {
            this.userBy = userBy;
        }
    }

    public static class ProviderWorkImagesBean {

        /**
         * id : 6
         * order_id : 14
         * provider_id : 243
         * image : 29e91a1d-fc1f-4f02-8db3-bfc0c5ff299f.jpg
         * type : 0
         * status : 1
         * created_at : 1611569446
         * updated_at : 1611569446
         */

        private int id;
        private int order_id;
        private int provider_id;
        private String image;
        private int type;
        private int status;
        private int created_at;
        private int updated_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }

        public int getProvider_id() {
            return provider_id;
        }

        public void setProvider_id(int provider_id) {
            this.provider_id = provider_id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getCreated_at() {
            return created_at;
        }

        public void setCreated_at(int created_at) {
            this.created_at = created_at;
        }

        public int getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(int updated_at) {
            this.updated_at = updated_at;
        }
    }

    public static class UserRatingBean implements Parcelable {
        private String id;
        private int userby;
        private int userTo;
        private int orderId;
        private String ratings;
        private String description;
        private int type;
        private int createdAt;
        private String updatedAt;
        private int userBy;

        protected UserRatingBean(Parcel in) {
            id = in.readString();
            userby = in.readInt();
            userTo = in.readInt();
            orderId = in.readInt();
            ratings = in.readString();
            description = in.readString();
            type = in.readInt();
            createdAt = in.readInt();
            updatedAt = in.readString();
            userBy = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeInt(userby);
            dest.writeInt(userTo);
            dest.writeInt(orderId);
            dest.writeString(ratings);
            dest.writeString(description);
            dest.writeInt(type);
            dest.writeInt(createdAt);
            dest.writeString(updatedAt);
            dest.writeInt(userBy);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<UserRatingBean> CREATOR = new Creator<UserRatingBean>() {
            @Override
            public UserRatingBean createFromParcel(Parcel in) {
                return new UserRatingBean(in);
            }

            @Override
            public UserRatingBean[] newArray(int size) {
                return new UserRatingBean[size];
            }
        };

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getUserby() {
            return userby;
        }

        public void setUserby(int userby) {
            this.userby = userby;
        }

        public int getUserTo() {
            return userTo;
        }

        public void setUserTo(int userTo) {
            this.userTo = userTo;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public String getRatings() {
            return ratings;
        }

        public void setRatings(String ratings) {
            this.ratings = ratings;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(int createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public int getUserBy() {
            return userBy;
        }

        public void setUserBy(int userBy) {
            this.userBy = userBy;
        }
    }

    public static class OrderCategoriesBean implements Parcelable {
        private int id;
        private int categoryId;
        /**
         * id : 48
         * name : Eventos
         * image : Events.png
         */

        private CategoryBean category;
        private List<?> subCategory;

        protected OrderCategoriesBean(Parcel in) {
            id = in.readInt();
            categoryId = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeInt(categoryId);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<OrderCategoriesBean> CREATOR = new Creator<OrderCategoriesBean>() {
            @Override
            public OrderCategoriesBean createFromParcel(Parcel in) {
                return new OrderCategoriesBean(in);
            }

            @Override
            public OrderCategoriesBean[] newArray(int size) {
                return new OrderCategoriesBean[size];
            }
        };

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public CategoryBean getCategory() {
            return category;
        }

        public void setCategory(CategoryBean category) {
            this.category = category;
        }

        public List<?> getSubCategory() {
            return subCategory;
        }

        public void setSubCategory(List<?> subCategory) {
            this.subCategory = subCategory;
        }

        public static class CategoryBean {
            private int id;
            private String name;
            private String image;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }
        }
    }

    public static class OrderImagesBean implements Parcelable{
        private int id;
        private int orderId;
        private String images;
        private String createdAt;
        private String updatedAt;

        protected OrderImagesBean(Parcel in) {
            id = in.readInt();
            orderId = in.readInt();
            images = in.readString();
            createdAt = in.readString();
            updatedAt = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeInt(orderId);
            dest.writeString(images);
            dest.writeString(createdAt);
            dest.writeString(updatedAt);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<OrderImagesBean> CREATOR = new Creator<OrderImagesBean>() {
            @Override
            public OrderImagesBean createFromParcel(Parcel in) {
                return new OrderImagesBean(in);
            }

            @Override
            public OrderImagesBean[] newArray(int size) {
                return new OrderImagesBean[size];
            }
        };

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
    }

    public static class OrderActivitesBean implements Parcelable{

        /**
         * id : 16
         * user_id : 295
         * order_id : 1
         * message : Arriving Provider
         * code : 3
         * status : 3
         * created_at : 1610108515
         * updated_at : 1610108515
         */

        private int id;
        private int user_id;
        private int order_id;
        private String message;
        private int code;
        private int status;
        private int created_at;
        private int updated_at;

        protected OrderActivitesBean(Parcel in) {
            id = in.readInt();
            user_id = in.readInt();
            order_id = in.readInt();
            message = in.readString();
            code = in.readInt();
            status = in.readInt();
            created_at = in.readInt();
            updated_at = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeInt(user_id);
            dest.writeInt(order_id);
            dest.writeString(message);
            dest.writeInt(code);
            dest.writeInt(status);
            dest.writeInt(created_at);
            dest.writeInt(updated_at);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<OrderActivitesBean> CREATOR = new Creator<OrderActivitesBean>() {
            @Override
            public OrderActivitesBean createFromParcel(Parcel in) {
                return new OrderActivitesBean(in);
            }

            @Override
            public OrderActivitesBean[] newArray(int size) {
                return new OrderActivitesBean[size];
            }
        };

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getCreated_at() {
            return created_at;
        }

        public void setCreated_at(int created_at) {
            this.created_at = created_at;
        }

        public int getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(int updated_at) {
            this.updated_at = updated_at;
        }
    }

    public static class AdditionalWorksBean{

        /**
         * id : 7
         * title : test
         * price : 234
         * paid : 0
         */

        private int id;
        private String title;
        private int price;
        private int paid;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getPaid() {
            return paid;
        }

        public void setPaid(int paid) {
            this.paid = paid;
        }
    }

    public static class NoAccessChargesBean{

        /**
         * id : 3
         * description : 230
         * price : 0
         * paid : 0
         */

        private int id;
        private String description;
        private double price;
        private int paid;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getPaid() {
            return paid;
        }

        public void setPaid(int paid) {
            this.paid = paid;
        }
    }


}
