package com.gloam.web.security.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2023年11月21日 11:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginModel {

    private String username;
    private String password;
}
