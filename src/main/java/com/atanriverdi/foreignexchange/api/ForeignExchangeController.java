package com.atanriverdi.foreignexchange.api;

import com.atanriverdi.foreignexchange.constant.Common;
import com.atanriverdi.foreignexchange.constant.PathConstants;
import com.atanriverdi.foreignexchange.error.ErrorMessage;
import com.atanriverdi.foreignexchange.model.entity.Conversion;
import com.atanriverdi.foreignexchange.model.request.ConversionFilterReq;
import com.atanriverdi.foreignexchange.model.request.ConversionReq;
import com.atanriverdi.foreignexchange.model.request.RateReq;
import com.atanriverdi.foreignexchange.model.response.ConversionRes;
import com.atanriverdi.foreignexchange.model.response.RateRes;
import com.atanriverdi.foreignexchange.service.ForeignExchangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping(PathConstants.API_FOREIGN_EXCHANGE)
@RestController
@Slf4j
@Tag(name = Common.FOREIGN_EXCHANGE_API)
public class ForeignExchangeController {

    final
    ForeignExchangeService foreignExchangeService;

    public ForeignExchangeController(ForeignExchangeService foreignExchangeService) {
        this.foreignExchangeService = foreignExchangeService;
    }

    @Operation(summary = "Calculates rate between currency pair")
    @ApiResponses(value = {
            @ApiResponse(responseCode = Common.HTTP_STATUS_CODE_200, description = Common.SUCCESS, content = @Content(schema = @Schema(implementation = RateRes.class))),
            @ApiResponse(responseCode = Common.HTTP_STATUS_CODE_400, description = Common.BAD_REQUEST, content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = Common.HTTP_STATUS_CODE_500, description = Common.UNEXPECTED_ERROR,
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping(PathConstants.RATE)
    public ResponseEntity<RateRes> getExchangeRate(@Valid @RequestBody RateReq rateReq) {
        return ResponseEntity.status(HttpStatus.OK).body(foreignExchangeService.getRate(rateReq));
    }

    @Operation(summary = "Returns the amount of target currency")
    @ApiResponses(value = {
            @ApiResponse(responseCode = Common.HTTP_STATUS_CODE_200, description = Common.SUCCESS, content = @Content(schema = @Schema(implementation = RateRes.class))),
            @ApiResponse(responseCode = Common.HTTP_STATUS_CODE_400, description = Common.BAD_REQUEST, content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = Common.HTTP_STATUS_CODE_500, description = Common.UNEXPECTED_ERROR,
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping(PathConstants.CONVERSION)
    public ResponseEntity<ConversionRes> getConversation(@Valid @RequestBody ConversionReq conversionReq) {
        return ResponseEntity.status(HttpStatus.OK).body(foreignExchangeService.getConversion(conversionReq));

    }

    @Operation(summary = "List of conversations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = Common.HTTP_STATUS_CODE_200, description = Common.SUCCESS, content = @Content(schema = @Schema(implementation = Conversion.class))),
            @ApiResponse(responseCode = Common.HTTP_STATUS_CODE_400, description = Common.BAD_REQUEST, content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = Common.HTTP_STATUS_CODE_500, description = Common.UNEXPECTED_ERROR,
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping(PathConstants.CONVERSIONS)
    public Page<Conversion> getConversationList(@Valid @RequestBody ConversionFilterReq conversionFilterReq, Pageable pageable) {
        return foreignExchangeService.listConversionsByFilter(conversionFilterReq, pageable);
    }
}
