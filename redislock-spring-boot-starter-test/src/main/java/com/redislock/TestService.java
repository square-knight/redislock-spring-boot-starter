package com.redislock;

import com.redislock.annotation.Fallback;
import com.redislock.annotation.RedisSynchronized;
import org.springframework.stereotype.Service;

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
    @RedisSynchronized(value = "talk",fallbackMethod = "shutup")
    public String myTurn(String speak){
        return speak;
    }
//    @Fallback(value = "shutup",replaceReturn = true)
    private String notYourTurn(RedisLockJoinPoint redisLockJoinPoint){
        return "It's not your turn!!!";
    }
}
