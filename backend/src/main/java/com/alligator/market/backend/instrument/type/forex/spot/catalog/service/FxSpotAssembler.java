package com.alligator.market.backend.instrument.type.forex.spot.catalog.service;

import com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto.FxSpotDto;
import com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto.FxSpotUpdateDto;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.ref.currency.repository.CurrencyRepository;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotCurrencyNotFoundException;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.type.forex.spot.codec.FxSpotCodec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Сборщик модели FX_SPOT из DTO.
 */
@Component
@RequiredArgsConstructor
public class FxSpotAssembler {

    private final CurrencyRepository currencyRepository;

    /** Основной DTO ⇒ доменная модель. */
    public FxSpot toDomain(FxSpotDto dto) {
        Currency base = currencyRepository.findByCode(dto.baseCurrency())
                .orElseThrow(() -> new FxSpotCurrencyNotFoundException(dto.baseCurrency()));
        Currency quote = currencyRepository.findByCode(dto.quoteCurrency())
                .orElseThrow(() -> new FxSpotCurrencyNotFoundException(dto.quoteCurrency()));
        return new FxSpot(base, quote, dto.valueDateCode(), dto.quoteDecimal());
    }

    /** Код инструмента + DTO обновления ⇒ доменная модель. */
    public FxSpot toDomainByCode(String instrumentCode, FxSpotUpdateDto dto) {
        FxSpotCodec.FxSpotCodeParts parts = FxSpotCodec.parseFxSpotCode(instrumentCode);

        Currency base = currencyRepository.findByCode(parts.baseCode().toString())
                .orElseThrow(() -> new FxSpotCurrencyNotFoundException(parts.baseCode().toString()));
        Currency quote = currencyRepository.findByCode(parts.quoteCode().toString())
                .orElseThrow(() -> new FxSpotCurrencyNotFoundException(parts.quoteCode().value()));
        return new FxSpot(base, quote, parts.valueDate(), dto.quoteDecimal());
    }
}
