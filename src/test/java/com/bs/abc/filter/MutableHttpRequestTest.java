package com.bs.abc.filter;

import com.bs.abc.filter.MutableHttpRequest;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MutableHttpRequestTest {

  @Test
  public void testGetHeader() {
    // Header with value in original request
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("customHeader", "originalValue");
    MutableHttpRequest wrappedRequest = new MutableHttpRequest(request, null);
    assertEquals("originalValue", wrappedRequest.getHeader("customHeader"));

    // Header with overridden value
    request = new MockHttpServletRequest();
    request.addHeader("customHeader", "originalValue");
    Map<String, String> customHeaders = new HashMap<>();
    customHeaders.put("customHeader", "customValue");
    wrappedRequest = new MutableHttpRequest(request, customHeaders);
    assertEquals("customValue", wrappedRequest.getHeader("customHeader"));
  }

  @Test
  public void testGetHeaderNames() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("originalHeader", "originalValue");
    Map<String, String> customHeaders = new HashMap<>();
    customHeaders.put("customHeader", "customValue");
    MutableHttpRequest wrappedRequest = new MutableHttpRequest(request, customHeaders);

    Enumeration<String> headerNames = wrappedRequest.getHeaderNames();
    List<String> expectedHeaders = Arrays.asList("originalHeader", "customHeader");
    assertTrue(expectedHeaders.contains(headerNames.nextElement()));
    assertTrue(expectedHeaders.contains(headerNames.nextElement()));
  }

  @Test
  public void testGetHeaders() {
    // Header with value in original request
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("customHeader", "originalValue");
    MutableHttpRequest wrappedRequest = new MutableHttpRequest(request, null);
    assertTrue(wrappedRequest.getHeaders("customHeader").nextElement().equals("originalValue"));

    // Header with overridden value
    request = new MockHttpServletRequest();
    request.addHeader("customHeader", "originalValue");
    Map<String, String> customHeaders = new HashMap<>();
    customHeaders.put("customHeader", "customValue");
    wrappedRequest = new MutableHttpRequest(request, customHeaders);
    assertTrue(wrappedRequest.getHeaders("customHeader").nextElement().equals("customValue"));
  }

}
