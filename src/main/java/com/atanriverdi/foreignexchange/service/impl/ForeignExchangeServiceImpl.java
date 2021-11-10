package com.atanriverdi.foreignexchange.service.impl;

import com.atanriverdi.foreignexchange.config.ApplicationConfiguration;
import com.atanriverdi.foreignexchange.constant.Common;
import com.atanriverdi.foreignexchange.enums.ProviderType;
import com.atanriverdi.foreignexchange.model.entity.Conversion;
import com.atanriverdi.foreignexchange.model.entity.dto.ConversionDTO;
import com.atanriverdi.foreignexchange.model.mapper.ConversionMapper;
import com.atanriverdi.foreignexchange.model.request.ConversionFilterReq;
import com.atanriverdi.foreignexchange.model.request.ConversionReq;
import com.atanriverdi.foreignexchange.model.request.RateReq;
import com.atanriverdi.foreignexchange.model.response.ConversionRes;
import com.atanriverdi.foreignexchange.model.response.RateRes;
import com.atanriverdi.foreignexchange.service.ConversionService;
import com.atanriverdi.foreignexchange.service.ForeignExchangeService;
import com.atanriverdi.foreignexchange.service.provider.ServiceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Service
public class ForeignExchangeServiceImpl implements ForeignExchangeService {

    @Autowired
    ConversionService conversionService;

    @Autowired
    ConversionMapper conversionMapper;

    @Autowired
    ApplicationConfiguration applicationConfiguration;

    @Autowired
    RestTemplate restTemplate;

    /**
     * Fetch rates from service provider for given currencies and amount.
     *
     * @param sourceCurrencyCode
     * @param targetCurrencyCode
     * @param amount
     * @return
     */
    private String getRate(String sourceCurrencyCode, String targetCurrencyCode, String amount, ProviderType providerType) {
        return getProvider(providerType).getRate(sourceCurrencyCode, targetCurrencyCode, amount);
    }

    private ServiceProvider getProvider(ProviderType providerType) {

        try {
            Class<?> c = Class.forName(providerType.getValue());
            return ((ServiceProvider) c.newInstance()).getInstance(applicationConfiguration, restTemplate);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            log.error("Given provider type [{}] has no concrete class, so that default provider is being returned", providerType.getValue(), e);
            return new DefaultProvider();
        }

    }


    @Override
    public ConversionRes getConversion(ConversionReq conversionReq) {

        var calculatedRate = getRate(conversionReq.getSourceCurrency(), conversionReq.getTargetCurrency(),
                conversionReq.getSourceAmount(), ProviderType.FRANKFURTER);

        var conversionDTO = new ConversionDTO();
        conversionDTO.setAmount(new BigDecimal(conversionReq.getSourceAmount()));
        conversionDTO.setTargetAmount(new BigDecimal(calculatedRate));
        conversionDTO.setSourceCurrencyCode(conversionReq.getSourceCurrency());
        conversionDTO.setTargetCurrencyCode(conversionReq.getTargetCurrency());
        conversionDTO.setTransactionId(UUID.randomUUID().toString().replaceAll(Common.DASH, Common.EMPTY));
        conversionDTO.setTransactionDate(LocalDate.now());

        var conversion = conversionService.save(conversionDTO);
        return conversionMapper.entityToRes(conversion);
    }


    @Override
    public RateRes getRate(RateReq rateReq) {
        var rate = getRate(rateReq.getSourceCurrency(), rateReq.getTargetCurrency(), BigDecimal.ONE.toString(), ProviderType.FRANKFURTER);
        return new RateRes(rateReq.getSourceCurrency() + Common.SLASH + rateReq.getTargetCurrency(), new BigDecimal(rate));
    }


    @Override
    public Page<Conversion> listConversionsByFilter(ConversionFilterReq conversionFilterReq, Pageable pageable) {
        return conversionService.listConversionsByFilter(conversionFilterReq, pageable);
    }

}
