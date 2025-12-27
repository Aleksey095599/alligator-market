package com.alligator.market.backend.instrument.type.forex.currency.catalog.web.dto.out;

import com.alligator.market.backend.instrument.type.forex.currency.catalog.web.dto.common.CurrencyDto;
import lombok.NoArgsConstructor;

/**
 * DTO ответа для валют (out).
 */
@NoArgsConstructor
public class CurrencyResponseDto extends CurrencyDto {

    public CurrencyResponseDto(
            String code,
            String name,
            String country,
            Integer fractionDigits
    ) {
        super(code, name, country, fractionDigits);
    }
}
