package com.atanriverdi.foreignexchange.error;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorCodes {

    public static final ErrorMessage ER01 = new ErrorMessage("ER01", "Invalid currency code!");
    public static final ErrorMessage ER02 = new ErrorMessage("ER02", "Service provider returned an error!");
    public static final ErrorMessage HTTP_500 = new ErrorMessage("500", "Unexpected error occurred!");
}
