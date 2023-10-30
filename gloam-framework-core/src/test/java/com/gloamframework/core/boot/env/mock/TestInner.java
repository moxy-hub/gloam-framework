package com.gloamframework.core.boot.env.mock;

import com.gloamframework.core.boot.env.NestedOverrideClass;
import com.gloamframework.core.boot.env.OverrideProperty;
import lombok.Data;

@NestedOverrideClass
@Data
public class TestInner {

    @OverrideProperty("url")
    private String url = "66887yyyy";

}
