package com.atanriverdi.foreignexchange.model.request;


import com.atanriverdi.foreignexchange.constant.RegexConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class RateReq {

    @NotEmpty
    @NotNull
    @Pattern(regexp = RegexConstants.CURRENCY_CODE)
    @Schema(example = "TRY", pattern = RegexConstants.CURRENCY_CODE)
    private String sourceCurrency;

    @NotEmpty
    @NotNull
    @Pattern(regexp = RegexConstants.CURRENCY_CODE)
    @Schema(example = "TRY", pattern = RegexConstants.CURRENCY_CODE)
    private String targetCurrency;


    @AssertTrue(message = "Source currency and target currency cannot be same!")
    private boolean isPairValid() {
        return !sourceCurrency.equals(targetCurrency);
    }

}
