package com.hansung.vinyl.security.infrastructure.filter;

import com.hansung.vinyl.authority.domain.Resource;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PermitAllFilter extends FilterSecurityInterceptor {
    private static final String FILTER_APPLIED = "__spring_security_filterSecurityInterceptor_filterApplied";

    private boolean observeOncePerRequest = true;
    private List<RequestMatcher> permitAllRequestMatchers = new ArrayList<>();

    public PermitAllFilter(List<Resource> permitResources) {
        permitResources.forEach(resource -> permitAllRequestMatchers.add(new AntPathRequestMatcher(resource.getPathValue(),
                resource.getHttpMethod().name())));
    }

    // 요청 path 가 permitAll path 이면, null 을 리턴하여 인가처리하지 않는다.
    @Override
    protected InterceptorStatusToken beforeInvocation(Object object) {
        HttpServletRequest request = ((FilterInvocation) object).getRequest();
        if (permitAllRequestMatchers.stream()
                .anyMatch(requestMatcher -> requestMatcher.matches(request))) {
            return null;
        }

        return super.beforeInvocation(object);
    }

    public void invoke(FilterInvocation filterInvocation) throws IOException, ServletException {
        if (isApplied(filterInvocation) && this.observeOncePerRequest) {
            filterInvocation.getChain().doFilter(filterInvocation.getRequest(), filterInvocation.getResponse());
            return;
        }

        if (filterInvocation.getRequest() != null && this.observeOncePerRequest) {
            filterInvocation.getRequest().setAttribute(FILTER_APPLIED, Boolean.TRUE);
        }
        InterceptorStatusToken token = beforeInvocation(filterInvocation);
        try {
            filterInvocation.getChain().doFilter(filterInvocation.getRequest(), filterInvocation.getResponse());
        }
        finally {
            super.finallyInvocation(token);
        }
        super.afterInvocation(token, null);
    }

    private boolean isApplied(FilterInvocation filterInvocation) {
        return (filterInvocation.getRequest() != null)
                && (filterInvocation.getRequest().getAttribute(FILTER_APPLIED) != null);
    }
}
