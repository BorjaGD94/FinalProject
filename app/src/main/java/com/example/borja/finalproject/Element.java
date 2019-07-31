package com.example.borja.finalproject;


public class Element {

    public Distance distance;
    public Duration duration;
    public String status;
    public Fare fare;

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Element{" +
                "distance=" + distance +
                ", duration=" + duration +
                ", status='" + status + '\'' +
                '}';
    }

    public Integer elementDistanceValue(Element el) {
        int ls = el.distance.value;
        return ls;
    }

    public Integer elementDurationValue(Element el) {
        int ls = el.duration.value;
        return ls;
    }

    public Fare getFare() {
        return fare;
    }

    public void setFare(Fare fare) {
        this.fare = fare;
    }
}
