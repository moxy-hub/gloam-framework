package com.gloamframework.web.security.rsa;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.gloamframework.web.response.WebResult;
import com.gloamframework.web.security.annotation.Token;
import com.gloamframework.web.security.properties.SecurityProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth/rsa")
@Api(tags = "RSA密钥对")
@Slf4j
public class RsaController {

    @Autowired
    private RsaService rsaService;

    @Autowired
    private SecurityProperties securityProperties;

    @ApiOperation(value = "获取服务器公钥")
    @GetMapping("/publicKey")
    @Token(strategy = Token.Strategy.NONE)
    public WebResult<String> getRsaPublicKey(@ApiIgnore HttpServletRequest request) {
        String serviceCode = request.getHeader(securityProperties.getRsa().getServiceHeader());
        if (StrUtil.isBlank(serviceCode) || !ArrayUtil.contains(securityProperties.getRsa().getSupportService(), serviceCode)) {
            return WebResult.fail("不支持的服务");
        }
        // 获取公钥
        return WebResult.success(rsaService.getPublicKey(serviceCode));
    }

}
