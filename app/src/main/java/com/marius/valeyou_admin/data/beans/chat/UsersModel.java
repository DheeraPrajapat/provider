package com.marius.valeyou_admin.data.beans.chat;

import android.os.Parcel;
import android.os.Parcelable;

public class UsersModel implements Parcelable {


    /**
     * id : 4
     * lastMsgId : 141
     * chat : {"id":141,"userId":88,"user2Id":127,"message":"Hye","createdAt":1582196916,"unread_message":1,"user":{"id":88,"name":"Sanjeev Sharma","image":"90c38d2c-7b66-4dd1-ba44-b81a7c5a7aa8.jpg"}}
     */

    private int id;
    private int lastMsgId;
    private ChatBean chat;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLastMsgId() {
        return lastMsgId;
    }

    public void setLastMsgId(int lastMsgId) {
        this.lastMsgId = lastMsgId;
    }

    public ChatBean getChat() {
        return chat;
    }

    public void setChat(ChatBean chat) {
        this.chat = chat;
    }

    public static class ChatBean implements Parcelable {
        /**
         * id : 141
         * userId : 88
         * user2Id : 127
         * message : Hye
         * createdAt : 1582196916
         * unread_message : 1
         * user : {"id":88,"name":"Sanjeev Sharma","image":"90c38d2c-7b66-4dd1-ba44-b81a7c5a7aa8.jpg"}
         */

        private int id;
        private int userId;
        private int user2Id;
        private String message;
        private int createdAt;
        private int unread_message;
        private UserBean user;
        private int msgType;

        public int getMsgType() {
            return msgType;
        }

        public void setMsgType(int msgType) {
            this.msgType = msgType;
        }

        public static Creator<ChatBean> getCREATOR() {
            return CREATOR;
        }

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

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(int createdAt) {
            this.createdAt = createdAt;
        }

        public int getUnread_message() {
            return unread_message;
        }

        public void setUnread_message(int unread_message) {
            this.unread_message = unread_message;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean implements Parcelable {
            /**
             * id : 88
             * name : Sanjeev Sharma
             * image : 90c38d2c-7b66-4dd1-ba44-b81a7c5a7aa8.jpg
             */

            private int id;
            private String name;
            private String image;

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

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.id);
                dest.writeString(this.name);
                dest.writeString(this.image);
            }

            public UserBean() {
            }

            protected UserBean(Parcel in) {
                this.id = in.readInt();
                this.name = in.readString();
                this.image = in.readString();
            }

            public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
                @Override
                public UserBean createFromParcel(Parcel source) {
                    return new UserBean(source);
                }

                @Override
                public UserBean[] newArray(int size) {
                    return new UserBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeInt(this.userId);
            dest.writeInt(this.user2Id);
            dest.writeString(this.message);
            dest.writeInt(this.createdAt);
            dest.writeInt(this.unread_message);
            dest.writeParcelable(this.user, flags);
        }

        public ChatBean() {
        }

        protected ChatBean(Parcel in) {
            this.id = in.readInt();
            this.userId = in.readInt();
            this.user2Id = in.readInt();
            this.message = in.readString();
            this.createdAt = in.readInt();
            this.unread_message = in.readInt();
            this.user = in.readParcelable(UserBean.class.getClassLoader());
        }

        public static final Creator<ChatBean> CREATOR = new Creator<ChatBean>() {
            @Override
            public ChatBean createFromParcel(Parcel source) {
                return new ChatBean(source);
            }

            @Override
            public ChatBean[] newArray(int size) {
                return new ChatBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.lastMsgId);
        dest.writeParcelable(this.chat, flags);
    }

    public UsersModel() {
    }

    protected UsersModel(Parcel in) {
        this.id = in.readInt();
        this.lastMsgId = in.readInt();
        this.chat = in.readParcelable(ChatBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<UsersModel> CREATOR = new Parcelable.Creator<UsersModel>() {
        @Override
        public UsersModel createFromParcel(Parcel source) {
            return new UsersModel(source);
        }

        @Override
        public UsersModel[] newArray(int size) {
            return new UsersModel[size];
        }
    };
}
