package com.hansung.vinyl.common;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

public class ApiDocumentDefinition {
    public static final String CONSTRAINT_KEY = "constraint";

    protected RestDocumentationResultHandler docResultHandler;

    public ApiDocumentDefinition(RestDocumentationResultHandler docResultHandler) {
        this.docResultHandler = docResultHandler;
    }
}
