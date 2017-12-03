package com.bs.abc.layout;

import com.bs.abc.constant.LoggerConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.status.StatusLogger;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Log4j2 component for Json logging (
 * <class>JsonLayout</class>) had issues like incorrect message break up which resulted in
 * <exception>JsonParseException</exception>. This class aims at preventing those errors.
 */

@Plugin(name = "CustomJsonLayout", category = "Core", elementType = "layout", printObject = true)
public class CustomJsonLayout extends AbstractStringLayout {

  private static final ObjectMapper        MAPPER          = initializeMapper();

  private static final String              APP_NAME        =
      System.getProperty("app.name", getBootstrapValue("spring.application.name"));

  private static final String              ENV_NAME        = System.getProperty("env", "env not set");

  private static final String              REGION_NAME     = System.getProperty("dc", "dc not set");

  private static final FastDateFormat ISO_8601_FORMAT =
      FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSSZ", TimeZone.getTimeZone("America/Chicago"));

  public final static String               LINE_BREAK      = "\n";

  protected CustomJsonLayout() {
    super(Charset.defaultCharset());
  }

  @PluginFactory
  public static CustomJsonLayout createLayout() {
    return new CustomJsonLayout();
  }

  @Override
  public String toSerializable(LogEvent event) {
    String jsonMessage = "";
    if (event != null) {
      Map<String, Object> logMap = new LinkedHashMap<>();
      logMap.put("eventTimeStamp", ISO_8601_FORMAT.format(event.getTimeMillis()));
      logMap.put(LoggerConstants.APP_NAME, APP_NAME);
      logMap.put(LoggerConstants.ENV_NAME, ENV_NAME);
      logMap.put(LoggerConstants.REGION_NAME, REGION_NAME);
      logMap.put("level", event.getLevel().toString());
      logMap.put("timeMillis", event.getTimeMillis());
      logMap.put("loggerName", event.getLoggerName());
      logMap.put("message", event.getMessage() != null && event.getMessage().getFormattedMessage() != null
          ? event.getMessage().getFormattedMessage() : "");

      logMap.put("threadId", event.getThreadId());
      logMap.put("thread", event.getThreadName());
      logMap.put("threadPriority", event.getThreadPriority());

      if (null != event.getThrown())
        logMap.put("exception", mapException(event.getThrown()));
      else if (null != event.getThrownProxy())
        logMap.put("exception", mapException(event.getThrownProxy().getThrowable()));
      try {
        jsonMessage = MAPPER.writeValueAsString(logMap) + LINE_BREAK;
      } catch (JsonProcessingException jEx) {
        LOGGER.error("Json Processing Exception {} while parsing log message {}", jEx.getMessage(),
            logMap.get("message"));
      }
    }
    return jsonMessage;
  }

  private Map<String, String> mapException(Throwable exception) {
    Map<String, String> exceptionThrown = new LinkedHashMap<>();
    if (exception != null) {
      exceptionThrown.put("message", exception.getMessage());
      exceptionThrown.put("name", exception.getClass().getCanonicalName());
      exceptionThrown.put("localizedMessage", exception.getLocalizedMessage());
      exceptionThrown.put("stackTrace", ExceptionUtils.getStackTrace(exception));
    }
    return exceptionThrown;
  }

  private static ObjectMapper initializeMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
    return objectMapper;
  }

  /**
   * Read a specific property from boostrap.yml
   * 
   * @param key
   * @return String
   */
  private static String getBootstrapValue(String key) {
    try {
      YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
      MapPropertySource bootstrapSource =
          (MapPropertySource) loader.load("bootstrap", new ClassPathResource("bootstrap.yml"), null);
      return String.valueOf(bootstrapSource.getProperty(key));
    } catch (RuntimeException | IOException e) {
      StatusLogger.getLogger().warn("Exception when reading property {} from boostrap.yml", key);
    }

    return null;
  }

}
