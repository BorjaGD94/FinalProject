package com.example.borja.finalproject;

public class UberPriceJSON {

    public prices[] prices ;
    public UberPriceJSON(){

    }

}


class prices {
    public String localized_display_name;
    public double distance;
    public String display_name;
    public String product_id;
    public Integer high_estimate;
    public Integer low_estimate;
    public float duration;
    public String estimate;
    public String currency_code;
}
