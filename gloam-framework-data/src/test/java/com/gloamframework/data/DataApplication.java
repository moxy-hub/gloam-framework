package com.gloamframework.data;

import com.gloamframework.data.druid.EnableDruidMonitor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2023年11月07日 11:07
 */
@SpringBootApplication
@EnableDruidMonitor(username = "qqq", password = "123")
@MapperScan({"com.gloamframework.**.mapper"})
public class DataApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataApplication.class);
    }
}
