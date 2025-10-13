package com.alligator.market.backend.instrument.type.forex.spot.catalog.service;

import com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto.in.FxSpotDto;
import com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto.in.FxSpotUpdateDto;
import com.alligator.market.domain.instrument.type.forex.ref.currency.exception.CurrencyNotFoundException;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;
import com.alligator.market.domain.instrument.type.forex.ref.currency.repository.CurrencyRepository;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.type.forex.spot.codec.FxSpotCodec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Сборщик модели FX_SPOT из DTO.
 */
@Component
@RequiredArgsConstructor
public class FxSpotAssembler {

    private final CurrencyRepository currencyRepository;

    /** Основной DTO ⇒ доменная модель. */
    public FxSpot toDomain(FxSpotDto dto) {
        Objects.requireNonNull(dto, "dto must not be null");

        Currency base = currencyRepository.findByCode(CurrencyCode.of(dto.baseCurrency()))
                .orElseThrow(() -> new CurrencyNotFoundException(CurrencyCode.of(dto.baseCurrency())));
        Currency quote = currencyRepository.findByCode(CurrencyCode.of(dto.quoteCurrency()))
                .orElseThrow(() -> new CurrencyNotFoundException(CurrencyCode.of(dto.quoteCurrency())));

        return new FxSpot(base, quote, dto.valueDate(), dto.defaultQuoteFractionDigits());
    }

    /** Код инструмента + DTO обновления ⇒ доменная модель. */
    public FxSpot toDomainByCode(String instrumentCode, FxSpotUpdateDto dto) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(dto, "dto must not be null");

        FxSpotCodec.FxSpotCodeParts parts = FxSpotCodec.parseFxSpotCode(instrumentCode);

        String baseCode = parts.baseCode().value();
        String quoteCode = parts.quoteCode().value();

        Currency base = currencyRepository.findByCode(CurrencyCode.of(baseCode))
                .orElseThrow(() -> new CurrencyNotFoundException(CurrencyCode.of(baseCode)));
        Currency quote = currencyRepository.findByCode(CurrencyCode.of(quoteCode))
                .orElseThrow(() -> new CurrencyNotFoundException(CurrencyCode.of(quoteCode)));
        return new FxSpot(base, quote, parts.valueDate(), dto.defaultQuoteFractionDigits());
    }
}
