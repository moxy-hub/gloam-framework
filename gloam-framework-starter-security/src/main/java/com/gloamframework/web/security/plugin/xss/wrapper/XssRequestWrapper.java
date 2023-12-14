package com.gloamframework.web.security.plugin.xss.wrapper;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.gloamframework.web.security.plugin.xss.exception.XssAttackException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class XssRequestWrapper extends HttpServletRequestWrapper {
    public XssRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    /**
     * 过滤springmvc中的 @RequestParam 注解中的参数
     */
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (ArrayUtil.isEmpty(values)) {
            return new String[]{};
        }
        // 对每一个参数进行过滤
        int i = values.length;
        String[] cleanValues = new String[i];
        for (int j = 0; j < i; j++) {
            cleanValues[j] = this.clean(name, values[j]);
        }
        return cleanValues;
    }

    /**
     * 过滤request.getParameter的参数
     */
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (StrUtil.isBlank(value)) {
            return null;
        } else {
            return this.clean(name, value);
        }
    }

    private String clean(String name, String value) {
        String cleanValue = cleanXSS(cleanSQLInject(value));
        log.debug("XSS净化:-> 字段:{} 原始参数:{} 净化参数:{}", name, value, cleanValue);
        return cleanValue;
    }

    /**
     * 过滤请求体 json 格式的
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream input = new ByteArrayInputStream(inputHandlers(super.getInputStream()).getBytes());
        return new ServletInputStream() {
            @Override
            public int read() {
                return input.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }


    public String inputHandlers(ServletInputStream servletInputStream) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(servletInputStream, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            throw new XssAttackException("处理请求输入信息失败", e);
        } finally {
            if (servletInputStream != null) {
                try {
                    servletInputStream.close();
                } catch (IOException e) {
                    log.error("关闭请求输入流失败", e);
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error("关闭请求输入流失败", e);
                }
            }
        }
        String body = sb.toString();
        String cleanBody = cleanXSS(body);
        log.debug("XSS净化:-> 原始body:{} 净化body:{}", body, cleanBody);
        return cleanBody;
    }

    public String cleanXSS(String src) {
        String temp = src;
        /*-----------------------start--------------------------*/
        src = src.replaceAll("eval\\((.*)\\)", "");
        src = src.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        src = src.replaceAll("script", "");
        src = src.replaceAll("link", "");
        src = src.replaceAll("frame", "");
        /*-----------------------end--------------------------*/
        Pattern pattern = Pattern.compile("(eval\\((.*)\\)|script)",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(src);
        src = matcher.replaceAll("");
        pattern = Pattern.compile("[\\\"\\'][\\s]*javascript:(.*)[\\\"\\']",
                Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(src);
        src = matcher.replaceAll("\"\"");
        // 增加脚本
        src = src.replaceAll("script", "").replaceAll(";", "")
                /*.replaceAll("\"", "").replaceAll("@", "")*/
                .replaceAll("0x0d", "").replaceAll("0x0a", "");
        if (!temp.equals(src)) {
            log.error("XSS净化: 参数含有非法攻击字符，已禁止继续访问!");
            log.error("原始输入信息-> {}", temp);
            log.error("处理后信息-> {}", src);
            throw new XssAttackException("XSS攻击检查: 参数含有非法攻击字符,已禁止继续访问!");
        }
        return src;
    }

    /**
     * 需要增加通配，过滤大小写组合
     */
    public String cleanSQLInject(String src) {
        String lowSrc = src.toLowerCase();
        String lowSrcAfter = lowSrc.replaceAll("insert ", "forbidI")
                .replaceAll("select ", "forbidS")
                .replaceAll("update ", "forbidU")
                .replaceAll("delete ", "forbidD")
                .replaceAll(" and ", "forbidA")
                .replaceAll(" or ", "forbidO");

        if (!lowSrcAfter.equals(lowSrc)) {
            log.error("XSS净化 [SQL注入检查]: 输入信息存在SQL攻击!");
            log.error("原始输入信息-> {}", src);
            log.error("处理后信息-> {}", lowSrc);
            throw new XssAttackException("SQL注入检查: 参数含有非法攻击字符,已禁止继续访问!");

        }
        return src;
    }

}
