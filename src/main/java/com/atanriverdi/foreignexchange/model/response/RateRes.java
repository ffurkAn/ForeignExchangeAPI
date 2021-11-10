package com.atanriverdi.foreignexchange.model.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RateRes {
    private String pair;
    private BigDecimal rate;

    public RateRes(String pair, BigDecimal rate) {
        this.pair = pair;
        this.rate = rate;
    }
}
