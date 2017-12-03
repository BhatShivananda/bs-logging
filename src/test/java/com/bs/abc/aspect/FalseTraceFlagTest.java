package com.bs.abc.aspect;

import com.bs.abc.bean.DummyBean;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@Configuration
@ContextConfiguration(classes = FalseTraceFlagTest.class)
@TestPropertySource(properties = {"log.root.level=info"})
@ComponentScan("com.bs.abc")
@EnableAutoConfiguration
public class FalseTraceFlagTest {

  @Autowired
  public DummyBean dummyBean;

  @Mock
  private Appender                 mockAppender;
  @Captor
  private ArgumentCaptor<LogEvent> captorLoggingEvent;
  private Logger                   logger;


  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    when(mockAppender.getName()).thenReturn("MockAppender");
    when(mockAppender.isStarted()).thenReturn(true);
    when(mockAppender.isStopped()).thenReturn(false);

    logger = (Logger) LogManager.getLogger(FalseTraceFlagTest.class);
    logger.addAppender(mockAppender);
    logger.setLevel(Level.INFO);
  }

  @Test
  public void testMethodAspectBefore() throws Exception {

    dummyBean.setIntegerProperty(9999);
    assertTrue(captorLoggingEvent.getAllValues().size() == 0);
  }
}
