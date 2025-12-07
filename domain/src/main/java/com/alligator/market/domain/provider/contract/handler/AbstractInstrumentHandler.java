package com.alligator.market.domain.provider.contract.handler;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.exception.InstrumentNotSupportedException;
import com.alligator.market.domain.provider.exception.InstrumentWrongClassException;
import com.alligator.market.domain.provider.exception.InstrumentWrongTypeException;
import com.alligator.market.domain.quote.tick.model.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Абстрактный каркас контракта обработчика инструмента {@link InstrumentHandler}.
 */
public abstract non-sealed class AbstractInstrumentHandler<P extends MarketDataProvider, I extends Instrument>
        implements InstrumentHandler<P, I> {

    /* Нормализованный код обработчика: UPPERCASE, формат [A-Z0-9_]+. */
    private final String normHandlerCode;

    /* Декларируемый класс поддерживаемых инструментов. */
    private final Class<I> instrumentClass;

    /* Декларируемый тип поддерживаемых инструментов. */
    private final InstrumentType instrumentType;

    /* Нормализованные и неизменяемые коды поддерживаемых инструментов: UPPERCASE, формат [A-Z0-9_]+. */
    private final Set<String> normSupportedInstrumentCodes;

    /* Ссылка на провайдера (присваивается один раз, видимость между потоками гарантируется volatile). */
    private volatile P provider;

    //=================================================================================================================
    // КОНСТРУКТОР
    //=================================================================================================================

    /**
     * Конструктор с проверками.
     *
     * <p>Проверяет инварианты, нормализует код обработчика, нормализует коды в наборе кодов инструментов.
     *
     * @param handlerCode              код обработчика; нормализуется в UPPERCASE; формат [A-Z0-9_]+
     * @param instrumentClass          класс поддерживаемых инструментов
     * @param instrumentType           тип поддерживаемых инструментов
     * @param supportedInstrumentCodes набор кодов инструментов; нормализуются в UPPERCASE; формат [A-Z0-9_]+; без дублей
     * @throws NullPointerException     если любой параметр равен null
     * @throws IllegalArgumentException если код пустой/с пробелами/не соответствует формату; набор пуст; содержит null/blank/дубликаты
     */
    protected AbstractInstrumentHandler(
            String handlerCode,
            Class<I> instrumentClass,
            InstrumentType instrumentType,
            Set<String> supportedInstrumentCodes
    ) {
        Objects.requireNonNull(handlerCode, "handlerCode must not be null");
        Objects.requireNonNull(instrumentClass, "instrumentClass must not be null");
        Objects.requireNonNull(instrumentType, "instrumentType must not be null");
        Objects.requireNonNull(supportedInstrumentCodes, "supportedInstrumentCodes must not be null");

        if (handlerCode.isBlank()) {
            throw new IllegalArgumentException("handlerCode must not be blank");
        }
        if (supportedInstrumentCodes.isEmpty()) {
            throw new IllegalArgumentException("supportedInstrumentCodes must not be empty");
        }

        this.normHandlerCode = normalizeHandlerCode(handlerCode);
        this.instrumentClass = instrumentClass;
        this.instrumentType = instrumentType;
        this.normSupportedInstrumentCodes = getNormalizedCodes(supportedInstrumentCodes);
    }

    //=================================================================================================================
    // РЕАЛИЗАЦИЯ МЕТОДОВ КОНТРАКТА
    //=================================================================================================================

    /**
     * Уникальный код обработчика: UPPERCASE, формат [A-Z0-9_]+.
     */
    @Override
    public final String handlerCode() {
        return normHandlerCode;
    }

    /**
     * Декларируем класс поддерживаемых инструментов.
     */
    @Override
    public Class<I> instrumentClass() {
        return instrumentClass;
    }

    /**
     * Декларируемый тип поддерживаемых инструментов.
     */
    @Override
    public final InstrumentType instrumentType() {
        return instrumentType;
    }

    /**
     * Неизменяемый набор поддерживаемых кодов инструментов: UPPERCASE, формат [A-Z0-9_]+.
     */
    @Override
    public final Set<String> supportedInstrumentCodes() {
        return normSupportedInstrumentCodes;
    }

    /**
     * Однократное прикрепление обработчика к провайдеру.
     *
     * @throws IllegalStateException если провайдер уже прикреплён
     * @throws NullPointerException  если provider == null
     */
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
     * <p>Содержит ряд базовых проверок инструмента и факт прикрепления обработчика к провайдеру. Далее вызывается
     * чистая логика получения котировки от провайдера {@link #doQuote(Instrument)}.
     *
     * @param instrument инструмент, для которого требуется котировка
     * @return поток котировок
     * @throws NullPointerException            если instrument == null
     * @throws IllegalStateException           если провайдер не прикреплён
     * @throws InstrumentWrongClassException   если класс инструмента не соответствует {@link #instrumentClass()}
     * @throws InstrumentWrongTypeException    если тип инструмента не соответствует {@link #instrumentType()}
     * @throws IllegalArgumentException        если instrumentCode null или пустой
     * @throws InstrumentNotSupportedException если код инструмента не входит в поддерживаемый набор
     */
    @Override
    public final Publisher<QuoteTick> quote(I instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");

        // 1) Проверяем, что провайдер прикреплен к данному обработчику
        P currentProvider = provider;
        if (currentProvider == null) {
            throw new IllegalStateException("Provider is not attached");
        }

        // 2) Проверяем, что класс инструмента соответствует ожиданиям обработчика
        if (!instrumentClass.isInstance(instrument)) {
            throw new InstrumentWrongClassException( // <-- Неверный класс
                    instrument.instrumentCode(),
                    instrument.getClass(),
                    normHandlerCode,
                    instrumentClass
            );
        }

        // 3) Проверяем, что тип инструмента соответствует ожиданиям обработчика
        if (instrument.instrumentType() != instrumentType) {
            throw new InstrumentWrongTypeException( // <-- Неверный тип
                    instrument.instrumentCode(),
                    instrument.instrumentType(),
                    normHandlerCode,
                    instrumentType
            );
        }

        // 4) Проверяем, что код инструмента поддерживается обработчиком
        var rawCode = instrument.instrumentCode();
        if (rawCode == null || rawCode.isBlank()) {
            throw new IllegalArgumentException("instrumentCode must not be null or blank");
        }
        var normCode = rawCode.trim().toUpperCase(java.util.Locale.ROOT); // <-- Нормализуем код
        if (!normSupportedInstrumentCodes.contains(normCode)) {
            throw new InstrumentNotSupportedException( // <-- Не поддерживается
                    instrument.instrumentCode(),
                    normHandlerCode);
        }
        return doQuote(instrument);
    }

    //=================================================================================================================
    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
    //=================================================================================================================

    /**
     * Чистая логика получения котировки для переданного инструмента.
     *
     * <p>Вызывается исключительно финальной реализацией {@link #quote(Instrument)} этого класса,
     * в котором выполнены все нужные проверки.
     */
    protected abstract Publisher<QuoteTick> doQuote(I instrument);

    /**
     * Полезный геттер для наследников: возвращает прикреплённого провайдера.
     */
    protected final P provider() {
        P current = provider;
        if (current == null) {
            throw new IllegalStateException("Provider is not attached");
        }
        return current;
    }

    //=================================================================================================================
    // УТИЛИТЫ
    //=================================================================================================================

    /**
     * Нормализует и валидирует набор кодов инструментов: trim --> UPPERCASE --> проверка формата [A-Z0-9_]+ --> проверка на дубли.
     *
     * @throws IllegalArgumentException если в наборе есть null/blank/неверный формат/дубликаты
     */
    private static Set<String> getNormalizedCodes(Set<String> supportedInstrumentCodes) {
        var codes = new LinkedHashSet<String>();
        for (String code : supportedInstrumentCodes) {
            // Набор не должен содержать null или пустые коды инструментов
            if (code == null || code.isBlank()) {
                throw new IllegalArgumentException("supportedInstrumentCodes must not contain null/blank");
            }
            var norm = code.trim().toUpperCase(java.util.Locale.ROOT);
            // Разрешены только латинские заглавные, цифры и подчёркивание
            if (!norm.chars().allMatch(ch ->
                    (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9') || ch == '_')) {
                throw new IllegalArgumentException("Instrument code must match [A-Z0-9_]+: '" + norm + "'");
            }
            if (!codes.add(norm)) {
                // Набор не должен содержать дублирующиеся коды инструментов
                throw new IllegalArgumentException("Duplicate instrumentCode '" + norm + "' in supportedInstrumentCodes");
            }
        }
        return java.util.Collections.unmodifiableSet(codes); // <-- Фиксируем неизменяемость
    }

    /**
     * Нормализует и валидирует код обработчика: trim --> UPPERCASE --> проверка формата [A-Z0-9_]+.
     *
     * @throws IllegalArgumentException если код обработчика не соответствует формату
     */
    private static String normalizeHandlerCode(String code) {
        // Нормализуем код обработчика
        var normalized = code.trim().toUpperCase(java.util.Locale.ROOT);
        // Разрешены только латинские заглавные, цифры и подчёркивание
        if (!normalized.chars().allMatch(ch ->
                (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9') || ch == '_')) {
            throw new IllegalArgumentException(
                    "handlerCode must match pattern [A-Z0-9_]+: '" + normalized + "'");
        }
        return normalized;
    }
}
