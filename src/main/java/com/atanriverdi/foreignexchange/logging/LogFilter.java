package com.atanriverdi.foreignexchange.logging;

import io.micrometer.core.instrument.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class LogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        log.info("Log Filter is being invoked");

        var request = (HttpServletRequest) servletRequest;
        final var response = (HttpServletResponse) servletResponse;
        Map<String, String[]> extraParams = new TreeMap<>();
        var wrappedRequest = new WrappedRequest(request, extraParams);

        var responseWrapper = new ContentCachingResponseWrapper(response);

        log.info("Starting transaction for method : {}, requested URI : {}", request.getMethod(), request.getRequestURI());

        try {

            var body = IOUtils.toString(wrappedRequest.getInputStream(), StandardCharsets.UTF_8);
            if (!StringUtils.isEmpty(body)) {
                log.info("Request body: [{}]", body);
            }

            wrappedRequest.resetStream(body.getBytes(StandardCharsets.UTF_8));

            filterChain.doFilter(wrappedRequest, responseWrapper);
        } finally {
            byte[] responseArray = responseWrapper.getContentAsByteArray();
            var respBody = new String(responseArray, responseWrapper.getCharacterEncoding());
            if (!StringUtils.isEmpty(respBody)) {
                log.info("Response body: [{}]", respBody);
            }

            responseWrapper.copyBodyToResponse();
            log.info("Finishing the transaction for req : {}", wrappedRequest.getRequestURI());
        }
    }
}
