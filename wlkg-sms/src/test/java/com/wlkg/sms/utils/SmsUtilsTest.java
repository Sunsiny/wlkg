package com.wlkg.sms.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsUtilsTest {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public void sendTest()
    {
        Map<String,String> map = new HashMap<>();
        map.put("phone", "16696379284");
        map.put("code","521521");
        amqpTemplate.convertAndSend("wlkg.sms.exchange","sms.verify.code",map);
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}