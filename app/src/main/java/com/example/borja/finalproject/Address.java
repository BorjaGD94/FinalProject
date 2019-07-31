package com.example.borja.finalproject;

public class Address {


    private int id;
    private String address;
    private String  lat;
    private String log;
    private String type;

    public Address () {}

    public Address(String address, String lat, String log, String type) {
        super();
        this.address=address;
        this.lat=lat;
        this.log = log;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Place{" + "id=" + id + ", address='" + address + '\'' + ", lat=" + lat + ", log=" + log + ", type='" + type + '\'' + '}';
    }
}
