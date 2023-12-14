package com.gloam.test.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.stereotype.Service;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2023年12月14日 15:40
 */
@Service
public class ServiceTest {

    @SentinelResource("default")
    public String limit() {
        return "ok";
    }
}
