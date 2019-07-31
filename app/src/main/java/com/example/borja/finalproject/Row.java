package com.example.borja.finalproject;

import java.util.ArrayList;
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

    public List<Integer> ElementsToListOfDistances(List<Element> elements){
        List<Integer> distances = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++){
            distances.add(elements.get(i).distance.value);
        }
        return distances;
    }

    public List<Integer> ElementsToListOfDurations(List<Element> elements){
        List<Integer> durations = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++){
            durations.add(elements.get(i).duration.value);
        }
        return durations;
    }

    @Override
    public String toString() {
        return "Row{" +
                "elements=" + elements +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}
