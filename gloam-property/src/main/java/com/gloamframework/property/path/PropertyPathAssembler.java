package com.gloamframework.property.path;

/**
 * @author 晓龙
 */
public interface PropertyPathAssembler {
    void assemblePath(String originalPath, String mappingPath, Class<?> mappingClass, PathAnalysisAcquirer pathAnalysisAcquirer);
}
