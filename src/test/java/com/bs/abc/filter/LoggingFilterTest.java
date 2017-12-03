package com.bs.abc.filter;

import com.bs.abc.utility.LogUtility;
import com.bs.abc.constant.LoggerConstants;
import com.bs.abc.test.support.controller.DummyController;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.*;
import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@TestPropertySource(properties = {"abc.log.allHeaders=true", "abc.log.additionalHeaders=[abc]"})
@ContextConfiguration(classes = {LoggingFilterTest.class, DummyController.class},
    loader = SpringBootContextLoader.class)

@ComponentScan("com.bs.abc")
@EnableAutoConfiguration
public class LoggingFilterTest {

  @Autowired
  private Filter                loggingFilter;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Mock
  private Appender mockAppender;
  private Logger   logger;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    when(mockAppender.getName()).thenReturn("MockAppender");
    when(mockAppender.isStarted()).thenReturn(true);
    when(mockAppender.isStopped()).thenReturn(false);
    logger = (Logger) LogManager.getLogger(LoggingFilter.class);
    logger.addAppender(mockAppender);
    logger.setLevel(Level.TRACE);
  }

  @Test
  public void testLoggingFilterForIgnoredPath() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/actuator");
    MockHttpServletResponse response = new MockHttpServletResponse();
    FilterChain filterChain = new DummyFilterChain();
    loggingFilter.doFilter(request, response, filterChain);
    verify(mockAppender, times(0)).append(any(LogEvent.class));
  }

  @Test
  public void testLoggingFilterWithErrorResponse() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/mocklogging");
    MockHttpServletResponse response = new MockHttpServletResponse();
    response.setStatus(400);
    FilterChain filterChain = new DummyFilterChain();
    loggingFilter.doFilter(request, response, filterChain);
    verify(mockAppender, times(2)).append(any(LogEvent.class));
  }

  @Test
  public void testLoggingFilterWithErrorResponseAndQueryString() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/mocklogging");
    request.setQueryString("name=abcd");
    MockHttpServletResponse response = new MockHttpServletResponse();
    response.setStatus(400);
    FilterChain filterChain = new DummyFilterChain();
    loggingFilter.doFilter(request, response, filterChain);
    verify(mockAppender, times(2)).append(any(LogEvent.class));
  }

  @Test
  public void testLoggingFilterWithNoHeader() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/mocklogging");
    MockHttpServletResponse response = new MockHttpServletResponse();
    FilterChain filterChain = new DummyFilterChain();
    loggingFilter.doFilter(request, response, filterChain);
    verify(mockAppender, times(2)).append(any(LogEvent.class));
  }

  @Test
  public void testLoggingFilterWithLoggingHeader() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/mocklogging");
    String traceId = LogUtility.nextTransactionId();
    request.addHeader(LoggerConstants.HOSTNAME, "localhost");
    request.addHeader(LoggerConstants.TRACEID, traceId);
    request.addHeader(LoggerConstants.GUESTID, "me");
    request.addHeader(LoggerConstants.TRACEID_FROM_CLIENT, "T_ID_FROM_CLIENT");
    request.addHeader("abc", "abcd");
    MockHttpServletResponse response = new MockHttpServletResponse();
    response.setHeader(LoggerConstants.TRACEID, traceId);
    FilterChain filterChain = new DummyFilterChain();
    loggingFilter.doFilter(request, response, filterChain);
    verify(mockAppender, times(2)).append(any(LogEvent.class));
  }

  @Test
  public void testAsyncLogging() throws Exception {
    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilter(loggingFilter).build();
    MvcResult mvcResult = mockMvc.perform(get("/asynclogging")).andExpect(request().asyncStarted()).andReturn();
    mvcResult.getAsyncResult();
    mockMvc.perform(asyncDispatch(mvcResult))
        .andExpect(status().is(HttpStatus.CREATED.value()))
        .andExpect(content().string("response string"));
    verify(mockAppender, times(4)).append(any(LogEvent.class));
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
