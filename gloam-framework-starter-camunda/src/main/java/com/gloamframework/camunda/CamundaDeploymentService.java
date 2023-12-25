package com.gloamframework.camunda;

import cn.hutool.core.util.StrUtil;
import com.gloamframework.camunda.exception.CamundaBpmDeployException;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * camunda部署服务
 *
 * @author 晓龙
 */
public interface CamundaDeploymentService {

    /**
     * 流程部署，部署时可重复调用，已经部署的没有修改的，则会跳过
     *
     * @param processKey 执行key
     * @param input      流程图数组
     */
    default void deploy(String processKey, byte[] input) {
        if (input == null || input.length == 0) {
            throw new CamundaBpmDeployException("请输入流程定义图资源");
        }
        this.deploy(processKey, new ByteArrayInputStream(input));
    }

    /**
     * 流程部署，部署时可重复调用，已经部署的没有修改的，则会跳过
     *
     * @param processKey  执行key
     * @param resourceUrl 流程图资源地址
     */
    default void deploy(String processKey, String resourceUrl) {
        if (StrUtil.isBlank(resourceUrl)) {
            throw new CamundaBpmDeployException("请输入流程定义图资源地址");
        }
        try {
            Resource resource = new DefaultResourceLoader().getResource(resourceUrl);
            this.deploy(processKey, resource.getInputStream());
        } catch (IOException e) {
            throw new CamundaBpmDeployException("流程资源获取失败", e);
        }
    }

    /**
     * 流程部署，部署时可重复调用，已经部署的没有修改的，则会跳过
     *
     * @param processKey 执行key
     * @param input      流程图输入流
     */
    void deploy(String processKey, InputStream input);

}
