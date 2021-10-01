package com.marius.valeyou_admin.data.beans;

public class NotificationBadgeModel {


    /**
     * count : 108
     */

    private int count;
    private int notification;
    private String stripeId;


    public String getStripeId() {
        return stripeId;
    }

    public void setStripeId(String stripeId) {
        this.stripeId = stripeId;
    }

    public int getNotification() {
        return notification;
    }

    public void setNotification(int notification) {
        this.notification = notification;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
