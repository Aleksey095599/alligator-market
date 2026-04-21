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
import java.util.concurrent.atomic.AtomicReference;

/**
 * Обработчик финансового инструмента.
 */
public abstract class AbstractInstrumentHandler<P extends MarketDataProvider, I extends Instrument>
        implements InstrumentHandler<P, I> {

    /* Код обработчика. */
    private final HandlerCode handlerCode;

    /* Java-класс поддерживаемых инструментов. */
    private final Class<I> instrumentJavaClass;

    /* Класс актива поддерживаемых инструментов. */
    private final AssetClass assetClass;

    /* Тип контракта поддерживаемых инструментов. */
    private final ContractType contractType;

    /* Коды поддерживаемых инструментов. */
    private final Set<InstrumentCode> supportedInstrumentCodes;

    /* Ссылка на провайдера (однократная потокобезопасная привязка). */
    private final AtomicReference<P> providerRef = new AtomicReference<>();

    /**
     * Конструктор с базовыми проверками.
     */
    protected AbstractInstrumentHandler(
            HandlerCode handlerCode,
            Class<I> instrumentJavaClass,
            AssetClass assetClass,
            ContractType contractType,
            Set<InstrumentCode> supportedInstrumentCodes
    ) {
        Objects.requireNonNull(handlerCode, "handlerCode must not be null");
        Objects.requireNonNull(instrumentJavaClass, "instrumentJavaClass must not be null");
        Objects.requireNonNull(assetClass, "assetClass must not be null");
        Objects.requireNonNull(contractType, "contractType must not be null");
        Objects.requireNonNull(supportedInstrumentCodes, "supportedInstrumentCodes must not be null");

        if (supportedInstrumentCodes.isEmpty()) {
            throw new IllegalArgumentException("supportedInstrumentCodes must not be empty");
        }

        this.handlerCode = handlerCode;
        this.instrumentJavaClass = instrumentJavaClass;
        this.assetClass = assetClass;
        this.contractType = contractType;
        this.supportedInstrumentCodes = freezeSupportedInstrumentCodes(supportedInstrumentCodes);
    }

    @Override
    public final HandlerCode handlerCode() {
        return handlerCode;
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
    public final void attachTo(P provider) {
        Objects.requireNonNull(provider, "provider must not be null");

        if (!providerRef.compareAndSet(null, provider)) {
            throw new IllegalStateException(
                    "Provider is already attached to handler '%s'".formatted(handlerCode.value())
            );
        }
    }

    @Override
    public final Publisher<QuoteTick> quote(I instrument) {
        validateQuoteRequest(instrument);

        return Objects.requireNonNull(doQuote(instrument),"quote publisher must not be null");
    }

    /**
     * Точка расширения метода {@link #quote(Instrument)}: чистая логика получения потока котировок
     * для переданного инструмента.
     */
    protected abstract Publisher<QuoteTick> doQuote(I instrument);

    /**
     * Возвращает провайдера, к которому прикреплен обработчик.
     */
    protected final P provider() {
        return requireAttachedProvider();
    }

    /*
     * Единая точка валидации запроса на котировку.
     */
    private void validateQuoteRequest(I instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");

        requireAttachedProvider();
        InstrumentCode instrumentCode = requireInstrumentCode(instrument);
        requireCompatibleInstrument(instrument, instrumentCode);
        requireSupportedInstrumentCode(instrumentCode);
    }

    /*
     * Проверка, что обработчик прикреплён к провайдеру.
     */
    private P requireAttachedProvider() {
        P provider = providerRef.get();
        if (provider == null) {
            throw new IllegalStateException(
                    "Provider is not attached to handler '%s'".formatted(handlerCode.value())
            );
        }
        return provider;
    }

    /*
     * Извлекает и валидирует код инструмента.
     */
    private InstrumentCode requireInstrumentCode(I instrument) {
        return Objects.requireNonNull(instrument.instrumentCode(),
                "Instrument code is missing for handler '%s'".formatted(handlerCode.value())
        );
    }

    /*
     * Проверка совместимости инструмента с конфигурацией обработчика.
     */
    private void requireCompatibleInstrument(I instrument, InstrumentCode instrumentCode) {
        if (!instrumentJavaClass.isInstance(instrument)) {
            throw new IllegalArgumentException(
                    "Instrument '%s' has java class '%s', but handler '%s' expects '%s'"
                            .formatted(
                                    instrumentCode.value(),
                                    instrument.getClass().getName(),
                                    handlerCode.value(),
                                    instrumentJavaClass.getName()
                            )
            );
        }

        if (instrument.assetClass() != assetClass) {
            throw new IllegalArgumentException(
                    "Instrument '%s' has assetClass '%s', but handler '%s' expects '%s'"
                            .formatted(
                                    instrumentCode.value(),
                                    instrument.assetClass(),
                                    handlerCode.value(),
                                    assetClass
                            )
            );
        }

        if (instrument.contractType() != contractType) {
            throw new IllegalArgumentException(
                    "Instrument '%s' has contractType '%s', but handler '%s' expects '%s'"
                            .formatted(
                                    instrumentCode.value(),
                                    instrument.contractType(),
                                    handlerCode.value(),
                                    contractType
                            )
            );
        }
    }

    /*
     * Проверка, что код инструмента поддерживается обработчиком.
     */
    private void requireSupportedInstrumentCode(InstrumentCode instrumentCode) {
        if (!supportedInstrumentCodes.contains(instrumentCode)) {
            throw new IllegalArgumentException(
                    "Instrument '%s' is not supported by handler '%s'"
                            .formatted(instrumentCode.value(), handlerCode.value())
            );
        }
    }

    /*
     * Возвращает неизменяемую защищенную копию списка поддерживаемых инструментов.
     */
    private static Set<InstrumentCode> freezeSupportedInstrumentCodes(Set<InstrumentCode> supportedInstrumentCodes) {
        Objects.requireNonNull(supportedInstrumentCodes, "supportedInstrumentCodes must not be null");

        LinkedHashSet<InstrumentCode> copy = new LinkedHashSet<>(supportedInstrumentCodes.size());
        for (InstrumentCode code : supportedInstrumentCodes) {
            Objects.requireNonNull(code, "code must not be null");
            copy.add(code);
        }

        return java.util.Collections.unmodifiableSet(copy);
    }
}
