package com.marius.valeyou_admin.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

public class IdentityModel implements Parcelable {


    /**
     * id : 1
     * providerId : 127
     * image : 8eb8554b-8371-4689-ae9e-6cb1d1578822.jpg
     */

    private int id;
    private int status;
    private int providerId;
    private String image;
    private String backImage;
    private String selfie;


    public String getSelfie() {
        return selfie;
    }

    public void setSelfie(String selfie) {
        this.selfie = selfie;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBackImage() {
        return backImage;
    }

    public void setBackImage(String backImage) {
        this.backImage = backImage;
    }

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
        dest.writeInt(this.providerId);
        dest.writeString(this.image);
        dest.writeString(this.backImage);
        dest.writeString(this.selfie);
    }

    public IdentityModel() {
    }

    protected IdentityModel(Parcel in) {
        this.id = in.readInt();
        this.providerId = in.readInt();
        this.image = in.readString();
        this.backImage = in.readString();
        this.selfie = in.readString();
    }

    public static final Parcelable.Creator<IdentityModel> CREATOR = new Parcelable.Creator<IdentityModel>() {
        @Override
        public IdentityModel createFromParcel(Parcel source) {
            return new IdentityModel(source);
        }

        @Override
        public IdentityModel[] newArray(int size) {
            return new IdentityModel[size];
        }
    };
}
