package com.example.borja.finalproject;

public class LocationDivvy
{
    private String latitude;

    private String human_address;

    private String longitude;

    public String getLatitude ()
    {
        return latitude;
    }

    public void setLatitude (String latitude)
    {
        this.latitude = latitude;
    }

    public String getHuman_address ()
    {
        return human_address;
    }

    public void setHuman_address (String human_address)
    {
        this.human_address = human_address;
    }

    public String getLongitude ()
    {
        return longitude;
    }

    public void setLongitude (String longitude)
    {
        this.longitude = longitude;
    }

    @Override
    public String toString()
    {
        return "Station Location {latitude = "+latitude+", human_address = "+human_address+", longitude = "+longitude+"}";
    }
}
