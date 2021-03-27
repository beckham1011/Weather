package com.springboot.demo.response;

import org.springframework.http.HttpStatus;

/**
 *  Http response result enum.
 *
 * @author Beck.Xu
 * @since 26/03/2021
 */
public enum ResultStatus {

    SUCCESS(HttpStatus.OK, 200, "OK"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, 400, "Bad Request"),
    NOT_FOUND(HttpStatus.NOT_FOUND, 404, "Not Found The City"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "Internal Server Error");

    /**
     * Response with http code.
     */
    private HttpStatus httpStatus;

    /**
     * Business code.
     */
    private Integer code;

    /**
     * Business error message.
     */
    private String message;

    ResultStatus(HttpStatus httpStatus, Integer code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResultStatus{" +
                "httpStatus=" + httpStatus +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
