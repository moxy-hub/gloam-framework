package com.gloamframework.web.security.plugin;

import com.gloamframework.web.security.plugin.envelope.WebEnvelopeConfigure;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Import;

/**
 * gloam 安全插件配置
 *
 * @author 晓龙
 */
@Configurable
@Import(WebEnvelopeConfigure.class)
public class GloamSecurityPluginConfigure {
}
