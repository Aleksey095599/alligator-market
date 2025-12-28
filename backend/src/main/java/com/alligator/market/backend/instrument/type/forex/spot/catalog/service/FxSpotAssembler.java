package com.alligator.market.backend.instrument.type.forex.spot.catalog.service;

import com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto.in.FxSpotCreateDto;
import com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto.in.FxSpotUpdateDto;
import com.alligator.market.domain.instrument.code.InstrumentCode;
import com.alligator.market.domain.instrument.type.forex.currency.exception.CurrencyNotFoundException;
import com.alligator.market.domain.instrument.type.forex.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.currency.model.CurrencyCode;
import com.alligator.market.domain.instrument.type.forex.currency.repository.CurrencyRepository;
import com.alligator.market.domain.instrument.type.forex.spot.codec.FxSpotCodec;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Сборщик модели инструмента FX_SPOT.
 */
@Component
@RequiredArgsConstructor
public class FxSpotAssembler {

    private final CurrencyRepository currencyRepository;

    /**
     * Доменная модель существующего инструмента FX_SPOT из DTO создания.
     */
    public FxSpot toDomain(FxSpotCreateDto dto) {
        Objects.requireNonNull(dto, "dto must not be null");

        Currency base = currencyRepository.findByCode(CurrencyCode.of(dto.baseCurrency()))
                .orElseThrow(() -> new CurrencyNotFoundException(CurrencyCode.of(dto.baseCurrency())));
        Currency quote = currencyRepository.findByCode(CurrencyCode.of(dto.quoteCurrency()))
                .orElseThrow(() -> new CurrencyNotFoundException(CurrencyCode.of(dto.quoteCurrency())));

        return new FxSpot(base, quote, dto.tenor(), dto.defaultQuoteFractionDigits());
    }

    /**
     * Доменная модель существующего инструмента FX_SPOT из DTO обновления и строкового кода инструмента.
     */
    public FxSpot toDomainByCode(String instrumentCode, FxSpotUpdateDto dto) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(dto, "dto must not be null");

        // Парсим строковый код инструмента в объект-значение
        InstrumentCode code = InstrumentCode.of(instrumentCode);

        // Разбираем код инструмента на составные компоненты, необходимые для создания доменной модели
        FxSpotCodec.FxSpotCodeParts parts = FxSpotCodec.parseFxSpotCode(code);

        // Фиксируем коды базовой и котируемой валют
        CurrencyCode baseCode = parts.baseCode();
        CurrencyCode quoteCode = parts.quoteCode();

        // Проверяем наличие валют в репозитории
        Currency base = currencyRepository.findByCode(baseCode)
                .orElseThrow(() -> new CurrencyNotFoundException(baseCode));
        Currency quote = currencyRepository.findByCode(quoteCode)
                .orElseThrow(() -> new CurrencyNotFoundException(quoteCode));

        // Собираем и возвращаем доменную модель
        return new FxSpot(base, quote, parts.tenor(), dto.defaultQuoteFractionDigits());
    }
}
