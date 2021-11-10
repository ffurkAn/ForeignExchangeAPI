package com.atanriverdi.foreignexchange.service.impl;

import com.atanriverdi.foreignexchange.config.ApplicationConfiguration;
import com.atanriverdi.foreignexchange.error.ErrorCodes;
import com.atanriverdi.foreignexchange.error.ExchangeException;
import com.atanriverdi.foreignexchange.model.entity.Conversion;
import com.atanriverdi.foreignexchange.model.entity.dto.ConversionDTO;
import com.atanriverdi.foreignexchange.model.mapper.ConversionMapper;
import com.atanriverdi.foreignexchange.model.request.ConversionFilterReq;
import com.atanriverdi.foreignexchange.model.request.ConversionReq;
import com.atanriverdi.foreignexchange.model.request.RateReq;
import com.atanriverdi.foreignexchange.model.response.ConversionRes;
import com.atanriverdi.foreignexchange.repository.ConversionRepository;
import com.atanriverdi.foreignexchange.service.ConversionService;
import com.atanriverdi.foreignexchange.service.ForeignExchangeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

//@ExtendWith(SpringExtension.class)
//@SpringBootTest
class ForeignExchangeServiceImplTest {

    @Mock
    ConversionMapper conversionMapper;

    @Mock
    RestTemplate restTemplate;

    @Mock
    ApplicationConfiguration applicationConfiguration;

    @Mock
    ConversionService conversionService;

    @Mock
    ConversionRepository conversionRepository;

    @InjectMocks
    ForeignExchangeService foreignExchangeService = new ForeignExchangeServiceImpl();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(applicationConfiguration.getFrankfurterUrl()).thenReturn("https://api.frankfurter.app/latest");
    }

    @Test
    void testGetConversionSuccess() {
        ConversionReq conversionReq = new ConversionReq();
        conversionReq.setSourceAmount("25");
        conversionReq.setSourceCurrency("EUR");
        conversionReq.setTargetCurrency("TRY");

        ConversionDTO conversionDTO = new ConversionDTO();
        conversionDTO.setAmount(new BigDecimal(conversionReq.getSourceAmount()));
        conversionDTO.setTargetAmount(new BigDecimal("279.64"));
        conversionDTO.setSourceCurrencyCode(conversionReq.getSourceCurrency());
        conversionDTO.setTargetCurrencyCode(conversionReq.getTargetCurrency());

        Conversion conversion = new Conversion();
        conversion.setAmount(new BigDecimal(conversionReq.getSourceAmount()));
        conversion.setTargetAmount(new BigDecimal("279.64"));
        conversion.setSourceCurrencyCode(conversionReq.getSourceCurrency());
        conversion.setTargetCurrencyCode(conversionReq.getTargetCurrency());

        ConversionRes conversionRes = new ConversionRes();
        conversionRes.setTargetAmount(conversion.getTargetAmount().toString());
        conversionRes.setTransactionId(UUID.randomUUID().toString());

        String responseJsonStr = "{\"amount\":25.0,\"base\":\"EUR\",\"date\":\"2021-11-05\",\"rates\":{\"TRY\":279.64}}";
        when(restTemplate.getForEntity("https://api.frankfurter.app/latest?from=EUR&to=TRY&amount=25", String.class))
                .thenReturn(new ResponseEntity<>(responseJsonStr, HttpStatus.OK));


        when(conversionService.save(any())).thenReturn(conversion); // todo any()
        when(conversionMapper.entityToRes(conversion)).thenReturn(conversionRes); // todo any()


        Assertions.assertEquals("279.64", foreignExchangeService.getConversion(conversionReq).getTargetAmount());
    }

    @Test
    void testGetRateER01() {
        RateReq rateReq = new RateReq();
        rateReq.setSourceCurrency("EUR");
        rateReq.setTargetCurrency("UZA"); // Theres is no currency with code UZA

        Assertions.assertThrows(ExchangeException.class, () -> foreignExchangeService.getRate(rateReq));

    }

    @Test
    void testGetRateER02() {
        RateReq rateReq = new RateReq();
        rateReq.setSourceCurrency("EUR");
        rateReq.setTargetCurrency("TRY");

        doThrow(RestClientException.class).when(restTemplate).getForEntity(anyString(), any());

        try {
            foreignExchangeService.getRate(rateReq);
        } catch (ExchangeException e) {
            Assertions.assertEquals(ErrorCodes.ER02.getErrCode(), e.getCode());
        }

    }

    @Test
    void testGetRateSuccess() {
        RateReq rateReq = new RateReq();
        rateReq.setSourceCurrency("EUR");
        rateReq.setTargetCurrency("TRY");

        String responseJsonStr = "{\"amount\":10.0,\"base\":\"EUR\",\"date\":\"2021-11-05\",\"rates\":{\"TRY\":111.86}}";
        when(restTemplate.getForEntity("https://api.frankfurter.app/latest?from=EUR&to=TRY&amount=1", String.class))
                .thenReturn(new ResponseEntity<>(responseJsonStr, HttpStatus.OK));


        Assertions.assertEquals("111.86", foreignExchangeService.getRate(rateReq).getRate().toString());

    }

    @Test
    void listConversions() {

        Conversion c1 = new Conversion();
        c1.setSourceCurrencyCode("TRY");
        c1.setSourceCurrencyCode("USD");
        c1.setAmount(new BigDecimal("10"));
        c1.setTargetAmount(new BigDecimal("1.028"));

        Conversion c2 = new Conversion();
        c2.setSourceCurrencyCode("EUR");
        c2.setSourceCurrencyCode("USD");
        c2.setAmount(new BigDecimal("10"));
        c2.setTargetAmount(new BigDecimal("11.575"));

        Pageable paging = PageRequest.of(0, 2);

        Page<Conversion> page = new PageImpl<>(Arrays.asList(c1, c2), paging, 3);

        LocalDate now = LocalDate.now();

        ConversionFilterReq conversionFilterReq = new ConversionFilterReq();
        conversionFilterReq.setTransactionDate(now.toString());
        conversionFilterReq.setTransactionId(null);

        when(conversionService.listConversionsByFilter(conversionFilterReq, paging)).thenReturn(page);

        Assertions.assertEquals(page.getContent().size(),
                foreignExchangeService.listConversionsByFilter(conversionFilterReq, paging).getContent().size());

    }
}