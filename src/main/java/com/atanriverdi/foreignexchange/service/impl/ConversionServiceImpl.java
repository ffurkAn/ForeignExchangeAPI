package com.atanriverdi.foreignexchange.service.impl;

import com.atanriverdi.foreignexchange.constant.Common;
import com.atanriverdi.foreignexchange.model.entity.Conversion;
import com.atanriverdi.foreignexchange.model.entity.dto.ConversionDTO;
import com.atanriverdi.foreignexchange.model.mapper.ConversionMapper;
import com.atanriverdi.foreignexchange.model.request.ConversionFilterReq;
import com.atanriverdi.foreignexchange.repository.ConversionRepository;
import com.atanriverdi.foreignexchange.service.ConversionService;
import com.atanriverdi.foreignexchange.specification.ConversionSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ConversionServiceImpl implements ConversionService {

    @Autowired
    ConversionRepository conversionRepository;

    @Autowired
    ConversionMapper conversionMapper;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Conversion save(ConversionDTO conversionDTO) {
        return conversionRepository.save(conversionMapper.dtoToEntity(conversionDTO));
    }


    @Override
    public Page<Conversion> listConversionsByFilter(ConversionFilterReq conversionFilterReq, Pageable pageable) {
        return conversionRepository.findAll(ConversionSpecification.filter(conversionFilterReq), pageable);
    }

    /**
     * Generates 1000 records of conversion for test purposes.
     */
    @PostConstruct
    public void init() {

        List<Conversion> conversionList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {

            // H2 precision ,2 olduğu için 2 kullanılmıştır.
            var randomRate = BigDecimal.valueOf(RandomUtils.nextDouble(1.00, 99.00)).setScale(2, RoundingMode.HALF_DOWN);

            var sourceCurrencyCode = getRandomCurrencyCode();
            var targetCurrencyCode = getRandomCurrencyCode();
            while (sourceCurrencyCode.equals(targetCurrencyCode)) {
                targetCurrencyCode = getRandomCurrencyCode();
            }

            var c = new Conversion();
            c.setAmount(new BigDecimal(RandomUtils.nextInt(10, 10000)));
            c.setSourceCurrencyCode(sourceCurrencyCode);
            c.setTargetCurrencyCode(targetCurrencyCode);
            c.setTargetAmount(c.getAmount().multiply(randomRate));
            c.setTransactionId(UUID.randomUUID().toString().replaceAll(Common.DASH, Common.EMPTY));
            c.setTransactionDate(getRandomDate());

            conversionList.add(c);
        }

        conversionRepository.saveAll(conversionList);
    }

    /**
     * Returns LocalDate of one of the following 50 days or past 50 days randomly
     *
     * @return
     */
    private LocalDate getRandomDate() {

        var isForward = RandomUtils.nextBoolean();
        var days = RandomUtils.nextLong(0L, 50L);

        return isForward ? LocalDate.now().plusDays(days) : LocalDate.now().minusDays(days);
    }

    /**
     * Returns random currency code from specified list
     *
     * @return
     */
    private String getRandomCurrencyCode() {

        List<String> currencyCodeList = Arrays.asList("AUD",
                "BGN",
                "BRL",
                "CAD",
                "CHF",
                "CNY",
                "CZK",
                "DKK",
                "EUR",
                "GBP",
                "HKD",
                "HRK",
                "HUF",
                "IDR",
                "ILS",
                "INR",
                "ISK",
                "JPY",
                "KRW",
                "MXN",
                "MYR",
                "NOK",
                "NZD",
                "PHP",
                "PLN",
                "RON",
                "RUB",
                "SEK",
                "SGD",
                "THB",
                "TRY",
                "USD",
                "ZAR");

        var random = RandomUtils.nextInt(0, currencyCodeList.size());

        return currencyCodeList.get(random);
    }
}
