package com.bs.abc.sysout;

import lombok.extern.slf4j.Slf4j;

import java.io.OutputStream;
import java.io.PrintStream;

@Slf4j
public class Slf4jLoggerErrorStream extends PrintStream {
  public Slf4jLoggerErrorStream(OutputStream out) {
    super(out);
  }

  public void println(String line) {
    LOGGER.error(line);
  }
}
