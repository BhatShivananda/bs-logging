package com.bs.abc.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Getter
@Setter
@ConfigurationProperties("abc.log")
public class LogConfigProps {
  private List<String> ignorePathList    = Arrays.asList("/actuator");
  private Boolean      allHeaders        = false;
  private List<String> additionalHeaders = new ArrayList<>();
}
