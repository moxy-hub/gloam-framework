package com.gloamframework.core.boot.exception;

import com.gloamframework.common.error.GloamInternalException;
import com.gloamframework.core.Application;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
@Slf4j
public class TestException {

    @Test
    public void runtimeException1() {
        throw new GloamInternalException("测试{}异常{}", 1, 2);
    }

    @Test
    public void runtimeException2() {
        GloamInternalException cause = new GloamInternalException("测试{}异常{}", "cause", 1);
        throw new GloamInternalException("运行异常:{}", cause, "测试成功");
    }

}
