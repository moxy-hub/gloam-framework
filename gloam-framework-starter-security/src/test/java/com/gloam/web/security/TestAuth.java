package com.gloam.web.security;

import com.gloamframework.web.security.GloamSecurityAuthority;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
//@Component
public class TestAuth implements GloamSecurityAuthority {
    @Override
    public Set<String> authorities(String switchUser) {
        HashSet<String> auths = new HashSet<>();
        auths.add("sss:ss");
        return auths;
    }
}
