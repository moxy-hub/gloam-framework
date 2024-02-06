package com.gloam.web.security;

import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gloamframework.common.crypto.GloamKeyGenerator;
import com.gloamframework.common.crypto.RSAUtil;
import com.gloamframework.web.envelope.WebEnvelope;
import com.gloamframework.web.envelope.rsa.RsaService;
import com.gloamframework.web.response.WebResult;
import com.gloamframework.web.security.GloamSecurityContext;
import com.gloamframework.web.security.token.constant.Device;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private RsaService rsaService;

    @Data
    public static class User {
        private String username;
        private String password;
    }

    @WebEnvelope
    @PostMapping("/login")
    public WebResult<User> login(@RequestBody User user) {
        if ("admin".equals(user.username) && "admin".equals(user.password)) {
            GloamSecurityContext.passAuthenticationWithResponseHeader(user.username, user.password, Device.PC);
            return WebResult.success(user, "登录成功");
        }
        return WebResult.fail("用户名密码错误");
    }

    @GetMapping("/encrypt")
    public WebResult<Pojo> testEncrypt() throws Exception {
        JSONObject object = new JSONObject();
        object.put("username", "admin");
        object.put("password", "admin");
        String oriData = object.toJSONString();
        // 随机key
        String aeskey = GloamKeyGenerator.generateAES128KeyString();
        AES aes = new AES(aeskey.getBytes());
        // 加密
        String data = aes.encryptHex(oriData);
        Pojo pojo = new Pojo();
        pojo.setData(data);
        pojo.setOriData(oriData);
        pojo.setOriKey(aeskey);
        String publicKey = rsaService.getPublicKey("0");
        String key = RSAUtil.encryptByPublicKey(aeskey, publicKey);
        pojo.setKey(key);
        return WebResult.success(pojo);
    }

    @GetMapping("/dencrypt")
    public WebResult<User> testDencrypt(String data, String key) {
        AES aes = new AES(key.getBytes());
        String string = aes.decryptStr(data);
        return WebResult.success(JSON.parseObject(string, User.class));
    }

    @Data
    public static class Pojo {
        private String oriData;
        private String oriKey;
        private String data;
        private String key;
    }
}
