package com.example.borja.finalproject;

public class Adress {


    private int id;
    private String adress;
    private String  lat;
    private String log;
    private String type;

    public Adress () {}

    public Adress (String adress, String lat, String log, String type) {
        super();
        this.adress=adress;
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

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
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
        return "Place{" + "id=" + id + ", adress='" + adress + '\'' + ", lat=" + lat + ", log=" + log + ", type='" + type + '\'' + '}';
    }
}
