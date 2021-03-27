package com.springboot.demo.exception;

import com.springboot.demo.response.Result;
import com.springboot.demo.response.ResultStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.DocumentException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.SocketException;

/**
 * Deal with global exception
 *
 * @author Beck.Xu
 * @since 27/03/2021
 */
@RestControllerAdvice
public class WeatherExceptionHandler {

    private static final Logger log = LogManager.getLogger();

    /**
     * Handler exception with special {@link ResultStatus}.
     *
     * @param ex The exception.
     * @return The {@link Result}.
     */
    @ExceptionHandler(value = Exception.class)
    public Result<Object> errorHandler(Exception ex) {
        log.error("Exception: ", ex);
        Result<Object> result = Result.failure();
        //Check exception type.
        if (ex instanceof SocketException) {
            result = Result.connectionTimeOut();
        } else if (ex instanceof DocumentException) {
            result = Result.cityNotFoundFailure();
        }
        return result;
    }
}
