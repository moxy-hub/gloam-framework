package com.gloamframework.web.security.envelope;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.IOUtils;
import com.gloamframework.common.crypto.AESUtil;
import com.gloamframework.common.crypto.RSAUtil;
import com.gloamframework.common.crypto.exception.DecryptException;
import com.gloamframework.web.response.Result;
import com.gloamframework.web.security.annotation.WebEnvelope;
import com.gloamframework.web.security.envelope.exception.EnvelopeAnalysisException;
import com.gloamframework.web.security.envelope.wrapper.WebEnvelopeRequestWrapper;
import com.gloamframework.web.security.envelope.wrapper.WebEnvelopeResponseWrapper;
import com.gloamframework.web.security.filter.GloamOncePerRequestFilter;
import com.gloamframework.web.security.match.WebEnvelopeMatcher;
import com.gloamframework.web.security.properties.SecurityProperties;
import com.gloamframework.web.security.rsa.RsaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;

import javax.crypto.NoSuchPaddingException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 请求解密filter，执行顺序，在认证之后
 *
 * @author 晓龙
 */
@Slf4j
public class WebEnvelopeFilter extends GloamOncePerRequestFilter {

    @FunctionalInterface
    private interface AnalysisEnvelope {
        void analysis(String data, AES aes) throws ServletException, IOException;
    }

    private static final String responseTypeHeader = "response_type";
    private static final String UN_ENCRYPT = "un_encrypt";
    private static final String ENCRYPT = "encrypt";

    @Autowired
    private WebEnvelopeMatcher webEnvelopeMatcher;
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private RsaService rsaService;

    @Override
    public int getOrder() {
        return 4;
    }

    @Override
    protected void doGloamFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader(responseTypeHeader,UN_ENCRYPT);
        // 匹配是否为信封保护的接口
        WebEnvelope webEnvelope = webEnvelopeMatcher.match(request);
        if (webEnvelope == null) {
//            WebEnvelopeResponseWrapper responseWrapper = new WebEnvelopeResponseWrapper(response);
            filterChain.doFilter(request, response);
//            this.encryptResult(responseWrapper,response,null);
            return;
        }
        // 获取服务code
        String serviceCode = request.getHeader(securityProperties.getRsa().getServiceHeader());
        if (StringUtils.isBlank(serviceCode)) {
            throw new EnvelopeAnalysisException("获取serviceCode失败");
        }
        // 由信封保护
        log.info("接口:{}#{} 由信封加密保护，开始解密", request.getRequestURL(), request.getMethod());
        // 拉取请求体
        String requestBody = ServletUtil.getBody(request);
        if (StrUtil.isBlank(requestBody)) {
            log.warn("接口:{}#{} 的请求体为空，不进行信封保护，请检查接口", request.getRequestURL(), request.getMethod());
            filterChain.doFilter(request, response);
            return;
        }
        // 解密
        this.analysis(serviceCode, requestBody, ((data, aes) -> {
            // 解密数据
            data = aes.decryptStr(data);
            log.debug("解密请求信封 -> 请求:{}#{} 解密后参数:{}", request.getRequestURI(), request.getMethod(), data);
            WebEnvelopeRequestWrapper requestWrapper = new WebEnvelopeRequestWrapper(request, data);
            WebEnvelopeResponseWrapper responseWrapper = new WebEnvelopeResponseWrapper(response);
            filterChain.doFilter(requestWrapper, response);
            this.encryptResult(responseWrapper,responseWrapper,aes);
        }));
    }


    private void analysis(String serviceCode, String requestBody, AnalysisEnvelope analysisEnvelope) throws ServletException, IOException {
        // 解析为信封
        WebEnvelopeData envelopeData = JSON.parseObject(requestBody, WebEnvelopeData.class);
        String encryptAesKey, data;
        if (envelopeData == null || StringUtils.isAnyBlank(encryptAesKey = envelopeData.getKey(), data = envelopeData.getData())) {
            throw new EnvelopeAnalysisException("解析信封失败,请检查信封是否为空或其结果不正确或为null");
        }
        analysisEnvelope.analysis(data, getValidAes(encryptAesKey, serviceCode));
    }

    /**
     * 获取AES
     */
    private AES getValidAes(String serviceCode, String encryptAesKey) {
        // 获取私钥
        String privateKey = rsaService.getPrivateKey(serviceCode);
        if (StringUtils.isBlank(privateKey)) {
            throw new EnvelopeAnalysisException("获取私钥失败");
        }
        // 解密aesKey
        String aesKey;
        try {
            aesKey = RSAUtil.decryptByPrivateKey(encryptAesKey, privateKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException |
                 IOException | DecryptException e) {
            throw new EnvelopeAnalysisException("解密aesKey失败", e);
        }
        log.debug("解密AES_KEY：{}", aesKey);
        // 初始化 aes
        return new AES(aesKey.getBytes(UTF_8));
    }

    private void encryptResult(WebEnvelopeResponseWrapper response,HttpServletResponse outResponse, AES aes) throws IOException {
//        // 加载返回
//        byte[] body = response.getResponseData();
//        String bodyString = new String(body);
//        JSONObject jsonResult = JSON.parseObject(bodyString);
//        if (jsonResult!=null&&jsonResult.containsKey("success")&&!jsonResult.getBooleanValue("success")) {
//            log.warn("响应未加密:请求处理返回结果为不成功");
//        }else if (aes !=null){
//            body = aes.encrypt(bodyString,UTF_8);
//            // 设置加密响应
//            outResponse.setHeader(responseTypeHeader,ENCRYPT);
//        }
//        outResponse.getOutputStream().write(body);
    }
}
