package com.atanriverdi.foreignexchange.model.request;


import com.atanriverdi.foreignexchange.constant.RegexConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Setter
@Getter
public class ConversionFilterReq {

    @Pattern(regexp = RegexConstants.UUID)
    @Schema(pattern = RegexConstants.UUID)
    private String transactionId;

    @Pattern(regexp = RegexConstants.DATE_YYYY_MM_DD)
    @Schema(pattern = RegexConstants.DATE_YYYY_MM_DD)
    private String transactionDate;
}