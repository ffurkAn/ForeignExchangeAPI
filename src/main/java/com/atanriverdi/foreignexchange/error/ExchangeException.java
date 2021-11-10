package com.atanriverdi.foreignexchange.error;

import lombok.Getter;

@Getter
public class ExchangeException extends RuntimeException {

    private final String code;

    public ExchangeException(ErrorMessage errorMessage) {
        super(errorMessage.getErrDesc());
        this.code = errorMessage.getErrCode();
    }

    public ExchangeException(ErrorMessage errorMessage, String additionalMessage) {
        super(errorMessage.getErrDesc().concat(additionalMessage));
        this.code = errorMessage.getErrCode();
    }
}