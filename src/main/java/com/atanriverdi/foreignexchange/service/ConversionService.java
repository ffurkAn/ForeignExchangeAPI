package com.atanriverdi.foreignexchange.service;

import com.atanriverdi.foreignexchange.model.entity.Conversion;
import com.atanriverdi.foreignexchange.model.entity.dto.ConversionDTO;
import com.atanriverdi.foreignexchange.model.request.ConversionFilterReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConversionService {

    /**
     * Conversion is saved over new transaction since rate had already calculated
     *
     * @param conversionDTO {@link com.atanriverdi.foreignexchange.model.entity.dto.ConversionDTO}
     * @return Saved Conversion {@link com.atanriverdi.foreignexchange.model.entity.Conversion}
     */
    Conversion save(ConversionDTO conversionDTO);

    /**
     * Returns page of conversions by given input
     *
     * @param conversionFilterReq {@link com.atanriverdi.foreignexchange.model.request.ConversionFilterReq}
     * @param pageable            {@link org.springframework.data.domain.Pageable}
     * @return Page of Conversions {@link com.atanriverdi.foreignexchange.model.entity.Conversion}
     */
    Page<Conversion> listConversionsByFilter(ConversionFilterReq conversionFilterReq, Pageable pageable);

}
