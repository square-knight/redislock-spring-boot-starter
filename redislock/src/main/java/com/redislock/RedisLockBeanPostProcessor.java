package com.redislock;

import com.redislock.annotation.Fallback;
import com.redislock.annotation.RedisSynchronized;
import com.redislock.exception.IllegalReturnException;
import com.redislock.exception.LockFailedException;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Usage:
 * <p>
 * Description:
 * User: fuxinpeng
 * Date: 2018-10-10
 * Time: 上午10:18
 */

public class RedisLockBeanPostProcessor implements BeanPostProcessor {
    private String prifex = "";
    public String getPrifex() {
        return prifex;
    }

    public void setPrifex(String prifex) {
        this.prifex = prifex;
    }
    private RedisLock redisLock;

    public void setRedisLock(RedisLock redisLock) {
        this.redisLock = redisLock;
    }

    private Map<String,ReflectiveMethodInvocation> reflectiveFallbackInvocations = new HashMap<>();
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {



        Class<?> aClass = bean.getClass();
        for (Method method : aClass.getDeclaredMethods()) {
            if(method.isAnnotationPresent(Fallback.class)){
                Fallback annotation = method.getAnnotation(Fallback.class);
                String key = annotation.name();
                method.setAccessible(true);
                ReflectiveMethodInvocation reflectiveMethodInvocation = new ReflectiveMethodInvocation(bean, method, null,annotation.replaceReturn());
                reflectiveFallbackInvocations.put(key,reflectiveMethodInvocation);
            }


            if(method.isAnnotationPresent(RedisSynchronized.class)){
                Enhancer enhancer = new Enhancer();
                enhancer.setSuperclass(bean.getClass());
                enhancer.setCallback(new MyHandler(bean));
                return enhancer.create();
            }
        }
        return bean;
    }


    class MyHandler implements InvocationHandler {
        private Object o;
        MyHandler(Object o){
            this.o=o;
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if(!method.isAnnotationPresent(RedisSynchronized.class)){
                return method.invoke(o, args);
            }
            String key = getKey(o,method,args);
            boolean locked = redisLock.lock(key);
            if(locked){
                try{
                    return method.invoke(o, args);
                }finally {
                    redisLock.unlock(key);
                }
            }
            RedisSynchronized annotation = method.getAnnotation(RedisSynchronized.class);
            String fallbackMethod = annotation.fallbackMethod();
            ReflectiveMethodInvocation invocation = reflectiveFallbackInvocations.get(fallbackMethod);
            if(null == invocation){
                throw new NoSuchMethodException("no method named \"" + fallbackMethod +"\"");
            }
            RedisLockJoinPoint redisLockJoinPoint = new RedisLockJoinPoint(o, method, args);

            if(invocation.isReplaceReturn()) {
                if(!method.getReturnType().isAssignableFrom(invocation.getMethod().getReturnType())){
                    throw new IllegalReturnException("return type illegal,require:"+method.getReturnType()
                            +",but returned:"+invocation.getMethod().getReturnType());
                }
                return invocation.proceed(redisLockJoinPoint);
            }else try {
                invocation.proceed();
            } catch (Throwable t) {
                t.printStackTrace();
            }
            throw new LockFailedException("lock failed");
        }

        private String getKey(Object o, Method method, Object[] args) {
            RedisSynchronized annotation = method.getAnnotation(RedisSynchronized.class);
            String key = annotation.value();
            if("".equals(key)){
                key = method.toGenericString();
            }
            return prifex+key;
        }
    }
}
