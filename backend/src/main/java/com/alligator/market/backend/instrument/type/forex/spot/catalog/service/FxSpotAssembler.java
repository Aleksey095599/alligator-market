package com.alligator.market.backend.instrument.type.forex.spot.catalog.service;

import com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto.FxSpotDto;
import com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto.FxSpotUpdateDto;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.ref.currency.repository.CurrencyRepository;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotCurrencyNotFoundException;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.type.forex.spot.model.ValueDateCode;
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
        Currency base = currencyRepository.findByCode(dto.baseCurrency())
                .orElseThrow(() -> new FxSpotCurrencyNotFoundException(dto.baseCurrency()));
        Currency quote = currencyRepository.findByCode(dto.quoteCurrency())
                .orElseThrow(() -> new FxSpotCurrencyNotFoundException(dto.quoteCurrency()));
        return new FxSpot(base, quote, dto.valueDateCode(), dto.quoteDecimal());
    }

    /** Код инструмента + DTO обновления ⇒ доменная модель. */
    public FxSpot toDomainByCode(String instrumentCode, FxSpotUpdateDto dto) {
        Objects.requireNonNull(instrumentCode, "Instrument code must not be null");

        String prefix = InstrumentType.FX_SPOT + "_";
        if (!instrumentCode.startsWith(prefix)) {
            throw new IllegalArgumentException("Instrument code must start with " + prefix);
        }

        // Отбрасываем префикс типа инструмента и разделяем символ и дату валютирования
        String[] parts = instrumentCode.substring(prefix.length()).split("_");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Instrument code must contain currency pair and value date");
        }

        String symbol = parts[0];
        if (symbol.length() != 6) {
            throw new IllegalArgumentException("Currency pair must contain 6 characters");
        }
        String baseCode = symbol.substring(0, 3);
        String quoteCode = symbol.substring(3, 6);

        ValueDateCode valueDateCode;
        try {
            valueDateCode = ValueDateCode.valueOf(parts[1]);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unsupported value date code: " + parts[1], ex);
        }

        Currency base = currencyRepository.findByCode(baseCode)
                .orElseThrow(() -> new FxSpotCurrencyNotFoundException(baseCode));
        Currency quote = currencyRepository.findByCode(quoteCode)
                .orElseThrow(() -> new FxSpotCurrencyNotFoundException(quoteCode));

        return new FxSpot(base, quote, valueDateCode, dto.quoteDecimal());
    }

}
