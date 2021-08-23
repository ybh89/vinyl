package com.hansung.vinyl.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RequiredArgsConstructor
@Service
public class EmailContentBuilder {
    private final TemplateEngine templateEngine;

    public String buildIdentification(String link) {
        Context context = new Context();
        context.setVariable("link", link);
        return templateEngine.process("identification", context);
    }
}
