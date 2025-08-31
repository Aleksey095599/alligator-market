package com.alligator.market.backend.instrument.type.forex.spot.catalog.service;

import com.alligator.market.backend.instrument.type.forex.spot.catalog.api.dto.FxSpotDto;
import com.alligator.market.backend.instrument.type.forex.spot.catalog.api.dto.FxSpotUpdateDto;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.ref.currency.repository.CurrencyRepository;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotCurrencyNotFoundException;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Сборщик модели FX_SPOT из DTO.
 */
@Component
@RequiredArgsConstructor
public class FxSpotAssembler {

    private final CurrencyRepository currencyRepository;

    /** Преобразует основной DTO в доменную модель. */
    public FxSpot toDomain(FxSpotDto dto) {
        Currency base = currencyRepository.findByCode(dto.baseCurrency())
                .orElseThrow(() -> new FxSpotCurrencyNotFoundException(dto.baseCurrency()));
        Currency quote = currencyRepository.findByCode(dto.quoteCurrency())
                .orElseThrow(() -> new FxSpotCurrencyNotFoundException(dto.quoteCurrency()));
        return new FxSpot(base, quote, dto.valueDateCode(), dto.quoteDecimal());
    }

    /** Преобразует код инструмента и DTO для обновления в доменную модель. */
    public FxSpot toDomainByCode(String instrumentCode, FxSpotUpdateDto dto) {

    }

}
