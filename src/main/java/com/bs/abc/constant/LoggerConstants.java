package com.bs.abc.constant;

public interface LoggerConstants {
    String TRACEID = "traceId";
    String GUESTID = "guestId";
    String HOSTNAME = "hostName";
    String TRACEID_FROM_CLIENT = "clientTraceId";
    String APP_NAME = "appName";
    String ENV_NAME = "environment";
    String HOST_PORT = "hostPort";
    String LOG_SOURCE_NAME = "abcLog";
    String LOG_INFO_ROOT_NAME = "abcLogInfo";
    String USER_AGENT = "User-Agent";
    String PATH = "ctx_path";
    String METHOD = "ctx_method";
    String DURATION = "totalDurationMS";
    String STATUS = "responseStatus";
    String REQUEST_HEADERS = "requestHeaders";
    String QUERY_PARAMS = "queryParams";
    String START_TIME = "startTime";
    String TRACE_INFO_ROOT_NAME = "abcTraceInfo";
    String REGION_NAME = "dc";
    String PROPERTY_NAME  = "log.root.level";
    static String PROPERTY_VALUE = "trace";
}
