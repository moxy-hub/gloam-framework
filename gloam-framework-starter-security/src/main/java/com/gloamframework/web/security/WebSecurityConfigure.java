package com.gloamframework.web.security;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.ComponentScan;

/**
 * web安全配置
 *
 * @author 晓龙
 */
@Configurable
@AutoConfigurationPackage(basePackages = {"com.gloamframework.web.security.chain.assembler"})
public class WebSecurityConfigure {
}
