package com.alligator.market.domain.provider.model.handler;

import com.alligator.market.domain.instrument.model.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.provider.exception.InstrumentNotSupportedException;
import com.alligator.market.domain.provider.exception.InstrumentWrongClassException;
import com.alligator.market.domain.provider.exception.InstrumentWrongTypeException;
import com.alligator.market.domain.provider.model.MarketDataProvider;
import com.alligator.market.domain.provider.model.vo.HandlerCode;
import com.alligator.market.domain.quote.tick.model.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Абстрактная реализация обработчика инструмента {@link InstrumentHandler}.
 */
public abstract non-sealed class AbstractInstrumentHandler<P extends MarketDataProvider, I extends Instrument>
        implements InstrumentHandler<P, I> {

    /* Код обработчика. */
    private final HandlerCode handlerCode;

    /* Класс поддерживаемых инструментов. */
    private final Class<I> instrumentClass;

    /* Тип поддерживаемых инструментов. */
    private final InstrumentType instrumentType;

    /* Коды поддерживаемых инструментов. */
    private final Set<InstrumentCode> supportedInstrumentCodes;

    /* Ссылка на провайдера (однократная потокобезопасная привязка). */
    private final java.util.concurrent.atomic.AtomicReference<P> providerRef =
            new java.util.concurrent.atomic.AtomicReference<>();

    //=================================================================================================================
    // КОНСТРУКТОР
    //=================================================================================================================

    /**
     * Конструктор с базовыми проверками.
     */
    protected AbstractInstrumentHandler(
            HandlerCode handlerCode,
            Class<I> instrumentClass,
            InstrumentType instrumentType,
            Set<InstrumentCode> supportedInstrumentCodes
    ) {
        Objects.requireNonNull(handlerCode, "handlerCode must not be null");
        Objects.requireNonNull(instrumentClass, "instrumentClass must not be null");
        Objects.requireNonNull(instrumentType, "instrumentType must not be null");
        Objects.requireNonNull(supportedInstrumentCodes, "supportedInstrumentCodes must not be null");

        if (supportedInstrumentCodes.isEmpty()) {
            throw new IllegalArgumentException("supportedInstrumentCodes must not be empty");
        }

        this.handlerCode = handlerCode;
        this.instrumentClass = instrumentClass;
        this.instrumentType = instrumentType;
        this.supportedInstrumentCodes = freezeSupportedInstrumentCodes(supportedInstrumentCodes);
    }

    //=================================================================================================================
    // РЕАЛИЗАЦИЯ МЕТОДОВ КОНТРАКТА
    //=================================================================================================================

    @Override
    public final HandlerCode handlerCode() {
        return handlerCode;
    }

    @Override
    public Class<I> instrumentClass() {
        return instrumentClass;
    }

    @Override
    public final InstrumentType instrumentType() {
        return instrumentType;
    }

    @Override
    public final Set<InstrumentCode> supportedInstrumentCodes() {
        return supportedInstrumentCodes;
    }

    /**
     * Однократное прикрепление обработчика к провайдеру.
     */
    @Override
    public final void attachTo(P provider) {
        Objects.requireNonNull(provider, "provider must not be null");

        if (!providerRef.compareAndSet(null, provider)) {
            throw new IllegalStateException("Provider is already attached");
        }
    }

    /**
     * Возвращает реактивный поток котировок для указанного инструмента.
     *
     * <p>Выполняет базовые проверки инструмента и факта прикрепления обработчика к провайдеру,
     * после чего делегирует выполнение в чистую логику получения потока котировок методом {@link #doQuote doQuote()}.</p>
     *
     * <p>Поток:
     * <ul>
     *     <li>Может быть реализован как опрос внешнего API (API_POLL) с интервалами согласно ProviderPolicy;</li>
     *     <li>Может быть реализован как push‑подписка (websocket, streaming API и т.п.).</li>
     * </ul></p>
     *
     * <p>Примечание: возврат Mono<QuoteTick> допустим как частный случай, когда нужна единичная котировка.</p>
     *
     * @param instrument инструмент, для которого требуется котировка
     * @return поток котировок
     * @throws NullPointerException            если {@code instrument == null}
     * @throws IllegalStateException           если провайдер не прикреплён
     * @throws InstrumentWrongClassException   если класс инструмента не соответствует {@link #instrumentClass()}
     * @throws InstrumentWrongTypeException    если тип инструмента не соответствует {@link #instrumentType()}
     * @throws IllegalArgumentException        если код инструмента {@code null}
     * @throws InstrumentNotSupportedException если код инструмента не входит в поддерживаемый набор
     */
    @Override
    public final Publisher<QuoteTick> quote(I instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");

        // 1) Проверяем, что провайдер прикреплен к данному обработчику
        P currentProvider = providerRef.get();
        if (currentProvider == null) {
            throw new IllegalStateException("Provider is not attached");
        }

        // 2) Проверяем, что класс инструмента соответствует ожиданиям обработчика
        if (!instrumentClass.isInstance(instrument)) {
            throw new InstrumentWrongClassException( // <-- Неверный класс
                    instrument.instrumentCode(),
                    instrument.getClass(),
                    handlerCode,
                    instrumentClass
            );
        }

        // 3) Проверяем, что тип инструмента соответствует ожиданиям обработчика
        if (instrument.instrumentType() != instrumentType) {
            throw new InstrumentWrongTypeException( // <-- Неверный тип
                    instrument.instrumentCode(),
                    instrument.instrumentType(),
                    handlerCode,
                    instrumentType
            );
        }

        // 4) Проверяем, что код инструмента поддерживается обработчиком
        final InstrumentCode instrumentCode = instrument.instrumentCode();
        if (instrumentCode == null) {
            throw new IllegalArgumentException("instrumentCode must not be null");
        }
        if (!supportedInstrumentCodes.contains(instrumentCode)) {
            throw new InstrumentNotSupportedException( // <-- Не поддерживается
                    instrument.instrumentCode(),
                    handlerCode);
        }
        return doQuote(instrument);
    }

    //=================================================================================================================
    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
    //=================================================================================================================

    /**
     * Чистая логика получения потока котировок для переданного инструмента.
     *
     * <p>Вызывается final-методом {@link #quote(Instrument)} после того, как выполнены все необходимые проверки.</p>
     */
    protected abstract Publisher<QuoteTick> doQuote(I instrument);

    /**
     * Полезный геттер для наследников: возвращает прикреплённого провайдера.
     */
    protected final P provider() {
        P current = providerRef.get();
        if (current == null) {
            throw new IllegalStateException("Provider is not attached");
        }
        return current;
    }

    //=================================================================================================================
    // УТИЛИТЫ
    //=================================================================================================================

    /**
     * Возвращает неизменяемую защищенную копию списка поддерживаемых инструментов.
     */
    private static Set<InstrumentCode> freezeSupportedInstrumentCodes(Set<InstrumentCode> supportedInstrumentCodes) {
        Objects.requireNonNull(supportedInstrumentCodes, "supportedInstrumentCodes must not be null");

        final LinkedHashSet<InstrumentCode> copy = new LinkedHashSet<>(supportedInstrumentCodes.size());

        for (InstrumentCode code : supportedInstrumentCodes) {
            Objects.requireNonNull(code, "code must not be null");

            copy.add(code);
        }

        return java.util.Collections.unmodifiableSet(copy);
    }
}
