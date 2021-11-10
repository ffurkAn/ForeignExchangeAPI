package com.atanriverdi.foreignexchange.service.provider;

import com.atanriverdi.foreignexchange.config.ApplicationConfiguration;
import org.springframework.web.client.RestTemplate;

public interface ServiceProvider {

    String getRate(String sourceCurrencyCode, String targetCurrencyCode, String amount);

    <T> T getInstance(ApplicationConfiguration applicationConfiguration, RestTemplate restTemplate);

}
