package com.bs.abc.filter;

import com.bs.abc.constant.LoggerConstants;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.*;
import java.io.IOException;

import static org.mockito.Mockito.when;


@RunWith(SpringJUnit4ClassRunner.class)
@Configuration
@ContextConfiguration(classes = ConsoleLogTest.class, loader = SpringBootContextLoader.class)
@ComponentScan("com.bs.abc")
@EnableAutoConfiguration
public class ConsoleLogTest {

  @Autowired
  private Filter loggingFilter;

  @Mock
  private Appender mockAppender;

  @Captor
  private ArgumentCaptor<LogEvent> captorLoggingEvent;
  private Logger                   logger;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    when(mockAppender.getName()).thenReturn("MockAppender");
    when(mockAppender.isStarted()).thenReturn(true);
    when(mockAppender.isStopped()).thenReturn(false);

    logger = (Logger) LogManager.getLogger(ConsoleLogTest.class);
    logger.addAppender(mockAppender);
    logger.setLevel(Level.DEBUG);
  }


  @Test
  public void testLogging() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/mocklogging");
    request.addHeader(LoggerConstants.HOSTNAME, "localhost");
    request.addHeader(LoggerConstants.TRACEID, "TRACE-ID");
    request.addHeader(LoggerConstants.GUESTID, "me");
    request.addHeader(LoggerConstants.TRACEID_FROM_CLIENT, "T_ID_FROM_CLIENT");

    MockHttpServletResponse response = new MockHttpServletResponse();
    FilterChain filterChain = new DummyFilterChain();
    loggingFilter.doFilter(request, response, filterChain);

  }

  private static class DummyFilterChain implements FilterChain {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {}
  }

  @After
  public void tearDown() {
    loggingFilter = null;
    logger.removeAppender(mockAppender);
  }

}
