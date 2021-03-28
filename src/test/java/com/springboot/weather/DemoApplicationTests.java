package com.springboot.weather;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test application.
 *
 * @author Beck.Xu
 * @since 28/03/2021
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WeatherApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoApplicationTests {

    private static final Logger log = LogManager.getLogger();

    private URL base;

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Init.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        String url = String.format("http://localhost:%d/", port);
        this.base = new URL(url);
    }

    /**
     * Test get city weather success.
     */
    @Test
    public void testCityTemperature() {
        ResponseEntity<String> response = this.restTemplate.getForEntity(
                this.base.toString() + "/cityWeather/sydney", String.class, "");
        log.info(String.format("Test result: %s", response.getBody()));
        assertTrue(response.getBody().contains("200"));
        assertTrue(response.getBody().contains("OK"));
        assertTrue(response.getBody().contains("cityName"));
        assertTrue(response.getBody().contains("Sydney"));
        assertTrue(response.getBody().contains("updateTime"));
        assertTrue(response.getBody().contains("weather"));
        assertTrue(response.getBody().contains("temperature"));
        assertTrue(response.getBody().contains("wind"));
    }

    /**
     * Test not found city.
     */
    @Test
    public void testCityTemperatureNotFoundCity() {
        ResponseEntity<String> response = this.restTemplate.getForEntity(
                this.base.toString() + "/cityWeather/sydney11", String.class, "");
        log.info(String.format("Test result: %s", response.getBody()));
        assertTrue(response.getBody().contains("404"));
        assertTrue(response.getBody().contains("Not Found The City"));
    }
}
