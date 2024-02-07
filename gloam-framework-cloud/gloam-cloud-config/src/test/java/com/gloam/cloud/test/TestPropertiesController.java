package com.gloam.cloud.test;

import com.gloam.cloud.test.properties.OrginProperties;
import com.gloamframework.web.response.WebResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 晓龙
 */
@RestController
@RequestMapping("/t1")
public class TestPropertiesController {

    @Autowired
    private OrginProperties dynamicDataSourceProperties;

    @GetMapping("/mysql")
    public WebResult<OrginProperties> test() {
        return WebResult.success(dynamicDataSourceProperties);
    }
}
