package com.bs.abc.sysout;

import com.bs.abc.sysout.Slf4jLoggerPrintStream;
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

public class Slf4jLoggerPrintStreamTest {
  private Appender mockAppender;
  private Logger   logger;

  @Before
  public void setup() {
    mockAppender = mock(Appender.class);
    when(mockAppender.getName()).thenReturn("Slf4jLoggerPrintStreamAppender");
    when(mockAppender.isStarted()).thenReturn(true);
    when(mockAppender.isStopped()).thenReturn(false);
    logger = (Logger) LogManager.getLogger(Slf4jLoggerPrintStream.class);
    logger.addAppender(mockAppender);
    logger.setLevel(Level.INFO);
  }

  @Test
  public void testPrintln() {
    PrintStream printStream = new Slf4jLoggerPrintStream(System.out);
    printStream.println("info message");
    verify(mockAppender, times(1)).append(any(LogEvent.class));
    printStream.close();
  }
}
