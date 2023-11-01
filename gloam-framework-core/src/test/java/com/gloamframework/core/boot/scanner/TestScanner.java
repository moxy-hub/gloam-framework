package com.gloamframework.core.boot.scanner;

import com.gloamframework.core.Application;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

/**
 * @author 晓龙
 */
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
@Slf4j
public class TestScanner {

    private final ResourceScanner resourceScanner = ResourceScanner.getDefault();

    @Test
    public void testScannerResource() throws IOException {
        Resource[] resources = resourceScanner.scannerForResource("com.gloamframework", null, null);
        for (Resource resource : resources) {
            log.info("资源：{}", resource.getURI());
        }
    }

    @Test
    public void testScannerClasses() throws IOException {
        List<Class<?>> classes = resourceScanner.scannerForClasses("com.gloamframework", null, "PathMatchingResourceScanner");
        for (Class<?> aClass : classes) {
            log.info("class:{}", aClass.getName());
        }
    }
}
