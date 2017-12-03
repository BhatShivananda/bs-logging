package com.bs.abc.model;

public class LogInfoBase {

  protected String eventName = "";
  protected String logSource = "";
  private String   path;
  private String   method;

  public String getLogSource() {
    return logSource;
  }

  public void setLogSource(String logSource) {
    this.logSource = logSource;
  }

  public String getEventName() {
    return eventName;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }


}
