package org.hisp.india.core.exceptions;

/**
 * Created by nhancao on 4/5/17.
 */

public class ApiException extends Exception {

    private int code;
    private String message;

    public static ApiException put(int code, String message) {
        ApiException exception = new ApiException();
        exception.code = code;
        exception.message = message;
        return exception;
    }

    public static ApiException put(int code) {
        return put(code, null);
    }

    @Override
    public String getMessage() {
        return String.format("Error Code: %s\nMessage: %s\n", code, message);
    }

    public int getCode() {
        return code;
    }
}
