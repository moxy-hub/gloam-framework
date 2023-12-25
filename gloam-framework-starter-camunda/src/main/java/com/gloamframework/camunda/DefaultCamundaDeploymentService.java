package com.gloamframework.camunda;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.gloamframework.camunda.exception.CamundaBpmDeployException;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.Deployment;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

/**
 * 基于Camunda的流程部署实现
 *
 * @author 晓龙
 */
@Slf4j
class DefaultCamundaDeploymentService implements CamundaDeploymentService {

    private final static String SUFFIX = ".bpmn";
    private final RepositoryService repository;

    public DefaultCamundaDeploymentService(RepositoryService repository) {
        this.repository = repository;
    }

    public void deploy(String processKey, InputStream input) {
        if (StrUtil.isBlank(processKey) || input == null) {
            throw new CamundaBpmDeployException("请输入流程key或定义流程图");
        }
        Deployment deployment = repository.createDeploymentQuery().deploymentName(processKey).orderByDeploymentTime().desc().singleResult();
        byte[] oldDeployBytes = null;
        if (deployment != null) {
            // 获取原始流程定义图
            InputStream oldInput = repository.getResourceAsStream(deployment.getId(), processKey + SUFFIX);
            oldDeployBytes = IoUtil.readBytes(oldInput, true);
        }
        byte[] newDeployBytes = IoUtil.readBytes(input, true);
        // 发布过，并且没有变化，则不进行发布
        if (ArrayUtil.isNotEmpty(oldDeployBytes) && Arrays.equals(oldDeployBytes, newDeployBytes)) {
            log.warn("流程：{} 已经发布过，并且没有检查到修改，本次发布跳过", processKey);
            return;
        }
        // 发布
        repository.createDeployment().name(processKey).addInputStream(processKey + SUFFIX, new ByteArrayInputStream(newDeployBytes)).deploy();
    }
}
