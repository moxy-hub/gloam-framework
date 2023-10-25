package com.gloamframework.core.boot.diagnostics;

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

/**
 * 项目异常诊断,拦截{@link GloamStartException},阻止spring项目启动
 */
public class GloamStartExceptionDiagnostics extends AbstractFailureAnalyzer<GloamStartException> {

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, GloamStartException cause) {
        return new FailureAnalysis(cause.getDescription(), cause.getAction(), cause.getCause() == null ? cause : cause.getCause());
    }

}
