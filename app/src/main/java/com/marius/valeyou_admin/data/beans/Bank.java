package com.marius.valeyou_admin.data.beans;

public class Bank {
    String id;
    String name="";

    public Bank(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String price) {
        this.name = price;
    }
}