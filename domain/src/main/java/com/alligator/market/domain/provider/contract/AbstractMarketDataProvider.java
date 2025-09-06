package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.base.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.provider.exception.InstrumentNotSupportedException;
import com.alligator.market.domain.provider.handler.contract.InstrumentHandler;
import com.alligator.market.domain.quote.QuoteTick;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import java.util.Collections;
import java.util.Set;

/**
 * Абстрактный каркас провайдера рыночных данных.
 */
public abstract class AbstractMarketDataProvider implements MarketDataProvider {

    // Набор обработчиков инструментов
    protected final Set<InstrumentHandler> handlers;

    /**
     * Конструктор базового провайдера.
     *
     * @param handlers набор обработчиков
     */
    protected AbstractMarketDataProvider(Set<InstrumentHandler> handlers) {
        this.handlers = handlers;
    }

    /** Возвращает котировку. */
    @Override
    public Publisher<QuoteTick> getQuote(Instrument instrument) throws InstrumentNotSupportedException {
        InstrumentType type = instrument.getType();
        InstrumentHandler handler = this.findHandlerForInstrument(type);
        if (handler == null) {
            return Flux.error(new InstrumentNotSupportedException(type, getProfile().providerCode()));
        }
        return handler.getInstrumentQuote(instrument);
    }

    /** Возвращает набор обработчиков. */
    @Override
    public Set<InstrumentHandler> getHandlers() {
        return Collections.unmodifiableSet(handlers);
    }

    /**
     * Находит обработчик для указанного типа инструмента.
     *
     * @return подходящий обработчик или null
     */
    protected InstrumentHandler findHandlerForInstrument(InstrumentType type) {
        for (InstrumentHandler h : handlers) {
            if (h.getSupportedInstrumentType() == type) {
                return h;
            }
        }
        return null;
    }
}
