package com.ztiany.mall.exception;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.17 23:47
 */
public class AppException extends RuntimeException {

    public AppException(Throwable cause) {
        super(cause);
    }

    public AppException(String message) {
        super(message);
    }
}
