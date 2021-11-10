package com.atanriverdi.foreignexchange.service.impl;

import com.atanriverdi.foreignexchange.model.entity.Conversion;
import com.atanriverdi.foreignexchange.model.entity.dto.ConversionDTO;
import com.atanriverdi.foreignexchange.model.mapper.ConversionMapper;
import com.atanriverdi.foreignexchange.model.request.ConversionFilterReq;
import com.atanriverdi.foreignexchange.repository.ConversionRepository;
import com.atanriverdi.foreignexchange.service.ConversionService;
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
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ConversionServiceImplTest {

    @Mock
    ConversionRepository conversionRepository;

    @Mock
    ConversionMapper conversionMapper;

    @InjectMocks
    ConversionService conversionService = new ConversionServiceImpl();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save() {

        LocalDate now = LocalDate.now();
        String uuid = UUID.randomUUID().toString();

        ConversionDTO conversionDTO = new ConversionDTO();
        conversionDTO.setTargetAmount(new BigDecimal(124));
        conversionDTO.setAmount(new BigDecimal(10));
        conversionDTO.setSourceCurrencyCode("TRY");
        conversionDTO.setTargetCurrencyCode("EUR");

        Conversion conversion = new Conversion();
        conversion.setTargetAmount(new BigDecimal(124));
        conversion.setAmount(new BigDecimal(10));
        conversion.setSourceCurrencyCode("TRY");
        conversion.setTargetCurrencyCode("EUR");

        when(conversionMapper.dtoToEntity(conversionDTO)).thenReturn(conversion);

        when(conversionRepository.save(conversion)).thenReturn(conversion);

        Assertions.assertEquals(conversionDTO.getSourceCurrencyCode(), conversionService.save(conversionDTO).getSourceCurrencyCode());
        Assertions.assertEquals(conversionDTO.getTargetCurrencyCode(), conversionService.save(conversionDTO).getTargetCurrencyCode());
        Assertions.assertTrue(conversionDTO.getTargetAmount().compareTo(conversionService.save(conversionDTO).getTargetAmount()) == 0);

    }

    @Test
    void listConversionsByFilter() {

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

        ConversionFilterReq conversionFilterReq = new ConversionFilterReq();
        conversionFilterReq.setTransactionDate(LocalDate.now().toString());
        conversionFilterReq.setTransactionId(null);

        when(conversionRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Assertions.assertEquals(page.getContent().size(),
                conversionService.listConversionsByFilter(conversionFilterReq, paging).getContent().size());

    }
}