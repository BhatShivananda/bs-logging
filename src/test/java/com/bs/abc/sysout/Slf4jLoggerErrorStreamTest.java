package com.bs.abc.sysout;

import com.bs.abc.sysout.Slf4jLoggerErrorStream;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.junit.Before;
import org.junit.Test;

import java.io.PrintStream;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class Slf4jLoggerErrorStreamTest {
  private Appender mockAppender;
  private Logger   logger;

  @Before
  public void setup() {
    mockAppender = mock(Appender.class);
    when(mockAppender.getName()).thenReturn("Slf4jLoggerErrorStreamAppender");
    when(mockAppender.isStarted()).thenReturn(true);
    when(mockAppender.isStopped()).thenReturn(false);
    logger = (Logger) LogManager.getLogger(Slf4jLoggerErrorStream.class);
    logger.addAppender(mockAppender);
    logger.setLevel(Level.ERROR);
  }

  @Test
  public void testPrintln() {
    PrintStream errorStream = new Slf4jLoggerErrorStream(System.err);
    errorStream.println("error message");
    verify(mockAppender, times(1)).append(any(LogEvent.class));
    errorStream.close();
  }
}
