package com.springboot.weather.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * Utils for {@link OkHttpClient} operation.
 *
 * @author Beck.Xu
 * @since 26/03/2021
 */
@Component
public class OKHttpUtils {

    private static final Logger log = LogManager.getLogger();

    private static OkHttpClient client = new OkHttpClient();

    /**
     * Get the special city weather.
     *
     * @param url The url.
     * @return The {@link InputStream} with xml format data.
     * @throws IOException The connection error.
     */
    public ResponseBody get(String url) throws IOException {
        log.info("Ok http client get url:" + url);
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        return response.body();
    }
}
