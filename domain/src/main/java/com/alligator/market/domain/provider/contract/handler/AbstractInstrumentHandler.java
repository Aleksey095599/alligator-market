package com.alligator.market.domain.provider.contract.handler;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.exception.InstrumentNotSupportedException;
import com.alligator.market.domain.provider.exception.InstrumentWrongClassException;
import com.alligator.market.domain.quote.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Абстрактный каркас обработчика инструмента.
 */
public abstract non-sealed class AbstractInstrumentHandler<P extends MarketDataProvider, I extends Instrument>
        implements InstrumentHandler<P, I> {

    /* ↓↓ Базовые атрибуты обработчика. */
    private final String nHandlerCode;
    private final Class<I> instrumentClass;
    private final InstrumentType instrumentType;

    /* Нормализованные (UPPERCASE) и неизменяемые коды поддерживаемых инструментов. */
    private final Set<String> nSupportedInstrumentCodes;

    /* Ссылка на провайдера (volatile гарантирует видимость присвоения между потоками). */
    private volatile P provider;

    /** Конструктор. */
    protected AbstractInstrumentHandler(
            String handlerCode,
            Class<I> instrumentClass,
            InstrumentType instrumentType,
            Set<String> supportedInstrumentCodes
    ) {
        Objects.requireNonNull(handlerCode,"handlerCode must not be null");
        Objects.requireNonNull(instrumentClass, "instrumentClass must not be null");
        Objects.requireNonNull(instrumentType, "instrumentType must not be null");
        Objects.requireNonNull(supportedInstrumentCodes, "supportedInstrumentCodes must not be null");

        if (handlerCode.isBlank()) {
            throw new IllegalArgumentException("handlerCode must not be blank");
        }
        if (supportedInstrumentCodes.isEmpty()) {
            throw new IllegalArgumentException("supportedInstrumentCodes must not be empty");
        }

        this.nHandlerCode = normalizeCode(handlerCode);
        this.instrumentClass = instrumentClass;
        this.instrumentType = instrumentType;
        this.nSupportedInstrumentCodes = getNormalizedCodes(supportedInstrumentCodes);
    }

    /** Уникальный код обработчика (для логов/метрик). */
    @Override public final String handlerCode() { return nHandlerCode; }

    /** Декларируем класс поддерживаемых инструментов. */
    @Override public Class<I> instrumentClass() { return instrumentClass; }

    /** Тип поддерживаемых инструментов. */
    @Override public final InstrumentType instrumentType() { return instrumentType; }

    /** Набор кодов инструментов, которые поддерживает обработчик (UPPERCASE, неизменяемый). */
    @Override public final Set<String> supportedInstrumentCodes() { return nSupportedInstrumentCodes; }

    /** Прикрепить обработчик к заданному провайдеру. */
    @Override
    public final void attachTo(P provider) {
        if (this.provider != null) {
            throw new IllegalStateException("Provider is already attached");
        }
        this.provider = Objects.requireNonNull(provider, "provider must not be null");
    }

    /**
     * Котировка заданного инструмента.
     *
     * @throws IllegalStateException           если нет ссылки на провайдера
     * @throws InstrumentWrongClassException   если класс инструмента не соответствует {@code instrumentClass}
     * @throws InstrumentWrongTypeException    если тип инструмента не соответствует {@code instrumentType}
     * @throws InstrumentNotSupportedException если инструмент не содержится в наборе {@code nSupportedInstrumentCodes}
     */
    @Override
    public final Publisher<QuoteTick> quote(I instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");

        P currentProvider = provider;
        if (currentProvider == null) {
            throw new IllegalStateException("There is null link to provider");
        }

        // ↓↓ Проверки инструмента
        if (!instrumentClass.isInstance(instrument)) {
            throw new InstrumentWrongClassException(
                    instrument.instrumentCode(),
                    instrument.getClass(),
                    nHandlerCode,
                    instrumentClass
            );
        }
        if (instrument.instrumentType() != instrumentType) {
            throw new InstrumentWrongTypeException(
                    instrument.instrumentCode(),
                    instrument.instrumentType(),
                    nHandlerCode,
                    instrumentType
            );
        }
        var codeUpper = instrument.instrumentCode().toUpperCase(java.util.Locale.ROOT);
        if (!nSupportedInstrumentCodes.contains(codeUpper)) {
            throw new InstrumentNotSupportedException(instrument.instrumentCode(), nHandlerCode);
        }
        return doQuote(instrument);
    }

    /** Реализация получения котировки. */
    protected abstract Publisher<QuoteTick> doQuote(I instrument);



    //=================================================================================================================
    //                                          Вспомогательные методы
    //=================================================================================================================

    /** Получаем набор нормализованных кодов инструментов (trim + upper case). */
    private static LinkedHashSet<String> getNormalizedCodes(Set<String> supportedInstrumentCodes) {
        var codes = new LinkedHashSet<String>();
        for (String code : supportedInstrumentCodes) {
            if (code == null || code.isBlank()) {
                throw new IllegalStateException("supportedInstrumentCodes must not contain null/blank");
            }
            var upper = code.trim().toUpperCase(java.util.Locale.ROOT);
            if (!codes.add(upper)) {
                throw new IllegalStateException("Duplicate instrumentCode '" + upper + "' in supportedInstrumentCodes");
            }
        }
        return codes;
    }

    /** Нормализовать код: trim + upper case. */
    private static String normalizeCode(String code) {
        return code.trim().toUpperCase(java.util.Locale.ROOT);
    }
}
