package com.gloam.web.security.domain;

import lombok.Data;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2023年11月21日 11:51
 */
@Data
public class LoginModel {

    private String username;
    private String password;
}
