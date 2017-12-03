package com.bs.abc.model;

import com.bs.abc.constant.LoggerConstants;
import com.google.common.base.MoreObjects;

import java.util.HashMap;

public class ResponseInfo extends LogInfoBase {

  private static String           EVENT_NAME = "COMPLETED";

  private Integer                 status;
  private long                    duration;
  private HashMap<String, String> responseHeaders;


  public ResponseInfo() {
    setEventName(EVENT_NAME);
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public long getDuration() {
    return duration;
  }

  public void setDuration(long duration) {
    this.duration = duration;
  }

  public HashMap<String, String> getResponseHeaders() {
    return responseHeaders;
  }

  public void setResponseHeaders(HashMap<String, String> responseHeaders) {
    this.responseHeaders = responseHeaders;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(LoggerConstants.LOG_INFO_ROOT_NAME)
        .add("logSource", getLogSource())
        .add("eventName", getEventName())
        .add("status", getStatus())
        .add("method", getMethod())
        .add("path", getPath())
        .add("duration", getDuration())
        .add("responseHeaders", getResponseHeaders())
        .toString();
  }
}
