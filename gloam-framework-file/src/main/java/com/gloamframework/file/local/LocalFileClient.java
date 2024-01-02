package com.gloamframework.file.local;

import cn.hutool.core.io.IoUtil;
import com.gloamframework.core.lang.Assert;
import com.gloamframework.file.client.AbstractFileClient;
import com.gloamframework.file.exception.FileDeleteException;
import com.gloamframework.file.exception.FileDownloadException;
import com.gloamframework.file.exception.FileUploadException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 默认实现，本地存储客户端
 *
 * @author 晓龙
 */
@Slf4j
public class LocalFileClient extends AbstractFileClient<LocalFileClientProperties> {

    private final LocalFileClientProperties fileClientProperties;

    private static final int CAPACITY = 1024;

    public LocalFileClient(String symbol, LocalFileClientProperties properties) {
        super(symbol, properties);
        this.fileClientProperties = properties;
    }

    @Override
    protected void startup() {
        log.debug("本地文件存储客户端创建成功:{}", super.symbol());
    }

    @Override
    public String upload(InputStream fileStream, String relativePath, String fileName) throws FileUploadException {
        Assert.notBlank(relativePath, "文件上传请传入相对路径");
        Assert.notBlank(fileName, "文件上传请传入文件名字");
        // 文件通道
        if (fileStream instanceof FileInputStream &&
                FileInputStream.class.equals(fileStream.getClass())) {
            return this.upload(getPath(relativePath, fileName), ((FileInputStream) fileStream).getChannel());
        }
        return this.upload(getPath(relativePath, fileName), Channels.newChannel(fileStream));
    }

    /**
     * 基于Nio上传
     */
    private String upload(Path path, ReadableByteChannel readableByteChannel) {
        if (!readableByteChannel.isOpen()) {
            throw new FileUploadException("文件上传通道已关闭");
        }
        ByteBuffer buffer = ByteBuffer.allocate(CAPACITY);
        try (WritableByteChannel writableChannel = Channels.newChannel(Files.newOutputStream(path))) {
            while (readableByteChannel.read(buffer) != -1) {
                // 反转buffer
                buffer.flip();
                writableChannel.write(buffer);
                // 清除buffer
                buffer.clear();
            }
            log.debug("[local]:文件上传成功:{}", path.toUri());
            return StringUtils.replace(path.toString(), "\\", "/");
        } catch (IOException e) {
            throw new FileUploadException("文件上传失败", e);
        } finally {
            IoUtil.close(readableByteChannel);
        }
    }

    /**
     * 基于Nio的fileChannel0拷贝上传
     */
    private String upload(Path path, FileChannel channel) {
        if (!channel.isOpen()) {
            throw new FileUploadException("文件上传通道已关闭");
        }
        try (FileOutputStream outputStream = new FileOutputStream(path.toFile());
             FileChannel fileOutChannel = outputStream.getChannel()
        ) {
            channel.transferTo(0, channel.size(), fileOutChannel);
            log.debug("[local]:文件上传成功:{}", path.toUri());
            return StringUtils.replace(path.toString(), "\\", "/");
        } catch (IOException e) {
            throw new FileUploadException("文件上传失败", e);
        } finally {
            IoUtil.close(channel);
        }
    }

    @Override
    public void delete(String path) throws FileDeleteException {
        if (StringUtils.isBlank(path)) {
            throw new FileDeleteException("文件不存在");
        }
        Path downloadPath = Paths.get(path);
        if (!Files.exists(downloadPath)) {
            throw new FileDeleteException("文件不存在");
        }
        try {
            Files.deleteIfExists(downloadPath);
        } catch (IOException e) {
            log.error("[local]:文件删除失败", e);
            throw new FileDeleteException("文件不存在");
        }
    }

    @Override
    public void download(String path, OutputStream outputStream) throws FileDownloadException {
        if (StringUtils.isBlank(path) || outputStream == null) {
            throw new FileDownloadException("文件下载失败");
        }
        Path downloadPath = Paths.get(path);
        if (!Files.exists(downloadPath)) {
            throw new FileDownloadException("下载文件不存在");
        }
        // 使用nio进行下载
        ByteBuffer buffer = ByteBuffer.allocate(CAPACITY);
        try (ReadableByteChannel readChannel = Channels.newChannel(Files.newInputStream(downloadPath));
             WritableByteChannel writeChannel = Channels.newChannel(outputStream)) {
            while (readChannel.read(buffer) != -1) {
                buffer.flip();
                writeChannel.write(buffer);
                buffer.clear();
            }
        } catch (IOException e) {
            throw new FileDownloadException("文件下载失败", e);
        }
    }

    private Path getPath(String relativePath, String fileName) {
        // 获取路径
        String uploadDir = fileClientProperties.getUploadDir();
        if (StringUtils.isBlank(uploadDir)) {
            throw new FileUploadException("请配置文件上传路径");
        }
        try {
            Path path = Paths.get(uploadDir, relativePath);
            // 检查path
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            // 创建文件
            String filePathUrl = buildPath(path.toString(), fileName);
            if (StringUtils.isBlank(filePathUrl)) {
                throw new FileUploadException("文件路径加载错误");
            }
            Path filePath = Paths.get(filePathUrl);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
            return Files.createFile(filePath);
        } catch (IOException e) {
            throw new FileUploadException("文件路径加载错误", e);
        }
    }

    private static String buildPath(String... pathOptions) {
        if (ArrayUtils.isEmpty(pathOptions)) {
            return null;
        }
        if (pathOptions.length == 1) {
            return pathOptions[0];
        }
        // 拼接前两个
        String currentPath = pathOptions[0] + File.separatorChar + pathOptions[1];
        pathOptions = ArrayUtils.remove(pathOptions, 0);
        pathOptions = ArrayUtils.remove(pathOptions, 0);
        String[] currentOptions = {currentPath};
        return buildPath(ArrayUtils.addAll(currentOptions, pathOptions));
    }
}
