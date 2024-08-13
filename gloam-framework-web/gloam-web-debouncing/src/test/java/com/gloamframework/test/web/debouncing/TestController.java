package com.gloamframework.test.web.debouncing;

import com.gloamframework.web.debouncing.annotation.Debouncing;
import com.gloamframework.web.response.WebResult;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2024年08月13日 09:05
 */

@RestController
@RequestMapping("/test")
@Debouncing(tokenLimit = false,enableIp = false)
public class TestController {

    @GetMapping
    public WebResult<String> testQuery(String name,long id){
        return WebResult.success();
    }

    @Data
    public static class TestBody{
        private String name;
        private int age;
    }
    @PostMapping
    public WebResult<String> testBody(@RequestBody TestBody testBody){
        return WebResult.success();
    }

    @PostMapping("/{id}")
    public WebResult<String> testPathBody(@PathVariable String id,@RequestBody TestBody testBody){
        return WebResult.success();
    }
}
