package com.springboot.weather.cache;

import com.springboot.weather.entity.CityWeatherVO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Mock cache use {@link ConcurrentHashMap}, but can replace with redis when in prod environment.
 *
 * @author Beck.Xu
 * @since 27/03/2021
 */
public class CityWeatherCache {

    /**
     * Cache valid in 5 minutes.
     */
    private static final long CACHE_HOLD_TIME_5M = 5 * 60 * 1000L;
    private static final String CACHE_TIME_SIGNAL = "cachetime";

    private static Map<String, Object> cityWeatherMapCache = new ConcurrentHashMap<>();

    /**
     * Get {@link CityWeatherVO}.
     *
     * @param cityName The city name.
     * @return The {@link CityWeatherVO}.
     */
    public static CityWeatherVO getCityWeather(String cityName) {
        return (CityWeatherVO) cityWeatherMapCache.get(cityName);
    }

    /**
     * Cache the {@link CityWeatherVO}.
     *
     * @param cityWeather The {@link CityWeatherVO}.
     */
    public static void cacheCityWeather(CityWeatherVO cityWeather) {
        String cityName = cityWeather.getCityName().toLowerCase();
        cityWeatherMapCache.put(cityName + CACHE_TIME_SIGNAL, System.currentTimeMillis() + CACHE_HOLD_TIME_5M);
        cityWeatherMapCache.put(cityName, cityWeather);
    }

    /**
     * Remove the {@link CityWeatherVO} when its time expired.
     *
     * @param cityName The city name.
     */
    public static void remove(String cityName) {
        cityWeatherMapCache.remove(cityName);
        cityWeatherMapCache.remove(cityName + CACHE_TIME_SIGNAL);
    }

    /**
     * Check if the {@link CityWeatherVO} cache is expired or not.
     *
     * @param cacheName The city name.
     * @return True when the cache is good.
     */
    public static boolean checkCache(String cacheName) {
        Long cacheHoldTime = (Long) cityWeatherMapCache.get(cacheName + CACHE_TIME_SIGNAL);
        if (cacheHoldTime == null || cacheHoldTime == 0L) {
            return false;
        }
        if (cacheHoldTime < System.currentTimeMillis()) {
            remove(cacheName);
            return false;
        }
        return true;
    }
}
