package com.redislock.annotation;

import java.lang.annotation.*;

/**
 * Usage:
 * <p>
 * Description:
 * User: fuxinpeng
 * Date: 2018-10-18
 * Time: 下午2:42
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Fallback {
    String name() default "fallback";
    boolean replaceReturn() default false;
}
