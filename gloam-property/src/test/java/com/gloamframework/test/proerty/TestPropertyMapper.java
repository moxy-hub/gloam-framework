package com.gloamframework.test.proerty;

import com.gloamframework.test.proerty.mock.Mock2Properties;
import com.gloamframework.test.proerty.mock.source.MappingProperties;
import com.gloamframework.test.proerty.mock.source.SourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
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
    private SourceProperties sourceProperties;

    @Autowired
    private MappingProperties mappingProperties;

    @Test
    public void testMapping(){
        // 先检查两个类的值是否一致，如果成功，则代表映射成功
        Assert.assertEquals("映射失败",sourceProperties.getTestMapping(),mappingProperties.getTestMapping());
        // 同样我们可以将映射类的值打印出来看看
        System.out.println(mappingProperties.getTestMapping());
    }
}
