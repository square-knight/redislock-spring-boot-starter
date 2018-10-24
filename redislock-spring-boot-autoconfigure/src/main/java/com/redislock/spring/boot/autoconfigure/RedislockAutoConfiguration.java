package com.redislock.spring.boot.autoconfigure;

import com.redislock.MyRedisTemplate;
import com.redislock.RedisLock;
import com.redislock.autoproxy.RedisLockAutoProxyCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Usage:
 * RedislockAutoConfiguration
 * Description:
 * User: fuxinpeng
 * Date: 2018-10-22
 * Time: 下午4:56
 */
@Configuration
@ConditionalOnClass({MyRedisTemplate.class,RedisLock.class,RedisLockAutoProxyCreator.class})
@ConditionalOnBean(RedisTemplate.class)
@EnableConfigurationProperties(RedislockProperties.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedislockAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(RedislockAutoConfiguration.class);

    private final RedislockProperties properties;

    public RedislockAutoConfiguration(RedislockProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    MyRedisTemplate myRedisTemplate(RedisTemplate<String,String> redisTemplate){
        MyRedisTemplate myRedisTemplate = new MyRedisTemplate();
        myRedisTemplate.setRedisTemplate(redisTemplate);
        return myRedisTemplate;
    }
    @Bean
    @ConditionalOnMissingBean
    RedisLock redisLock(MyRedisTemplate myRedisTemplate){
        RedisLock redisLock = new RedisLock();
        redisLock.setMyRedisTemplate(myRedisTemplate);
        if(0 != properties.getHeartBeat()){
            redisLock.setHeartBeat(properties.getHeartBeat());
        }
        long timeout = 0 == properties.getTimeout() ? RedisLock.LOCK_TIMEOUT : properties.getTimeout();
        if(properties.getTimeout() < properties.getHeartBeat()){
            logger.warn(
                    "timeout:{} can not smaller than heart-beat:{},set timeout=heart-beat+RedisLock.DEFAULT_SAFETY_RANGE "
                    ,properties.getTimeout()
                    ,properties.getHeartBeat()
            );
            timeout = properties.getHeartBeat() + RedisLock.DEFAULT_SAFETY_RANGE;
        }
        redisLock.setTimeout(timeout);
        return redisLock;
    }
    @Bean
    @ConditionalOnMissingBean
    RedisLockAutoProxyCreator redisLockAutoProxyCreator(RedisLock redisLock){
        RedisLockAutoProxyCreator RedisLockAutoProxyCreator = new RedisLockAutoProxyCreator();
        RedisLockAutoProxyCreator.setRedisLock(redisLock);
        if(null != properties.getPrefix()){
            RedisLockAutoProxyCreator.setPrifex(properties.getPrefix());
        }
        return RedisLockAutoProxyCreator;
    }
}
