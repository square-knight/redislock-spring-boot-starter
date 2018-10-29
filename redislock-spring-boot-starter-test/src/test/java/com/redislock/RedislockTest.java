package com.redislock;

import com.redislock.test.RedislockApplication;
import com.redislock.test.TestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Usage:
 * test
 * Description:
 * User: fuxinpeng
 * Date: 2018-10-23
 * Time: 下午3:12
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedislockApplication.class)
public class RedislockTest {
    @Autowired
    private TestService testService;
    @Test
    public void testLock() throws NoSuchMethodException {
        System.out.println("*****************************");
        try{
            System.out.println(testService.myTurn("bulaha",0));
        }catch (Exception e){
            System.out.println(e.getMessage() + " 锁失败");
        }

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
