package com.app.launcherapphomescreenpracticalapp.Widget;

public class WeatherDetails {
    String city, country, description;
    Integer temperature;

    public WeatherDetails(String city, String country, Integer temperature, String description) {
        this.city = city;
        this.country = country;
        this.temperature = temperature;
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public String getCounty() {
        return country;
    }

    public Integer getTemperature(){
        return temperature;
    }

    public String getDescription(){
        return description;
    }

}