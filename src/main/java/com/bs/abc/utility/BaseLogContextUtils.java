package com.bs.abc.utility;

import com.bs.abc.constant.LoggerConstants;

abstract class BaseLogContextUtils implements LogContextUtils {

  @Override
  public void setTraceId(String transactionId) {
    set(LoggerConstants.TRACEID, transactionId);
  }

  @Override
  public String getTraceId() {
    return get(LoggerConstants.TRACEID);
  }

  @Override
  public void clearTraceId() {
    clear(LoggerConstants.TRACEID);
  }

  @Override
  public void setGuestId(String userId) {
    set(LoggerConstants.GUESTID, userId);
  }

  @Override
  public String getGuestId() {
    return get(LoggerConstants.GUESTID);
  }

  @Override
  public void clearGuestId() {
    clear(LoggerConstants.GUESTID);
  }

  @Override
  public void setClientTranId(String transactionIdFromClient) {
    set(LoggerConstants.TRACEID_FROM_CLIENT, transactionIdFromClient);
  }

  @Override
  public String getClientTranId() {
    return get(LoggerConstants.TRACEID_FROM_CLIENT);
  }

  @Override
  public void clearClientTranId() {
    clear(LoggerConstants.TRACEID_FROM_CLIENT);
  }

  @Override
  public void setServerHostName(String serverName) {
    set(LoggerConstants.HOSTNAME, serverName);
  }

  @Override
  public String getServerHostName() {
    return get(LoggerConstants.HOSTNAME);
  }

  @Override
  public void clearServerHostName() {
    clear(LoggerConstants.HOSTNAME);
  }

  @Override
  public void setServerHostPort(String serverHostPort) {
    set(LoggerConstants.HOST_PORT, serverHostPort);
  }

  @Override
  public String getServerHostPort() {
    return get(LoggerConstants.HOST_PORT);
  }

  @Override
  public void clearServerHostPort() {
    clear(LoggerConstants.HOST_PORT);
  }

  @Override
  public void setAppName(String appName) {
    set(LoggerConstants.APP_NAME, appName);
  }

  @Override
  public String getAppName() {
    return get(LoggerConstants.APP_NAME);
  }

  @Override
  public void clearAppName() {
    clear(LoggerConstants.APP_NAME);
  }

  @Override
  public void setUserAgent(String userAgent) {
    set(LoggerConstants.USER_AGENT, userAgent);
  }

  @Override
  public String getUserAgent() {
    return get(LoggerConstants.USER_AGENT);
  }

  @Override
  public void clearUserAgent() {
    clear(LoggerConstants.USER_AGENT);
  }

  @Override
  public void setContextPath(String contextPath) {
    set(LoggerConstants.PATH, contextPath);
  }

  @Override
  public String getContextPath() {
    return get(LoggerConstants.PATH);
  }

  @Override
  public void clearContextPath() {
    clear(LoggerConstants.PATH);
  }

  @Override
  public void setRequestMethod(String method) {
    set(LoggerConstants.METHOD, method);
  }

  @Override
  public String getRequestMethod() {
    return get(LoggerConstants.METHOD);
  }

  @Override
  public void clearRequestMethod() {
    clear(LoggerConstants.METHOD);
  }

  @Override
  public void setResponseStatus(String responseStatus) {
    set(LoggerConstants.STATUS, responseStatus);
  }

  @Override
  public String getResponseStatus() {
    return get(LoggerConstants.STATUS);
  }

  @Override
  public void clearResponseStatus() {
    clear(LoggerConstants.STATUS);
  }

  @Override
  public void setDuration(String duration) {
    set(LoggerConstants.DURATION, duration);
  }

  @Override
  public String getDuration() {
    return get(LoggerConstants.DURATION);
  }

  @Override
  public void clearDuration() {
    clear(LoggerConstants.DURATION);
  }

  @Override
  public void setRequestHeaders(String requestHeadersStr) {
    set(LoggerConstants.REQUEST_HEADERS, requestHeadersStr);
  }

  @Override
  public String getRequestHeaders() {
    return get(LoggerConstants.REQUEST_HEADERS);
  }

  @Override
  public void clearRequestHeaders() {
    clear(LoggerConstants.REQUEST_HEADERS);
  }

  @Override
  public void setQueryParams(String queryParams) {
    set(LoggerConstants.QUERY_PARAMS, queryParams);
  }

  @Override
  public String getQueryParams() {
    return get(LoggerConstants.QUERY_PARAMS);
  }

  @Override
  public void clearQueryParams() {
    clear(LoggerConstants.QUERY_PARAMS);
  }

}
