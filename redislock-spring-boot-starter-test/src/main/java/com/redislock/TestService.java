package com.redislock;

import com.redislock.annotation.Fallback;
import com.redislock.annotation.RedisSynchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;

/**
 * Usage:
 * <p>
 * Description:
 * User: fuxinpeng
 * Date: 2018-10-23
 * Time: 下午3:16
 */
@Service
public class TestService {
    @Autowired
    private RetryTask retryTask;
    private int count = 0;
    @RedisSynchronized(value = "talk",fallbackMethod = "shutup")
    public String myTurn(String speak,int count){
        return speak;
    }
    @Fallback(value = "shutup")
    private void notYourTurn(RedisLockJoinPoint redisLockJoinPoint){
        Object[] arguments = redisLockJoinPoint.getArguments();
        int count = (int) arguments[1];
        arguments[1]=++count;
        if(count <5){//失败后重试5次
            retryTask.retry(redisLockJoinPoint);
        }
    }

}
