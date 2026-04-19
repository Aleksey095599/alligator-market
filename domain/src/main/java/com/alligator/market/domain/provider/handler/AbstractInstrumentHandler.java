package com.alligator.market.domain.provider.handler;

import com.alligator.market.domain.instrument.base.Instrument;
import com.alligator.market.domain.instrument.base.classification.AssetClass;
import com.alligator.market.domain.instrument.base.classification.ContractType;
import com.alligator.market.domain.instrument.base.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.tick.model.QuoteTick;
import com.alligator.market.domain.provider.MarketDataProvider;
import com.alligator.market.domain.provider.vo.HandlerCode;
import org.reactivestreams.Publisher;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Обработчик финансового инструмента.
 */
public abstract class AbstractInstrumentHandler<P extends MarketDataProvider, I extends Instrument>
        implements InstrumentHandler<P, I> {

    //region FIELDS

    /* Код обработчика. */
    private final HandlerCode handlerCode;

    /* Класс поддерживаемых инструментов. */
    private final Class<I> instrumentClass;

    /* Класс актива поддерживаемых инструментов. */
    private final AssetClass assetClass;

    /* Тип контракта поддерживаемых инструментов. */
    private final ContractType contractType;

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
            AssetClass assetClass,
            ContractType contractType,
            Set<InstrumentCode> supportedInstrumentCodes
    ) {
        Objects.requireNonNull(handlerCode, "handlerCode must not be null");
        Objects.requireNonNull(instrumentClass, "instrumentClass must not be null");
        Objects.requireNonNull(assetClass, "assetClass must not be null");
        Objects.requireNonNull(contractType, "contractType must not be null");
        Objects.requireNonNull(supportedInstrumentCodes, "supportedInstrumentCodes must not be null");

        if (supportedInstrumentCodes.isEmpty()) {
            throw new IllegalArgumentException("supportedInstrumentCodes must not be empty");
        }

        this.handlerCode = handlerCode;
        this.instrumentClass = instrumentClass;
        this.assetClass = assetClass;
        this.contractType = contractType;
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
    public final AssetClass assetClass() {
        return assetClass;
    }

    @Override
    public final ContractType contractType() {
        return contractType;
    }

    @Override
    public final Set<InstrumentCode> supportedInstrumentCodes() {
        return supportedInstrumentCodes;
    }

    @Override
    public final boolean isCompatible(Instrument instrument) {
        return instrumentClass.isInstance(instrument)
                && instrument.assetClass() == assetClass
                && instrument.contractType() == contractType;
    }

    @Override
    public final boolean isSupported(InstrumentCode instrumentCode) {
        return instrumentCode != null && supportedInstrumentCodes.contains(instrumentCode);
    }

    @Override
    public final void attachTo(P provider) {
        Objects.requireNonNull(provider, "provider must not be null");

        if (!providerRef.compareAndSet(null, provider)) {
            throw new IllegalStateException("Provider is already attached to handler '%s'".formatted(handlerCode.value()));
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
     * <p>Назначение: Валидирует инварианты и требования контракта {@link InstrumentHandler}, после чего вызывает
     * хук {@link #doQuote doQuote()}, который описывает чистую логику получения потока котировок.</p>
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
            throw new IllegalStateException("Provider is not attached to handler '%s'".formatted(handlerCode.value()));
        }
        return current;
    }

    //endregion

    //region INTERNALS

    /* Требование контракта: обработчик прикреплён к провайдеру. */
    private void requireProviderAttached() {
        if (!isAttached()) {
            throw new IllegalStateException("Provider is not attached to handler '%s'".formatted(handlerCode.value()));
        }
    }

    /* Проверка, что инструмент совместим с обработчиком. */
    private void requireCompatible(I instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");

        final InstrumentCode instrumentCode = Objects.requireNonNull(
                instrument.instrumentCode(),
                "Instrument code is missing for handler '%s'".formatted(handlerCode.value())
        );

        if (!instrumentClass.isInstance(instrument)) {
            throw new IllegalArgumentException(buildClassMismatchMessage(instrumentCode, instrument.getClass()));
        }

        if (instrument.assetClass() != assetClass) {
            throw new IllegalArgumentException(
                    "Instrument '%s' has assetClass '%s', but handler '%s' expects '%s'"
                            .formatted(instrumentCode, instrument.assetClass(), handlerCode.value(), assetClass)
            );
        }

        if (instrument.contractType() != contractType) {
            throw new IllegalArgumentException(
                    "Instrument '%s' has contractType '%s', but handler '%s' expects '%s'"
                            .formatted(instrumentCode, instrument.contractType(), handlerCode.value(), contractType)
            );
        }
    }

    /* Проверка, что код инструмента поддерживается обработчиком. */
    private void requireSupportedCode(I instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");

        final InstrumentCode instrumentCode = Objects.requireNonNull(
                instrument.instrumentCode(),
                "Instrument code is missing for handler '%s'".formatted(handlerCode.value())
        );

        if (!isSupported(instrumentCode)) {
            throw new IllegalArgumentException(
                    "Instrument '%s' is not supported by handler '%s'"
                            .formatted(instrumentCode, handlerCode.value())
            );
        }
    }

    /* Формирует диагностическое сообщение для несовпадения java-класса инструмента. */
    private String buildClassMismatchMessage(InstrumentCode instrumentCode, Class<?> actualClass) {
        return "Instrument '%s' has java class '%s', but handler '%s' expects '%s'"
                .formatted(instrumentCode, actualClass.getName(), handlerCode.value(), instrumentClass.getName());
    }

    /* Возвращает неизменяемую защищенную копию списка поддерживаемых инструментов. */
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
