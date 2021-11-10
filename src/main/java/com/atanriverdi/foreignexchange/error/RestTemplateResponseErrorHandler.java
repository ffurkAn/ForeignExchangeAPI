package com.atanriverdi.foreignexchange.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

@Slf4j
//@Component
public class RestTemplateResponseErrorHandler
        implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse)
            throws IOException {

        return (
                httpResponse.getStatusCode().series() == CLIENT_ERROR
                        || httpResponse.getStatusCode().series() == SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse)
            throws IOException {

        if (httpResponse.getStatusCode()  // handle 5xx codes
                .series() == HttpStatus.Series.SERVER_ERROR) {

            log.error("Server side error. ", httpResponse.getStatusText());
            throw new ExchangeException(ErrorCodes.ER02, httpResponse.getStatusCode().getReasonPhrase());

        } else if (httpResponse.getStatusCode() // handle 4xx codes
                .series() == HttpStatus.Series.CLIENT_ERROR) {
            log.error("Client side error. ", httpResponse.getStatusText());
            throw new ExchangeException(ErrorCodes.ER02, httpResponse.getStatusCode().getReasonPhrase());
        }
    }
}
