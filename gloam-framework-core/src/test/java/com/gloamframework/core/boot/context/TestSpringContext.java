package com.gloamframework.core.boot.context;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class TestSpringContext {

    @Test
    public void testGetContext() {
        ApplicationContext context = SpringContext.getContext();
        log.info("获取spring上下文对象:{}", context);
        Assert.assertNotNull("获取spring上下文对象为空", context);
    }

}
