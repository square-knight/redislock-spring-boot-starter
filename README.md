<p align="center">
   <img src="https://github.com/square-knight/redislock-spring-boot-starter/blob/master/doc/images/logo.jpg" width="150"/>
   <h3 align="center">REDIS-LOCK</h3>
   <p align="center">
      基于redis的分布式锁
      </br>
      
      [--博客地址--](https://blog.csdn.net/litterfrog/article/details/83377589)
      </br>
      版本要求
      </br>
      spring-boot 2.0.0以上
      </br>
      jdk 1.6以上
      </br>
      redis 2.6.12以上
      </br>
   </p>
</p>



# Introduction

基于redis的分布式锁

# Features

1. 分布式锁
2. 锁降级（支持锁重试）

# Quick Start

1. 环境准备
本地安装好redis，启动redis服务（默认端口）
2. 下载代码
从github克隆代码到本地，导入项目
3. 运行测试用例
运行redislock-spring-boot-starter-test里的RedislockTest测试用例

# Documentation

如果想在自己的项目中使用也很简单，只要参照如下步骤：

1. 依赖引入

```
        <dependency>
            <groupId>com.redislock</groupId>
            <artifactId>redislock-spring-boot-starter</artifactId>
            <version>1.0.0</version>
        </dependency>
```
注意：由于并没有把jar包上传的maven中央仓库，所以克隆项目后请使用mvn install把jar包安装到本地。
Spring-boot版本要大于2.0.0

2. 配置项

```
redislock.prefix #redis锁的前缀默认为空
redislock.timeout #redis锁过期时间 默认20s
redislock.heart-beat #redis锁心跳默认15s，即在锁过期时间内如果到达心跳时间将重置锁的过期时间，
                     #例如：默认情况下假设加锁的方法执行的时间大于15s，在第15秒时锁的过期时间将被重置为20s
                     #也就是说在锁住的方法执行完之前锁不会被释放！
```

3. @RedisSynchronized

```
public @interface RedisSynchronized {
    String value() default "";
    String fallbackMethod() default "";
}
```

在方法上标注@RedisSynchronized来给方法加锁，
- value为redis中的key（key的生成策略为prefix+value，不指定value时value默认为方法的字符串描述，即value=返回值全类名+方法名+参数列表）
- fallbackMethod为服务降级目标方法，不指定时如果加锁失败直接抛出LockFailedException（nested in TargetInvocationException）

4. @Fallback

```
public @interface Fallback {
    String value() default "fallback";
    boolean replaceReturn() default false;
}
```

在方法上标注@Fallback来指定降级方法
- value为降级方法标识，与fallbackMethod值对应，默认为"fallback"
- replaceReturn默认为false，表示忽略降级方法的返回值，在降级方法执行完之后，抛出LockFailedException给加锁的方法的调用者
如果replaceReturn为true，则降级方法的返回值会替换加锁的返回值返回给加锁的方法的调用者
- RedisLockJoinPoint加锁方法会自动注入此参数，类似切面方法的JoinPoint，不过比AspectJ更好的地方是不论这个参数在参数列表的
哪个位置都会正确注入（虽然目前还没发现有什么用）。利用RedisLockJoinPoint可以实现加锁重试，具体可见项目中redislock-spring-boot-starter-test的测试方法。

注意：实现锁重试最好使用异步方式（如示例），如消息队列等。在降级方法中同步调用加锁方法虽然也可以正常执行但会导致无用的方法栈堆积无法释放。

5. @FallbackHandler

在类上标注@FallbackHandler来指定降级方法全局处理类
在这个类中声明的fallback方法可被所有加锁方法感知，相对的局部fallback方法只能被当前类中的加锁方法感知，当全局降级方法和局部降级方法
的value冲突时局部将覆盖全局
