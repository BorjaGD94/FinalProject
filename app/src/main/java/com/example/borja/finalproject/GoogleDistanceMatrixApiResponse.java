package com.example.borja.finalproject;


import java.util.List;

public class GoogleDistanceMatrixApiResponse {

    public List<String> destination_addresses = null;
    public List<String> origin_addresses = null;
    public List<Row> rows = null;
    public String status;

}





   /* List<Rows> rows;
    private List<String> destination_addresses;
    private List<String> origin_addresses;

    public List<String> getDestination_addresses() {
        return destination_addresses;
    }

    public List<String> getOrigin_addresses() {
        return origin_addresses;
    }

    public List<Rows> getRows() {
        return rows;
    }

    static class Rows {
        List<Element> elements;

        public List<Element> getElements() {
            return elements;
        }
    }


    class Element {

        Distance distance;
        Duration duration;

        public Distance getDistance() {
            return distance;
        }

        public Duration getDuration() {
            return duration;
        }
    }

    class Distance {
        String text;
        String value;

        public String getText() {
            return text;
        }

        public String getValue() {
            return value;
        }
    }

    class Duration {
        String text;
        String value;

        public String getText() {
            return text;
        }

        public String getValue() {
            return value;
        }

    }
}*/