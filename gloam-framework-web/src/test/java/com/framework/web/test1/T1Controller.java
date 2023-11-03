package com.framework.web.test1;

import com.gloamframework.web.response.WebList;
import com.gloamframework.web.response.WebPage;
import com.gloamframework.web.response.WebResult;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/t1")
@Api("tt")
public class T1Controller {

    @GetMapping("/throw")
    public WebResult<?> throwEx() {
        throw new NullPointerException("错了");
    }

    @GetMapping("/list")
    public WebList<String> testList() {
        List<String> res = new ArrayList<>();
        res.add("1");
        res.add("2");
        res.add("3");
        return WebList.success(res);
    }

    @GetMapping("/page")
    public WebPage<String> testPage() {
        List<String> res = new ArrayList<>();
        res.add("1");
        res.add("2");
        res.add("3");
        return WebPage.success(res, 1, 20, res.size(), "分页结果:{}", true);
    }

    @GetMapping("/data")
    public WebResult<Object> testData() {
        return WebResult.success("我是对象");
    }

}
