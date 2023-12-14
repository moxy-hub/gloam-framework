package com.gloamframework.web.security.envelope;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.gloamframework.common.crypto.RSAUtil;
import com.gloamframework.common.crypto.exception.DecryptException;
import com.gloamframework.web.security.annotation.WebEnvelope;
import com.gloamframework.web.security.envelope.exception.EnvelopeAnalysisException;
import com.gloamframework.web.security.envelope.wrapper.WebEnvelopeRequestWrapper;
import com.gloamframework.web.security.envelope.wrapper.WebEnvelopeResponseWrapper;
import com.gloamframework.web.security.filter.GloamOncePerRequestFilter;
import com.gloamframework.web.security.match.WebEnvelopeMatcher;
import com.gloamframework.web.security.properties.SecurityProperties;
import com.gloamframework.web.security.rsa.RsaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.NoSuchPaddingException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        response.addHeader(responseTypeHeader, UN_ENCRYPT);
        // 匹配是否为信封保护的接口
        WebEnvelope webEnvelope = webEnvelopeMatcher.match(request);
        if (webEnvelope == null) {
            filterChain.doFilter(request, response);
            return;
        }
        // 获取服务code
        String serviceCode = request.getHeader(securityProperties.getRsa().getServiceHeader());
        if (StringUtils.isBlank(serviceCode)) {
            throw new EnvelopeAnalysisException("获取serviceCode失败");
        }
        // 拉取请求体
        String requestBody = ServletUtil.getBody(request);
        if (StrUtil.isBlank(requestBody)) {
            log.warn("接口:{}#{} 的请求体为空，不进行信封保护，请检查接口", request.getRequestURL(), request.getMethod());
            filterChain.doFilter(request, response);
            return;
        }
        // 由信封保护
        log.info("接口:{}#{} 由信封加密保护，开始解密", request.getRequestURL(), request.getMethod());
        // 解密
        this.analysis(serviceCode, requestBody, ((data, aes) -> {
            // 解密数据
            data = aes.decryptStr(data);
            log.debug("解密请求信封 -> 请求:{}#{} 解密后参数:{}", request.getRequestURI(), request.getMethod(), data);
            WebEnvelopeRequestWrapper requestWrapper = new WebEnvelopeRequestWrapper(request, data);
            WebEnvelopeResponseWrapper responseWrapper = new WebEnvelopeResponseWrapper(response);
            filterChain.doFilter(requestWrapper, responseWrapper);
            this.encryptResult(responseWrapper, response, aes);
        }));
    }


    private void analysis(String serviceCode, String requestBody, AnalysisEnvelope analysisEnvelope) throws ServletException, IOException {
        // 解析为信封
        WebEnvelopeData envelopeData;
        try {
            envelopeData = JSON.parseObject(requestBody, WebEnvelopeData.class);
        } catch (JSONException jsonException) {
            throw new EnvelopeAnalysisException("请求信封格式不正确", jsonException);
        }
        String encryptAesKey, data;
        if (envelopeData == null || StringUtils.isAnyBlank(encryptAesKey = envelopeData.getKey(), data = envelopeData.getData())) {
            throw new EnvelopeAnalysisException("解析信封失败,请检查信封是否为空或其结果不正确或为null");
        }
        analysisEnvelope.analysis(data, getValidAes(serviceCode, encryptAesKey));
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

    private void encryptResult(WebEnvelopeResponseWrapper response, HttpServletResponse outResponse, AES aes) throws IOException {
        // 加载返回
        byte[] body = response.getResponseData();
        if (ArrayUtil.isEmpty(body)) {
            log.warn("响应为空，不进行响应加密");
            return;
        }
        if (aes == null) {
            // aes为null，不进行响应加密
            log.warn("响应AES为空，不进行响应加密");
            outResponse.getOutputStream().write(body);
            return;
        }
        JSONObject bodyResult;
        try {
            bodyResult = JSON.parseObject(new String(body));
            if (bodyResult == null) {
                log.warn("响应解析json失败，不进行响应");
                return;
            }
        } catch (JSONException exception) {
            log.warn("响应解析json失败，不进行响应");
            return;
        }
        if (!bodyResult.containsKey("data")) {
            log.warn("响应data字段不存在，不进行响应加密");
            outResponse.getOutputStream().write(body);
            return;
        }
        String data = bodyResult.getString("data");
        if (StrUtil.isBlank(data)) {
            log.warn("响应data为空，不进行响应加密");
            outResponse.getOutputStream().write(body);
            return;
        }
        // 加密
        data = aes.encryptHex(data);
        bodyResult.put("data", data);
        // 设置加密响应
        outResponse.setHeader(responseTypeHeader, ENCRYPT);
        outResponse.getOutputStream().write(bodyResult.toJSONString().getBytes(UTF_8));
    }
}
