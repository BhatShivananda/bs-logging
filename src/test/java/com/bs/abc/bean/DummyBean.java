package com.bs.abc.bean;

import com.google.common.base.MoreObjects;
import com.bs.abc.aspect.Traceable;
import org.springframework.stereotype.Component;

@Component
public class DummyBean {

  private Integer integerProperty;
  private String  stringProperty;


  @Traceable
  public Integer getIntegerProperty() {
    return integerProperty;
  }

  @Traceable
  public void setIntegerProperty(final Integer integerProperty) {
    this.integerProperty = integerProperty;
  }

  @Traceable
  public String getStringProperty() {
    return stringProperty;
  }

  @Traceable
  public void setStringProperty(final String stringProperty) {
    this.stringProperty = stringProperty;
  }

  @Traceable
  public String getAStringResponse() {
    return "This is a string response from SimpleBean.";
  }

  @Traceable
  public String getAnException() throws Exception {
    throw new Exception("Exception From SimpleBean.getAnException");
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("integerProperty", integerProperty)
        .add("stringProperty", stringProperty).toString();
  }

}
