package com.marius.valeyou_admin.data.beans;

import java.util.List;

public class RequestBean {
    private String category_id;
    private String price;
    private List<RequestSubCategory> subcategory;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public List<RequestSubCategory> getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(List<RequestSubCategory> subcategory) {
        this.subcategory = subcategory;
    }
}
