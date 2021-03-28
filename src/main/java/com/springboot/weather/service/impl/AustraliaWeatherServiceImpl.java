package com.springboot.weather.service.impl;

import com.springboot.weather.cache.CityWeatherCache;
import com.springboot.weather.constant.Constants;
import com.springboot.weather.entity.CityWeatherVO;
import com.springboot.weather.service.WeatherService;
import com.springboot.weather.utils.DocumentUtils;
import com.springboot.weather.utils.OKHttpUtils;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Service for getting the special city weather in Australia.
 *
 * @author Beck.Xu
 * @since 25/03/2021
 */
@Service
public class AustraliaWeatherServiceImpl implements WeatherService {

    private static final Logger log = LogManager.getLogger();

    private static DecimalFormat windSpeedWith2ScaleFormat
            = new DecimalFormat(Constants.WINDS_SPEED_FORMAT_WITH_TWO_SCALE);

    @Value("${australia.weather.url}")
    private String weatherUrl;

    @Autowired
    private OKHttpUtils okHttpUtils;

    @Override
    public CityWeatherVO getCityWeather(String cityName) throws IOException, DocumentException {
        log.info("Get city weather begin.");

        if (CityWeatherCache.checkCache(cityName)) {
            log.info("Get city weather from cache.");
            return CityWeatherCache.getCityWeather(cityName);
        }

        ResponseBody responseBody = okHttpUtils.get(MessageFormat.format(weatherUrl, cityName));
        Document weatherDocument = null;
        if (Objects.nonNull(responseBody)) {
            weatherDocument = DocumentUtils.parse(responseBody.byteStream());
        }

        CityWeatherVO nowCityWeather = null;
        if (Objects.nonNull(weatherDocument)) {
            Element channelElement = weatherDocument.getRootElement().element("channel");
            String lastBuildDate = channelElement.element("lastBuildDate").getText();
            String currentCityWeather = channelElement.element("item").element("description").getText();
            nowCityWeather = generateCityWeather(currentCityWeather, lastBuildDate);
            CityWeatherCache.cacheCityWeather(nowCityWeather);
        }
        log.info("Get city weather end.");
        return nowCityWeather;
    }

    /**
     * The raw weather data.
     *
     * @param cityWeatherDescription The city weather description.
     * @param lastBuildDate          Update time.
     * @return The {@link CityWeatherVO} with value.
     */
    private CityWeatherVO generateCityWeather(String cityWeatherDescription, String lastBuildDate) {
        CityWeatherVO cityWeather = null;
        if (StringUtils.isNotBlank(cityWeatherDescription)) {
            cityWeather = new CityWeatherVO();
            String[] values = cityWeatherDescription.split("\\.");
            String[] cityAndWeather = values[0].split("--");
            String city = cityAndWeather[0].trim();
            String weather = cityAndWeather[1].trim();
            String wind = values[2];
            String temperature = values[values.length - 1];

            cityWeather.setCityName(city);
            cityWeather.setUpdateTime(formatTime(lastBuildDate));
            cityWeather.setTemperature(formatTemperature(temperature));
            cityWeather.setWeather(weather);
            cityWeather.setWind(generateWindsValue(wind.trim()));
        }
        return cityWeather;
    }

    /**
     * Format date.
     * eg.
     * source format: Thu, 25 Mar 2021 08:53:28 -0400.
     * target format: Thursday 08:53 AM
     *
     * @param sourceDate The source time value.
     * @return The special format time.
     */
    private String formatTime(String sourceDate) {
        DateTimeFormatter sourceDateFormat = DateTimeFormatter
                .ofPattern(Constants.TIME_FORMAT_RAW_WITH_ZONE, Locale.ENGLISH);
        DateTimeFormatter targetDateFormat = DateTimeFormatter.ofPattern(Constants.TIME_FORMAT_NEED, Locale.ENGLISH);
        String targetFormatDate = LocalDateTime.parse(sourceDate, sourceDateFormat).format(targetDateFormat);
        return StringUtils.replaceOnce(targetFormatDate, Constants.COMMA_SIGNAL, StringUtils.SPACE);
    }

    /**
     * Convert raw temperature data.
     *
     * @param rawTemperature The source temperature.
     * @return The target temperature data.
     */
    private String formatTemperature(String rawTemperature) {
        return StringUtils.trim(rawTemperature.substring(0, rawTemperature.indexOf(" (")));
    }

    /**
     * The method special for the following format winds.
     *
     * @param wind The raw value of wind.
     * @return The wind value.
     */
    private String generateWindsValue(String wind) {

        String[] winds = wind.split(StringUtils.SPACE);
        Stream.iterate(0, i -> i + 1).limit(Arrays.asList(winds).size()).forEach(i -> {
            String windItem = winds[i].trim();
            if (StringUtils.isNumeric(windItem)) {
                winds[i] = transferMphToKmh(windItem);
            }
            if (Constants.WINDS_SPEED_UNIT_MPH.equals(windItem)) {
                winds[i] = Constants.WINDS_SPEED_UNIT_KMH;
            }
        });
        return StringUtils.join(winds, StringUtils.SPACE);
    }

    /**
     * The format of raw wind speed with mph unit, we need kmh.
     *
     * @param rawWindSpeed The raw wind speed.
     * @return The kmh format data.
     */
    private String transferMphToKmh(String rawWindSpeed) {
        return windSpeedWith2ScaleFormat
                .format(Double.parseDouble(rawWindSpeed) * Constants.WINDS_SPEED_MPH_TO_KMH_UNIT);
    }
}
