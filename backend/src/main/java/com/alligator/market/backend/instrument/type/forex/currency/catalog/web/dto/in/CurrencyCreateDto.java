package com.alligator.market.backend.instrument.type.forex.currency.catalog.web.dto.in;

import com.alligator.market.backend.instrument.type.forex.currency.catalog.web.dto.common.CurrencyDto;
import lombok.NoArgsConstructor;

/**
 * DTO создания валюты (in).
 */
@NoArgsConstructor
public class CurrencyCreateDto extends CurrencyDto {

    public CurrencyCreateDto(
            String code,
            String name,
            String country,
            Integer fractionDigits
    ) {
        super(code, name, country, fractionDigits);
    }
}
