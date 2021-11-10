package com.atanriverdi.foreignexchange.model.mapper;

import com.atanriverdi.foreignexchange.model.entity.Conversion;
import com.atanriverdi.foreignexchange.model.entity.dto.ConversionDTO;
import com.atanriverdi.foreignexchange.model.response.ConversionRes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.text.NumberFormat;

@Mapper(componentModel = "spring")
public interface ConversionMapper {

    Conversion dtoToEntity(ConversionDTO conversionDTO);

    @Mapping(target = "targetAmount", expression = "java(getFormattedAmount(conversion.getTargetAmount()))")
    ConversionRes entityToRes(Conversion conversion);


    default String getFormattedAmount(BigDecimal targetAmount) { // todo why default
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(4);
        return numberFormat.format(targetAmount);
    }

}
