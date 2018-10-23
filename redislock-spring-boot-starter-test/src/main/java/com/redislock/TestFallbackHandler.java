package com.redislock;

import com.redislock.annotation.Fallback;
import com.redislock.annotation.FallbackHandler;

/**
 * Usage:
 * <p>
 * Description:
 * User: fuxinpeng
 * Date: 2018-10-23
 * Time: 下午8:14
 */
@FallbackHandler
public class TestFallbackHandler {
    @Fallback(value = "shutup")
    private void hah(RedisLockJoinPoint redisLockJoinPoint){
        System.out.println("finally you can't speak!!!");
    }
}
