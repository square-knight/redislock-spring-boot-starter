package com.redislock.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Usage:
 * Marks a class Indicates that it is a global fallback handler.
 *
 * See {@link Fallback}
 *
 * This annotation serves as a specialization of {@link Component @Component},
 * allowing for implementation classes to be autodetected through classpath scanning.
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
