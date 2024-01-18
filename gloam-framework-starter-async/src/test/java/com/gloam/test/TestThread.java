package com.gloam.test;

import com.gloamframework.async.sync.WaitGroup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * @author 晓龙
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class TestThread {

    @Autowired
    private TestService testService;

    @Test
    @SneakyThrows
    public void test() {
        testService.out();
        WaitGroup waitGroup = new WaitGroup(1);
        testService.out2();
        testService.out3();
        testService.out4();
        waitGroup.await(2, TimeUnit.SECONDS);


    }

}
