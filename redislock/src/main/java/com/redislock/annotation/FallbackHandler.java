package com.redislock.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Usage:
 * <p>
 * Description:
 * User: fuxinpeng
 * Date: 2018-10-23
 * Time: 下午7:08
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Documented
public @interface FallbackHandler {
}
