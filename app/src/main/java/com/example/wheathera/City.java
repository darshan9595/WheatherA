package com.example.wheathera;

public class City {
    private String cityName;
    private double temp;
    private double speed;

    public City(String cityName, double temp, double speed) {
        this.cityName = cityName;
        this.temp = temp;
        this.speed = speed;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }


    @Override
    public String toString() {
        return "City{" +
                "cityName='" + cityName + '\'' +
                ", temp=" + temp +
                ", speed=" + speed +
                '}';
    }
}