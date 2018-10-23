package com.redislock.spring.boot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Usage:
 * <p>
 * Description:
 * User: fuxinpeng
 * Date: 2018-10-22
 * Time: 下午4:57
 */
@ConfigurationProperties(prefix = RedislockProperties.REDISLOCK_PREFIX)
public class RedislockProperties {
    public static final String REDISLOCK_PREFIX = "redislock";

    private String prefix;

    private long timeout;

    private long heartBeat;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

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

}
