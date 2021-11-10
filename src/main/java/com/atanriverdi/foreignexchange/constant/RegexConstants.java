package com.atanriverdi.foreignexchange.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RegexConstants {
    public static final String CURRENCY_CODE = "[A-Z]{3}";
    public static final String UUID = "[a-z0-9]{32}";
    public static final String DATE_YYYY_MM_DD = "(19|20)[0-9][0-9]-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])";
    public static final String AMOUNT = "[1-9][0-9]{0,5}";
}
