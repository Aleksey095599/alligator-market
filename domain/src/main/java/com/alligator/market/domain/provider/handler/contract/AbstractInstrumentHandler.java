package com.alligator.market.domain.provider.handler.contract;

import com.alligator.market.domain.instrument.base.contract.Instrument;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.quote.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.Objects;
import java.util.Set;

/**
 * Абстрактный каркас обработчика инструмента.
 */
public abstract class AbstractInstrumentHandler<P extends MarketDataProvider, I extends Instrument>
        implements InstrumentHandler<P, I> {

    private final P provider;
    private final Class<I> instrumentClass;
    private final Set<I> supportedInstruments;

    /**
     * Конструктор с базовыми проверками.
     */
    protected AbstractInstrumentHandler(P provider, Class<I> instrumentClass, Set<I> supportedInstruments) {
        this.provider = Objects.requireNonNull(provider, "provider must not be null");
        this.instrumentClass = Objects.requireNonNull(instrumentClass, "instrumentClass must not be null");
        this.supportedInstruments = Set.copyOf(Objects.requireNonNull(supportedInstruments, "supportedInstruments must not be null"));
    }

    /** Провайдер, к которому относится обработчик. */
    @Override
    public P provider() {
        return provider;
    }

    /** Класс поддерживаемых инструментов. */
    @Override
    public Class<I> instrumentClass() {
        return instrumentClass;
    }

    /** Набор поддерживаемых инструментов. */
    @Override
    public Set<I> supportedInstruments() {
        return supportedInstruments;
    }

    /** Котировка заданного инструмента. */
    @Override
    public final Publisher<QuoteTick> quote(I instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");
        if (!instrumentClass.isInstance(instrument)) {
            throw new IllegalArgumentException("instrument type is not supported");
        }
        if (!supportedInstruments.contains(instrument)) {
            throw new IllegalArgumentException("instrument is not supported");
        }
        return doQuote(instrument);
    }

    /** Реализация получения котировки. */
    protected abstract Publisher<QuoteTick> doQuote(I instrument);
}
