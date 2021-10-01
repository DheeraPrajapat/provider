package com.marius.valeyou_admin.data.beans;

public class Adress {
    String zip="";

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    String state;
    String city;
    String neighborhood;
    String street;
    String service;

    public Adress(String zip) {
        this.zip = zip;
    }
    public String getZip() {
        return zip;
    }

    public void setZip(String price) {
        this.zip = price;
    }
}