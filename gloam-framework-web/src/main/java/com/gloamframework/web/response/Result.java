package com.gloamframework.web.response;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * 为了解决不同的返回类，导致方法不能正确使用，使用抽象类，将全部的字段进行管理
 *
 * @author 晓龙
 */
@Getter
@NoArgsConstructor
@Setter
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 8188019319002238761L;

    /**
     * 传输数据
     */
    @ApiModelProperty(value = "服务器结果数据")
    private T data;

    /**
     * 请求状态
     */
    @ApiModelProperty(value = "服务器请求状态")
    private int status;

    /**
     * 执行
     */
    @ApiModelProperty(value = "服务器处理状态")
    private boolean success;

    /**
     * 执行消息
     */
    @ApiModelProperty(value = "服务器信息")
    private String message;

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
