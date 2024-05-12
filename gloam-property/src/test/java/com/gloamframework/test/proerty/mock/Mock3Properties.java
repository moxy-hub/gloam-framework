package com.gloamframework.test.proerty.mock;

import com.gloamframework.property.annotation.GloamConfigurationProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;

import java.util.List;
import java.util.Map;


@Data
public class Mock3Properties {


    private DataSize dataSize;
    private Mock4Properties mock4;


}
