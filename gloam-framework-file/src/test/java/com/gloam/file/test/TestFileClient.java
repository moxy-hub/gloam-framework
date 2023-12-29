package com.gloam.file.test;


import com.gloamframework.file.client.FileClient;
import com.gloamframework.file.client.factory.FileClientFactory;
import com.gloamframework.file.client.factory.LevelFileClientFactory;
import com.gloamframework.file.client.properties.FileClientProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 晓龙
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class TestFileClient {

    @Autowired
    private FileClientFactory fileClientFactory;

    @Test
    public void testUpload() {
        LevelFileClientFactory clientFactory = (LevelFileClientFactory) fileClientFactory;
        FileClient<? extends FileClientProperties> client = clientFactory.getMasterFileClient();
        client.upload(null, null, null);
    }

}
