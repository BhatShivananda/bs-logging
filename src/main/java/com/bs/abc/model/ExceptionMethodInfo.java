package com.bs.abc.model;

import com.bs.abc.constant.LoggerConstants;
import com.google.common.base.MoreObjects;

public class ExceptionMethodInfo extends TraceInfoBase {

  private static final String EVENT_NAME = "EXCEPTION_THROWN";

  private String errorMessage;

  public ExceptionMethodInfo() {
    setEventName(EVENT_NAME);
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(LoggerConstants.TRACE_INFO_ROOT_NAME).add("logSource", getLogSource())
        .add("eventName", getEventName()).add("methodName", getMethodName()).add("className", getClassName())
        .add("errorMessage", getErrorMessage()).toString();
  }
}
