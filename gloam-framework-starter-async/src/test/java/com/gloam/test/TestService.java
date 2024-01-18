package com.gloam.test;

import com.gloamframework.async.thread.GloamThread;
import com.gloamframework.core.exception.GloamRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author 晓龙
 */
@Service
@Slf4j
public class TestService {

    @Async
    public void out() {
        log.info("success");
    }

    @Async
    public void out2() {
        throw new GloamRuntimeException("fail");
    }

    public void out3() {
        GloamThread.go(() -> log.info("success2"));
    }

    public void out4() throws ExecutionException, InterruptedException {
        Future<String> future = GloamThread.go(() -> {
            log.info("callable");
            return "success3";
        });
        String s = future.get();
        log.info(s);
    }
}
