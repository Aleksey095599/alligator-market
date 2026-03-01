package com.alligator.market.domain.provider.model.handler;

import com.alligator.market.domain.instrument.model.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.provider.model.MarketDataProvider;
import com.alligator.market.domain.provider.model.handler.exception.*;
import com.alligator.market.domain.provider.model.vo.HandlerCode;
import com.alligator.market.domain.quote.tick.model.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Абстрактная реализация обработчика инструмента {@link InstrumentHandler}.
 */
public abstract class AbstractInstrumentHandler<P extends MarketDataProvider, I extends Instrument>
        implements InstrumentHandler<P, I> {

    //region FIELDS

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

    //endregion

    //region CONSTRUCTION

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
            throw new SupportedInstrumentCodesEmptyException(handlerCode);
        }

        this.handlerCode = handlerCode;
        this.instrumentClass = instrumentClass;
        this.instrumentType = instrumentType;
        this.supportedInstrumentCodes = freezeSupportedInstrumentCodes(supportedInstrumentCodes);
    }

    //endregion

    //region PUBLIC API

    @Override
    public final HandlerCode handlerCode() {
        return handlerCode;
    }

    @Override
    public final Class<I> instrumentClass() {
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
     * Признак: инструмент сопоставим с обработчиком по доменным признакам (класс + тип).
     */
    @Override
    public final boolean isCompatible(Instrument instrument) {
        return instrument != null
                && instrumentClass.isInstance(instrument)
                && instrument.instrumentType() == instrumentType;
    }

    /**
     * Признак: поддерживается ли конкретный код инструмента.
     */
    @Override
    public final boolean isSupported(InstrumentCode instrumentCode) {
        return instrumentCode != null && supportedInstrumentCodes.contains(instrumentCode);
    }

    /**
     * Однократное прикрепление обработчика к провайдеру.
     */
    @Override
    public final void attachTo(P provider) {
        Objects.requireNonNull(provider, "provider must not be null");

        if (!providerRef.compareAndSet(null, provider)) {
            throw new ProviderAlreadyAttachedException(handlerCode);
        }
    }

    @Override
    public final boolean isAttached() {
        return providerRef.get() != null;
    }

    //endregion

    //region TEMPLATE METHOD

    /**
     * Возвращает реактивный поток котировок для указанного инструмента.
     *
     * <p>Назначение: Валидирует инварианты и трбоевания контракта {@link InstrumentHandler}, после чего вызывает
     * хук {@link #doQuote doQuote()}, который описывает чистую логику получения потока котировок.</p>
     *
     * <p>Поток может быть реализован как:</p>
     * <ul>
     *     <li>Опрос внешнего API (API_POLL) с интервалами согласно ProviderPolicy;</li>
     *     <li>Push‑подписка (websocket, streaming API и т.п.).</li>
     * </ul>
     *
     * <p>Примечание: возврат Mono<QuoteTick> допустим как частный случай, когда нужна единичная котировка.</p>
     *
     * @param instrument инструмент, для которого требуется получить поток котировок
     * @return поток котировок
     * @throws ProviderNotAttachedException    если провайдер не прикреплён
     * @throws InstrumentWrongClassException   если класс инструмента не соответствует {@link #instrumentClass()}
     * @throws InstrumentWrongTypeException    если тип инструмента не соответствует {@link #instrumentType()}
     * @throws InstrumentCodeMissingException  если код инструмента {@code null}
     * @throws InstrumentNotSupportedException если код инструмента не входит в поддерживаемый набор
     */
    @Override
    public final Publisher<QuoteTick> quote(I instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");

        requireProviderAttached();
        requireCompatible(instrument);
        requireSupportedCode(instrument);

        return Objects.requireNonNull(doQuote(instrument), "quote publisher must not be null");
    }

    /**
     * Точка расширения (hook) шаблонного метода {@link #quote(Instrument)}: чистая логика получения потока котировок
     * для переданного инструмента.
     *
     * @param instrument инструмент, для которого требуется получить поток котировок
     * @return поток котировок
     */
    protected abstract Publisher<QuoteTick> doQuote(I instrument);

    //endregion

    //region PROTECTED API

    /**
     * Возвращает провайдера, к которому прикреплен обработчик.
     *
     * <p>Назначение: Обработчик может обращаться к провайдеру для получения дополнительной информации.</p>
     */
    protected final P provider() {
        P current = providerRef.get();
        if (current == null) {
            throw new ProviderNotAttachedException(handlerCode);
        }
        return current;
    }

    //endregion

    //region INTERNALS

    /* Требование контракта: обработчик прикреплён к провайдеру. */
    private void requireProviderAttached() {
        if (!isAttached()) {
            throw new ProviderNotAttachedException(handlerCode);
        }
    }

    /**
     * Требование контракта: инструмент совместим с обработчиком (класс + тип).
     */
    private void requireCompatible(I instrument) {
        if (!instrumentClass.isInstance(instrument)) {
            throw new InstrumentWrongClassException(
                    instrument.instrumentCode(),
                    instrument.getClass(),
                    handlerCode,
                    instrumentClass
            );
        }

        if (instrument.instrumentType() != instrumentType) {
            throw new InstrumentWrongTypeException(
                    instrument.instrumentCode(),
                    instrument.instrumentType(),
                    handlerCode,
                    instrumentType
            );
        }
    }

    /**
     * Требование контракта: код инструмента задан и поддерживается обработчиком.
     */
    private void requireSupportedCode(I instrument) {
        final InstrumentCode instrumentCode = instrument.instrumentCode();

        if (instrumentCode == null) {
            throw new InstrumentCodeMissingException(handlerCode);
        }

        if (!isSupported(instrumentCode)) {
            throw new InstrumentNotSupportedException(instrumentCode, handlerCode);
        }
    }

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

    //endregion
}
