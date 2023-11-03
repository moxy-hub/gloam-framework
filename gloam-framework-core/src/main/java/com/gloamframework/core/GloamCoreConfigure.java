package com.gloamframework.core;

import com.gloamframework.core.boot.BootConfigure;
import com.gloamframework.core.http.GloamHttpConfigure;
import com.gloamframework.core.logging.LoggingConfigure;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Import;

@Configurable
@Import({BootConfigure.class, GloamHttpConfigure.class, LoggingConfigure.class})
public class GloamCoreConfigure {

}
