package com.atanriverdi.foreignexchange;

import com.atanriverdi.foreignexchange.constant.PathConstants;
import com.atanriverdi.foreignexchange.logging.LogFilter;
import com.atanriverdi.foreignexchange.model.request.ConversionFilterReq;
import com.atanriverdi.foreignexchange.model.request.ConversionReq;
import com.atanriverdi.foreignexchange.model.request.RateReq;
import com.atanriverdi.foreignexchange.service.ForeignExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;
import org.springframework.web.client.RestTemplate;

import javax.servlet.Filter;
import java.time.Duration;

/**
 * Simple foreign exchange application which has the following functions:
 * - Exchange Rate API. To see {@link com.atanriverdi.foreignexchange.service.impl.ForeignExchangeServiceImpl#getRate(RateReq)}
 * - Conversion API. To see {@link com.atanriverdi.foreignexchange.service.impl.ForeignExchangeServiceImpl#getConversion(ConversionReq)}
 * - Conversion List API. To see {@link com.atanriverdi.foreignexchange.service.impl.ForeignExchangeServiceImpl#listConversionsByFilter(ConversionFilterReq, Pageable)}
 *
 * @author furkantanriverdi
 */
@SpringBootApplication
public class ForeignExchangeApplication {

    @Autowired
    ForeignExchangeService foreignExchangeService;

    public static void main(String[] args) {
        SpringApplication.run(ForeignExchangeApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.setConnectTimeout(Duration.ofSeconds(30))
                .setReadTimeout(Duration.ofSeconds(30))
//                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

    @Bean
    public FilterRegistrationBean<Filter> logFiler() {

        var logFilterBean = new FilterRegistrationBean<>();
        logFilterBean.setFilter(new LogFilter());

        logFilterBean.addUrlPatterns(PathConstants.API_CONTEXT);
        return logFilterBean;
    }

}
