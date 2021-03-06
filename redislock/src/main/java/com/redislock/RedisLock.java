package com.redislock;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Usage:
 * based on redis command SET (with the last param NX),DEL,EXPIRE
 *
 * once locked means that the specific key seted with expired time
 * default {@code LOCK_TIMEOUT} 20s
 *
 * once unlock means that specific key is removed from redis
 *
 * if the lock not release over {@code LOCK_HEART_BEAT} the expired
 * time will reset to {@code LOCK_TIMEOUT} until unlock see {@link #init()}
 *
 * Description:
 * User: fuxinpeng
 * Date: 2018-10-10
 * Time: 下午5:46
 */
public class RedisLock {
    public static final long LOCK_TIMEOUT = 20000;//20s
    public static final long DEFAULT_SAFETY_RANGE = 5000;//20s
    public static final long LOCK_HEART_BEAT= LOCK_TIMEOUT-DEFAULT_SAFETY_RANGE;//20-5s

    private long timeout = LOCK_TIMEOUT;
    private long heartBeat = LOCK_HEART_BEAT;
    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getHeartBeat() {
        return heartBeat;
    }

    public void setHeartBeat(long heartBeat) {
        this.heartBeat = heartBeat;
    }

    private MyRedisTemplate myRedisTemplate;

    public void setMyRedisTemplate(MyRedisTemplate myRedisTemplate) {
        this.myRedisTemplate = myRedisTemplate;
    }

    private Map<String,String> aliveLocks = new ConcurrentHashMap<String,String>();

    private final Timer timer = new Timer();

    @PostConstruct
    public void init(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                keeplockAlive();
            }
        },0,heartBeat);
    }
    @PreDestroy
    private void destroy(){
        timer.cancel();
    }
    //    @Scheduled(cron = "*/10 * * * * ?")
    private void keeplockAlive(){
        if(aliveLocks.size() > 0){
            for (String key:
                    aliveLocks.keySet()) {
                myRedisTemplate.expire(key,timeout);
            }
        }
    }

    public void unlock(String key) {
        aliveLocks.remove(key);
        myRedisTemplate.delete(key);
    }

    public boolean lock(String key){
        return lock(key,true);
    }

    public boolean lock(String key,boolean keepAlive){
        Boolean redisLock = myRedisTemplate.setifAbsent(key, "redisLock", timeout);
        if(redisLock && keepAlive){
            aliveLocks.put(key,"");
        }
        return redisLock;
    }
    public boolean lock(String key,boolean keepAlive,long timeoutInMillis){
        Boolean redisLock = myRedisTemplate.setifAbsent(key, "redisLock", timeoutInMillis);
        if(redisLock && keepAlive){
            aliveLocks.put(key,"");
        }
        return redisLock;
    }
}
