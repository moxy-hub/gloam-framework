package com.gloam.mybatis.flex;

import com.gloamframework.data.druid.EnableDruidMonitor;
import com.gloamframework.web.doc.EnableHttpDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2023年11月07日 20:44
 */
@SpringBootApplication
@EnableHttpDoc
@EnableDruidMonitor
public class FlexApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlexApplication.class);
    }

}
