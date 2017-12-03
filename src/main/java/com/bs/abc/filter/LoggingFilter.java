package com.bs.abc.filter;

import com.bs.abc.config.LogConfigProps;
import com.bs.abc.model.RequestInfo;
import com.bs.abc.model.ResponseInfo;
import com.bs.abc.utility.LogContextUtils;
import com.bs.abc.utility.LogUtility;
import com.bs.abc.constant.LoggerConstants;
import com.bs.abc.model.LogInfoBase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;



@Component @Order(1) @Slf4j public class LoggingFilter extends OncePerRequestFilter {
    private final LogContextUtils logContextUtils;
    private final LogConfigProps  logConfigProps;

    @Autowired public LoggingFilter(LogContextUtils logContextUtils, LogConfigProps logConfigProps) {
        this.logContextUtils = logContextUtils;
        this.logConfigProps = logConfigProps;
    }

    /**
     * The default value is "false" so that the filter may log a "before" message at the start of request processing and
     * an "after" message at the end from when the last asynchronously dispatched thread is exiting.
     */
    @Override protected boolean shouldNotFilterAsyncDispatch() {
        return false;
    }

    @Override protected void doFilterInternal(HttpServletRequest inputRequest, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest wrappedRequest = null;

        boolean isIgnorePath = isPathFoundInIgnoreList(inputRequest.getRequestURI().toLowerCase());
        if (isIgnorePath)
            filterChain.doFilter(inputRequest, response);
        else {
            String traceId = StringUtils.isEmpty(inputRequest.getHeader(LoggerConstants.TRACEID)) ?
                LogUtility.nextTransactionId() :
                inputRequest.getHeader(LoggerConstants.TRACEID);
            String guestId =
                StringUtils.isEmpty(inputRequest.getHeader(LoggerConstants.GUESTID)) ? null : inputRequest.getHeader(
                    LoggerConstants.GUESTID);
            String traceIdFromClient = StringUtils.isEmpty(inputRequest.getHeader(LoggerConstants.TRACEID_FROM_CLIENT)) ?
                "" :
                inputRequest.getHeader(LoggerConstants.TRACEID_FROM_CLIENT);

            logContextUtils.setTraceId(traceId);
            logContextUtils.setGuestId(guestId);
            logContextUtils.setClientTranId(traceIdFromClient);
            logContextUtils.setServerHostName(inputRequest.getServerName());
            logContextUtils.setServerHostPort(Integer.toString(inputRequest.getServerPort()));
            logContextUtils.setUserAgent(inputRequest.getHeader(LoggerConstants.USER_AGENT));

            logConfigProps.getAdditionalHeaders().forEach(headerName -> {
                logContextUtils.set(headerName, inputRequest.getHeader(headerName));
            });

            boolean isFirstRequest = !isAsyncDispatch(inputRequest);
            if (isFirstRequest) {
                inputRequest.setAttribute(LoggerConstants.START_TIME, System.currentTimeMillis());
                Map<String, String> customHeaders = new HashMap<>();
                customHeaders.put(LoggerConstants.TRACEID, traceId);
                wrappedRequest = new MutableHttpRequest(inputRequest, customHeaders);
                logRequest(logConfigProps.getAllHeaders(), wrappedRequest);
            } else {
                wrappedRequest = inputRequest;
                LOGGER.debug("Asynchronous request start");
            }

            try {
                if (isFirstRequest) {
                    response.addHeader(LoggerConstants.TRACEID, URLEncoder.encode(traceId, "UTF-8"));
                    response.addHeader(LoggerConstants.TRACEID_FROM_CLIENT, URLEncoder.encode(traceIdFromClient, "UTF-8"));
                }
                filterChain.doFilter(wrappedRequest, response);

                if (!isAsyncStarted(wrappedRequest)) {
                    long startTime = (long) wrappedRequest.getAttribute(LoggerConstants.START_TIME);
                    wrappedRequest.removeAttribute(LoggerConstants.START_TIME);
                    long endTime = System.currentTimeMillis();
                    long durationMillis = endTime - startTime;
                    logResponse(logConfigProps.getAllHeaders(), response, wrappedRequest, durationMillis);
                } else {
                    LOGGER.debug("Synchronous request end");
                }
            } finally {
                logContextUtils.clearAll();
            }
        }
    }

    private void logRequest(boolean allHeaders, HttpServletRequest request) throws IOException {
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setLogSource(LoggerConstants.LOG_SOURCE_NAME);
        requestInfo.setMethod(request.getMethod());
        requestInfo.setPath(request.getRequestURI());
        requestInfo.setClientHostName(request.getRemoteHost());
        requestInfo.setRequestHeaders(getRequestHeaders(request, null));
        logContextUtils.setContextPath(request.getRequestURI());
        logContextUtils.setRequestMethod(request.getMethod());
        logMessage(requestInfo, allHeaders);

    }

    private void logResponse(boolean allHeaders, HttpServletResponse response, HttpServletRequest request,
        long duration) throws IOException {
        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setLogSource(LoggerConstants.LOG_SOURCE_NAME);
        responseInfo.setStatus(response.getStatus());
        responseInfo.setMethod(request.getMethod());
        responseInfo.setPath(request.getRequestURI());
        responseInfo.setDuration(duration);
        if (!CollectionUtils.isEmpty(response.getHeaderNames())) {
            responseInfo.setResponseHeaders(getResponseHeaders(response));
        }
        logContextUtils.setResponseStatus(String.valueOf(response.getStatus()));
        logContextUtils.setDuration(String.valueOf(duration));
        if (isErrorResponse(response)) {
            logContextUtils.setRequestHeaders(getRequestHeaders(request, response).toString());
            if (StringUtils.isNotBlank(request.getQueryString())) {
                logContextUtils.setQueryParams(request.getQueryString());
            }
        }
        logMessage(responseInfo, allHeaders);
    }

    private HashMap<String, String> getRequestHeaders(HttpServletRequest req, HttpServletResponse response) {
        // Only adding usr-Agent header from request
        HashMap<String, String> headers = new HashMap<>();
        headers.put(LoggerConstants.USER_AGENT, req.getHeader(LoggerConstants.USER_AGENT));
        if ((logConfigProps.getAllHeaders() || isErrorResponse(response)) && req.getHeaderNames() != null) {
            Enumeration<String> headerNames = req.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                headers.put(headerName, req.getHeader(headerName));
            }
        }
        return headers;
    }

    private boolean isErrorResponse(HttpServletResponse response) {
        return response != null && response.getStatus() >= 400;
    }

    private HashMap<String, String> getResponseHeaders(HttpServletResponse response) {
        HashMap<String, String> headers = new HashMap<>();
        if (logConfigProps.getAllHeaders() || isErrorResponse(response)) {
            for (String headerName : response.getHeaderNames()) {
                headers.put(headerName, response.getHeader(headerName));
            }
        }
        return headers;
    }

    private void logMessage(LogInfoBase info, boolean isHeaders) throws IOException {
        LOGGER.info(info.toString());
    }

    private boolean isPathFoundInIgnoreList(String path) {
        for (String anIgnorePath : logConfigProps.getIgnorePathList()) {
            if (path.contains(anIgnorePath)) {
                return true;
            }
        }
        return false;
    }

}
