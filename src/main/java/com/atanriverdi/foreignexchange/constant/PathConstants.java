package com.atanriverdi.foreignexchange.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PathConstants {

    public static final String API_FOREIGN_EXCHANGE = "/api/foreign-exchange";
    public static final String RATE = "/rate";
    public static final String CONVERSION = "/conversion";
    public static final String CONVERSIONS = "/conversions";

    public static final String REQ_PARAM_FROM = "?from=";
    public static final String REQ_PARAM_TO = "&to=";
    public static final String REQ_PARAM_AMOUNT = "&amount=";

    public static final String API_CONTEXT = "/api/*";
}
