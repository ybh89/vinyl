package com.hansung.vinyl.security.metadatasource;

import com.hansung.vinyl.authority.application.AuthorityService;
import com.hansung.vinyl.authority.domain.AuthorityCommandedEvent;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class UrlFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    private final AuthorityService authorityService;
    private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap;

    public UrlFilterInvocationSecurityMetadataSource(LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap,
                                                     AuthorityService authorityService) {
        this.requestMap = requestMap;
        this.authorityService = authorityService;
    }

    // request path 패턴이 인가처리 대상인지 판단. 인가처리 대상이 아니면 null 을 리턴하고 요청을 허용한다.
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        HttpServletRequest request = ((FilterInvocation) object).getRequest();
        if (Objects.nonNull(requestMap)) {
            Set<ConfigAttribute> result = new HashSet<>();
            requestMap.entrySet().stream()
                    .filter(entry -> entry.getKey().matches(request))
                    .map(entry -> entry.getValue())
                    .forEach(configAttributes -> result.addAll(configAttributes));
            return result;
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> allAttributes = new HashSet<>();
        this.requestMap.values().forEach(allAttributes::addAll);
        return allAttributes;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    @TransactionalEventListener
    public void reload(AuthorityCommandedEvent event) {
        System.out.println("AuthorityCommandedEvent!!");
        requestMap.clear();
        requestMap = authorityService.findAuthorityPathMap();
    }
}
