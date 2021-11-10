package com.atanriverdi.foreignexchange.service.impl;

import com.atanriverdi.foreignexchange.config.ApplicationConfiguration;
import com.atanriverdi.foreignexchange.service.provider.ServiceProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service("default")
public class DefaultProvider implements ServiceProvider {

    @Override
    public String getRate(String sourceCurrencyCode, String targetCurrencyCode, String amount) {
        return "10.0000";
    }

    @Override
    public <T> T getInstance(ApplicationConfiguration applicationConfiguration, RestTemplate restTemplate) {
        return null;
    }
}
