package com.bs.abc.model;

import com.bs.abc.constant.LoggerConstants;
import com.google.common.base.MoreObjects;

import java.util.Arrays;

public class EnterMethodInfo extends TraceInfoBase {

  private static String EVENT_NAME = "ENTERING_METHOD";
  private Object[]      parameters;

  public EnterMethodInfo() {
    setEventName(EVENT_NAME);
  }

  public Object[] getParameters() {
    return parameters.clone();
  }

  public void setParameters(Object[] parameters) {
    this.parameters = parameters.clone();
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(LoggerConstants.TRACE_INFO_ROOT_NAME).add("logSource", logSource)
        .add("eventName", getEventName()).add("methodName", getMethodName()).add("className", getClassName())
        .add("parameters", Arrays.toString(getParameters())).toString();
  }

}
