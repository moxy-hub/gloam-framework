package com.gloam.cloud.test.properties;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2024年02月07日 10:41
 */
@ConfigurationProperties("spring.properties")
@Data
@ToString
public class OrginProperties {

    private String value;

    private List<String> listValue;

}
