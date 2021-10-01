package com.marius.valeyou_admin.data.beans;

public class SubCatBean {
    int id;
    String price="";

    public SubCatBean(int id, String price) {
        this.id = id;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}