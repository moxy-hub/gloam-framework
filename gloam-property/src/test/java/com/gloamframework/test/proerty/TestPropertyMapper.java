package com.gloamframework.test.proerty;

import com.gloamframework.test.proerty.mock.Mock2Properties;
import com.gloamframework.test.proerty.mock.source.MappingProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2024年05月12日 16:22
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class TestPropertyMapper {

    @Autowired
    private Mock2Properties mockProperties;

    @Autowired
    private MappingProperties mappingProperties;

    @Test
    public void testMapping(){
        System.out.println(mappingProperties);
    }
}
