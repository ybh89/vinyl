package com.hansung.vinyl.common.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.hansung.vinyl.authority.domain.HttpMethod;
import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 로그 전략.
 * 1. request log(Controller) - tracing id, path, http method, request body
 * 2. response log(Controller) - tracing id, response body, time
 * 3. error log - tracing id, response body, exception message, exception
 */

@Component
@Aspect
public class ApplicationLogAop {
    private static final Logger appLogger = LoggerFactory.getLogger("app");
    private static final Logger errLogger = LoggerFactory.getLogger("err");

    private ObjectMapper objectMapper = new ObjectMapper();

    @Pointcut("execution(* com.hansung.vinyl.*.ui.*Controller.*(..))")
    public void controllerAdvice() {
    }

    @Before("controllerAdvice()")
    public void requestLogging(JoinPoint joinPoint) throws IOException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();

        if (request.getClass().getName().contains("SecurityContextHolderAwareRequestWrapper")) return;
        final ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;

        MDC.put(LogConstant.TRACING_ID, UUID.randomUUID().toString());
        MDC.put(LogConstant.START_TIME, String.valueOf(System.currentTimeMillis()));

        if (HttpMethod.GET.equalsIgnoreCase(request.getMethod()) ||
                HttpMethod.DELETE.equalsIgnoreCase(request.getMethod())) {
            appLogger.info("[{}][{}]", request.getRequestURI(), request.getMethod());
            return;
        }

        appLogger.info("[{}][{}][{}][{}]", request.getRequestURI(), request.getMethod(), getRequestParams(request),
                objectMapper.readTree(cachingRequest.getContentAsByteArray()));
    }

    @AfterReturning(pointcut = "controllerAdvice()", returning = "returnValue")
    public void responseLogging(JoinPoint joinPoint, Object returnValue) {
        if (returnValue instanceof List) {
            appLogger.info("[ListSize:{}][{}ms]", ((List) returnValue).size(), processingTime());
            return;
        }

        appLogger.info("[{}][{}ms]", returnValue, processingTime());
        MDC.clear();
    }

    @AfterThrowing(pointcut = "controllerAdvice()", throwing = "exception")
    public void errorLogging(JoinPoint joinPoint, Exception exception) throws RuntimeException, IOException {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getResponse();
        final ContentCachingResponseWrapper cachingResponse = (ContentCachingResponseWrapper) response;
        String message = "[" + objectMapper.readTree(cachingResponse.getContentAsByteArray()) + "][" +
                exception.getMessage() + "]" + "[" + processingTime() + "]ms";

        errLogger.error(message, exception);
        MDC.clear();
    }

    private long processingTime() {
        long startTime = Long.parseLong(MDC.get(LogConstant.START_TIME));
        long endTime = System.currentTimeMillis();

        return endTime - startTime;
    }

    private String paramMapToString(Map<String, String[]> paramMap) {
        return paramMap.entrySet().stream()
                .map(entry -> String.format("%s -> (%s)",
                        entry.getKey(), Joiner.on(",").join(entry.getValue())))
                .collect(Collectors.joining(", "));
    }

    private String getRequestParams(HttpServletRequest request) {
        String params = Strings.EMPTY;

        Map<String, String[]> paramMap = request.getParameterMap();
        if (!paramMap.isEmpty()) {
            params = " [" + paramMapToString(paramMap) + "]";
        }
        return params;
    }
}
