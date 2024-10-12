package com.gloamframework.common.lang.function;

/**
 * @author 晓龙
 */
@FunctionalInterface
public interface ProcessWithRes<T> {

    T doProcess() throws Throwable;

}
