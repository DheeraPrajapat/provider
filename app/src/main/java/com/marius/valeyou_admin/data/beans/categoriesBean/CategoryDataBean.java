package com.marius.valeyou_admin.data.beans.categoriesBean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class CategoryDataBean implements Serializable {

    /**
     * id : 19
     * name : House Cleaning
     * image : housecleaning.jpg
     * description : House Cleaning
     * type : 1
     * subCategories : [{"id":2,"name":"Bathroom Cleaning","image":"bathroom.jpg","categoryId":19,"description":"Bathroom Cleaning"}]
     */

    private int id;
    private String name;
    private String image;
    private String description;
    private int type;
    private int isCheck;
    private String price;
    private boolean isSelected;
    private List<SubCategoriesBean> subCategories;

    public int getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public List<SubCategoriesBean> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategoriesBean> subCategories) {
        this.subCategories = subCategories;
    }
    public static class SubCategoriesBean {
        /**
         * id : 2
         * name : Bathroom Cleaning
         * image : bathroom.jpg
         * categoryId : 19
         * description : Bathroom Cleaning
         */

        private int id;
        private String name;
        private String image;
        private int categoryId;
        private String description;
        private String price;
        private boolean check;

        private boolean localSelection;

        public boolean isLocalSelection() {
            return localSelection;
        }

        public void setLocalSelection(boolean localSelection) {
            this.localSelection = localSelection;
        }

        public int getIsCheck() {
            return isCheck;
        }

        public void setIsCheck(int isCheck) {
            this.isCheck = isCheck;
        }

        private int isCheck;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }
    }
}
