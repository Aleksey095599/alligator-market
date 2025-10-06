package com.alligator.market.backend.instrument.type.forex.spot.catalog.service;

import com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto.in.FxSpotDto;
import com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto.in.FxSpotUpdateDto;
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

        String baseCode = parts.baseCode().value();
        String quoteCode = parts.quoteCode().value();

        Currency base = currencyRepository.findByCode(baseCode)
                .orElseThrow(() -> new FxSpotCurrencyNotFoundException(baseCode));
        Currency quote = currencyRepository.findByCode(quoteCode)
                .orElseThrow(() -> new FxSpotCurrencyNotFoundException(quoteCode));
        return new FxSpot(base, quote, parts.valueDate(), dto.quoteDecimal());
    }
}
