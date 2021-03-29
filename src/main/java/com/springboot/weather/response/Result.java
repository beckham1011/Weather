package com.springboot.weather.response;


public class Result<T> {
    /**
     * Error code.
     */
    private Integer code;
    /**
     * Message description.
     */
    private String message;
    /**
     * Return parameter.
     */
    private T data;

    private Result(ResultStatus resultStatus, T data) {
        this.code = resultStatus.getCode();
        this.message = resultStatus.getMessage();
        this.data = data;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * Result with null.
     */
    public static Result<Void> success() {
        return new Result<>(ResultStatus.SUCCESS, null);
    }

    /**
     * Result with status code and description.
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultStatus.SUCCESS, data);
    }

    /**
     * Result status code, data and description.
     */
    public static <T> Result<T> success(ResultStatus resultStatus, T data) {
        if (resultStatus == null) {
            return success(data);
        }
        return new Result<>(resultStatus, data);
    }

    /**
     * Result status code, data and description.
     */
    public static <T> Result<T> failure() {
        return new Result<>(ResultStatus.INTERNAL_SERVER_ERROR, null);
    }

    /**
     * Result status code, data and description.
     */
    public static <T> Result<T> notFoundFailure(ResultStatus status) {
        return new Result<>(status, null);
    }

    /**
     * Result status code, data and description.
     */
    public static <T> Result<T> connectionTimeOut() {
        return new Result<>(ResultStatus.TIMEOUT, null);
    }

    /**
     * Result status code, data and description.
     */
    public static <T> Result<T> failure(ResultStatus resultStatus) {
        return failure(resultStatus, null);
    }

    /**
     * Result status code, data and description.
     */
    public static <T> Result<T> failure(ResultStatus resultStatus, T data) {
        if (resultStatus == null) {
            return new Result<>(ResultStatus.INTERNAL_SERVER_ERROR, null);
        }
        return new Result<>(resultStatus, data);
    }
}
