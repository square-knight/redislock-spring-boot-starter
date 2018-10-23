package com.redislock.exception;

/**
 * Usage:
 * <p>
 * Description:
 * User: fuxinpeng
 * Date: 2018-10-11
 * Time: 上午10:56
 */
public class IllegalReturnException extends RuntimeException{

    public IllegalReturnException() {}

    public IllegalReturnException(String message) {
        super(message);
    }
}
