package com.example.borja.finalproject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Row {

    public List<Element> elements = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
