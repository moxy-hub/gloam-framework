package com.gloamframework.core;

import com.gloamframework.core.boot.BootConfigure;
import com.gloamframework.core.json.JsonConfigure;
import com.gloamframework.core.logging.LoggingConfigure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Import;

@Configurable
@Import({BootConfigure.class, LoggingConfigure.class, JsonConfigure.class})
@Slf4j
public class GloamCoreConfigure {

}
