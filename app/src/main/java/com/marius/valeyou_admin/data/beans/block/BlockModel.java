package com.marius.valeyou_admin.data.beans.block;

public class BlockModel {

    /**
     * id : 8
     * userId : 295
     * user2Id : 124
     * type : 1
     * createdAt : 2020-12-10T14:42:36.000Z
     * updatedAt : 2020-12-10T14:42:36.000Z
     * user : {"id":124,"first_name":"Sanjeev","last_name":"Gautam","image":"5a32e6e1-96f8-4cf7-a9f2-147dba20be27.jpg"}
     */

    private int id;
    private int userId;
    private int user2Id;
    private int type;
    private String createdAt;
    private String updatedAt;
    private UserBean user;

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

    public int getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(int user2Id) {
        this.user2Id = user2Id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean {
        /**
         * id : 124
         * first_name : Sanjeev
         * last_name : Gautam
         * image : 5a32e6e1-96f8-4cf7-a9f2-147dba20be27.jpg
         */

        private int id;
        private String first_name;
        private String last_name;
        private String image;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
