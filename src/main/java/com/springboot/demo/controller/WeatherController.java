package com.springboot.demo.controller;

import com.springboot.demo.entity.CityWeather;
import com.springboot.demo.response.Result;
import com.springboot.demo.service.WeatherService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Weather controller for http connect.
 *
 * @author Beck.Xu
 * @since 26/03/2021
 */
@RestController
@RequestMapping("cityWeather")
public class WeatherController {

    private static final Logger log = LogManager.getLogger();

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/{cityName}")
    public Result<CityWeather> getCityWeather(@PathVariable String cityName) {
        CityWeather cityWeather;
        try {
            cityWeather = weatherService.getCityWeather(cityName);
            log.info(cityWeather);
        } catch (IOException e) {
            log.error(e);
            return Result.failure();
        }
        return Result.success(cityWeather);
    }
}
