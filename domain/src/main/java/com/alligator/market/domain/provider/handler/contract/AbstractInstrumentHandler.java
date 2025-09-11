package com.alligator.market.domain.provider.handler.contract;

import com.alligator.market.domain.instrument.base.contract.Instrument;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.exception.InstrumentNotSupportedException;
import com.alligator.market.domain.provider.exception.InstrumentWrongClassException;
import com.alligator.market.domain.quote.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.Objects;
import java.util.Set;

/**
 * Абстрактный каркас обработчика инструмента.
 */
public abstract class AbstractInstrumentHandler<P extends MarketDataProvider, I extends Instrument>
        implements InstrumentHandler<P, I> {

    private final String handlerCode;
    private final P provider;
    private final Class<I> instrumentClass;
    private final Set<I> supportedInstruments;

    /**
     * Конструктор с базовыми проверками.
     */
    protected AbstractInstrumentHandler(String handlerCode, P provider, Class<I> instrumentClass, Set<I> supportedInstruments) {
        this.handlerCode = Objects.requireNonNull(handlerCode,
                "handlerCode must not be null");
        this.provider = Objects.requireNonNull(provider,
                "provider must not be null");
        this.instrumentClass = Objects.requireNonNull(instrumentClass,
                "instrumentClass must not be null");
        this.supportedInstruments = Set.copyOf(Objects.requireNonNull(supportedInstruments,
                "supportedInstruments must not be null"));
    }

    /** Имя обработчика. */
    @Override
    public String code() {
        return handlerCode;
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

    /**
     * Котировка заданного инструмента.
     *
     * @throws InstrumentWrongClassException   если класс инструмента не соответствует @instrumentClass
     * @throws InstrumentNotSupportedException если инструмент не содержится в наборе @supportedInstruments
     */
    @Override
    public final Publisher<QuoteTick> quote(I instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");
        if (!instrumentClass.isInstance(instrument)) {
            throw new InstrumentWrongClassException(instrumentClass, instrument.getClass());
        }
        if (!supportedInstruments.contains(instrument)) {
            throw new InstrumentNotSupportedException(
                    instrument.code(),
                    handlerCode,
                    provider.profile().providerCode()
            );
        }
        return doQuote(instrument);
    }

    /** Реализация получения котировки. */
    protected abstract Publisher<QuoteTick> doQuote(I instrument);
}
