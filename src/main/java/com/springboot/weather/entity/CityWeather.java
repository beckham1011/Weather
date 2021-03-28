package com.springboot.weather.entity;

import java.util.Objects;

public class CityWeather {

    private String cityName;
    private String updateTime;
    private String weather;
    private String temperature;
    private String wind;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    @Override
    public String toString() {
        return "CityWeather{" +
                "cityName='" + cityName + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", weather='" + weather + '\'' +
                ", temperature='" + temperature + '\'' +
                ", wind='" + wind + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityWeather that = (CityWeather) o;
        return Objects.equals(cityName, that.cityName)
                && Objects.equals(updateTime, that.updateTime)
                && Objects.equals(weather, that.weather)
                && Objects.equals(temperature, that.temperature)
                && Objects.equals(wind, that.wind);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityName, updateTime, weather, temperature, wind);
    }
}
