package com.bs.abc.sysout;

import lombok.extern.slf4j.Slf4j;

import java.io.OutputStream;
import java.io.PrintStream;

@Slf4j
public class Slf4jLoggerPrintStream extends PrintStream {
  public Slf4jLoggerPrintStream(OutputStream out) {
    super(out);
  }

  public void println(String line) {
    LOGGER.info(line);
  }
}
