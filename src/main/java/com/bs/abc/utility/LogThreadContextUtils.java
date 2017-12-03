package com.bs.abc.utility;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Component;

@Component
public class LogThreadContextUtils extends BaseLogContextUtils {

  @Override
  public void clearAll() {
    ThreadContext.clearAll();
  }

  @Override
  public void set(String key, String value) {
    ThreadContext.put(key, value);
  }

  @Override
  public String get(String key) {
    return ThreadContext.get(key);
  }

  @Override
  public void clear(String key) {
    ThreadContext.remove(key);
  }
}
