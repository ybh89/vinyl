package com.hansung.vinyl.security.application;

import com.hansung.vinyl.authority.application.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class UrlPathMapFactoryBean implements FactoryBean<LinkedHashMap<RequestMatcher, List<ConfigAttribute>>> {
    private final AuthorityService authorityService;
    private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> pathMap;

    @Override
    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getObject() throws Exception {
        if (Objects.isNull(pathMap)) {
            pathMap = authorityService.findAuthorityPathMap();
        }
        return pathMap;
    }

    @Override
    public Class<?> getObjectType() {
        return LinkedHashMap.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
