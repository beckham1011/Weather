package com.springboot.weather.service;

import com.springboot.weather.entity.CityWeather;
import org.dom4j.DocumentException;

import java.io.IOException;

/**
 * Service for getting the special city weather.
 *
 * @author Beck.Xu
 * @since 25/03/2021
 */
public interface WeatherService {

    /**
     * Get the special city weather.
     *
     * @param cityName The name of city.
     * @return The {@link CityWeather}.
     * @throws IOException Exception occur when the city not exist.
     */
    CityWeather getCityWeather(String cityName) throws IOException, DocumentException;
}
