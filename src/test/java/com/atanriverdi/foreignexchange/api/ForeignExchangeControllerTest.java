package com.atanriverdi.foreignexchange.api;

import com.atanriverdi.foreignexchange.error.ErrorCodes;
import com.atanriverdi.foreignexchange.error.ExchangeException;
import com.atanriverdi.foreignexchange.model.request.RateReq;
import com.atanriverdi.foreignexchange.model.response.RateRes;
import com.atanriverdi.foreignexchange.service.ForeignExchangeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class ForeignExchangeControllerTest {

    @Mock
    ForeignExchangeService foreignExchangeService;

    @InjectMocks
    ForeignExchangeController foreignExchangeController;

    RateReq rateReq;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rateReq = new RateReq();
        rateReq.setSourceCurrency("EUR");
        rateReq.setTargetCurrency("TRY");

    }

    @Test
    void getExchangeRate() {

        RateRes rateRes = new RateRes("EUR/TRY", new BigDecimal("1.10"));

        when(foreignExchangeService.getRate(rateReq)).thenReturn(rateRes);
        ResponseEntity<RateRes> responseEntity = foreignExchangeController.getExchangeRate(rateReq);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void getExchangeRateFailure() {

        ExchangeException ex = new ExchangeException(ErrorCodes.ER01);
        doThrow(ex).when(foreignExchangeService).getRate(rateReq);
        assertThrows(ExchangeException.class, () -> foreignExchangeController.getExchangeRate(rateReq));

    }

    @Test
    void getConversation() {
    }

    @Test
    void getConversationList() {
    }
}