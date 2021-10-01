package com.marius.valeyou_admin.data.beans.reviews;

public class MyReview {

    /**
     * id : 4
     * userBy : 67
     * userTo : 122
     * ratings : 4.5
     * description : good job
     * type : 1
     * createdAt : 1585216676
     * userFirstName : chandan
     * userLastName : thakur
     * userImage : 860943ae-1f19-470b-8da6-3ee74f3da0b9.png
     */

    private int id;
    private int userBy;
    private int userTo;
    private double ratings;
    private String description;
    private int type;
    private int createdAt;
    private String userFirstName;
    private String userLastName;
    private String userImage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserBy() {
        return userBy;
    }

    public void setUserBy(int userBy) {
        this.userBy = userBy;
    }

    public int getUserTo() {
        return userTo;
    }

    public void setUserTo(int userTo) {
        this.userTo = userTo;
    }

    public double getRatings() {
        return ratings;
    }

    public void setRatings(double ratings) {
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

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
