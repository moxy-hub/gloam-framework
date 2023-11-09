package com.gloamframework.web.security.chain.assembler;

import com.gloamframework.web.security.chain.SecurityChain;

/**
 * security链装配器
 * @author 晓龙
 */
@FunctionalInterface
public interface SecurityChainAssembler {

    /**
     * 将安全链传递进行装配
     */
    void assemble(SecurityChain securityChain) throws Exception;
}
