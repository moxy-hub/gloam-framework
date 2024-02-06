package com.gloam.web.undertow.controller;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 晓龙
 */
@Data
public class Model {
    @ApiModelProperty(
            value = "服务器处理结果",
            allowableValues = "true,false"
    )
    private boolean success;
    @ApiModelProperty("服务器返回的信息")
    private String message;
    @ApiModelProperty("服务返回的结果类型")
    private String type;
    @ApiModelProperty("服务器返回编码code")
    private String code;
}
