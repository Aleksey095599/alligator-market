package com.alligator.market.backend.instrument.asset.forex.fxspot.catalog.service;

import com.alligator.market.backend.instrument.asset.forex.fxspot.catalog.web.dto.in.FxSpotCreateDto;
import com.alligator.market.backend.instrument.asset.forex.fxspot.catalog.web.dto.in.FxSpotUpdateDto;
import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.exception.CurrencyNotFoundException;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.model.Currency;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.model.vo.CurrencyCode;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.repository.CurrencyRepository;
import com.alligator.market.domain.instrument.asset.forex.spot.codec.FxSpotCodec;
import com.alligator.market.domain.instrument.asset.forex.spot.model.FxSpot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Фабрика доменной модели FOREX_SPOT из входных DTO.
 *
 * <p>Не является "чистым преобразованием типов": методы обращаются к репозиторию с валютами {@link CurrencyRepository}
 * и используют доменную утилиту {@link FxSpotCodec}.</p>
 */
@Component
@RequiredArgsConstructor
public class FxSpotDomainFactory {

    private final CurrencyRepository currencyRepository;

    /**
     * DTO создания -> доменная модель инструмента FOREX_SPOT.
     *
     * <p>Загружает валюты из репозитория и возвращает полностью собранную доменную модель.</p>
     */
    public FxSpot fromCreateDto(FxSpotCreateDto dto) {
        Objects.requireNonNull(dto, "dto must not be null");

        // Обращаемся к репозиторию для получения валют
        Currency base = currencyRepository.findByCode(CurrencyCode.of(dto.baseCurrency()))
                .orElseThrow(() -> new CurrencyNotFoundException(CurrencyCode.of(dto.baseCurrency())));
        Currency quote = currencyRepository.findByCode(CurrencyCode.of(dto.quoteCurrency()))
                .orElseThrow(() -> new CurrencyNotFoundException(CurrencyCode.of(dto.quoteCurrency())));

        // Собираем и возвращаем доменную модель
        return new FxSpot(base, quote, dto.tenor(), dto.defaultQuoteFractionDigits());
    }

    /**
     * DTO обновления + код инструмента -> доменная модель инструмента FOREX_SPOT.
     *
     * <p>Парсит код инструмента с помощью {@link FxSpotCodec} для получения кодов валют,
     * загружает валюты из репозитория {@link CurrencyRepository} и собирает доменную модель.</p>
     */
    public FxSpot fromUpdateDto(InstrumentCode instrumentCode, FxSpotUpdateDto dto) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(dto, "dto must not be null");

        // Разбираем код инструмента на составные компоненты, чтобы получить коды валют
        FxSpotCodec.FxSpotCodeParts parts = FxSpotCodec.parseFxSpotCode(instrumentCode);
        CurrencyCode baseCode = parts.baseCode();
        CurrencyCode quoteCode = parts.quoteCode();

        // Обращаемся к репозиторию для получения валют
        Currency base = currencyRepository.findByCode(baseCode)
                .orElseThrow(() -> new CurrencyNotFoundException(baseCode));
        Currency quote = currencyRepository.findByCode(quoteCode)
                .orElseThrow(() -> new CurrencyNotFoundException(quoteCode));

        // Собираем и возвращаем доменную модель
        return new FxSpot(base, quote, parts.tenor(), dto.defaultQuoteFractionDigits());
    }
}
