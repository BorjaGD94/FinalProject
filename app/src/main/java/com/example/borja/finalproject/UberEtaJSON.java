package com.example.borja.finalproject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class UberEtaJSON {

    public times[] times ;
    public UberEtaJSON(){

    }

}

@JsonIgnoreProperties(ignoreUnknown = true)


class times {
    public String localized_display_name;
    public Integer estimate;
    public String display_name;
    public String product_id;
}
