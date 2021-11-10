package com.atanriverdi.foreignexchange.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class ApplicationConfiguration {

    @Value("${frankfurter.url}")
    private String frankfurterUrl;

}
