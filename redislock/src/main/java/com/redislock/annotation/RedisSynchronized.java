package com.redislock.annotation;

import java.lang.annotation.*;

/**
 * Usage:
 * <p>
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
