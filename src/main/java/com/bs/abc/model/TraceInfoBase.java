package com.bs.abc.model;

public class TraceInfoBase extends LogInfoBase {
  private String methodName = "";
  private String className  = "";

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }
}
