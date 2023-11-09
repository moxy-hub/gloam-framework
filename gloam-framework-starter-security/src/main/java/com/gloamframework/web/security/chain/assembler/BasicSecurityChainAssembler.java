package com.gloamframework.web.security.chain.assembler;

import com.gloamframework.web.security.chain.SecurityChain;
import com.gloamframework.web.security.chain.annotation.SecurityChainAssemblerRegister;

/**
 * 对spring security chain进行基础配置
 *
 * @author 晓龙
 */
@SecurityChainAssemblerRegister
public class BasicSecurityChainAssembler implements SecurityChainAssembler {
    @Override
    public void assemble(SecurityChain securityChain) throws Exception {

    }

}
