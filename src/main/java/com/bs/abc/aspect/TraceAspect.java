package com.bs.abc.aspect;

import com.bs.abc.condition.TraceCondition;
import com.bs.abc.model.EnterMethodInfo;
import com.bs.abc.model.ExceptionMethodInfo;
import com.bs.abc.model.ExitMethodInfo;
import com.bs.abc.constant.LoggerConstants;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Aspect
@Component
@Conditional(TraceCondition.class)
public class TraceAspect {

  private static final Logger LOGGER = LoggerFactory.getLogger(TraceAspect.class.getCanonicalName());

  @Before("@annotation(Traceable)")
  public void before(JoinPoint joinPoint) throws IOException {
    logBeforeMethod(joinPoint.getSignature().getName(), joinPoint.getTarget().getClass().getCanonicalName(),
        joinPoint.getArgs());
  }

  @AfterReturning(value = "@annotation(Traceable)", returning = "returnValue",
      argNames = "joinPoint, returnValue")
  public void afterReturning(JoinPoint joinPoint, Object returnValue) throws IOException {
    String returnType = "";
    if (joinPoint.getSignature() instanceof MethodSignature) {
      returnType = ((MethodSignature) joinPoint.getSignature()).getReturnType().getName();
    }
    logAfterMethod(joinPoint.getSignature().getName(), joinPoint.getTarget().getClass().getCanonicalName(), returnType,
        returnValue);
  }

  @AfterThrowing(value = "@annotation(Traceable)", throwing = "throwable",
      argNames = "joinPoint, throwable")
  public void afterThrowing(JoinPoint joinPoint, Throwable throwable) throws IOException {
    logAfterException(joinPoint.getSignature().getName(), joinPoint.getTarget().getClass().getCanonicalName(),
        throwable.getMessage());
  }

  private void logBeforeMethod(String methodName, String className, Object[] paramList) throws IOException {
    EnterMethodInfo enterMethodInfo = new EnterMethodInfo();
    enterMethodInfo.setLogSource(LoggerConstants.LOG_SOURCE_NAME);
    enterMethodInfo.setClassName(className);
    enterMethodInfo.setMethodName(methodName);
    enterMethodInfo.setParameters(paramList);
    LOGGER.info(enterMethodInfo.toString());
  }

  private void logAfterMethod(String methodName, String className, String returnType, Object returnValue)
      throws IOException {
    ExitMethodInfo exitMethodInfo = new ExitMethodInfo();
    exitMethodInfo.setLogSource(LoggerConstants.LOG_SOURCE_NAME);
    exitMethodInfo.setClassName(className);
    exitMethodInfo.setMethodName(methodName);
    exitMethodInfo.setReturnValue(returnValue);
    exitMethodInfo.setReturnType(returnType);
    LOGGER.info(exitMethodInfo.toString());
  }

  private void logAfterException(String methodName, String className, String errorMessage) throws IOException {
    ExceptionMethodInfo exceptionMethodInfo = new ExceptionMethodInfo();
    exceptionMethodInfo.setLogSource(LoggerConstants.LOG_SOURCE_NAME);
    exceptionMethodInfo.setClassName(className);
    exceptionMethodInfo.setMethodName(methodName);
    exceptionMethodInfo.setErrorMessage(errorMessage);
    LOGGER.info(exceptionMethodInfo.toString());
  }


}
