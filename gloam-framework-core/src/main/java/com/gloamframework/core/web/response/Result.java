package com.gloamframework.core.web.response;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.http.HttpStatus;

/**
 * 为了解决不同的返回类，导致方法不能正确使用，使用抽象类，将全部的字段进行管理
 *
 * @author 晓龙
 */
@Getter
abstract class Result<T> {

    /**
     * 传输数据
     */
    @ApiModelProperty(value = "服务器结果数据")
    private final T data;

    /**
     * 请求状态
     */
    @ApiModelProperty(value = "服务器请求状态")
    private final int status;

    /**
     * 执行
     */
    @ApiModelProperty(value = "服务器处理状态")
    private final boolean success;

    /**
     * 执行消息
     */
    @ApiModelProperty(value = "服务器信息")
    private final String message;

    protected Result(T data, HttpStatus status, boolean success, String message, Object... params) {
        this.data = data;
        if (status == null) {
            status = HttpStatus.OK;
        }
        this.status = status.value();
        this.success = success;
        if (StrUtil.isNotBlank(message)) {
            this.message = MessageFormatter.arrayFormat(message, params).getMessage();
        } else {
            this.message = "";
        }
    }

}
