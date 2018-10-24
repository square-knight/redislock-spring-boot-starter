package com.redislock;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

/**
 * Usage:
 * Invokes the target object using reflection.
 * Description:
 * User: fuxinpeng
 * Date: 2018-10-18
 * Time: 下午4:17
 */
public class RedisLockJoinPoint implements MethodInvocation {
    protected Object target;

    protected Method method;

    protected Object[] arguments;

    public RedisLockJoinPoint(Object target, Method method, Object[] arguments) {
        this.target = target;
        this.method = method;
        this.arguments = arguments;
    }
    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public Object proceed() throws Throwable {
        return method.invoke(target, arguments);
    }

    @Override
    public Object getThis() {
        return target;
    }

    @Override
    public AccessibleObject getStaticPart() {
        return method;
    }
}
