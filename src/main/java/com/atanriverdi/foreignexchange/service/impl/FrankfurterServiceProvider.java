package com.atanriverdi.foreignexchange.service.impl;

import com.atanriverdi.foreignexchange.config.ApplicationConfiguration;
import com.atanriverdi.foreignexchange.constant.Common;
import com.atanriverdi.foreignexchange.constant.PathConstants;
import com.atanriverdi.foreignexchange.error.ErrorCodes;
import com.atanriverdi.foreignexchange.error.ExchangeException;
import com.atanriverdi.foreignexchange.service.provider.ServiceProvider;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Currency;

@Slf4j
@NoArgsConstructor
public class FrankfurterServiceProvider implements ServiceProvider {

    private FrankfurterServiceProvider singletonProvider = null;
    private RestTemplate restTemplate;
    private ApplicationConfiguration applicationConfiguration;

    private FrankfurterServiceProvider(ApplicationConfiguration applicationConfiguration, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.applicationConfiguration = applicationConfiguration;
    }

    @Override
    public String getRate(String sourceCurrencyCode, String targetCurrencyCode, String amount) {
        try {
            log.info("Validation source currency: {}", sourceCurrencyCode);
            var sourceCurrency = Currency.getInstance(sourceCurrencyCode);
            log.info("Validation target currency: {}", targetCurrencyCode);
            var targetCurrency = Currency.getInstance(targetCurrencyCode);

            var urlBuilder = new StringBuilder();
            urlBuilder.append(applicationConfiguration.getFrankfurterUrl());
            urlBuilder.append(PathConstants.REQ_PARAM_FROM);
            urlBuilder.append(sourceCurrency);
            urlBuilder.append(PathConstants.REQ_PARAM_TO);
            urlBuilder.append(targetCurrency);
            urlBuilder.append(PathConstants.REQ_PARAM_AMOUNT);
            urlBuilder.append(amount);

            ResponseEntity<String> responseEntity = restTemplate.getForEntity(urlBuilder.toString(), String.class);
            var rateResponseObject = new JSONObject(responseEntity.getBody());

            return rateResponseObject.getJSONObject(Common.RATES).get(targetCurrencyCode).toString();
        } catch (IllegalArgumentException e) {
            log.error("Invalid currency code!");
            throw new ExchangeException(ErrorCodes.ER01);
        } catch (RestClientException e) {
            log.error("An unexpected error occurred while getting information from a remote server.", e);
            throw new ExchangeException(ErrorCodes.ER02);
        } catch (Exception e) {
            log.error("An unexpected error occurred.", e);
            throw new ExchangeException(ErrorCodes.HTTP_500);
        }
    }

    @Override
    public <T> T getInstance(ApplicationConfiguration applicationConfiguration, RestTemplate restTemplate) {
        if (singletonProvider == null)
            singletonProvider = new FrankfurterServiceProvider(applicationConfiguration, restTemplate);

        return (T) singletonProvider;
    }

}
