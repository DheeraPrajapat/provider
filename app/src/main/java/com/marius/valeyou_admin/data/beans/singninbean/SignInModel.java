package com.marius.valeyou_admin.data.beans.singninbean;

import java.util.List;

public class SignInModel {

    /**
     * id : 270
     * name :
     * firstName : Navjot
     * lastName : Rao
     * email : navr595@gmail.com
     * forgotPassword :
     * countryCode :
     * state :
     * city :
     * street :
     * houseNumber :
     * appartment :
     * zipCode :
     * phone : 97816 30062
     * image :
     * dob :
     * age : 0
     * loginType : 0
     * isEmailVerify : 0
     * otp : 777
     * description :
     * socialId :
     * socialType : 0
     * status : 1
     * price :
     * tip : 0
     * isApprove : 0
     * isRead : 0
     * businessLicence :
     * driverLicence :
     * socialSecurityNo :
     * insurance :
     * resume :
     * vehicleType :
     * address :
     * latitude : 30.7046
     * longitude : 76.7179
     * authKey : 12d0d6fbbcd98638e27157d567e8d5ecbc8bb2a1fbc8fa712d
     * online : 0
     * profession :
     * deviceType : 1
     * deviceToken : xyz
     * paypalId :
     * categoryId :
     * tagId : 178d9b81c27ea948b5eb7e68bf7bbc38b6b7a015579807e33d_270
     * stripeId :
     * createdAt : 2020-10-31T06:45:30.000Z
     * updatedAt : 2020-10-31T06:46:08.000Z
     */

    private int id;
    private String name;
    private String firstName;
    private String lastName;
    private String email;
    private String forgotPassword;
    private String countryCode;
    private String state;
    private String city;
    private String street;
    private String houseNumber;
    private String appartment;
    private String zipCode;
    private String phone;
    private String image;
    private String dob;
    private int age;
    private int loginType;
    private int isEmailVerify;
    private String otp;
    private String description;
    private String socialId;
    private int socialType;
    private int status;
    private String price;
    private int tip;
    private int isApprove;
    private int isRead;
    private String businessLicence;
    private String driverLicence;
    private String socialSecurityNo;
    private String insurance;
    private String resume;
    private String vehicleType;
    private String address;
    private double latitude;
    private double longitude;
    private String authKey;
    private int online;
    private String profession;
    private int deviceType;
    private String deviceToken;
    private String paypalId;
    private String categoryId;
    private String tagId;
    private String stripeId;
    private String createdAt;
    private String updatedAt;

    private List<ProviderPortfoliosBean> providerPortfolios;
    private List<ProviderLanguagesBean> providerLanguages;
    private List<?> certificates;
    private List<?> business_hours;
    private List<ProviderCategoriesBean> providerCategories;


    public List<ProviderPortfoliosBean> getProviderPortfolios() {
        return providerPortfolios;
    }

    public void setProviderPortfolios(List<ProviderPortfoliosBean> providerPortfolios) {
        this.providerPortfolios = providerPortfolios;
    }

    public List<ProviderLanguagesBean> getProviderLanguages() {
        return providerLanguages;
    }

    public void setProviderLanguages(List<ProviderLanguagesBean> providerLanguages) {
        this.providerLanguages = providerLanguages;
    }

