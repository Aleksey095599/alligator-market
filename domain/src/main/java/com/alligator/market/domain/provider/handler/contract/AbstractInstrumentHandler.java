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

    /** Провайдер, к которому относится обработчик. */
    private final P provider;

    /** Класс поддерживаемых инструментов. */
    private final Class<I> instrumentClass;

    /** Набор поддерживаемых инструментов. */
    private final Set<I> supportedInstruments;

    /**
     * Конструктор с базовой настройкой.
     */
    protected AbstractInstrumentHandler(P provider, Class<I> instrumentClass, Set<I> supportedInstruments) {
        this.provider = Objects.requireNonNull(provider, "provider must not be null");
        this.instrumentClass = Objects.requireNonNull(instrumentClass, "instrumentClass must not be null");
        this.supportedInstruments = Set.copyOf(Objects.requireNonNull(supportedInstruments, "supportedInstruments must not be null"));
    }

    @Override
    public P provider() {
        return provider;
    }

    @Override
    public Class<I> instrumentClass() {
        return instrumentClass;
    }

    @Override
    public Set<I> supportedInstruments() {
        return supportedInstruments;
    }

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
