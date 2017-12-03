package com.bs.abc.utility;

public interface LogContextUtils {
  void clearAll();

  void setTraceId(String traceId);
  String getTraceId();
  void clearTraceId();

  void setGuestId(String guestId);
  String getGuestId();
  void clearGuestId();

  void setClientTranId(String transactionIdFromClient);
  String getClientTranId();
  void clearClientTranId();

  void setServerHostName(String serverName);
  String getServerHostName();
  void clearServerHostName();

  void setServerHostPort(String serverHostPort);
  String getServerHostPort();
  void clearServerHostPort();

  void setAppName(String appName);
  String getAppName();
  void clearAppName();

  void setUserAgent(String userAgent);
  String getUserAgent();
  void clearUserAgent();

  void setContextPath(String contextPath);
  String getContextPath();
  void clearContextPath();

  void setRequestMethod(String method);
  String getRequestMethod();
  void clearRequestMethod();

  void setResponseStatus(String responseStatus);
  String getResponseStatus();
  void clearResponseStatus();

  void setDuration(String duration);
  String getDuration();
  void clearDuration();

  void setRequestHeaders(String requestHeadersStr);
  String getRequestHeaders();
  void clearRequestHeaders();

  void setQueryParams(String queryParams);
  String getQueryParams();
  void clearQueryParams();

  // allow anything
  void set(String key, String value);
  String get(String key);
  void clear(String key);
}
