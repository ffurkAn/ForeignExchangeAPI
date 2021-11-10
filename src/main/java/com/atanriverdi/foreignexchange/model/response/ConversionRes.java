package com.atanriverdi.foreignexchange.model.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ConversionRes {
    private String targetAmount;
    private String transactionId;
}
