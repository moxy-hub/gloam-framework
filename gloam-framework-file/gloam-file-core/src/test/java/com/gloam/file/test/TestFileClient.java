package com.gloam.file.test;


import com.gloamframework.file.FileManager;
import com.gloamframework.file.client.FileClient;
import com.gloamframework.file.client.FileClientProperties;
import com.gloamframework.file.client.factory.FileClientFactory;
import com.gloamframework.file.client.factory.LevelFileClientFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @author 晓龙
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class TestFileClient {

    @Autowired
    private FileClientFactory fileClientFactory;

    @Autowired
    private FileManager fileManager;

    @Test
    public void testUpload() {
        LevelFileClientFactory clientFactory = (LevelFileClientFactory) fileClientFactory;
        FileClient<? extends FileClientProperties> client = clientFactory.getMasterFileClient();
        client.upload(null, null, null);
    }

    @Test
    @SneakyThrows
    public void testFile() {
        String fileName = "test.jpg";
        String rp = "t1";
        ClassPathResource resource = new ClassPathResource("WechatIMG33.jpg");
        InputStream inputStream = resource.getInputStream();
        fileManager.upload(fileName, rp, inputStream);
        log.info("[上传文件]-成功");
        byte[] fileByte = fileManager.download(fileName, rp);
        log.info("[文件下载]-byte:{}", fileByte.length);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        fileManager.download(fileName, rp, outputStream);
        log.info("[文件下载]-stream:{}", outputStream.toByteArray().length);
        fileManager.delete(fileName, rp);
    }

}
