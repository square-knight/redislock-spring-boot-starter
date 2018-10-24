package com.redislock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
    public void testLock(){
        try{
            System.out.println(testService.myTurn("bulaha",0));
        }catch (Exception e){
            System.out.println("锁失败");
        }

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
