package com.atanriverdi.foreignexchange.service;


import com.atanriverdi.foreignexchange.model.entity.Conversion;
import com.atanriverdi.foreignexchange.model.request.ConversionFilterReq;
import com.atanriverdi.foreignexchange.model.request.ConversionReq;
import com.atanriverdi.foreignexchange.model.request.RateReq;
import com.atanriverdi.foreignexchange.model.response.ConversionRes;
import com.atanriverdi.foreignexchange.model.response.RateRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ForeignExchangeService {

    /**
     * Returns conversion between currencies for given amount
     *
     * @param conversionReq {@link com.atanriverdi.foreignexchange.model.request.ConversionReq}
     * @return conversionRes {@link com.atanriverdi.foreignexchange.model.response.ConversionRes}
     */
    ConversionRes getConversion(ConversionReq conversionReq);

    /**
     * Returns rate of given currency pair
     *
     * @param rateReq {@link com.atanriverdi.foreignexchange.model.request.RateReq}
     * @return RateRes {@link com.atanriverdi.foreignexchange.model.response.RateRes}
     */
    RateRes getRate(RateReq rateReq);

    /**
     * Page of conversions filtered by given criteria
     *
     * @param conversionFilterReq {@link com.atanriverdi.foreignexchange.model.request.ConversionFilterReq}
     * @param pageable            {@link org.springframework.data.domain.Pageable}
     * @return Page of Conversions {@link com.atanriverdi.foreignexchange.model.entity.Conversion}
     */
    Page<Conversion> listConversionsByFilter(ConversionFilterReq conversionFilterReq, Pageable pageable);
}
