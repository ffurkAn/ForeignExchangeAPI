package com.atanriverdi.foreignexchange.model.frankfurter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class FrankfurterResponse {

    private Double amount;
    private String base;
    private LocalDate date;
    private Object rates;
}
