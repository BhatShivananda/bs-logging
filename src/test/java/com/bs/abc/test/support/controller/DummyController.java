package com.bs.abc.test.support.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

@RequestMapping("/asynclogging")
@RestController
public class DummyController {

  @RequestMapping(method = RequestMethod.GET)
  public Callable<ResponseEntity<String>> handle() {
    return () -> {
      return new ResponseEntity<>("response string", HttpStatus.CREATED);
    };
  }
}
