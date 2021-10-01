package com.marius.valeyou_admin.data.beans.profilebean;

public class LanguagesModel {

    /**
     * id : 4
     * providerId : 127
     * name : Spanish
     * type : Conversational
     * status : 0
     * createdAt : 2020-07-06T04:41:33.000Z
     * updatedAt : 2020-07-06T04:41:33.000Z
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
