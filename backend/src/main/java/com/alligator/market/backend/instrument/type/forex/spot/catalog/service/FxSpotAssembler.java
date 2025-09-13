package com.alligator.market.backend.instrument.type.forex.spot.catalog.service;

import com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto.FxSpotDto;
import com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto.FxSpotUpdateDto;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.ref.currency.repository.CurrencyRepository;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotCurrencyNotFoundException;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.type.forex.spot.model.ValueDateCode;
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
        // Разделяем код на валюты и дату валютирования
        String[] parts = instrumentCode.split("_");
        String pair = parts[0];
        String baseCode = pair.substring(0, 3);
        String quoteCode = pair.substring(3);
        ValueDateCode valueDateCode = ValueDateCode.valueOf(parts[1]);

        Currency base = currencyRepository.findByCode(baseCode)
                .orElseThrow(() -> new FxSpotCurrencyNotFoundException(baseCode));
        Currency quote = currencyRepository.findByCode(quoteCode)
                .orElseThrow(() -> new FxSpotCurrencyNotFoundException(quoteCode));

        return new FxSpot(base, quote, valueDateCode, dto.quoteDecimal());
    }

}