    public List<?> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<?> certificates) {
        this.certificates = certificates;
    }

    public List<?> getBusiness_hours() {
        return business_hours;
    }

    public void setBusiness_hours(List<?> business_hours) {
        this.business_hours = business_hours;
    }

    public List<ProviderCategoriesBean> getProviderCategories() {
        return providerCategories;
    }

    public void setProviderCategories(List<ProviderCategoriesBean> providerCategories) {
        this.providerCategories = providerCategories;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getForgotPassword() {
        return forgotPassword;
    }

    public void setForgotPassword(String forgotPassword) {
        this.forgotPassword = forgotPassword;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getAppartment() {
        return appartment;
    }

    public void setAppartment(String appartment) {
        this.appartment = appartment;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public int getIsEmailVerify() {
        return isEmailVerify;
    }

    public void setIsEmailVerify(int isEmailVerify) {
        this.isEmailVerify = isEmailVerify;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public int getSocialType() {
        return socialType;
    }

    public void setSocialType(int socialType) {
        this.socialType = socialType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getTip() {
        return tip;
    }

    public void setTip(int tip) {
        this.tip = tip;
    }

    public int getIsApprove() {
        return isApprove;
    }

    public void setIsApprove(int isApprove) {
        this.isApprove = isApprove;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public String getBusinessLicence() {
        return businessLicence;
    }

    public void setBusinessLicence(String businessLicence) {
        this.businessLicence = businessLicence;
    }

    public String getDriverLicence() {
        return driverLicence;
    }

    public void setDriverLicence(String driverLicence) {
        this.driverLicence = driverLicence;
    }

    public String getSocialSecurityNo() {
        return socialSecurityNo;
    }

    public void setSocialSecurityNo(String socialSecurityNo) {
        this.socialSecurityNo = socialSecurityNo;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getPaypalId() {
        return paypalId;
    }

    public void setPaypalId(String paypalId) {
        this.paypalId = paypalId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getStripeId() {
        return stripeId;
    }

    public void setStripeId(String stripeId) {
        this.stripeId = stripeId;
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

    public static class ProviderPortfoliosBean {
        /**
         * id : 22
         * providerId : 165
         * title : New Project
         * description : testing app
         * status : 0
         * image : 0d9b8a91-a274-41ef-b5dc-79569c690977.png
         * createdAt : 2020-07-23T09:30:34.000Z
         * updatedAt : 2020-07-23T09:30:34.000Z
         */

        private int id;
        private int providerId;
        private String title;
        private String description;
        private int status;
        private String image;
        private String createdAt;
        private String updatedAt;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
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

    public static class ProviderLanguagesBean {
        /**
         * id : 30
         * providerId : 165
         * name : Amharic
         * type : Conversational
         * status : 0
         * createdAt : 2020-07-23T09:29:58.000Z
         * updatedAt : 2020-07-23T09:29:58.000Z
         */

        private int id;
        private int providerId;
        private String name;
        private String type;
        private int status;
        private String createdAt;
        private String updatedAt;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getProviderId() {
            return providerId;
        }

        public void setProviderId(int providerId) {
            this.providerId = providerId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
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

    public static class ProviderCategoriesBean {
        /**
         * id : 182
         * providerId : 165
         * categoryId : 18
         * category : {"id":18,"name":"Saloon","image":"saloon.png"}
         * providerSubCategories : [{"id":73,"providerId":165,"subCategoryId":4,"price":20,"subCategory":{"id":4,"name":"Spa","image":"kindpng_1721146.png"}},{"id":74,"providerId":165,"subCategoryId":5,"price":30,"subCategory":{"id":5,"name":"Beard Cutting","image":"Beard-trim-04.png"}}]
         */

        private int id;
        private int providerId;
        private int categoryId;
        private CategoryBean category;
        private List<ProviderSubCategoriesBean> providerSubCategories;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getProviderId() {
            return providerId;
        }

        public void setProviderId(int providerId) {
            this.providerId = providerId;
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

        public List<ProviderSubCategoriesBean> getProviderSubCategories() {
            return providerSubCategories;
        }

        public void setProviderSubCategories(List<ProviderSubCategoriesBean> providerSubCategories) {
            this.providerSubCategories = providerSubCategories;
        }

        public static class CategoryBean {
            /**
             * id : 18
             * name : Saloon
             * image : saloon.png
             */

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

        public static class ProviderSubCategoriesBean {
            /**
             * id : 73
             * providerId : 165
             * subCategoryId : 4
             * price : 20
             * subCategory : {"id":4,"name":"Spa","image":"kindpng_1721146.png"}
             */

            private int id;
            private int providerId;
            private int subCategoryId;
            private int price;
            private SubCategoryBean subCategory;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getProviderId() {
                return providerId;
            }

            public void setProviderId(int providerId) {
                this.providerId = providerId;
            }

            public int getSubCategoryId() {
                return subCategoryId;
            }

            public void setSubCategoryId(int subCategoryId) {
                this.subCategoryId = subCategoryId;
            }

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }

            public SubCategoryBean getSubCategory() {
                return subCategory;
            }

            public void setSubCategory(SubCategoryBean subCategory) {
                this.subCategory = subCategory;
            }

            public static class SubCategoryBean {
                /**
                 * id : 4
                 * name : Spa
                 * image : kindpng_1721146.png
                 */

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
    }
}