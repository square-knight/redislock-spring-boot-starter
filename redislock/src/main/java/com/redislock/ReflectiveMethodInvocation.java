package com.redislock;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Usage:
 * <p>
 * Description:
 * User: fuxinpeng
 * Date: 2018-10-18
 * Time: 下午3:02
 */
public class ReflectiveMethodInvocation implements MethodInvocation {
    protected Object target;

    protected Method method;

    protected Object[] arguments;

    private boolean replaceReturn;

    public ReflectiveMethodInvocation(Object target, Method method, Object[] arguments, boolean replaceReturn) {
        this.target = target;
        this.method = method;
        this.arguments = arguments;
        this.replaceReturn = replaceReturn;
    }
    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    @Override
    public Object proceed() throws Throwable {
        return method.invoke(target, arguments);
    }
    public Object proceed(Object... arguments) throws Throwable {
        Object[] params = autoPackArgs(arguments);
        return method.invoke(target, params);
    }
    private Object[] autoPackArgs(Object... arguments){
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] params = new Object[parameterTypes.length];
        if(parameterTypes.length > 0){
            for (Object argument : arguments) {
                List<Class<?>> classes = Arrays.asList(parameterTypes);
                int index = classes.indexOf(RedisLockJoinPoint.class);
                if(index != -1) {
                    params[index] = argument;
                }
            }
        }
        return params;
    }
    @Override
    public Object getThis() {
        return target;
    }

    @Override
    public AccessibleObject getStaticPart() {
        return method;
    }

    public boolean isReplaceReturn() {
        return replaceReturn;
    }
}
