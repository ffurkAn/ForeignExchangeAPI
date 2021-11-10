package com.atanriverdi.foreignexchange.specification;

import com.atanriverdi.foreignexchange.model.entity.Conversion;
import com.atanriverdi.foreignexchange.model.entity.Conversion_;
import com.atanriverdi.foreignexchange.model.request.ConversionFilterReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public interface ConversionSpecification {

    static Specification<Conversion> getConversionByTransactionId(String transactionId) {
        return (conversion, cq, cb) ->
                cb.equal(conversion.get(Conversion_.TRANSACTION_ID), transactionId);
    }

    static Specification<Conversion> getConversionByTransactionDate(LocalDate transactionDate) {
        return (conversion, cq, cb) ->
                cb.equal(conversion.get(Conversion_.TRANSACTION_DATE), transactionDate);
    }


    static Specification<Conversion> orderByTransactionDate() {
        return (conversion, cq, cb) -> {
            cq.orderBy(cb.asc(conversion.get(Conversion_.TRANSACTION_DATE)));
            return null;
        };
    }

    //1 = 1
    static Specification<Conversion> getDistinct() {
        return (conversion, cq, cb) -> {
            cq.distinct(true);
            return cb.conjunction();
        };
    }

    /**
     * Specification used to construct dynamic query based on JPA Criteria API.
     *
     * @param conversionFilterReq
     * @return
     */
    static Specification<Conversion> filter(ConversionFilterReq conversionFilterReq) {
        Specification<Conversion> specification = Specification.where(getDistinct());
        if (conversionFilterReq != null) {
            if (!StringUtils.isEmpty(conversionFilterReq.getTransactionId())) {
                specification = specification.and(getConversionByTransactionId(conversionFilterReq.getTransactionId()));
            }
            if (!StringUtils.isEmpty(conversionFilterReq.getTransactionDate())) {
                specification = specification.and(getConversionByTransactionDate(LocalDate.parse(conversionFilterReq.getTransactionDate())));
            }
        }
        specification = specification.and(orderByTransactionDate());

        return specification;
    }
}
