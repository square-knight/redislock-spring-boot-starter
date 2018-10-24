package com.redislock.annotation;

import java.lang.annotation.*;
import java.lang.reflect.Method;

/**
 * Usage:
 * Marks a method if you want to make the method executing serializable in the distributed system.
 *
 * {@link #value()} define the value which is a part of the key of redis,
 * @see com.redislock.autoproxy.RedisLockAutoProxyCreator.MyHandler#getKey(Object, Method, Object[]) .
 *
 * {@link #fallbackMethod()} identify which fallback method should go in if current lock method
 * failed,
 * @see Fallback#value() ,throw {@link com.redislock.exception.LockFailedException} by default.
 *
 * Description:
 * User: fuxinpeng
 * Date: 2018-10-10
 * Time: 上午10:12
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisSynchronized {
    String value() default "";
    String fallbackMethod() default "";
}
