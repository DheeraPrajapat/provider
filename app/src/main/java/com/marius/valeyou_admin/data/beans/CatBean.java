package com.marius.valeyou_admin.data.beans;

import java.util.List;

public class CatBean {
    int category_id;
    List<SubCatBean> subcategory;

    public CatBean(int category_id, List<SubCatBean> subcategory) {
        this.category_id = category_id;
        this.subcategory = subcategory;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public List<SubCatBean> getSubCatBean() {
        return subcategory;
    }

    public void setSubCatBean(List<SubCatBean> subcategory) {
        this.subcategory = subcategory;
    }


}
