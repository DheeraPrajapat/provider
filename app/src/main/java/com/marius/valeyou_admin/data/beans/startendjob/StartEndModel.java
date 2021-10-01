package com.marius.valeyou_admin.data.beans.startendjob;

public class StartEndModel {

    /**
     * id : 201
     * userId : 88
     * providerId : 127
     * startjobDate : 2147483647
     * endjobDate : 2147483647
     */

    private int id;
    private int userId;
    private int providerId;
    private int startjobDate;
    private int endjobDate;

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

    public int getStartjobDate() {
        return startjobDate;
    }

    public void setStartjobDate(int startjobDate) {
        this.startjobDate = startjobDate;
    }

    public int getEndjobDate() {
        return endjobDate;
    }

    public void setEndjobDate(int endjobDate) {
        this.endjobDate = endjobDate;
    }
}
