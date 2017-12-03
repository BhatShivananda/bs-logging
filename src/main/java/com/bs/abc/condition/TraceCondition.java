package com.bs.abc.condition;

import com.bs.abc.constant.LoggerConstants;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

public class TraceCondition implements Condition {

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    String flag = context.getEnvironment().getProperty(LoggerConstants.PROPERTY_NAME);
    return !StringUtils.isEmpty(flag) && LoggerConstants.PROPERTY_VALUE.compareToIgnoreCase(flag) == 0;
  }
}


