package com.marius.valeyou_admin.data.beans;

public class LanguagesBean {

    /**
     * id : 1
     * name : Abkhaz
     * nativeName : аҧсуа
     * status : Active
     * createdAt : 2020-07-22T07:07:30.000Z
     * updatedAt : 2020-07-22T07:07:30.000Z
     */

    private int id;
    private String name;
    private String nativeName;
    private String status;
    private String createdAt;
    private String updatedAt;

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

    public String getNativeName() {
        return nativeName;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
