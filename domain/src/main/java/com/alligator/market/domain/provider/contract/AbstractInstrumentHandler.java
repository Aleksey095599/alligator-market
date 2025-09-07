package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.type.InstrumentType;

/**
 * Абстрактный каркас обработчика инструмента.
 */
public abstract class AbstractInstrumentHandler<P extends MarketDataProvider>
        implements InstrumentHandler<P> {

    // Ссылка на провайдера
    private P provider;

    // Поддерживаемый тип инструмента
    private final InstrumentType supportedInstrumentType;

    // Конструктор
    protected AbstractInstrumentHandler(InstrumentType supportedInstrumentType) {
        this.supportedInstrumentType = supportedInstrumentType;
    }

    // Устанавливаем провайдера
    void setProvider(P provider) {
        this.provider = provider;
    }

    /** Возвращает ссылку на провайдера. */
    @Override
    public P getProvider() {
        return provider;
    }

    /** Возвращает поддерживаемый тип инструмента. */
    @Override
    public InstrumentType getSupportedInstrumentType() {
        return supportedInstrumentType;
    }
}
