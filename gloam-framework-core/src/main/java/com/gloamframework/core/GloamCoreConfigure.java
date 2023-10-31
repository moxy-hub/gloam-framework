package com.gloamframework.core;

import com.gloamframework.core.boot.BootConfigure;
import com.gloamframework.core.logging.GloamLoggingConfigure;
import com.gloamframework.core.web.WebConfigure;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Import;

@Configurable
@Import({BootConfigure.class, WebConfigure.class, GloamLoggingConfigure.class})
public class GloamCoreConfigure {
}
