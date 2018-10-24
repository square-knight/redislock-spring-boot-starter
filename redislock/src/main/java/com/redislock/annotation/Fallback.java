package com.redislock.annotation;

import java.lang.annotation.*;

/**
 * Usage:
 *  Marks a method(fallback method) which execute while the lock method (marked with {@link RedisSynchronized}) lock failed.
 *
 *  Fallback method can be defined in the same class of lock method or {@link FallbackHandler} marked class.
 *
 *  Identify a value {@link #value()} so that the lock method is able to find the specific fallback method,
 *  when different fallback methods have the same value,only one fallback method will effective,and the method
 *  in {@link FallbackHandler} always being covered with that in the same class of lock method.
 *
 *  By default, {@link #replaceReturn()} is false which means the return of the fallback method will be ignored
 *  and in the end {@link com.redislock.exception.LockFailedException} will throw to caller of lock method.
 *  If you set {@link #replaceReturn()} to true the return type of the fallback method must the same as
 *  corresponding lock method,if not {@link com.redislock.exception.IllegalReturnException} will be throwed,
 *  and the return of the fallback method will return to caller of lock method instead.
 *
 * Description:
 * User: fuxinpeng
 * Date: 2018-10-18
 * Time: 下午2:42
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Fallback {
    String value() default "fallback";
    boolean replaceReturn() default false;
}
