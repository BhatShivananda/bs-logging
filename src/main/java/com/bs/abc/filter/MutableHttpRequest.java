package com.bs.abc.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

public class MutableHttpRequest extends HttpServletRequestWrapper {
  private final Map<String, String> customHeaders;

  public MutableHttpRequest(HttpServletRequest request, Map<String, String> customHeaders) {
    super(request);

    if (null != customHeaders) {
      this.customHeaders = Collections.unmodifiableMap(customHeaders);
    } else {
      this.customHeaders = Collections.unmodifiableMap(Collections.emptyMap());
    }
  }

  public String getHeader(String name) {
    // check the custom headers first
    String headerValue = customHeaders.get(name);

    if (headerValue != null) {
      return headerValue;
    }
    // else return from into the original wrapped object
    return ((HttpServletRequest) getRequest()).getHeader(name);
  }

  public Enumeration<String> getHeaderNames() {
    // create a set of the custom header names
    Set<String> allHeaders = new HashSet<String>(customHeaders.keySet());

    // now add the headers from the wrapped request object
    Enumeration<String> e = ((HttpServletRequest) getRequest()).getHeaderNames();
    while (e.hasMoreElements()) {
      // add the names of the request headers into the list
      allHeaders.add(e.nextElement());
    }

    // create an enumeration from the set and return
    return Collections.enumeration(allHeaders);
  }

  public Enumeration<String> getHeaders(String name) {
    // check the custom headers first
    String headerValue = customHeaders.get(name);

    if (headerValue != null) {
      return Collections.enumeration(Collections.singleton(headerValue));
    }
    // else return from into the original wrapped object
    return ((HttpServletRequest) getRequest()).getHeaders(name);
  }
}
