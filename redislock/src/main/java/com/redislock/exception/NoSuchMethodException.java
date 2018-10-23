package com.redislock.exception;

/**
 * Usage:
 * <p>
 * Description:
 * User: fuxinpeng
 * Date: 2018-10-11
 * Time: 上午10:56
 */
public class NoSuchMethodException extends RuntimeException{

    public NoSuchMethodException() {}

    public NoSuchMethodException(String message) {
        super(message);
    }
}
