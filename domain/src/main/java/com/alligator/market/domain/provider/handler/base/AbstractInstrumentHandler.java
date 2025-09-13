package com.alligator.market.domain.provider.handler.base;

import com.alligator.market.domain.instrument.base.contract.Instrument;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.exception.InstrumentNotSupportedException;
import com.alligator.market.domain.provider.exception.InstrumentWrongClassException;
import com.alligator.market.domain.provider.handler.contract.InstrumentHandler;
import com.alligator.market.domain.quote.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.Objects;
import java.util.Set;

/**
 * Абстрактный каркас обработчика инструмента.
 */
public abstract class AbstractInstrumentHandler<P extends MarketDataProvider, I extends Instrument>
        implements InstrumentHandler<P, I> {

    /* ↓↓ Базовые атрибуты обработчика. */
    private final String handlerCode;
    private final Class<I> instrumentClass;
    private final Set<I> supportedInstruments;

    /* Ссылка на провайдера (проставляется провайдером через attachTo()). */
    protected P provider;

    /* Конструктор. */
    protected AbstractInstrumentHandler(String handlerCode, Class<I> instrumentClass, Set<I> supportedInstruments) {
        this.handlerCode = Objects.requireNonNull(handlerCode,
                "handlerCode must not be null");
        this.instrumentClass = Objects.requireNonNull(instrumentClass,
                "instrumentClass must not be null");
        this.supportedInstruments = Set.copyOf(Objects.requireNonNull(supportedInstruments,
                "supportedInstruments must not be null"));
    }

    /** Уникальный код обработчика (для логов/метрик). */
    @Override
    public final String handlerCode() {
        return handlerCode;
    }

    /** Класс поддерживаемого инструмента. */
    @Override
    public Class<I> instrumentClass() {
        return instrumentClass;
    }

    /** Конкретные инструменты, которые поддерживает обработчик. */
    @Override
    public Set<I> supportedInstruments() {
        return supportedInstruments;
    }

    /** Прикрепить обработчик к заданному провайдеру. */
    @Override
    public final void attachTo(P provider) {
        this.provider = Objects.requireNonNull(provider, "provider must not be null");
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
            throw new InstrumentWrongClassException(
                    instrument.code(),
                    instrument.getClass(),
                    handlerCode,
                    instrumentClass
            );
        }
        if (!supportedInstruments.contains(instrument)) {
            throw new InstrumentNotSupportedException(
                    instrument.code(),
                    handlerCode
            );
        }
        return doQuote(instrument);
    }

    /** Реализация получения котировки. */
    protected abstract Publisher<QuoteTick> doQuote(I instrument);

    /** Полезный геттер для наследников. */
    protected final P provider() {
        return provider;
    }
}
