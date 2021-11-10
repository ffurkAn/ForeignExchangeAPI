package com.atanriverdi.foreignexchange.model.entity.dto;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ConversionDTO {

    private long id;
    private String sourceCurrencyCode;
    private String targetCurrencyCode;
    private BigDecimal amount;
    private BigDecimal targetAmount;
    private String transactionId;
    private LocalDate transactionDate;
}
