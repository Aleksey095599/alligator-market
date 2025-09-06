package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.provider.handler.contract.InstrumentHandler;
import java.util.Collections;
import java.util.Set;

/**
 * Базовый класс провайдеров рыночных данных.
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

    /** Возвращает набор обработчиков. */
    @Override
    public Set<InstrumentHandler> getHandlers() {
        return Collections.unmodifiableSet(handlers);
    }
}
