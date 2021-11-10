package com.atanriverdi.foreignexchange.service.impl;

import com.atanriverdi.foreignexchange.config.ApplicationConfiguration;
import com.atanriverdi.foreignexchange.service.provider.ServiceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service("forexio")
public class ForexioServiceProvider implements ServiceProvider {

    @Override
    public String getRate(String sourceCurrencyCode, String targetCurrencyCode, String amount) {
        return null;
    }

    @Override
    public <T> T getInstance(ApplicationConfiguration applicationConfiguration, RestTemplate restTemplate) {
        return null;
    }

}
