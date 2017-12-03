package com.bs.abc.model;

import com.bs.abc.constant.LoggerConstants;
import com.google.common.base.MoreObjects;


public class ExitMethodInfo extends TraceInfoBase {

  private static final String EVENT_NAME = "EXITING_METHOD";

  private Object returnValue;
  private String returnType = "";

  public ExitMethodInfo() {
    setEventName(EVENT_NAME);
  }

  public Object getReturnValue() {
    return returnValue;
  }

  public void setReturnValue(Object returnValue) {
    this.returnValue = returnValue;
  }

  public String getReturnType() {
    return returnType;
  }

  public void setReturnType(String returnType) {
    this.returnType = returnType;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(LoggerConstants.TRACE_INFO_ROOT_NAME).add("logSource", getLogSource())
        .add("eventName", getEventName()).add("methodName", getMethodName()).add("className", getClassName())
        .add("returnType", getReturnType()).add("returnValue", getReturnValue()).toString();

  }
}
