package com.gloamframework.test.scanner;

import com.gloamframework.scanner.ResourceCentre;
import com.gloamframework.scanner.ResourceCentreFactory;
import com.gloamframework.test.scanner.annotation.TestResourceAnno;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.logging.DeferredLog;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Set;

/**
 * @author 晓龙
 */
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
@Slf4j
public class TestScanner {
    private final DeferredLog deferredLog = new DeferredLog();
    private final ResourceCentre resourceCentre = ResourceCentreFactory.ofSingleDefault();

    public TestScanner() throws IOException {
    }

    @Test
    public void testScannerResource() throws IOException {
        // 获取资源中心
        ResourceCentre resourceCentre = ResourceCentreFactory.ofSingleDefault();
        // 获取class集合
        Set<Class<?>> resourcesClasses = resourceCentre.getResourcesClasses(null);
        System.out.println(resourcesClasses);
    }

    @Test
    public void testScannerResourceByAnnotation() throws IOException {
        // 获取资源中心
        ResourceCentre resourceCentre = ResourceCentreFactory.ofSingleDefault();
        // 获取标注了指定注解的class集合
        Set<Class<?>> resourcesClasses = resourceCentre.getResourcesClassesByAnnotation(
                null,
                TestResourceAnno.class
        );
        System.out.println(resourcesClasses);
    }
}
