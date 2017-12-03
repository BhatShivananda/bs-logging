package com.bs.abc.model;

import com.bs.abc.constant.LoggerConstants;
import com.google.common.base.MoreObjects;

import java.util.HashMap;

public class RequestInfo extends LogInfoBase {

  private static final String     EVENT_NAME = "STARTED";

  private String                  clientHostName;
  private HashMap<String, String> requestHeaders;

  public RequestInfo() {
    setEventName(EVENT_NAME);
  }

  public String getClientHostName() {
    return clientHostName;
  }

  public void setClientHostName(String clientHostName) {
    this.clientHostName = clientHostName;
  }

  public HashMap<String, String> getRequestHeaders() {
    return requestHeaders;
  }

  public void setRequestHeaders(HashMap<String, String> requestHeaders) {
    this.requestHeaders = requestHeaders;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(LoggerConstants.LOG_INFO_ROOT_NAME)
        .add("logSource", getLogSource())
        .add("eventName", getEventName())
        .add("method", getMethod())
        .add("path", getPath())
        .add("clientHostName", getClientHostName())
        .add("requestHeaders", getRequestHeaders())
        .toString();
  }
}
