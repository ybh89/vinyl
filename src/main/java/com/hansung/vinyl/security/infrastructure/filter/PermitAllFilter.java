package com.hansung.vinyl.security.infrastructure.filter;

import com.hansung.vinyl.authority.domain.Path;
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
    private List<RequestMatcher> permitAllRequestMatchers = new ArrayList<>();

    public PermitAllFilter(List<Path> permitPaths) {
        permitPaths.forEach(path -> permitAllRequestMatchers.add(new AntPathRequestMatcher(path.value())));
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

    @Override
    public void invoke(FilterInvocation filterInvocation) throws IOException, ServletException {
        super.invoke(filterInvocation);
    }
}
