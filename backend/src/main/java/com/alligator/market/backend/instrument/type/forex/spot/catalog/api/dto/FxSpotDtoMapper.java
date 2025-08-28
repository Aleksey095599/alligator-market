package com.alligator.market.backend.instrument.type.forex.spot.catalog.api.dto;

import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.type.forex.spot.model.ValueDateCode;
import org.springframework.stereotype.Component;

/**
 * Маппер: модель ⇄ DTO.
 */
@Component
public class FxSpotDtoMapper {

    /** Преобразует основной DTO в доменную модель. */
    public FxSpot toDomain(FxSpotDto dto) {
        return new FxSpot(
                new Currency(dto.baseCurrency(), null, null, null),
                new Currency(dto.quoteCurrency(), null, null, null),
                dto.valueDateCode(),
                dto.quoteDecimal()
        );
    }

    /**
     * Преобразует код инструмента и DTO обновления в доменную модель.
     */
    public FxSpot toDomain(String code, FxSpotUpdateDto dto) {
        // Код инструмента однозначно связан с кодами базовой, котируемой валют и кодом даты валютирования
        String base = code.substring(0, 3);
        String quote = code.substring(3, 6);
        ValueDateCode valueDate = ValueDateCode.valueOf(code.substring(7));

        return new FxSpot(
                new Currency(base, null, null, null),
                new Currency(quote, null, null, null),
                valueDate,
                dto.quoteDecimal()
        );
    }

    /** Преобразует доменную модель в основной DTO. */
    public FxSpotDto toDto(FxSpot model) {
        return new FxSpotDto(
                model.base().code(),
                model.quote().code(),
                model.quoteDecimal(),
                model.valueDateCode()
        );
    }
}

