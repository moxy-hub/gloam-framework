package com.gloamframework.cloud.remote;

import com.gloamframework.cloud.remote.retrofit.GloamHttpConfigure;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Import;

/**
 * 远程调用配置
 *
 * @author 晓龙
 */
@Configurable
@Import(GloamHttpConfigure.class)
public class GloamRemoteConfigure {
}
