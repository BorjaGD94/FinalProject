package com.example.borja.finalproject;

public class Service {

    public String serviceName;
    public Integer serviceLogo;
    public String serviceTime;
    public String servicePrice;

    public Service(){}


    public Service(String serviceName, Integer serviceLogo, String serviceTime, String servicePrice)
    {
        this.serviceName = serviceName;
        this.serviceLogo = serviceLogo;
        this.serviceTime = serviceTime;
        this.servicePrice = servicePrice;

    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getServiceLogo() {
        return serviceLogo;
    }

    public void setServiceLogo(Integer serviceLogo) {
        this.serviceLogo = serviceLogo;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

}