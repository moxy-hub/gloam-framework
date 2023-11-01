package com.gloamframework.core.boot.exception;

import com.gloamframework.core.boot.diagnostics.GloamStartException;
import org.springframework.boot.CommandLineRunner;

//@Component
public class StartExceptionRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        throw new GloamStartException("项目:{} 启动失败", "测试异常", "GLOAM_FRAMEWORK");
    }
}
