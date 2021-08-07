package com.hansung.vinyl.common;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

public class ApiDocumentDefinition {
    protected RestDocumentationResultHandler docResultHandler;

    public ApiDocumentDefinition(RestDocumentationResultHandler docResultHandler) {
        this.docResultHandler = docResultHandler;
    }
}
