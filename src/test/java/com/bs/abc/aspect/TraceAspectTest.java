package com.bs.abc.aspect;

import com.bs.abc.bean.DummyBean;
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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@Configuration
@ContextConfiguration(classes = TraceAspectTest.class)
@TestPropertySource(properties = {"log.root.level=trace"})
@ComponentScan("com.bs.abc")
@EnableAutoConfiguration
public class TraceAspectTest {

  @Autowired
  public DummyBean dummyBean;

  @Mock
  private Appender                 mockAppender;
  private Logger                   logger;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    when(mockAppender.getName()).thenReturn("MockAppender");
    when(mockAppender.isStarted()).thenReturn(true);
    when(mockAppender.isStopped()).thenReturn(false);

    logger = (Logger) LogManager.getLogger(TraceAspect.class);
    logger.addAppender(mockAppender);
    logger.setLevel(Level.TRACE);
  }

  @Test
  public void testMethodAspectBefore() throws Exception {

    dummyBean.setIntegerProperty(9999);
    Thread.sleep(1000);
    verify(mockAppender, times(2)).append(any(LogEvent.class));
  }

  @Test
  public void testMethodAspectAfterWithVoidReturnType() throws Exception {
    dummyBean.setStringProperty("After Method execution");
    Thread.sleep(1000);
    verify(mockAppender, times(2)).append(any(LogEvent.class));
  }


  @Test
  public void testMethodAspectWithReturnType() throws Exception {
    String response = dummyBean.getAStringResponse();
    logger.info(response);
    Thread.sleep(1000);
    verify(mockAppender, times(3)).append(any(LogEvent.class));
  }

  @Test(expected = Exception.class)
  public void testMethodAspectAfterThrowing() throws Exception {
    dummyBean.getAnException();
    Thread.sleep(1000);
    verify(mockAppender, times(2)).append(any(LogEvent.class));
  }

  @After
  public void tearDown() {
    logger.removeAppender(mockAppender);
  }

}
