package com.redislock.exception;

/**
 * Usage:
 * <p>
 * Description:
 * User: fuxinpeng
 * Date: 2018-10-11
 * Time: 上午10:56
 */
public class LockFailedException extends RuntimeException{

    public LockFailedException() {}

    public LockFailedException(String message) {
        super(message);
    }
}
