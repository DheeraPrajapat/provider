package com.marius.valeyou_admin.data.beans.userprofile;

public class UserProfileModel {


    /**
     * id : 124
     * firstName : Sanjeev
     * lastName : Gautam
     * email : sanjeev1991.ss@gmail.com
     * image : 5a32e6e1-96f8-4cf7-a9f2-147dba20be27.jpg
     * phone : 9781630062
     * countryCode : +91
     * description : I am Project Manager
     * location : São Carlos, State of São Paulo, Brazil
     * latitude : 31.3851609
     * longitude : 75.3857104
     * age : 0
     * dob : 10/11/1992
     * city : São Carlos
     * state : State of São Paulo
     * avg_rating : 4.7
     * total_completed_jobs : 12
     * total_completed_jobs_us : 1
     */

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String image;
    private String phone;
    private String countryCode;
    private String description;
    private String location;
    private String latitude;
    private String longitude;
    private int age;
    private String dob;
    private String city;
    private String state;
    private double avg_rating;
    private int total_completed_jobs;
    private int total_completed_jobs_us;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
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

    public double getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(double avg_rating) {
        this.avg_rating = avg_rating;
    }

    public int getTotal_completed_jobs() {
        return total_completed_jobs;
    }

    public void setTotal_completed_jobs(int total_completed_jobs) {
        this.total_completed_jobs = total_completed_jobs;
    }

    public int getTotal_completed_jobs_us() {
        return total_completed_jobs_us;
    }

    public void setTotal_completed_jobs_us(int total_completed_jobs_us) {
        this.total_completed_jobs_us = total_completed_jobs_us;
    }
}
